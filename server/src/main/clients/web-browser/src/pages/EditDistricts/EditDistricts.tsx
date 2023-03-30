import './EditDistricts.scss';
import {ChangeEvent, useEffect, useState} from 'react';
import {createService, district_management} from '../../protos';
import DistrictManagementService = district_management.DistrictManagementService;
import DistrictInformationResponse = district_management.DistrictInformationResponse;

type DistrictEntry = {id: number; name: string};

export function EditDistricts() {
  const [districts, setDistricts] = useState(new Array<DistrictEntry>());
  const [selectedDistrict, setSelectedDistrict] = useState(-1);
  const [districtName, setDistrictName] = useState('');

  const districtManagementService = createService(
    DistrictManagementService,
    'DistrictManagementService'
  );

  function processDistrictInformationResponse(
    response: DistrictInformationResponse
  ) {
    const districts = Object.entries(response.districts)
      .map((entry: [string, string]) => {
        return {
          id: Number(entry[0]),
          name: entry[1],
        };
      })
      .sort((a: DistrictEntry, b: DistrictEntry) =>
        a.name.localeCompare(b.name)
      );
    setDistricts(districts);
    selectDistrict(districts, response.modifiedDistrictId);
  }

  function selectDistrict(
    districts: Array<DistrictEntry>,
    districtId: number | null | undefined
  ) {
    setSelectedDistrict(-1);
    setDistrictName('');
    for (const district of districts) {
      if (district.id === districtId) {
        setSelectedDistrict(district.id);
        setDistrictName(district.name);
        break;
      }
    }
  }

  function addDistrict() {
    districtManagementService
      .addDistrict({district: districtName})
      .then(processDistrictInformationResponse);
  }

  function updateDistrict() {
    districtManagementService
      .updateDistrict({districtId: selectedDistrict, district: districtName})
      .then(processDistrictInformationResponse);
  }

  function deleteDistrict() {
    districtManagementService
      .removeDistrict({
        districtId: selectedDistrict,
      })
      .then(processDistrictInformationResponse);
  }

  useEffect(() => {
    districtManagementService
      .getDistricts({})
      .then(processDistrictInformationResponse);
  }, []);

  return (
    <div style={{textAlign: 'center'}}>
      <table className="form-table">
        <tbody>
          <tr>
            <th>District to Edit:</th>
            <td>
              <select
                style={{width: '100%'}}
                value={selectedDistrict}
                onChange={(e: ChangeEvent<HTMLSelectElement>) => {
                  selectDistrict(districts, Number(e.target.value));
                }}
              >
                <option key={-1} value={-1}>
                  - Create New District -
                </option>
                {districts.map(district => (
                  <option key={district.id} value={district.id}>
                    {district.name}
                  </option>
                ))}
              </select>
            </td>
          </tr>
          <tr>
            <th className="form-label">District Name:</th>
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
              <div hidden={selectedDistrict !== -1} onClick={addDistrict}>
                Add
              </div>
              <div hidden={selectedDistrict === -1} onClick={updateDistrict}>
                Update
              </div>
              <div
                className="delete-button"
                hidden={selectedDistrict === -1}
                onClick={deleteDistrict}
              >
                Delete
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  );
}
