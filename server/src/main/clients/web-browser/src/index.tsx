import React from 'react';
import ReactDOM from 'react-dom/client';
import {createBrowserRouter, RouterProvider} from 'react-router-dom';
import './index.scss';
import reportWebVitals from './reportWebVitals';

import Root from './pages/Root';
import StudentNav from './pages/student/Nav';
import StudentProjectGen from './pages/student/ProjectGen';
import StudentProjectImplementation from './pages/student/ProjectImplement';
import StudentUpload from './pages/student/Upload';

const router = createBrowserRouter([
  {
    path: '/',
    element: <Root />,
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
        path: 'project-implement',
        element: <StudentProjectImplementation />,
      },
      {
        path: 'upload',
        element: <StudentUpload />,
      },
    ],
  },
]);

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
