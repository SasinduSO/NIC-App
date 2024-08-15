import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Bar, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import CustomNavbar from '../shared/ustomNavbar';
import Card from '../shared/Card';
import { Link } from 'react-router-dom';

// Register necessary components
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement
);

interface Summary {
  totalRecords: number;
  maleUsers: number;
  femaleUsers: number;
  recordsByFileName: Record<string, number>;
}

const Dashboard = () => {
  const [summary, setSummary] = useState<Summary | null>(null);

  useEffect(() => {
    axios.get('http://localhost:8083/api/dashboard/summary')
      .then(response => setSummary(response.data))
      .catch(error => console.error('Error fetching summary:', error));
  }, []);

  if (!summary) {
    return <p>Loading...</p>;
  }

  const barData = {
    labels: ['Male', 'Female'],
    datasets: [
      {
        label: '# of Users',
        data: [summary.maleUsers, summary.femaleUsers],
        backgroundColor: ['#36A2EB', '#FF6384'],
        borderWidth: 1,
      },
    ],
  };

  const pieData = {
    labels: Object.keys(summary.recordsByFileName),
    datasets: [
      {
        label: '# of Records',
        data: Object.values(summary.recordsByFileName),
        backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0'],
        hoverBackgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0'],
      },
    ],
  };



  return (
    <div style={{
      height: '100vh',
      display: 'flex',
      flexDirection: 'column',
      background: '#071952'
    }}>
      <CustomNavbar />
      <div className="flex flex-grow mt-20 px-4 py-6" style={{ overflow: 'auto' }}>
        {/* Left Side: Stacked "Dashboard Summary" and "Total Records" */}
        <div className="flex flex-col" style={{ flex: '1', marginRight: '10px' }}>
          <Card title="Dashboard Summary">
            <Link
              to="/report"
              className="inline-block text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-3 py-1 text-center dark:bg-[#071952] dark:hover:bg-blue-700 dark:focus:ring-blue-800"
              style={{ textDecoration: 'none' }}
            >
              Generate Report
            </Link>
          </Card>
          <div className="mt-6">
            <Card title="Total Valid Records:">
              <div>
                <h2 className="text-6xl"> {summary.totalRecords}</h2>
              </div>
            </Card>
          </div>

        </div>

        {/* Right Side: Stacked Charts */}
        <div className="flex flex-col" style={{ flex: '1', marginLeft: '10px' }}>
          <Card title="Gender Distribution">
            <Bar data={barData} />
          </Card>
          <Card title="Records by File Name">
            <Pie data={pieData} />
          </Card>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
