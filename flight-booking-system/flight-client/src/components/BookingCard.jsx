// src/components/BookingCard.jsx
import React from 'react';
import { Paper, Typography, Grid, Chip, Box } from '@mui/material';
import ConnectingAirportsIcon from '@mui/icons-material/ConnectingAirports'; // For route
import EventIcon from '@mui/icons-material/Event';
import ConfirmationNumberOutlinedIcon from '@mui/icons-material/ConfirmationNumberOutlined';
import dayjs from 'dayjs';

export default function BookingCard({ booking }) {
  if (!booking) return null;

  const { reference, flightInfo, status, bookingTime } = booking;
  // const flight = booking.flightInfo; // Assuming flightInfo is the FlightDTO

  const getStatusChipColor = (statusStr) => {
    if (statusStr?.toUpperCase() === 'CONFIRMED' || statusStr?.toUpperCase() === 'UPCOMING') return 'success';
    if (statusStr?.toUpperCase() === 'COMPLETED' || statusStr?.toUpperCase() === 'PAST') return 'info';
    if (statusStr?.toUpperCase() === 'CANCELLED') return 'error';
    return 'default';
  };
  
  // Determine if booking is in the past for display purposes if status isn't 'PAST' or 'UPCOMING'
  // This is a fallback if the backend status isn't directly 'upcoming' or 'past'
  let displayStatus = status;
  let isPastBooking = false;
  if (flightInfo?.departureDate) {
    // Combine date and time for accurate comparison
    const departureDateTime = dayjs(`${flightInfo.departureDate}T${flightInfo.departureTime || '00:00:00'}`);
    isPastBooking = departureDateTime.isBefore(dayjs());
    if (status?.toUpperCase() === 'CONFIRMED' && isPastBooking) {
        displayStatus = "COMPLETED"; // Or "Past"
    }
  }


  return (
    <Paper elevation={2} className="p-3 sm:p-4 mb-4 rounded-xl"> {/* Tailwind: padding, margin, rounded corners */}
      <Grid container spacing={{xs: 1, sm: 2}} alignItems="center">
        <Grid item xs={12} sm={4} md={3}>
          <Typography variant="subtitle2" className="text-xs sm:text-sm text-gray-600">Booking Ref:</Typography> {/* Tailwind: responsive text size, color */}
          <Typography variant="h6" component="div" className="flex items-center text-base sm:text-lg font-semibold"> {/* Tailwind: flex, items-center, responsive text size, font weight */}
            <ConfirmationNumberOutlinedIcon fontSize="small" className="mr-1" aria-hidden="true" /> {reference || 'N/A'} {/* Tailwind: margin */}
          </Typography>
        </Grid>
        <Grid item xs={12} sm={5} md={6}>
          <Typography variant="subtitle2" className="text-xs sm:text-sm text-gray-600">Flight Details:</Typography> {/* Tailwind: responsive text size, color */}
          {flightInfo ? (
            <>
              <Box className="flex items-center mt-1"> {/* Tailwind: flex, items-center, margin */}
                <ConnectingAirportsIcon className="mr-2 text-primary-main" aria-hidden="true" /> {/* Tailwind: margin, color */}
                <Typography variant="body1" className="text-sm sm:text-base"> {/* Tailwind: responsive text size */}
                  {flightInfo.departureAirport?.code} to {flightInfo.destinationAirport?.code}
                </Typography>
              </Box>
              <Box className="flex items-center mt-1 ml-1"> {/* Tailwind: flex, items-center, margin */}
                 <EventIcon className="mr-2 text-gray-700 text-lg" aria-hidden="true" /> {/* Tailwind: margin, color, size */}
                <Typography variant="body2" className="text-xs sm:text-sm text-gray-600"> {/* Tailwind: responsive text size, color */}
                  {dayjs(flightInfo.departureDate).format('ddd, MMM D, YYYY')} at {flightInfo.departureTime}
                </Typography>
              </Box>
            </>
          ) : (
            <Typography variant="body2" className="text-xs sm:text-sm text-gray-600">Flight details not available.</Typography> {/* Tailwind: responsive text size, color */}
          )}
        </Grid>
        <Grid item xs={12} sm={3} md={3} className="text-left sm:text-right mt-2 sm:mt-0"> {/* Tailwind: text alignment, responsive margin */}
          <Typography variant="subtitle2" className="mb-1 text-xs sm:text-sm text-gray-600">Status:</Typography> {/* Tailwind: margin, responsive text size, color */}
          <Chip 
            label={displayStatus?.toUpperCase() || 'UNKNOWN'} 
            color={getStatusChipColor(displayStatus)} 
            size="small" 
            className="text-xs sm:text-sm" // Tailwind: responsive text size for chip
          />
           <Typography variant="caption" display="block" className="mt-2 text-xs text-gray-500"> {/* Tailwind: margin, text size, color */}
            Booked on: {dayjs(bookingTime).format('MMM D, YYYY')}
          </Typography>
        </Grid>
      </Grid>
    </Paper>
  );
}
