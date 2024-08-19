import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link , useNavigate} from 'react-router-dom';
import CustomNavbar from '../shared/ustomNavbar';
import ReactPaginate from 'react-paginate';
import { FormControlLabel, Switch } from '@mui/material'; // Import Material UI components


const InvalidView: React.FC = () => {
    const [invalidNics, setInvalidNics] = useState<any[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [itemsPerPage] = useState(10);
    const navigate = useNavigate();


    // Setting the default toggle states: Invalid NICs shown as false, Valid NICs as true
    const [state, setState] = useState({
        Invalid: true,
        Valid: false,
    });

    useEffect(() => {
        axios.get('http://localhost:8081/api/nicM/InvalidAll')
            .then(response => {
                setInvalidNics(response.data);
            })
            .catch(error => {
                console.error('Error fetching invalid NICs:', error);
            });
    }, []);

    const handlePageClick = (event: { selected: number }) => {
        setCurrentPage(event.selected);
    };
    const handleDelete = (nic_no: string) => {
        axios.delete(`/api/nicM/${nic_no}`)
            .then(() => {
                setInvalidNics(invalidNics.filter(nic => nic.nic_no !== nic_no));
            })
            .catch(error => {
                console.error('Error deleting NIC:', error);
            });
    };

    const handleToggle = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, checked } = event.target;
        setState({
            ...state,
            [name]: checked,
        });

        if (checked && name === 'Valid') {
            navigate('/invalidview'); // Redirect to /invalidview if the toggle is switched on
        } else if (!checked && name === 'Invalid') {
            navigate('/dataView'); // Redirect to /dataView if the toggle is switched off
        }
    };


    // Paginate NICs
    const indexOfLastItem = (currentPage + 1) * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = invalidNics.slice(indexOfFirstItem, indexOfLastItem);

    return (
        <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
            <CustomNavbar />

            <div className="mt-16 px-4 py-8 bg-[#071952] min-h-screen">
                <div className="flex items-center justify-between mb-4">
                    <caption className="text-lg mt-3 font-semibold text-left rtl:text-right text-white-900 bg-[#51829B] text-[#000000] dark:bg-gray-800 rounded-lg overflow-hidden whitespace-nowrap p-3">
                        All Rejected NICs are displayed here
                    </caption>

                    <div className="bg-[#51829B] rounded-lg shadow-md p-3 flex items-center justify-between">
                        <Link
                            to="/report"
                            className="inline-block text-white bg-black hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-3 py-1 text-center dark:bg-[#071952] dark:hover:bg-blue-700 dark:focus:ring-blue-800"
                            style={{ textDecoration: 'none' }}
                        >
                            Generate Report
                        </Link>

                        <div className="flex items-center ml-4 text-black">
                            <FormControlLabel
                                control={
                                    <Switch
                                        checked={state.Invalid} // Set the toggle state according to the current page view
                                        onChange={handleToggle}
                                        name="Invalid"
                                    />
                                }
                                label="Toggle to View Validated NIC data"
                            />
                        </div>
                    </div>
                </div>

                <div className="bg-[#D1E9F6] mt-4 p-4 rounded-lg">
                    <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 bg-[#D1E9F6] rounded-lg border border-gray-300">
                        <thead className="text-xs text-black uppercase bg-[#51829B] dark:bg-gray-700 dark:text-gray-400 rounded-t-lg">
                            <tr>
                                <th scope="col" className="px-6 py-3">NIC Number</th>
                                <th scope="col" className="px-6 py-3">File Name</th>
                                <th scope="col" className="px-6 py-3">Error Message</th>
                                <th scope="col" className="px-6 py-3"><span className="sr-only">Delete</span></th>
                            </tr>
                        </thead>
                        <tbody className="bg-[#D1E9F6]">
                            {currentItems.map(nic => (
                                <tr key={nic.nic_no} className="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                                    <td className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">{nic.nic_no}</td>
                                    <td className="px-6 py-4">{nic.fileName}</td>
                                    <td className="px-6 py-4">{nic.errorMessage}</td>
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

                <div className="mt-4 p-2 rounded-lg">
                    <ReactPaginate
                        previousLabel={"Previous"}
                        nextLabel={"Next"}
                        breakLabel={"..."}
                        pageCount={Math.ceil(invalidNics.length / itemsPerPage)}
                        marginPagesDisplayed={2}
                        pageRangeDisplayed={5}
                        onPageChange={handlePageClick}
                        containerClassName={"pagination flex justify-center space-x-2"}
                        pageClassName={"page-item"}
                        pageLinkClassName={"page-link bg-white text-black rounded-full p-2 hover:bg-[#f0f0f0]"}
                        previousClassName={"page-item"}
                        previousLinkClassName={"page-link bg-white text-black rounded-full p-2 hover:bg-[#f0f0f0]"}
                        nextClassName={"page-item"}
                        nextLinkClassName={"page-link bg-white text-black rounded-full p-2 hover:bg-[#f0f0f0]"}
                        breakClassName={"page-item"}
                        breakLinkClassName={"page-link bg-white text-black rounded-full p-2 hover:bg-[#f0f0f0]"}
                        activeClassName={"active bg-[#51829B] text-white rounded-full p-2"}
                    />
                </div>
            </div>
        </div>
    );
};

export default InvalidView;
