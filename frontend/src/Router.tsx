// src/routes/AppRouter.tsx
import React from 'react';

import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import App from './App';
import Login from './shared/Login'; 
import Home from './User/Home';
import Dashboard from './User/Dasboard';
import DataView from './User/DataView';
import Report from './User/Report';

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        path: '/',
        element: <Login />, // Default page (login form)
      },
      {
        path: '/Home',
        element: <Home/> //home route
      },
      
      {
        path: '/dashboard',
        element: <Dashboard />, // Example of a protected user route
      },
      {
        path: '/dataView',
        element: <DataView/>, // Example of a protected user route
      },
      {
        path: '/report',
        element: <Report/>, // Example of a protected user route
      },

      
    ],
  },
]);

const Router = () => {
  return <RouterProvider router={router} />;
};

export default Router;
