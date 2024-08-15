// src/User/ReportGenerator.tsx
import React, { useState } from 'react';
import CustomNavbar from '../shared/ustomNavbar';

const ReportGenerator: React.FC = () => {
  const [format, setFormat] = useState('pdf');

  const handleFormatChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setFormat(event.target.value);
  };

  const handleGenerateReport = async () => {
    try {
      const response = await fetch(`http://localhost:8083/api/dashboard/generate-report?format=${format}`, {
        method: 'GET',
      });
  
      if (response.ok) {
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `nic_report.${format}`;
        a.click();
        window.URL.revokeObjectURL(url);
      } else {
        console.error('Error generating report:', response.statusText);
      }
    } catch (error) {
      console.error('Error generating report:', error);
    }
  };

  return (
    <div className="bg-[#071952]"
    style={{
      height: '100vh',
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'center',
      alignItems: 'center'
    }}
    >
    <CustomNavbar />
    
    <div className="container mx-auto p-6 mt-20"> {/* Added mt-20 for margin top */}
      <h2 className="text-2xl font-bold mb-4 text-white">Generate Report</h2>
      <div className="bg-white p-6 rounded-lg shadow-lg">
        <label htmlFor="format" className="block text-lg font-medium mb-2">Select File Format:</label>
        <select
          id="format"
          value={format}
          onChange={handleFormatChange}
          className="mb-4 p-2 border border-gray-300 rounded-md"
        >
          <option value="pdf">PDF</option>
          <option value="csv">CSV</option>
          <option value="xlsx">Excel</option>
        </select>
        <button
          onClick={handleGenerateReport}
          className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600"
        >
          Generate Report
        </button>
      </div>
    </div>
  </div>
  
  );
};

export default ReportGenerator;
