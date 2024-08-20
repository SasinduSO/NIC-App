import React, { useState } from 'react';
import CustomNavbar from '../shared/ustomNavbar';
import Checkbox from '@mui/material/Checkbox';

const ReportGenerator: React.FC = () => {
  const [format, setFormat] = useState('pdf');
  const [selectedSummaries, setSelectedSummaries] = useState({
    includeFemaleNics: true,
    includeMaleNics: true,
    includeTotalRecords: true,
    includeInvalidRecords: true,
    includeTotalInvalidRecords: true,
    includeTotalValidRecords: true
  });

  const handleFormatChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setFormat(event.target.value);
  };

  const handleCheckboxChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSelectedSummaries({
      ...selectedSummaries,
      [event.target.name]: event.target.checked,
    });
  };

  const handleGenerateReport = async () => {
    const queryString = new URLSearchParams({
      format,
      includeFemaleNics: selectedSummaries.includeFemaleNics.toString(),
      includeMaleNics: selectedSummaries.includeMaleNics.toString(),
      includeTotalRecords: selectedSummaries.includeTotalRecords.toString(),
      includeInvalidRecords: selectedSummaries.includeInvalidRecords.toString(),
      includeTotalInvalidRecords: selectedSummaries.includeTotalInvalidRecords.toString(),
      includeTotalValidRecords: selectedSummaries.includeTotalValidRecords.toString(),
    }).toString();

    try {
      const response = await fetch(`http://localhost:8083/api/dashboard/generate-report?${queryString}`, {
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

      <div className="container mx-auto p-6 mt-20">
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
          
          <div>
            <label>
              <Checkbox
                name="includeFemaleNics"
                checked={selectedSummaries.includeFemaleNics}
                onChange={handleCheckboxChange}
              />
              Include Total Number of Female NIC Records
            </label>
            <br/>
            <label>
              <Checkbox
                name="includeMaleNics"
                checked={selectedSummaries.includeMaleNics}
                onChange={handleCheckboxChange}
              />
              Include Total Number of Male NIC Records
            </label>
            <br/>
            <label>
              <Checkbox
                name="includeTotalRecords"
                checked={selectedSummaries.includeTotalRecords}
                onChange={handleCheckboxChange}
              />
              Include Total Numver of Valid Records
            </label>
            <br/>
            <label>
              <Checkbox
                name="includeInvalidRecords"
                checked={selectedSummaries.includeInvalidRecords}
                onChange={handleCheckboxChange}
              />
              Include Total Numver of Invalid Records
            </label>
            <br/>
            <label>
              <Checkbox
                name="includeTotalValidRecords"
                checked={selectedSummaries.includeTotalValidRecords}
                onChange={handleCheckboxChange}
              />
              Include Valid NIC Records Table
            </label>
            <br/>
            <label>
              <Checkbox
                name="includeTotalInvalidRecords"
                checked={selectedSummaries.includeTotalInvalidRecords}
                onChange={handleCheckboxChange}
              />
              Include Invalid NIC Records Table
            </label>
          </div>
          
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
