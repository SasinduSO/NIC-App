import React, { useState } from 'react';
import '../css/Login.css';

import {
  MDBContainer,
  MDBInput,
  MDBCheckbox,
  MDBBtn,
  MDBIcon
}
  from 'mdb-react-ui-kit';
function Login() {
  return (
    <div className="login-page" style={{ height: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center',background:'#138D75' }}>
      <MDBContainer className="p-4 my-5 d-flex flex-column w-100" style={{ maxWidth: '400px', backgroundColor: 'white', borderRadius: '8px', boxShadow: '0px 0px 15px rgba(0, 0, 0, 0.1)' }}>

        <MDBInput wrapperClass='mb-4' label='Username' id='form1' type='email' />
        <MDBInput wrapperClass='mb-4' label='Password' id='form2' type='password' />

        <div className="d-flex justify-content-between mx-3 mb-4">
          <a href="#!">Forgot password?</a>
        </div>

        <MDBBtn className="mb-4 w-100">Sign in</MDBBtn>

        <div className="text-center">
          <p>Not a member? <a href="#!">Register</a></p>
          
        </div>

      </MDBContainer>
    </div>
  );
}

export default Login;

/*
<p>or sign up with:</p>

          <div className='d-flex justify-content-between mx-auto' style={{ width: '60%' }}>
            <MDBBtn tag='a' color='none' className='m-1' style={{ color: '#1266f1' }}>
              <MDBIcon fab icon='facebook-f' size="sm" />
            </MDBBtn>

            <MDBBtn tag='a' color='none' className='m-1' style={{ color: '#1266f1' }}>
              <MDBIcon fab icon='twitter' size="sm" />
            </MDBBtn>

            <MDBBtn tag='a' color='none' className='m-1' style={{ color: '#1266f1' }}>
              <MDBIcon fab icon='google' size="sm" />
            </MDBBtn>

            <MDBBtn tag='a' color='none' className='m-1' style={{ color: '#1266f1' }}>
              <MDBIcon fab icon='github' size="sm" />
            </MDBBtn>
          </div>

*/