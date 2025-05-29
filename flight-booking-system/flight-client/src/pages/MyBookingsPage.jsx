// src/pages/MyBookingsPage.jsx
import React, { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import bookingService from '../services/bookingService';
import BookingCard from '../components/BookingCard';
import BookingCardSkeleton from '../components/BookingCardSkeleton';
import { useAuth } from '../context/AuthContext';
import { Container, Typography, Tabs, Tab, Box, Alert, Pagination, Paper, Grid } from '@mui/material'; // Removed CircularProgress
import { toast } from 'react-toastify'; // Added toast

const BOOKINGS_PER_PAGE = 5;
const SKELETON_COUNT = 3; // Number of skeletons

function TabPanel(props) {
  const { children, value, index, ...other } = props;
  return (
    <div role="tabpanel" hidden={value !== index} id={`bookings-tabpanel-${index}`} aria-labelledby={`bookings-tab-${index}`} {...other}>
      {value === index && <Box sx={{ pt: 3 }}>{children}</Box>}
    </div>
  );
}

export default function MyBookingsPage() {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentTab, setCurrentTab] = useState(0); // 0 for Upcoming, 1 for Past
  const [pagination, setPagination] = useState({ page: 1, totalPages: 1 });

  const fetchBookings = useCallback(async (tabIndex, pageNum) => {
    setIsLoading(true);
    setError('');
    // Mapping tab index to status.
    // Backend's BookingService uses status like "CONFIRMED", "COMPLETED".
    // For "upcoming", we'd typically fetch "CONFIRMED" bookings that are not yet past.
    // For "past", we'd fetch "COMPLETED" or "CONFIRMED" bookings that are past.
    // The prompt assumes backend might handle "upcoming"/"past" directly.
    // If not, this mapping needs to align with actual backend capabilities or backend needs update.
    // Current backend (Phase 6) filters by exact status string.
    // Let's assume 'upcoming' means "CONFIRMED" and 'past' means "COMPLETED" as a simplification.
    // A more robust backend would interpret "upcoming" and "past" based on dates.
    const status = tabIndex === 0 ? 'CONFIRMED' : 'COMPLETED'; 
    // If backend were to support "upcoming" and "past" directly:
    // const status = tabIndex === 0 ? 'upcoming' : 'past';


    try {
      const data = await bookingService.getUserBookings({ 
        status, 
        page: pageNum - 1, // Backend page is 0-indexed
        size: BOOKINGS_PER_PAGE 
      });
      setBookings(data.content || []);
      setPagination({
        page: data.pageNo + 1, // Frontend page is 1-indexed
        totalPages: data.totalPages,
      });
    } catch (err) {
      const message = err.message || 'Failed to fetch bookings.';
      setError(message); // Keep local error for Alert if needed
      toast.error(message);
      setBookings([]);
      setPagination({ page: 1, totalPages: 1 });
    } finally {
      setIsLoading(false);
    }
  }, []); // Removed fetchBookings from its own dependency array

  useEffect(() => {
    if (!isAuthenticated) {
      sessionStorage.setItem('postLoginRedirect', '/bookings');
      navigate('/login');
      return; // Important to stop execution if not authenticated
    }
    // Fetch bookings when component mounts or when dependencies change
    fetchBookings(currentTab, pagination.page);
  }, [isAuthenticated, navigate, currentTab, pagination.page, fetchBookings]); // Added fetchBookings


  const handleTabChange = (event, newValue) => {
    setCurrentTab(newValue);
    setPagination(prev => ({ ...prev, page: 1 })); // Reset to first page
    // fetchBookings will be called by useEffect due to currentTab change
  };

  const handlePageChange = (event, value) => {
    setPagination(prev => ({ ...prev, page: value }));
    // fetchBookings will be called by useEffect due to pagination.page change
  };
  
  return (
    // Container uses MUI for max-width, Tailwind for padding/margin
    <Container maxWidth="lg" className="mt-4 sm:mt-8 p-2 sm:p-4">
      {/* Typography uses MUI variant, Tailwind for responsive size and margin */}
      <Typography variant="h4" component="h1" gutterBottom className="text-2xl sm:text-3xl md:text-4xl font-semibold mb-4 sm:mb-6">
        My Bookings
      </Typography>
      {/* Paper uses MUI elevation/theming, Tailwind for rounded corners */}
      <Paper elevation={1} className="rounded-xl">
        <Tabs value={currentTab} onChange={handleTabChange} aria-label="booking tabs" indicatorColor="primary" textColor="primary" centered>
          <Tab label="Upcoming" id="bookings-tab-0" aria-controls="bookings-tabpanel-0" />
          <Tab label="Past" id="bookings-tab-1" aria-controls="bookings-tabpanel-1" />
        </Tabs>
      </Paper>

      {error && <Alert severity="error" className="mt-4">{error}</Alert>} {/* Tailwind margin */}
      
      {/* Skeletons or Content Box uses Tailwind for margin */}
      <Box className="mt-6"> 
        {isLoading && bookings.length === 0 ? (
          <Grid container spacing={2}>
            {Array.from(new Array(SKELETON_COUNT)).map((_, index) => (
              <Grid item xs={12} key={`skeleton-booking-${index}`}>
                <BookingCardSkeleton />
              </Grid>
            ))}
          </Grid>
        ) : !isLoading && !error && bookings.length === 0 ? (
          // Typography uses MUI variant, Tailwind for margin and text alignment
          <Typography className="mt-4 text-center"> 
            {currentTab === 0 ? 'No upcoming bookings found.' : 'No past bookings found.'}
          </Typography>
        ) : !isLoading && !error && bookings.length > 0 ? (
          <>
            <Grid container spacing={2}>
              {bookings.map(booking => (
                <Grid item xs={12} key={booking.reference || booking.bookingId}>
                   <BookingCard booking={booking} />
                </Grid>
              ))}
            </Grid>
            {/* Pagination Box uses Tailwind for flex layout and margin */}
            {pagination.totalPages > 1 && (
              <Box className="flex justify-center mt-8 mb-4"> 
                <Pagination
                  count={pagination.totalPages}
                  page={pagination.page}
                  onChange={handlePageChange}
                  color="primary"
                  showFirstButton
                  showLastButton
                />
              </Box>
            )}
          </>
        ) : null}
      </Box>
    </Container>
  );
}
// Removed TabPanel as direct rendering is now handled above with Grid
// function TabPanel(props) { ... }
// Removed TabPanel as direct rendering is now handled above with Grid
// function TabPanel(props) { ... }
