import './CreateUser.scss';
import {createService, user_management} from '../../protos';
import {ChangeEvent, useState} from 'react';
import {FieldWithError} from '../../FieldWithError/FieldWithError';
import {useNavigate} from 'react-router';
import Operation = user_management.Operation;
import Role = user_management.Role;
import UserManagementService = user_management.UserManagementService;
import IUpsertUserRequest = user_management.IUpsertUserRequest;
import IUpsertUserResponse = user_management.IUpsertUserResponse;

export default function CreateUser() {
  const userManagementService = createService(
    UserManagementService,
    'UserManagementService'
  );
  const navigate = useNavigate();

  const [role, setRole] = useState(Role.TEACHER);

  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [emailAddress, setEmailAddress] = useState('');
  const [password, setPassword] = useState('');
  const [verifyPassword, setVerifyPassword] = useState('');

  const [firstNameError, setFirstNameError] = useState('');
  const [lastNameError, setLastNameError] = useState('');
  const [emailAddressError, setEmailAddressError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const [verifyPasswordError, setVerifyPasswordError] = useState('');

  function onRoleChanged(event: ChangeEvent<HTMLSelectElement>): void {
    setRole(Role[event.target.value as keyof typeof Role]);
  }

  function createUpsertUserRequest(operation: Operation): IUpsertUserRequest {
    return {
      operation: operation,
      role: role,
      firstName: firstName,
      lastName: lastName,
      emailAddress: emailAddress,
      password: password,
      verifyPassword: verifyPassword,
    } as IUpsertUserRequest;
  }

  function parseUpsertUserResponse(response: IUpsertUserResponse): void {
    setFirstNameError(response.firstNameError || '');
    setLastNameError(response.lastNameError || '');
    setEmailAddressError(response.emailAddressError || '');
    setPasswordError(response.passwordError || '');
    setVerifyPasswordError(response.verifyPasswordError || '');
  }

  function onBlur(): void {
    // Do nothing.
    // userManagementService
    //   .upsertUser(createUpsertUserRequest(Operation.CHECK))
    //   .then(response => parseUpsertUserResponse(response))
    //   // If there's an exception that's okay for the check.
    //   .catch(error => console.log(error));
  }

  function onAddUser(): void {
    userManagementService
      .upsertUser(createUpsertUserRequest(Operation.INSERT))
      .then(response => {
        if (response.success) {
          navigate('/login');
        } else {
          parseUpsertUserResponse(response);
        }
      });
  }

  function onCancel(): void {
    navigate('/');
  }

  return (
    <>
      <h1>Create User</h1>
      <div className="form-container">
        <form>
          <table>
            <tbody>
              <tr>
                <th>
                  <label htmlFor="role">User Type:</label>
                </th>
                <td>
                  <select
                    id="role"
                    value={Role[role]}
                    onChange={onRoleChanged}
                    onBlur={onBlur}
                  >
                    {Object.keys(Role).map((key: string) => (
                      <option key={key} value={key}>
                        {key}
                      </option>
                    ))}
                  </select>
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="first_name">First Name:</label>
                </th>
                <td>
                  <FieldWithError
                    id="first_name"
                    value={firstName}
                    setValue={setFirstName}
                    autoComplete="given-name"
                    onBlur={onBlur}
                    type="text"
                    maxLength={255}
                    error={firstNameError}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="last_name">Last Name:</label>
                </th>
                <td>
                  <FieldWithError
                    id="last_name"
                    value={lastName}
                    setValue={setLastName}
                    autoComplete="family-name"
                    onBlur={onBlur}
                    type="text"
                    maxLength={255}
                    error={lastNameError}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="email_address">Email Address:</label>
                </th>
                <td>
                  <FieldWithError
                    id="email_address"
                    value={emailAddress}
                    setValue={setEmailAddress}
                    autoComplete="username"
                    onBlur={onBlur}
                    type="email"
                    maxLength={254}
                    error={emailAddressError}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="password">Password:</label>
                </th>
                <td>
                  <FieldWithError
                    id="password"
                    value={password}
                    setValue={setPassword}
                    autoComplete="new-password"
                    onBlur={onBlur}
                    type="password"
                    maxLength={128}
                    error={passwordError}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="verify_password">Verify Password:</label>
                </th>
                <td>
                  <FieldWithError
                    id="verify_password"
                    value={verifyPassword}
                    setValue={setVerifyPassword}
                    autoComplete="new-password"
                    onBlur={onBlur}
                    type="password"
                    maxLength={128}
                    error={verifyPasswordError}
                  />
                </td>
              </tr>
              <tr>
                <td colSpan={2}>
                  <div className="form-buttons">
                    <input type="button" value="Add User" onClick={onAddUser} />
                    &nbsp;
                    <input type="button" value="Cancel" onClick={onCancel} />
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </form>
      </div>
    </>
  );
}
