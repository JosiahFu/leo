import {Outlet} from 'react-router';

function App() {
  return (
    <>
      <header className="site-header">
        <h1>Title</h1>
      </header>
      <Outlet />
    </>
  );
}

export default App;
