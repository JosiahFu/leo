import './MyAccount.scss';
import {SignupForm} from '../../../libs/SignupForm/SignupForm';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser} from '../../../libs/authentication';

export function MyAccount() {
  const user = getCurrentUser();
  if (user == null) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="My Account">
        <SignupForm />
      </DefaultPage>
    </>
  );
}
