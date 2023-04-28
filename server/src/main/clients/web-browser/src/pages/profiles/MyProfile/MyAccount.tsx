import './MyAccount.scss';
import {SignupForm} from '../../../libs/SignupForm/SignupForm';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';

export function MyAccount() {
  return (
    <>
      <DefaultPage title="My Account">
        <SignupForm />
      </DefaultPage>
    </>
  );
}
