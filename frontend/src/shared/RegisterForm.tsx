import React, { useState } from 'react';
import axios from 'axios';
import CustomNavbar from '../shared/ustomNavbar';

function Register() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    if (password !== confirmPassword) {
      setMessage("Passwords do not match.");
      return;
    }

    try {
      const response = await axios.post('http://localhost:8082/api/users/register', {
        email,
        password
      });

      if (response.status === 201) {
        setMessage("Registration successful!");
      } else {
        setMessage("Registration failed.");
        console.log(response);
      }
    } catch (error) {
      setMessage("An error occurred during registration.");
    }
  };

  return (
    <div className='backg' style={{background:'#9CDBA6'}}>
      <CustomNavbar />
      <main className="pt-20">
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
          <h1 className="text-5xl font-bold mb-8 text-white">Sri Lanka NIC-Validator</h1>
          <div className="bg-white shadow-md rounded-lg p-6 max-w-sm w-full">
            <h2 className="text-xl font-semibold mb-4">Register</h2>
            <form onSubmit={handleSubmit} className="flex flex-col">

              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Email"
                required
                className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 mb-4"
              />
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Password"
                required
                className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 mb-4"
              />
              <input
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="Confirm Password"
                required
                className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 mb-4"
              />
              <button 
                type="submit" 
                className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full px-5 py-2.5"
              >
                Register
              </button>
            </form>
            {message && <p className="mt-4 text-center text-gray-900">{message}</p>}
          </div>
        </div>
      </main>
    </div>
  );
}

export default Register;
