// src/services/flightService.js
import api from './api';

const searchFlights = async (searchParams) => {
  // searchParams should be an object like { from: 'JFK', to: 'LAX', date: '2024-12-31' }
  try {
    const response = await api.get('/flights', { params: searchParams });
    // Assuming backend returns BaseResponse structure
    if (response.data && response.data.success) {
      return response.data.data; // This should be List<FlightDTO>
    } else {
      throw new Error(response.data.message || 'Failed to fetch flights');
    }
  } catch (error) {
    console.error('Error fetching flights:', error);
    // Rethrow or handle as per application's error handling strategy
    // Ensure error.response exists before trying to access error.response.data.message
    const errorMessage = error.response && error.response.data && error.response.data.message 
                         ? error.response.data.message 
                         : 'Error fetching flights';
    throw error.response ? new Error(errorMessage) : error;
  }
};

export default {
  searchFlights,
};
