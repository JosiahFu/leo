import './MyAccount.scss';
import {SignupForm} from '../../../libs/SignupForm/SignupForm';
import {PageHeader} from '../../../libs/PageHeader/PageHeader';

export function MyAccount() {
  return (
    <>
      <PageHeader title="My Account" />
      <SignupForm />
    </>
  );
}
