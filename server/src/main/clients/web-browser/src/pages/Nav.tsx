import '../index.scss';
import {Outlet} from 'react-router';

export default function Login() {
  return (
    <>
      <header>
        <a href="/">PROJECT LEO</a>
      </header>
      <main>
        <Outlet />
      </main>
      <footer>&nbsp;</footer>
    </>
  );
}
