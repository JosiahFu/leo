import './Login.scss';
import {createService, user_management} from '../../protos';
import {useState} from 'react';
import {FieldWithError} from '../../FieldWithError/FieldWithError';
import {useNavigate} from 'react-router';
import UserManagementService = user_management.UserManagementService;
import ILoginRequest = user_management.ILoginRequest;
import ILoginResponse = user_management.ILoginResponse;

export default function Login() {
  const userManagementService = createService(
    UserManagementService,
    'UserManagementService'
  );
  const navigate = useNavigate();

  const [emailAddress, setEmailAddress] = useState('');
  const [password, setPassword] = useState('');

  const [emailAddressError, setEmailAddressError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const [loginFailure, setLoginFailure] = useState(false);

  function createLoginRequest(): ILoginRequest {
    return {
      emailAddress: emailAddress,
      password: password,
    } as ILoginRequest;
  }

  function parseLoginResponse(response: ILoginResponse): void {
    setEmailAddressError(response.emailAddressError || '');
    setPasswordError(response.passwordError || '');
    setLoginFailure(!!response.loginFailure);
  }

  function onLogin(): void {
    userManagementService.login(createLoginRequest()).then(response => {
      if (response.success) {
        navigate('/');
      } else {
        parseLoginResponse(response);
      }
    });
  }

  function onCancel(): void {
    navigate('/');
  }

  return (
    <>
      <h1>Login</h1>
      <div className="form-container">
        <form>
          <table>
            <tbody>
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
                    autoComplete="current-password"
                    type="password"
                    maxLength={128}
                    error={passwordError}
                  />
                </td>
              </tr>
              <tr>
                <td colSpan={2}>
                  <div className="form-buttons">
                    <div hidden={!loginFailure} className="field_with_error">
                      Username or password incorrect.
                    </div>
                    &nbsp;
                    <input type="submit" value="Login" onClick={onLogin} />
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
