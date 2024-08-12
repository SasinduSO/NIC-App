// src/routes/AppRouter.tsx
import React from 'react';

import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import App from './App';
import Login from './shared/Login'; 

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        path: '/',
        element: <Login />, // Default page (login form)
      },
      /*
      {
        path: '/dashboard',
        element: <Dashboard />, // Example of a protected user route
      },

      */
    ],
  },
]);

const Router = () => {
  return <RouterProvider router={router} />;
};

export default Router;
