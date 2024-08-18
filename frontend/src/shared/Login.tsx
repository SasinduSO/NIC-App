import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
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

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate(); // Hook to handle navigation
 
  //async to get inof back
  const handleLogin = async (event: React.FormEvent) => {
    event.preventDefault();

    try {
      const response = await axios.post('http://localhost:8082/api/users/login', {
        email,
        password
      });

      // catching response message token and reroute
      if (response.status === 200) {
        
        navigate('/Home'); 
      }
    } catch (error) {
      // login error
      setError('Invalid Email or password');
    }
  };


  return (
    
    <div
    className="login-page"
    style={{
      height: '100vh',
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'center',
      alignItems: 'center',
      background: '#071952'
    }}
  >
      <h1 className="text-3xl font-bold mb-4 text-white">Sri Lanka NIC-Validator</h1>
      <MDBContainer className="p-4 my-5 d-flex flex-column w-100" style={{ maxWidth: '400px', backgroundColor: 'white', borderRadius: '8px', boxShadow: '0px 0px 15px rgba(0, 0, 0, 0.1)' }}>

      <form onSubmit={handleLogin}>
        <MDBInput wrapperClass='mb-4' label='Email' id='form1' type='email' 
        onChange={(e) => setEmail(e.target.value)} 
        required
        />
        <MDBInput wrapperClass='mb-4' label='Password' id='form2' type='password'
        onChange={(e) => setPassword(e.target.value)}
        required
        />

        <div className="d-flex justify-content-between mx-3 mb-4">
          <a href="/Fgpass">Forgot password?</a>
        </div>

        <MDBBtn type="submit" className="mb-4 w-100">Sign in</MDBBtn>
        </form>

        {error && <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>}  
        
        <div className="text-center">
          <p>Not a member? <a href="/register">Register</a></p>

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