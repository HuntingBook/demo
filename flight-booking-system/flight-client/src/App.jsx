// src/App.jsx
import React from 'react';
import { Outlet, Link as RouterLink, useNavigate } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Container, Box, Button, IconButton } from '@mui/material';
import FlightIcon from '@mui/icons-material/Flight';
import { useAuth } from './context/AuthContext';
import { ToastContainer } from 'react-toastify'; // Added ToastContainer
import 'react-toastify/dist/ReactToastify.css'; // Added CSS for react-toastify

export default function App() {
  const { isAuthenticated, logoutContext, user } = useAuth(); // Add this
  const navigate = useNavigate(); // Add this for logout redirect

  const handleLogout = () => {
    logoutContext();
    navigate('/login'); // Redirect to login page after logout
  };

  return (
    // Box uses MUI for flex layout, Tailwind for min-height
    <Box className="flex flex-col min-h-screen"> 
      {/* AppBar uses MUI for structure and theming */}
      <AppBar position="static">
        {/* Toolbar uses MUI, Tailwind for potential responsive padding if needed (MUI handles most) */}
        <Toolbar className="px-2 sm:px-4"> 
          {/* IconButton uses MUI, Tailwind for margin */}
          <IconButton component={RouterLink} to="/" edge="start" color="inherit" aria-label="home" className="mr-2"> 
            <FlightIcon />
          </IconButton>
          {/* Typography uses MUI variant, Tailwind for flex grow and responsive text size */}
          <Typography variant="h6" component="div" className="flex-grow text-base sm:text-xl"> 
            Flight Booking {isAuthenticated && user ? <span className="text-sm sm:text-base">(Hi, {user.firstName})</span> : ''}
          </Typography>
          {/* Buttons use MUI structure/color, Tailwind for margin/padding (if sx is removed) */}
          <Button color="inherit" component={RouterLink} to="/" className="px-2 sm:px-3">Home</Button>
          {isAuthenticated ? (
            <>
              <Button color="inherit" component={RouterLink} to="/bookings" className="px-2 sm:px-3">My Bookings</Button>
              <Button color="inherit" onClick={handleLogout} className="px-2 sm:px-3">Logout</Button>
            </>
          ) : (
            <>
              <Button color="inherit" component={RouterLink} to="/login" className="px-2 sm:px-3">Login</Button>
              <Button color="inherit" component={RouterLink} to="/register" className="px-2 sm:px-3">Register</Button>
            </>
          )}
        </Toolbar>
      </AppBar>
      {/* Container uses MUI for max-width and centering, Tailwind for flex-grow and padding */}
      <Container component="main" className="flex-grow py-6 sm:py-8"> 
        <Outlet /> {/* Child routes will render here */}
      </Container>
      {/* Footer Box uses MUI for structure, Tailwind for padding, text alignment, background/text color, margin */}
      <Box component="footer" className="py-4 text-center text-white mt-auto" sx={{ backgroundColor: 'primary.main' }}> 
        <Typography variant="body2">© {new Date().getFullYear()} Flight Booking System</Typography>
      </Box>
      {/* ToastContainer is not directly styled by Tailwind here, uses its own CSS */}
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
    </Box>
  );
}
