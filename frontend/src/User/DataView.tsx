import React, { useState, useEffect } from 'react';
import axios from 'axios';
import CustomNavbar from '../shared/ustomNavbar';
const DataView: React.FC = () => {
  const [nics, setNics] = useState<any[]>([]);

  useEffect(() => {
    // Fetch all NICs from the backend
    axios.get('http://localhost:8081/api/nicM/all')
      .then(response => {
        setNics(response.data);
      })
      .catch(error => {
        console.error('Error fetching NICs:', error);
      });
  }, []);

  const handleDelete = (nic_no: string) => {
    axios.delete(`/api/nicM/${nic_no}`)
      .then(() => {
        // Remove the deleted NIC from state
        setNics(nics.filter(nic => nic.nic_no !== nic_no));
      })
      .catch(error => {
        console.error('Error deleting NIC:', error);
      });
  };

  return (
    <div className="relative overflow-x-auto shadow-md sm:rounded-lg"
      style={{
        height: '100vh',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        background: '#3C3D37'
      }}>

      <CustomNavbar/>

      <caption className="p-5 text-lg font-semibold text-left rtl:text-right text-white-900 bg-[#3C3D37] text-[#F6F5F5] dark:bg-gray-800 mt-40">
        All Validated Nics are displayed here
      </caption>
      <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
        <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
          <tr>
            <th scope="col" className="px-6 py-3">NIC Number</th>
            <th scope="col" className="px-6 py-3">Gender</th>
            <th scope="col" className="px-6 py-3">Age</th>
            <th scope="col" className="px-6 py-3">Birth Date</th>
            <th scope="col" className="px-6 py-3">File Name</th>
            <th scope="col" className="px-6 py-3"><span className="sr-only">Delete</span></th>
          </tr>
        </thead>
        <tbody>
          {nics.map(nic => (
            <tr key={nic.nic_no} className="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
              <td className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">{nic.nic_no}</td>
              <td className="px-6 py-4">{nic.gender}</td>
              <td className="px-6 py-4">{nic.age}</td>
              <td className="px-6 py-4">{new Date(nic.birthDate).toLocaleDateString()}</td>
              <td className="px-6 py-4">{nic.fileName}</td>
              <td className="px-6 py-4 text-right">
                <button
                  onClick={() => handleDelete(nic.nic_no)}
                  className="font-medium text-red-600 dark:text-red-500 hover:underline"
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default DataView;
