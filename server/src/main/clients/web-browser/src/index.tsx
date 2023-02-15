import React from 'react';
import ReactDOM from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import './index.scss';
import reportWebVitals from './reportWebVitals';

import StudentProjectGen from './pages/StudentProjectGen';
import Root from './pages/Root';
import StudentProjectImplementation from './pages/StudentProjectImplement';

const router = createBrowserRouter([
    {
        path: '/',
        element: <Root />
    },
    {
        path: '/student/project-gen',
        element: <StudentProjectGen />
    },
    {
        path: '/student/project-implement',
        element: <StudentProjectImplementation />
    }
])

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
