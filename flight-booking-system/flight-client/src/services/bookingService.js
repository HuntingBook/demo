// src/services/bookingService.js
import api from './api';

const getUserBookings = async (params) => {
  // params: { status: 'upcoming'/'past', page: 1, size: 10 }
  // Note: Backend currently supports status like "CONFIRMED", "COMPLETED".
  // Frontend will map "upcoming"/"past" to these, or backend needs enhancement.
  // For now, let's assume frontend sends what backend expects or backend handles "upcoming"/"past".
  // The backend GetMapping for /api/bookings takes status, page, size.
  try {
    const response = await api.get('/bookings', { params });
    if (response.data && response.data.success) {
      return response.data.data; // This should be PaginatedResponseDTO<BookingDTO>
    } else {
      throw new Error(response.data.message || 'Failed to fetch bookings');
    }
  } catch (error) {
    console.error('Error fetching user bookings:', error);
    const errorMessage = error.response && error.response.data && error.response.data.message 
                         ? error.response.data.message 
                         : 'Error fetching bookings';
    throw error.response ? new Error(errorMessage) : error;
  }
};

// Potential: getBookingByReference(reference) - already in backend, can add here if needed elsewhere.

const createBooking = async (bookingData) => {
  // bookingData is expected to be an object like:
  // { flightId: 'some-flight-id', passengers: [{ firstName: 'John', lastName: 'Doe', email: 'john.doe@example.com' }] }
  try {
    const response = await api.post('/bookings', bookingData);
    if (response.data && response.data.success) {
      return response.data.data; // This should be BookingDTO
    } else {
      throw new Error(response.data.message || 'Failed to create booking');
    }
  } catch (error) {
    console.error('Error creating booking:', error);
    const errorMessage = error.response && error.response.data && error.response.data.message
                         ? error.response.data.message
                         : 'Error creating booking';
    throw error.response ? new Error(errorMessage) : error;
  }
};
export default {
  getUserBookings,
  createBooking,
};
