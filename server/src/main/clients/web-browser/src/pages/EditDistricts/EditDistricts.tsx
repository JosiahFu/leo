import './EditDistricts.scss';
import {ChangeEvent, useEffect, useState} from 'react';
import {createService, district_management} from '../../protos';
import DistrictManagementService = district_management.DistrictManagementService;
import DistrictInformationResponse = district_management.DistrictInformationResponse;
import IDistrict = district_management.IDistrict;
import {Display, SelectFromList} from '../../SelectFromList/SelectFromList';

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
    <table className="form-table">
      <tbody>
        <tr>
          <th>District:</th>
          <td>
            <SelectFromList<number, IDistrict>
              display={Display.DROP_DOWN}
              values={districts}
              selectedKey={districtId}
              getKey={district => (district != null ? district.id! : -1)}
              stringToKey={key => Number(key)}
              compareValues={(a, b) =>
                (a.name || '').localeCompare(b.name || '')
              }
              onSelect={key => {
                setDistrictId(key);
                setDistrictName((districts.get(key) || {name: ''}).name!);
              }}
              renderValue={key => {
                return (
                  <>
                    {
                      (districts.get(key) || {name: '- Create New District -'})
                        .name!
                    }
                  </>
                );
              }}
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
  );
}
