import React, { useState } from 'react';
import axios from 'axios';
import CustomNavbar from '../shared/ustomNavbar';

function Home() {
  const [selectedFiles, setSelectedFiles] = useState<FileList | null>(null);
  const [message, setMessage] = useState('');

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSelectedFiles(event.target.files);
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    if (!selectedFiles || selectedFiles.length === 0) {
      setMessage('Please select at least one CSV file.');
      return;
    }

    const formData = new FormData();
    Array.from(selectedFiles).forEach((file) => {
      formData.append('files', file);
    });

    try {
      const response = await axios.post('http://localhost:8080/api/nics/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      if (response.status === 200) {
        setMessage('Files uploaded and validated successfully!');
      } else {
        setMessage('Failed to validate files.');
      }
    } catch (error) {
      setMessage('An error occurred during file upload.');
    }
  };

  return (
    
    <div className='backg' style={{background:'#9CDBA6'}}>
      <CustomNavbar />
      <main className="pt-20"> {/* Added padding top to avoid overlap with navbar */}
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
            <h2 className="text-xl font-semibold mb-4">Upload CSV Files</h2>
            <p className="mb-4 text-sm text-gray-600">Please select all files at once for multiple inputs.</p>
            <form onSubmit={handleSubmit} className="flex flex-col">
              <input 
                type="file" 
                multiple 
                accept=".csv" 
                onChange={handleFileChange}
                required 
                className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 mb-4"
              />
              <button 
                type="submit" 
                className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full px-5 py-2.5"
              >
                Upload and Validate
              </button>
            </form>
            {message && <p className="mt-4 text-center text-gray-900">{message}</p>}
          </div>
        </div>
      </main>
    </div>
  );
}

export default Home;
