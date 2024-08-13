import React, { useState }  from 'react';
import axios from 'axios';

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

    
    //http://localhost:8080/api/nics/upload
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
    <div style={{ padding: '20px', maxWidth: '400px', margin: '0 auto' }}>
      <h2>Upload CSV Files</h2>
      <form onSubmit={handleSubmit}>
        <input 
          type="file" 
          multiple 
          accept=".csv" 
          onChange={handleFileChange}
          required 
        />
        <button type="submit" style={{ marginTop: '10px' }}>
          Upload and Validate
        </button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
}

export default Home;
