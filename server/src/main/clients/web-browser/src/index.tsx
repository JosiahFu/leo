import './index.scss';
import React from 'react';
import ReactDOM from 'react-dom/client';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import reportWebVitals from './reportWebVitals';

import {Root} from './pages/Root';
import StudentNav from './pages/student/Nav';
import StudentProjectGen from './pages/student/ProjectGen';
import StudentProjectImplementation from './pages/student/ProjectImplement';
import StudentUpload from './pages/student/Upload';
import {EditUsers} from './pages/profiles/EditUsers/EditUsers';
import Login from './pages/login/Login';
import {EditDistricts} from './pages/profiles/EditDistricts/EditDistricts';
import {EditSchools} from './pages/profiles/EditSchools/EditSchools';
import {LandingPageNav} from './pages/LandingPageNav';
import {DefaultPageNav} from './pages/DefaultPageNav';
import {IkigaiBuilder} from './pages/projects/IkigaiBuilder/IkigaiBuilder';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/profiles" Component={DefaultPageNav}>
          <Route path="edit-districts" Component={EditDistricts} />
          <Route path="edit-schools" Component={EditSchools} />
          <Route path="edit-users" Component={EditUsers} />
        </Route>
        <Route path="/projects" Component={DefaultPageNav}>
          <Route path="ikigai-builder" Component={IkigaiBuilder} />
        </Route>
        <Route path="/login" Component={DefaultPageNav}>
          <Route path="" Component={Login} />
        </Route>
        <Route path="/" Component={LandingPageNav}>
          <Route path="" Component={Root} />
          <Route path="/studdent" Component={StudentNav} />
          <Route path="/student/project-gen" Component={StudentProjectGen} />
          <Route
            path="/student/project-implement"
            Component={StudentProjectImplementation}
          />
          <Route path="/student/upload" Component={StudentUpload} />
        </Route>
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
