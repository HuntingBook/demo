// src/pages/HomePage.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Box, Typography, TextField, Button, Grid, Paper, Alert } from '@mui/material';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs'; // For date manipulation/validation

// Placeholder for an API service to search flights (will be created in a later step)
// import flightService from '../services/flightService'; 

export default function HomePage() {
  const navigate = useNavigate();
  const [fromAirportCode, setFromAirportCode] = useState('');
  const [toAirportCode, setToAirportCode] = useState('');
  const [departureDate, setDepartureDate] = useState(null); // MUI DatePicker uses null for no date
  // const [returnDate, setReturnDate] = useState(null); // For round trip
  // const [tripType, setTripType] = useState('ONE_WAY'); // 'ONE_WAY' or 'ROUND_TRIP'
  const [passengers, setPassengers] = useState(1); // Default to 1 passenger
  const [error, setError] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');

    if (!fromAirportCode || !toAirportCode || !departureDate) {
      setError('Please fill in all required fields: From, To, and Departure Date.');
      return;
    }
    if (!dayjs(departureDate).isValid()) {
        setError('Invalid departure date.');
        return;
    }
    if (dayjs(departureDate).isBefore(dayjs(), 'day')) {
        setError('Departure date cannot be in the past.');
        return;
    }

    // Format date for API if needed, e.g., YYYY-MM-DD
    const formattedDepartureDate = dayjs(departureDate).format('YYYY-MM-DD');

    // Construct query parameters
    const queryParams = new URLSearchParams({
      from: fromAirportCode,
      to: toAirportCode,
      date: formattedDepartureDate,
      // passengers: passengers.toString(), // If backend supports
    }).toString();

    // Navigate to flight list page with query parameters
    navigate(`/flights?${queryParams}`);
    
    // Actual API call would be here if we were fetching directly and not just navigating
    // try {
    //   setIsLoading(true);
    //   const results = await flightService.searchFlights(fromAirportCode, toAirportCode, formattedDepartureDate);
    //   navigate('/flights', { state: { flights: results, searchParams: { from, to, date } } });
    //   setIsLoading(false);
    // } catch (err) {
    //   setError(err.message || 'Failed to search flights.');
    //   setIsLoading(false);
    // }
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      {/* Container uses MUI for max-width and centering, Tailwind for padding/margin */}
      <Container component="main" maxWidth="md" className="mt-4 sm:mt-8 p-2 sm:p-4"> 
        {/* Paper uses MUI for elevation/theming, Tailwind for padding, border, rounded corners */}
        <Paper elevation={3} className="p-4 sm:p-6 md:p-8 rounded-xl border-2 border-blue-500">
          {/* Typography uses MUI variants, Tailwind for color, weight, and responsive size (via sx for complex responsive) */}
          <Typography variant="h4" component="h1" gutterBottom align="center" className="text-purple-600 font-extrabold" sx={{fontSize: {xs: '1.75rem', sm: '2rem', md: '2.25rem'}}}>
            Search Flights
          </Typography>
          <p className="text-red-500 text-xl text-center my-2">Tailwind Test Paragraph</p>
          {error && <Alert severity="error" className="mb-4">{error}</Alert>} {/* Tailwind for margin */}
          <Box component="form" onSubmit={handleSubmit} noValidate>
            {/* Grid uses MUI for layout, Tailwind for responsive spacing (via sx for complex responsive) */}
            <Grid container spacing={{ xs: 2, md: 3 }}> 
              <Grid item xs={12} sm={6}>
                <TextField
                  required
                  fullWidth
                  id="fromAirportCode"
                  label="From (Airport Code)"
                  name="fromAirportCode"
                  value={fromAirportCode}
                  onChange={(e) => setFromAirportCode(e.target.value.toUpperCase())}
                  // InputProps={{
                  //   startAdornment: <InputAdornment position="start"><FlightTakeoffIcon /></InputAdornment>,
                  // }}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  required
                  fullWidth
                  id="toAirportCode"
                  label="To (Airport Code)"
                  name="toAirportCode"
                  value={toAirportCode}
                  onChange={(e) => setToAirportCode(e.target.value.toUpperCase())}
                  // InputProps={{
                  //   startAdornment: <InputAdornment position="start"><FlightLandIcon /></InputAdornment>,
                  // }}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <DatePicker
                  label="Departure Date"
                  value={departureDate}
                  onChange={(newValue) => setDepartureDate(newValue)}
                  slotProps={{ textField: { required: true, fullWidth: true, name: 'departureDate', className:"mt-2" } }} // Tailwind: margin-top for DatePicker's TextField
                  disablePast
                  format="YYYY-MM-DD"
                />
              </Grid>
              {/* <Grid item xs={12} sm={6}> // For Round Trip
                <DatePicker
                  label="Return Date"
                  value={returnDate}
                  onChange={(newValue) => setReturnDate(newValue)}
                  disabled={tripType === 'ONE_WAY'}
                  slotProps={{ textField: { fullWidth: true } }}
                  minDate={departureDate ? dayjs(departureDate).add(1, 'day') : undefined}
                  disablePast
                />
              </Grid> */}
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  id="passengers"
                  label="Passengers"
                  name="passengers"
                  type="number"
                  value={passengers}
                  onChange={(e) => setPassengers(Math.max(1, parseInt(e.target.value, 10) || 1))}
                  InputProps={{ inputProps: { min: 1 } }}
                  className="mt-2" // Tailwind: margin-top for consistency if DatePicker above has margin
                />
              </Grid>
              <Grid item xs={12} className="text-center"> {/* Tailwind: text-center */}
                <Button
                  type="submit"
                  variant="contained"
                  color="primary"
                  size="large"
                  className="mt-4 px-6 sm:px-10 py-2 sm:py-3 w-full sm:w-auto" // Tailwind: margin, padding, responsive width
                >
                  Search Flights
                </Button>
              </Grid>
            </Grid>
          </Box>
        </Paper>
      </Container>
    </LocalizationProvider>
  );
}
