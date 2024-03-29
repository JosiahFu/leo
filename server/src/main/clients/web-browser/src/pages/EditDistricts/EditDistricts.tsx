import './EditDistricts.scss';
import {ChangeEvent, useEffect, useState} from 'react';
import {createService, district_management} from '../../protos';
import DistrictManagementService = district_management.DistrictManagementService;
import DistrictInformationResponse = district_management.DistrictInformationResponse;
import IDistrict = district_management.IDistrict;
import {Display, SelectFromList} from '../../SelectFromList/SelectFromList';

export function SelectDistrictFromList(props: {
  id: string;
  display: Display;
  districts: Map<number, IDistrict>;
  districtId: number;
  onSelect: (key: number) => void;
  defaultText: string;
}) {
  return SelectFromList<number, IDistrict>({
    id: props.id,
    display: props.display,
    values: props.districts,
    selectedKey: props.districtId,
    getKey: district => (district != null ? district.id! : -1),
    stringToKey: Number,
    compareValues: (a, b) => (a.name || '').localeCompare(b.name || ''),
    onSelect: props.onSelect,
    renderValue: key => (
      <>{(props.districts.get(key) || {name: props.defaultText}).name!}</>
    ),
  });
}

export function EditDistricts() {
  const [districts, setDistricts] = useState(new Map<number, IDistrict>());
  const [districtId, setDistrictId] = useState(-1);
  const [districtName, setDistrictName] = useState('');

  const districtManagementService = createService(
    DistrictManagementService,
    'DistrictManagementService'
  );

  function addDistrict() {
    districtManagementService
      .addDistrict({district: {name: districtName}})
      .then(processDistrictInformationResponse);
  }

  function updateDistrict() {
    districtManagementService
      .updateDistrict({district: {id: districtId, name: districtName}})
      .then(processDistrictInformationResponse);
  }

  function removeDistrict() {
    districtManagementService
      .removeDistrict({
        districtId: districtId,
      })
      .then(processDistrictInformationResponse);
  }

  function processDistrictInformationResponse(
    response: DistrictInformationResponse
  ) {
    setDistricts(new Map(response.districts.map(v => [v.id!, v!])));
    setDistrictId(response.modifiedDistrictId!);
  }

  useEffect(() => {
    districtManagementService
      .getDistricts({})
      .then(processDistrictInformationResponse);
  }, []);

  return (
    <>
      <h2>Edit Districts</h2>
      <table className="form-table">
        <tbody>
          <tr>
            <th>District:</th>
            <td>
              <SelectDistrictFromList
                id="districts"
                display={Display.RADIO_BUTTONS}
                districts={districts}
                districtId={districtId}
                onSelect={districtId => {
                  setDistrictId(districtId);
                  setDistrictName(
                    (districts.get(districtId) || {name: ''}).name!
                  );
                }}
                defaultText="[Create New District]"
              />
            </td>
          </tr>
          <tr>
            <th>Name:</th>
            <td>
              <input
                type="text"
                placeholder="New District Name"
                onChange={(e: ChangeEvent<HTMLInputElement>) => {
                  setDistrictName(e.target.value);
                }}
                value={districtName}
              />
            </td>
          </tr>
          <tr>
            <th></th>
            <td className="form-buttons">
              <div hidden={districtId !== -1} onClick={addDistrict}>
                Add
              </div>
              <div hidden={districtId === -1} onClick={updateDistrict}>
                Update
              </div>
              <div
                className="delete-button"
                hidden={districtId === -1}
                onClick={removeDistrict}
              >
                Delete
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </>
  );
}
