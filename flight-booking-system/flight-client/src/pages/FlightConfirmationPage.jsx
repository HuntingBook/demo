// src/pages/FlightConfirmationPage.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import bookingService from '../services/bookingService';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Container, Typography, Paper, Button, Box, Grid, Divider, List, ListItem, ListItemIcon, ListItemText, Alert } from '@mui/material';
import FlightTakeoffIcon from '@mui/icons-material/FlightTakeoff';
import FlightLandIcon from '@mui/icons-material/FlightLand';
import EventSeatIcon from '@mui/icons-material/EventSeat'; // For cabin/class or passenger
import PaymentsIcon from '@mui/icons-material/Payments'; // For payment button
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import ConfirmationNumberIcon from '@mui/icons-material/ConfirmationNumber'; // For flight number
import dayjs from 'dayjs';

// Helper to calculate duration (can be imported from a utils file if shared)
const calculateDuration = (departureTime, arrivalTime) => {
    if(!departureTime || !arrivalTime) return "N/A";
    try {
        const [depH, depM] = departureTime.split(':').map(Number);
        const [arrH, arrM] = arrivalTime.split(':').map(Number);
        let durationMinutes = (arrH * 60 + arrM) - (depH * 60 + depM);
        if (durationMinutes < 0) durationMinutes += 24 * 60;
        const hours = Math.floor(durationMinutes / 60);
        const minutes = durationMinutes % 60;
        return `${hours}h ${minutes}m`;
    } catch (e) { return "N/A"; }
};


export default function FlightConfirmationPage() {
  const navigate = useNavigate();
  const { user, isAuthenticated } = useAuth(); // Assuming user object has firstName, lastName, email
  const [flightDetails, setFlightDetails] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!isAuthenticated) {
        // If somehow user gets here without being authenticated, redirect to login
        // Storing intended path for post-login redirect
        sessionStorage.setItem('postLoginRedirect', '/confirm-flight');
        navigate('/login');
        return;
    }

    const storedFlight = sessionStorage.getItem('selectedFlight');
    if (storedFlight) {
      try {
        const parsedFlight = JSON.parse(storedFlight);
        setFlightDetails(parsedFlight);
      } catch (e) {
        setError('Could not load flight details. Please try selecting a flight again.');
        console.error("Error parsing flight details from sessionStorage:", e);
      }
    } else {
      setError('No flight selected for confirmation. Please start a new search.');
      // Optionally navigate back to home or flight list after a delay
      // setTimeout(() => navigate('/'), 3000);
    }
  }, [navigate, isAuthenticated]);

  const handleContinueToPayment = async () => {
    if (!flightDetails || !user) {
      toast.error('Flight details or user information is missing.');
      return;
    }

    const bookingData = {
      flightId: flightDetails.flightId, // Assuming flightDetails has an 'id' property for the flight
      passengers: [
        {
          firstName: user.firstName,
          lastName: user.lastName,
          email: user.email,
          // Add other passenger details if your backend expects them e.g. dateOfBirth, passportNumber
        },
      ],
    };

    try {
      const createdBooking = await bookingService.createBooking(bookingData);
      toast.success(`Booking successful! Reference: ${createdBooking.reference}`);
      // Clear selected flight from session storage after successful booking
      sessionStorage.removeItem('selectedFlight');
      navigate('/bookings'); // Navigate to bookings page
    } catch (err) {
      console.error('Booking failed:', err);
      toast.error(err.message || 'Booking failed. Please try again.');
      setError(err.message || 'Booking failed. Please try again.');
    }
  };

  if (error) {
    return (
      <Container maxWidth="md" sx={{ mt: 4, textAlign: 'center' }}>
        <Alert severity="error">{error}</Alert>
        <Button variant="contained" sx={{ mt: 2 }} onClick={() => navigate('/')}>Search Flights</Button>
      </Container>
    );
  }

  if (!flightDetails) {
    return (
      <Container maxWidth="md" sx={{ mt: 4, textAlign: 'center' }}>
        <Typography>Loading flight details...</Typography> {/* Or a CircularProgress */}
      </Container>
    );
  }

  const { flightNumber, departureAirport, destinationAirport, departureDate, departureTime, arrivalTime, price } = flightDetails;
  const duration = calculateDuration(departureTime, arrivalTime);
  const taxes = price * 0.1; // Example: 10% tax
  const totalPrice = price + taxes;

  return (
    // Container uses MUI for max-width, Tailwind for padding/margin
    <Container maxWidth="lg" className="mt-4 sm:mt-8 p-2 sm:p-4 mb-8">
<ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="colored"
      />
      {/* Paper uses MUI for elevation/theming, Tailwind for padding, rounded corners */}
      <Paper elevation={3} className="p-4 sm:p-6 md:p-8 rounded-xl">
        {/* Typography uses MUI variant, Tailwind for responsive size, margin, text alignment */}
        <Typography variant="h4" component="h1" gutterBottom className="text-center mb-6 text-2xl sm:text-3xl md:text-4xl font-semibold">
          Confirm Your Flight
        </Typography>

        {/* Grid uses MUI for layout, Tailwind for responsive spacing */}
        <Grid container spacing={{xs: 2, md: 3}}>
          {/* Flight Details Section */}
          <Grid item xs={12} md={7}>
            {/* Typography uses MUI variant, Tailwind for responsive size */}
            <Typography variant="h6" gutterBottom className="text-lg sm:text-xl font-medium mb-3">Flight Details</Typography>
            <List dense>
              <ListItem className="px-0"> {/* Tailwind: padding reset */}
                <ListItemIcon><ConfirmationNumberIcon color="primary" aria-hidden="true" /></ListItemIcon>
                <ListItemText primary="Flight Number" secondary={flightNumber || 'N/A'} />
              </ListItem>
              <ListItem className="px-0">
                <ListItemIcon><FlightTakeoffIcon color="primary" aria-hidden="true" /></ListItemIcon>
                <ListItemText primary="Departure" secondary={`${departureAirport?.code} (${departureAirport?.city})`} />
              </ListItem>
              <ListItem className="px-0">
                <ListItemIcon><CalendarTodayIcon color="primary" aria-hidden="true" /></ListItemIcon>
                <ListItemText primary="Date & Time" secondary={`${dayjs(departureDate).format('ddd, MMM D, YYYY')} at ${departureTime}`} />
              </ListItem>
               <ListItem className="px-0">
                <ListItemIcon><AccessTimeIcon color="primary" aria-hidden="true" /></ListItemIcon>
                <ListItemText primary="Duration" secondary={duration} />
              </ListItem>
              <ListItem className="px-0">
                <ListItemIcon><FlightLandIcon color="primary" aria-hidden="true" /></ListItemIcon>
                <ListItemText primary="Arrival" secondary={`${destinationAirport?.code} (${destinationAirport?.city})`} />
              </ListItem>
              <ListItem className="px-0">
                <ListItemIcon><CalendarTodayIcon color="primary" aria-hidden="true" /></ListItemIcon>
                <ListItemText primary="Arrival Date & Time" secondary={`${dayjs(departureDate).format('ddd, MMM D, YYYY')} at ${arrivalTime || 'N/A'}`} />
              </ListItem>
            </List>
          </Grid>

          {/* Price & Passenger Details Section */}
          <Grid item xs={12} md={5}>
            <Typography variant="h6" gutterBottom className="text-lg sm:text-xl font-medium mb-3">Passenger Details</Typography>
            {user && (
              <List dense>
                <ListItem className="px-0">
                  <ListItemIcon><EventSeatIcon color="primary" aria-hidden="true" /></ListItemIcon>
                  <ListItemText primary="Lead Passenger" secondary={`${user.firstName} ${user.lastName}`} />
                </ListItem>
                 <ListItem className="px-0">
                  <ListItemIcon sx={{minWidth: '40px'}}></ListItemIcon>
                  <ListItemText secondary={user.email} />
                </ListItem>
              </List>
            )}
            
            <Divider className="my-4" /> {/* Tailwind: margin */}

            <Typography variant="h6" gutterBottom className="text-lg sm:text-xl font-medium mb-3">Price Summary</Typography>
            <List dense>
              <ListItem className="px-0 flex justify-between"> {/* Tailwind: padding reset, flex layout */}
                <ListItemText primary="Base Fare" />
                <Typography variant="body1">${price?.toFixed(2)}</Typography>
              </ListItem>
              <ListItem className="px-0 flex justify-between">
                <ListItemText primary="Taxes & Fees (Est.)" />
                <Typography variant="body1">${taxes.toFixed(2)}</Typography>
              </ListItem>
              <ListItem className="px-0 flex justify-between font-bold"> {/* Tailwind: padding reset, flex layout, font weight */}
                <ListItemText primary="Total Price" classes={{primary: "font-bold"}} /> {/* Tailwind: font weight for primary text */}
                <Typography variant="h6" color="primary" className="text-xl sm:text-2xl">${totalPrice.toFixed(2)}</Typography> {/* Tailwind: responsive text size */}
              </ListItem>
            </List>
          </Grid>
        </Grid>

        {/* Box uses Tailwind for text alignment and margin */}
        <Box className="text-center mt-8"> 
          <Button 
            variant="contained" 
            color="primary" 
            size="large" 
            startIcon={<PaymentsIcon />}
            onClick={handleContinueToPayment}
            aria-label="Proceed to payment for the confirmed flight"
            className="w-full sm:w-auto" // Tailwind: responsive width
          >
            Continue to Payment
          </Button>
        </Box>
      </Paper>
    </Container>
  );
}
