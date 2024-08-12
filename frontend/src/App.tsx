import React from 'react';
import { Outlet } from 'react-router-dom';
import Header from './shared/Header';
import Footer from './shared/Footer';
import './css/Login.css'
import './App.css';


function App() {
  return (
    <>
      <div>
      
        <Outlet />
      </div>

    </>
  );
}

export default App;

//

/*

*/