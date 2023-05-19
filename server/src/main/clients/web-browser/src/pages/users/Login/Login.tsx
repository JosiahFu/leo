import './Login.scss';
import {Layout} from 'antd';
import {useState} from 'react';
import {useNavigate} from 'react-router';
import {user_management} from '../../../generated/protobuf-js';
import UserManagementService = user_management.UserManagementService;
import {createService} from '../../../protos';
import {FieldWithError} from '../../../FieldWithError/FieldWithError';
import ILoginResponse = user_management.ILoginResponse;
import ILoginRequest = user_management.ILoginRequest;
import {login} from '../../../libs/authentication';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
const {Content} = Layout;

export function Login() {
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
    userManagementService
      .login(createLoginRequest())
      .then(response => {
        if (response.success) {
          login(response.user!);
          console.log('Here');
          navigate('/projects/ikigai-builder');
          window.location.reload();
        } else {
          parseLoginResponse(response);
        }
      })
      .catch(reason => console.log(reason));
  }

  function onCancel(): void {
    navigate('/');
  }

  return (
    <>
      <DefaultPage title="Login">
        <Layout style={{height: '100%'}}>
          <Content style={{borderRight: '#F0781F solid 1px', padding: '0.5em'}}>
            <div className="subtitle">Login</div>
            <div className="brief-instructions">
              Enter your e-mail address and password.
            </div>
            <div className="form-container">
              <table className="form">
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
                        <span
                          hidden={!loginFailure}
                          className="field_with_error"
                          style={{whiteSpace: 'nowrap'}}
                        >
                          Username or password incorrect.
                        </span>
                        &nbsp;
                        <input type="submit" value="Login" onClick={onLogin} />
                        &nbsp;
                        <input
                          type="button"
                          value="Cancel"
                          onClick={onCancel}
                        />
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </Content>
        </Layout>
      </DefaultPage>
    </>
  );
}
