import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Bar, Pie, Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, LineElement, Title, Tooltip, Legend, ArcElement, PointElement } from 'chart.js';
import CustomNavbar from '../shared/ustomNavbar'; // Fixed import path
import Card from '../shared/Card';
import Card2 from '../shared/Card2'; // Import Card2

// Register necessary components
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
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
  totalInvalidRecords: number;
  invalidRecordsByFileName: Record<string, number>;
  ageDistributionPercentages: Record<string, number>;
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

  const getRandomColor = () => {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  };

  const pieColors = Object.keys(summary.recordsByFileName).map(() => getRandomColor());
  const invalidPieColors = Object.keys(summary.invalidRecordsByFileName).map(() => getRandomColor());

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
  };

  const barData = {
    labels: ['Male', 'Female'],
    datasets: [
      {
        label: 'Genders of Validated NIC',
        data: [summary.maleUsers, summary.femaleUsers],
        backgroundColor: ['#36A2EB', '#FF6384'],
        borderWidth: 1,
        borderColor:'#000000'
      },
    ],
  };

  const pieData = {
    labels: Object.keys(summary.recordsByFileName),
    datasets: [
      {
        label: 'Number of Records',
        data: Object.values(summary.recordsByFileName),
        backgroundColor: pieColors,
        hoverBackgroundColor: pieColors,
        borderColor:'#000000'
      },
    ],
  };

  const invalidPieData = {
    labels: Object.keys(summary.invalidRecordsByFileName),
    datasets: [
      {
        label: 'Number of Invalid Records',
        data: Object.values(summary.invalidRecordsByFileName),
        backgroundColor: invalidPieColors,
        hoverBackgroundColor: invalidPieColors,
        borderColor:'#000000'
      },
    ],
  };

  const ageBarData = {
    labels: Object.keys(summary.ageDistributionPercentages),
    datasets: [
      {
        label: 'Percentage of People',
        data: Object.values(summary.ageDistributionPercentages),
        backgroundColor: '#000000',
        borderColor: '#FFD700',
        borderWidth: 4,
        fill: false,
        pointRadius: 8,
        tension: 0.5,

      },
    ],
  };

  return (
    <div className="container-fluid" style={{ height: '100vh', display: 'flex', flexDirection: 'column', background: '#071952' }}>
      <CustomNavbar />
      <div className="flex-grow mt-20" style={{ overflow: 'auto' }}>
      
        <div className="row justify-content-center mb-2">
          <div className="col-lg-5 col-md-6 mb-2 ml-40">
            <Card title="Total Valid Records:">
              <div>
                <h2 className="text-6xl">{summary.totalRecords}</h2>
              </div>
            </Card>
          </div>
          <div className="col-lg-5 col-md-6 mb-2">
            <Card title="Total Invalid Records:">
              <div>
                <h2 className="text-6xl">{summary.totalInvalidRecords}</h2>
              </div>
            </Card>
          </div>
        </div>
      
        <div className="row  mb-2">
          <div className="col-lg-4 col-md-6 mb-3 p-0">
            <Card2 title="Records by File Name">
              <div style={{ height: '300px' }}>
                <Pie data={pieData} options={chartOptions} />
              </div>
            </Card2>
          </div>
          <div className="col-lg-4 col-md-6 mb-3 p-0">
            <Card2 title="Gender Distribution">
              <div style={{ height: '300px' }}>
                <Bar data={barData} options={chartOptions} />
              </div>
            </Card2>
          </div>
          <div className="col-lg-4 col-md-6 mb-3 p-0">
            <Card2 title="Invalid Records by File Name">
              <div style={{ height: '300px' }}>
                <Pie data={invalidPieData} options={chartOptions} />
              </div>
            </Card2>
          </div>
        </div>
        <div className="row mb-4">
          <div className="col-12">
            <Card2 title="Age Distribution Percentages">
              <div style={{ height: '400px', width: '100%' }}>
                <Line data={ageBarData} options={chartOptions} />
              </div>
            </Card2>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;

/*
 <Card title="Dashboard Summary">
              <Link
                to="/report"
                className="inline-block text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-3 py-1 text-center dark:bg-[#071952] dark:hover:bg-blue-700 dark:focus:ring-blue-800"
                style={{ textDecoration: 'none' }}
              >
                Generate Report
              </Link>
            </Card>
*/