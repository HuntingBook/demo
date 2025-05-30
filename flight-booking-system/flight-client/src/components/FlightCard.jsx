// src/components/FlightCard.jsx
import React from 'react';
import { Paper, Typography, Button, Grid, Box, Chip } from '@mui/material';
import FlightTakeoffIcon from '@mui/icons-material/FlightTakeoff';
import FlightLandIcon from '@mui/icons-material/FlightLand';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext'; // To check if user is logged in
import dayjs from 'dayjs'; // For date formatting

// Helper to calculate duration (example)
const calculateDuration = (departureTime, arrivalTime) => {
    // Assuming times are in "HH:MM:SS" or "HH:MM" format from LocalTime.toString()
    // This is a simplified calculation and might need adjustment based on actual time format and day changes.
    if(!departureTime || !arrivalTime) return "N/A";
    try {
        const [depH, depM] = departureTime.split(':').map(Number);
        const [arrH, arrM] = arrivalTime.split(':').map(Number);
        let durationMinutes = (arrH * 60 + arrM) - (depH * 60 + depM);
        if (durationMinutes < 0) durationMinutes += 24 * 60; // Handles overnight flights simply
        const hours = Math.floor(durationMinutes / 60);
        const minutes = durationMinutes % 60;
        return `${hours}h ${minutes}m`;
    } catch (e) {
        console.error("Error calculating duration:", e);
        return "N/A";
    }
};

export default function FlightCard({ flight, onSelectFlight }) { // Added onSelectFlight prop
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();

  const handleSelectFlightInternal = () => {
    if (onSelectFlight) {
      onSelectFlight(flight); // Call the passed handler
    } else {
      // Fallback to old behavior if onSelectFlight is not provided
      sessionStorage.setItem('selectedFlight', JSON.stringify(flight));
      if (isAuthenticated) {
        navigate('/confirm-flight');
      } else {
        sessionStorage.setItem('postLoginRedirect', '/confirm-flight');
        navigate('/login');
      }
    }
  };

  if (!flight) return null;

  const { flightNumber, airlineCompany, departureAirport, destinationAirport, departureDate, departureTime, price, isDirect, cabinClass } = flight;
  const arrivalTime = flight.arrivalTime; 
  const duration = calculateDuration(departureTime, arrivalTime); 

  return (
    <Paper elevation={3} className="p-4 sm:p-6 mb-6 rounded-xl"> {/* Tailwind: padding, margin, rounded corners */}
      <Grid container spacing={2} alignItems="center">
        <Grid item xs={12} sm={6} md={2}>
          <Typography variant="h6" component="div" className="font-semibold">{flightNumber || 'N/A'}</Typography>
          <Typography variant="body2" className="text-gray-600">{airlineCompany?.name || 'N/A'}</Typography> {/* Tailwind: text color */}
          {cabinClass && <Chip label={cabinClass} size="small" className="mt-1" />}
        </Grid>
        <Grid item xs={12} sm={6} md={5}>
          <Box className="flex items-center mb-2"> {/* Tailwind: flex, items-center, margin */}
            <FlightTakeoffIcon className="mr-2 text-primary-main" aria-hidden="true" /> {/* Tailwind: margin, color (assuming primary.main is mapped or a custom class) */}
            <Typography variant="body1"><strong>{departureAirport?.code || 'N/A'}</strong> ({departureAirport?.city || 'N/A'})</Typography>
          </Box>
          <Typography variant="body2" className="text-gray-600 ml-8">{dayjs(departureDate).format('ddd, MMM D, YYYY')} at {departureTime || 'N/A'}</Typography> {/* Tailwind: margin, color */}
          
          <Box className="my-2 ml-3"> {/* Tailwind: margin */}
            <AccessTimeIcon className="text-sm align-middle mr-1" aria-hidden="true" /> {/* Tailwind: font-size, alignment, margin */}
            <Typography variant="caption" className="text-gray-600">{duration}</Typography> {/* Tailwind: text color */}
          </Box>

          <Box className="flex items-center mt-2"> {/* Tailwind: flex, items-center, margin */}
            <FlightLandIcon className="mr-2 text-primary-main" aria-hidden="true" /> {/* Tailwind: margin, color */}
            <Typography variant="body1"><strong>{destinationAirport?.code || 'N/A'}</strong> ({destinationAirport?.city || 'N/A'})</Typography>
          </Box>
          <Typography variant="body2" className="text-gray-600 ml-8"> {/* Tailwind: margin, color */}
            {arrivalTime ? `${dayjs(departureDate).format('ddd, MMM D, YYYY')} at ${arrivalTime}` : 'Arrival time N/A'}
          </Typography> 
        </Grid>
        <Grid item xs={6} sm={3} md={2} className="text-left sm:text-center"> {/* Tailwind: text alignment */}
          <Chip label={isDirect ? "Direct" : "1+ Stops"} color={isDirect ? "success" : "warning"} variant="outlined" size="small" />
        </Grid>
        <Grid item xs={6} sm={9} md={3} className="text-right"> {/* Tailwind: text alignment */}
          <Typography variant="h5" component="div" color="primary" className="flex items-center justify-end text-lg sm:text-xl"> {/* Tailwind: flex, items-center, justify-end, responsive text size */}
            <AttachMoneyIcon aria-hidden="true" />{price?.toFixed(2) || 'N/A'}
          </Typography>
          <Button 
            variant="contained" 
            color="primary" 
            className="mt-2 w-full sm:w-auto" // Tailwind: margin, responsive width
            onClick={handleSelectFlightInternal}
            aria-label={`Select flight ${flightNumber || ''} from ${departureAirport?.code || ''} to ${destinationAirport?.code || ''} for $${price?.toFixed(2) || 'N/A'}`}
          >
            Select Flight
          </Button>
        </Grid>
      </Grid>
    </Paper>
  );
}
