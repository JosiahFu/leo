import './index.scss';
import React from 'react';
import ReactDOM from 'react-dom/client';
import {createBrowserRouter, RouterProvider} from 'react-router-dom';
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
import {DefaultPageNav} from './libs/DefaultPage/DefaultPageNav';
import {IkigaiBuilder} from './pages/projects/IkigaiBuilder/IkigaiBuilder';
import {MyProjects} from './pages/projects/MyProjects/MyProjects';
import {MyAccount} from './pages/profiles/MyProfile/MyAccount';
import {PrivacyPolicy} from './pages/docs/PrivacyPolicy';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

const router = createBrowserRouter([
  {
    path: '/login',
    element: <DefaultPageNav />,
    children: [
      {
        path: '',
        element: <Login />,
      },
    ],
  },
  {
    path: '/docs',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'privacy-policy',
        element: <PrivacyPolicy />,
      },
    ],
  },
  {
    path: '/profiles',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'edit-districts',
        element: <EditDistricts />,
      },
      {
        path: 'edit-schools',
        element: <EditSchools />,
      },
      {
        path: 'edit-users',
        element: <EditUsers />,
      },
      {
        path: 'my-account',
        element: <MyAccount />,
      },
    ],
  },
  {
    path: '/projects',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'ikigai-builder',
        element: <IkigaiBuilder />,
      },
      {
        path: 'my-projects',
        element: <MyProjects />,
      },
    ],
  },
  {
    path: '/student',
    element: <StudentNav />,
    children: [
      {
        path: 'project-gen',
        element: <StudentProjectGen />,
      },
      {
        path: 'project-implementation',
        element: <StudentProjectImplementation />,
      },
      {
        path: 'upload',
        element: <StudentUpload />,
      },
    ],
  },
  {
    path: '',
    children: [
      {
        path: '',
        element: <Root />,
      },
      {
        path: '/',
        element: <Root />,
      },
    ],
  },
]);

root.render(
  <React.StrictMode>
    <>
      <RouterProvider router={router} />
    </>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
