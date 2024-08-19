// src/routes/AppRouter.tsx
import React from 'react';

import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import App from './App';
import Login from './shared/Login'; 
import Home from './User/Home';
import Dashboard from './User/Dasboard';
import DataView from './User/DataView';
import Report from './User/Report';
import ForgotPassword from './shared/ForgotPassword';
import Register from './shared/RegisterForm'
import Invalid from './User/InvalidTable'

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
        path: '/Fgpass',
        element: <ForgotPassword/>, // forhot password pathwy
      },
      {
        path: '/Home',
        element: <Home/> //home route
      },
      
      {
        path: '/dashboard',
        element: <Dashboard />, //dashbpard pathway
      },
      {
        path: '/dataView',
        element: <DataView/>, // Valid Nic view pathway
      },
      {
        path: '/InvalidView',
        element: <Invalid/>, // Register pathwy
      },
      {
        path: '/report',
        element: <Report/>, // Report pathwy
      },
      {
        path: '/register',
        element: <Register/>, // Register pathwy
      },

      
    ],
  },




]);

const Router = () => {
  return <RouterProvider router={router} />;
};

export default Router;
