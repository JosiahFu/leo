import './index.scss';
import React from 'react';
import ReactDOM from 'react-dom/client';
import {HashRouter, Route, Routes} from 'react-router-dom';
import reportWebVitals from './reportWebVitals';

import {Root} from './pages/Root';
import StudentNav from './pages/student/Nav';
import StudentProjectGen from './pages/student/ProjectGen';
import StudentProjectImplementation from './pages/student/ProjectImplement';
import StudentUpload from './pages/student/Upload';
import CreateUser from './pages/create-user/CreateUser';
import Login from './pages/login/Login';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

// We use HashRouter as a temporary fix for a longer-term solution.
root.render(
  <React.StrictMode>
    <div className="header">
      <a href="/">PROJECT LEO</a>
    </div>
    <div className="main">
      <HashRouter>
        <Routes>
          <Route path="/" Component={Root} />
          <Route path="/create-user" Component={CreateUser} />
          <Route path="/login" Component={Login} />
          <Route path="/studdent" Component={StudentNav} />
          <Route path="/student/project-gen" Component={StudentProjectGen} />
          <Route
            path="/student/project-implement"
            Component={StudentProjectImplementation}
          />
          <Route path="/student/upload" Component={StudentUpload} />
        </Routes>
      </HashRouter>
    </div>
    <div className="footer">&nbsp;</div>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
