import './EditUser.scss';
import {
  createService,
  district_management,
  user_management,
} from '../../protos';
import {useEffect, useState} from 'react';
import {FieldWithError} from '../../FieldWithError/FieldWithError';
import UserManagementService = user_management.UserManagementService;
import {Display, SelectFromList} from '../../SelectFromList/SelectFromList';
import IUser = user_management.IUser;
import DistrictManagementService = district_management.DistrictManagementService;
import IDistrict = district_management.IDistrict;
import {SelectDistrictFromList} from '../EditDistricts/EditDistricts';
import IUserInformationResponse = user_management.IUserInformationResponse;

export function SelectUserFromList(props: {
  id: string;
  display: Display;
  users: Map<number, IUser>;
  userId: number;
  onSelect: (userId: number) => void;
  defaultText: string;
}) {
  return SelectFromList<number, IUser>({
    id: props.id,
    display: props.display,
    values: props.users,
    selectedKey: props.userId,
    getKey: user => (user != null ? user.id! : -1),
    stringToKey: Number,
    compareValues: (a, b) =>
      (a.lastName || '').localeCompare(b.lastName || '') ||
      (a.firstName || '').localeCompare(b.firstName || ''),
    onSelect: props.onSelect,
    renderValue: userId => {
      const user = props.users.get(userId);
      if (user) {
        return (
          <>
            <span className="user">
              <span className="last-name">{user.lastName}</span>,&nbsp;
              <span className="first-name">{user.firstName}</span>
            </span>
          </>
        );
      } else {
        return <>{props.defaultText}</>;
      }
    },
  });
}

export function EditUsers() {
  const [districts, setDistricts] = useState(new Map<number, IDistrict>());
  const [districtId, setDistrictId] = useState(-1);

  const [users, setUsers] = useState(new Map<number, IUser>());
  const [userId, setUserId] = useState(-1);

  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [emailAddress, setEmailAddress] = useState('');
  const [password, setPassword] = useState('');
  const [verifyPassword, setVerifyPassword] = useState('');
  const [isAdmin, setIsAdmin] = useState(false);
  const [isTeacher, setIsTeacher] = useState(false);
  const [isStudent, setIsStudent] = useState(false);

  const [firstNameError, setFirstNameError] = useState('');
  const [lastNameError, setLastNameError] = useState('');
  const [emailAddressError, setEmailAddressError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const [verifyPasswordError, setVerifyPasswordError] = useState('');

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

  const userManagementService = createService(
    UserManagementService,
    'UserManagementService'
  );

  useEffect(() => {
    if (districtId !== -1) {
      userManagementService
        .getUsers({districtId: districtId})
        .then(processUserInformationResponse);
    }
  }, [districtId]);

  function upsertUser() {
    userManagementService
      .upsertUser({
        user: {
          districtId: districtId,
          id: userId !== -1 ? userId : undefined,
          firstName: firstName,
          lastName: lastName,
          emailAddress: emailAddress,
          password: password,
          verifyPassword: verifyPassword,
          isAdmin: isAdmin,
          isTeacher: isTeacher,
          isStudent: isStudent,
        },
      })
      .then(processUserInformationResponse);
  }

  function removeUser() {
    userManagementService
      .removeUser({
        districtId: districtId,
        userId: userId,
      })
      .then(processUserInformationResponse);
  }

  function processUserInformationResponse(
    response: IUserInformationResponse
  ): void {
    if (response.success) {
      setDistrictId(response.districtId!);
      setUsers(new Map(response.users!.map(v => [v.id!, v])));
      setUserId(response.nextUserId!);
      setFirstNameError('');
      setLastNameError('');
      setEmailAddressError('');
      setPasswordError('');
      setVerifyPasswordError('');
      setPassword('');
      setVerifyPassword('');
    } else {
      setFirstNameError(response.firstNameError || '');
      setLastNameError(response.lastNameError || '');
      setEmailAddressError(response.emailAddressError || '');
      setPasswordError(response.passwordError || '');
      setVerifyPasswordError(response.verifyPasswordError || '');
    }
  }

  return (
    <>
      <h2>Edit Users</h2>
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
            <th>Users:</th>
            <td>
              <SelectUserFromList
                id="users"
                display={Display.RADIO_BUTTONS}
                users={users}
                userId={userId}
                onSelect={userId => {
                  setUserId(userId);
                  const user = users.get(userId);
                  if (user) {
                    setFirstName(user.firstName!);
                    setLastName(user.lastName!);
                    setEmailAddress(user.emailAddress!);
                    setIsAdmin(user.isAdmin!);
                    setIsTeacher(user.isTeacher!);
                    setIsStudent(user.isStudent!);
                  } else {
                    setFirstName('');
                    setLastName('');
                    setEmailAddress('');
                    setIsAdmin(false);
                    setIsTeacher(false);
                    setIsStudent(false);
                  }
                  setPassword('');
                  setVerifyPassword('');

                  setFirstNameError('');
                  setLastNameError('');
                  setEmailAddressError('');
                  setPasswordError('');
                  setVerifyPasswordError('');
                }}
                defaultText="[Create New School]"
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th>
              <label htmlFor="first_name">First Name:</label>
            </th>
            <td>
              <FieldWithError
                id="first_name"
                value={firstName}
                setValue={setFirstName}
                autoComplete="given-name"
                type="text"
                maxLength={255}
                error={firstNameError}
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th>
              <label htmlFor="last_name">Last Name:</label>
            </th>
            <td>
              <FieldWithError
                id="last_name"
                value={lastName}
                setValue={setLastName}
                autoComplete="family-name"
                type="text"
                maxLength={255}
                error={lastNameError}
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th>
              <label htmlFor="email_address">Email Address:</label>
            </th>
            <td>
              <FieldWithError
                id="email_address"
                value={emailAddress}
                setValue={setEmailAddress}
                autoComplete="username"
                type="email"
                maxLength={254}
                error={emailAddressError}
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th>
              <label htmlFor="password">Password:</label>
            </th>
            <td>
              <FieldWithError
                id="password"
                value={password}
                setValue={setPassword}
                autoComplete="new-password"
                type="password"
                maxLength={128}
                error={passwordError}
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th>
              <label htmlFor="verify_password">Verify Password:</label>
            </th>
            <td>
              <FieldWithError
                id="verify_password"
                value={verifyPassword}
                setValue={setVerifyPassword}
                autoComplete="new-password"
                type="password"
                maxLength={128}
                error={verifyPasswordError}
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th>
              <label htmlFor="is_admin">Admin:</label>
            </th>
            <td>
              <input
                id="is_admin"
                type="checkbox"
                checked={isAdmin}
                onChange={() => setIsAdmin(!isAdmin)}
                className="checkbox"
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th>
              <label htmlFor="is_teacher">Teacher:</label>
            </th>
            <td>
              <input
                id="is_teacher"
                type="checkbox"
                checked={isTeacher}
                onChange={() => setIsTeacher(!isTeacher)}
                className="checkbox"
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th>
              <label htmlFor="is_student">Student:</label>
            </th>
            <td>
              <input
                id="is_student"
                type="checkbox"
                checked={isStudent}
                onChange={() => setIsStudent(!isStudent)}
                className="checkbox"
              />
            </td>
          </tr>
          <tr hidden={districtId === -1}>
            <th></th>
            <td className="form-buttons">
              <div hidden={userId !== -1} onClick={upsertUser}>
                Add
              </div>
              <div hidden={userId === -1} onClick={upsertUser}>
                Update
              </div>
              <div
                className="delete-button"
                hidden={userId === -1}
                onClick={removeUser}
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
