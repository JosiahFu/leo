import './EditSchools.scss';
import {ChangeEvent, useEffect, useState} from 'react';
import {
  createService,
  district_management,
  school_management,
} from '../../protos';
import {Display, SelectFromList} from '../../SelectFromList/SelectFromList';
import {SelectDistrictFromList} from '../EditDistricts/EditDistricts';
import DistrictManagementService = district_management.DistrictManagementService;
import IDistrict = district_management.IDistrict;
import SchoolManagementService = school_management.SchoolManagementService;
import ISchool = school_management.ISchool;
import ISchoolInformationResponse = school_management.ISchoolInformationResponse;
import {
  MultipleDisplay,
  SelectMultipleFromList,
} from '../../SelectMultipleFromList/SelectMultipleFromList';

export function SelectSchoolFromList(props: {
  id: string;
  display: Display;
  schools: Map<number, ISchool>;
  schoolId: number;
  onSelect: (key: number) => void;
  defaultText: string;
}) {
  return SelectFromList<number, ISchool>({
    id: props.id,
    display: props.display,
    values: props.schools,
    selectedKey: props.schoolId,
    getKey: school => (school != null ? school.id! : -1),
    stringToKey: Number,
    compareValues: (a, b) =>
      (a.name || '').localeCompare(b.name || '') ||
      (a.city || '').localeCompare(b.city || ''),
    onSelect: props.onSelect,
    renderValue: schoolId => {
      const school = props.schools.get(schoolId);
      if (school != null) {
        return (
          <>
            <span className="school">
              <span className="school-name">{school.name}</span>,&nbsp;
              <span className="school-city">{school.city}</span>
            </span>
          </>
        );
      } else {
        return <>{props.defaultText}</>;
      }
    },
  });
}

export function SelectMultipleSchoolsFromList(props: {
  id: string;
  display: MultipleDisplay;
  schools: Map<number, ISchool>;
  schoolIds: Set<number>;
  onSelect: (keys: Set<number>) => void;
}) {
  return SelectMultipleFromList<number, ISchool>({
    id: props.id,
    display: props.display,
    values: props.schools,
    selectedKeys: props.schoolIds,
    getKey: school => (school != null ? school.id! : -1),
    stringToKey: Number,
    compareValues: (a, b) =>
      (a.name || '').localeCompare(b.name || '') ||
      (a.city || '').localeCompare(b.city || ''),
    onSelect: props.onSelect,
    renderValue: schoolId => {
      const school = props.schools.get(schoolId);
      if (school != null) {
        return (
          <>
            <span className="school">
              <span className="school-name">{school.name}</span>,&nbsp;
              <span className="school-city">{school.city}</span>
            </span>
          </>
        );
      } else {
        return <></>;
      }
    },
  });
}

export function EditSchools() {
  const [districts, setDistricts] = useState(new Map<number, IDistrict>());
  const [districtId, setDistrictId] = useState(-1);

  const [schools, setSchools] = useState(new Map<number, ISchool>());
  const [schoolId, setSchoolId] = useState(-1);
  const [name, setName] = useState('');
  const [city, setCity] = useState('');

  const districtManagementService = createService(
    DistrictManagementService,
    'DistrictManagementService'
  );

  useEffect(() => {
    districtManagementService.getDistricts({}).then(response => {
      setDistricts(new Map(response.districts.map(v => [v.id!, v!])));
      setDistrictId(response.modifiedDistrictId!);
    });
  }, []);

  const schoolManagementService = createService(
    SchoolManagementService,
    'SchoolManagementService'
  );

  useEffect(() => {
    if (districtId !== -1) {
      schoolManagementService
        .getSchools({districtId: districtId})
        .then(processSchoolInformationResponse);
    }
  }, [districtId]);

  function upsertSchool() {
    schoolManagementService
      .upsertSchool({
        school: {
          districtId: districtId,
          id: schoolId !== -1 ? schoolId : undefined,
          name: name,
          city: city,
        },
      })
      .then(processSchoolInformationResponse);
  }

  function removeSchool() {
    schoolManagementService
      .removeSchool({
        districtId: districtId,
        schoolId: schoolId,
      })
      .then(processSchoolInformationResponse);
  }

  function processSchoolInformationResponse(
    response: ISchoolInformationResponse
  ) {
    setDistrictId(response.districtId!);
    setSchools(new Map(response.schools!.map(v => [v.id!, v!])));
    setSchoolId(response.nextSchoolId!);
  }

  return (
    <>
      <h2>Edit Schools</h2>
      <table className="form-table">
        <tbody>
          <tr>
            <th>District:</th>
            <td>
              <SelectDistrictFromList
                id="districts"
                display={Display.DROP_DOWN}
                districts={districts}
                districtId={districtId}
                onSelect={setDistrictId}
                defaultText="- Select District -"
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th>School:</th>
            <td>
              <SelectSchoolFromList
                id="schools"
                display={Display.RADIO_BUTTONS}
                schools={schools}
                schoolId={schoolId}
                onSelect={schoolId => {
                  setSchoolId(schoolId);
                  const school = schools.get(schoolId);
                  if (school != null) {
                    setName(school.name!);
                    setCity(school.city!);
                  } else {
                    setName('');
                    setCity('');
                  }
                }}
                defaultText="[Create New School]"
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th>Name:</th>
            <td>
              <input
                type="text"
                placeholder="New School Name"
                onChange={(e: ChangeEvent<HTMLInputElement>) => {
                  setName(e.target.value);
                }}
                value={name}
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th>City:</th>
            <td>
              <input
                type="text"
                placeholder="New School City"
                onChange={(e: ChangeEvent<HTMLInputElement>) => {
                  setCity(e.target.value);
                }}
                value={city}
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th></th>
            <td className="form-buttons">
              <div hidden={schoolId !== -1} onClick={upsertSchool}>
                Add
              </div>
              <div hidden={schoolId === -1} onClick={upsertSchool}>
                Update
              </div>
              <div
                className="delete-button"
                hidden={schoolId === -1}
                onClick={removeSchool}
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
