import React from 'react';
import ReactDOM from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import App from './App';
import './index.scss';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { AuthProvider } from './context/AuthContext'; // Added AuthProvider

// Page Imports
import HomePage from './pages/HomePage';
import FlightListPage from './pages/FlightListPage';
import FlightConfirmationPage from './pages/FlightConfirmationPage';
import MyBookingsPage from './pages/MyBookingsPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import NotFoundPage from './pages/NotFoundPage';

const theme = createTheme({
  palette: {
    mode: 'light', // or 'dark' based on preference or future toggle
    primary: {
      main: '#0077B6', // A new primary color (e.g., a shade of blue)
      // light: '#...', dark: '#...', contrastText: '#...' (MUI calculates if not provided)
    },
    secondary: {
      main: '#FF8C00', // A new secondary color (e.g., a shade of orange)
    },
    background: {
      default: '#f4f6f8', // Slightly off-white background
      paper: '#ffffff',
    },
  },
  typography: {
    fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif', // Example: Using Inter font
    h1: { fontSize: '2.5rem', fontWeight: 700 },
    h4: { fontSize: '1.75rem', fontWeight: 600 }, // Adjust existing usage
    // Add other overrides for h2, h3, h5, h6, body1, body2, button, etc.
  },
  components: { // Default prop overrides for MUI components
    MuiButton: {
      defaultProps: {
        disableElevation: true, // Flatter buttons
        // variant: 'contained', // If you want most buttons to be contained by default
      },
      styleOverrides: {
        root: {
          borderRadius: '8px', // Rounded buttons
          textTransform: 'none', // No uppercase text
        },
        // containedPrimary: { '&:hover': { backgroundColor: '#005A8C' } }
      }
    },
    MuiTextField: {
      defaultProps: {
        variant: 'outlined', // Ensure all are outlined
        size: 'small',
      }
    },
    MuiPaper: {
      defaultProps: {
        elevation: 2, // Default elevation for Paper
      },
      styleOverrides: {
        root: {
          borderRadius: '12px',
        }
      }
    }
  }
});
// Remember to import Inter font in index.html or index.scss if using it
// e.g., in index.html: <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />, // App component with AppBar and Outlet
    errorElement: <NotFoundPage />, // Or a more integrated error page within App layout
    children: [
      { index: true, element: <HomePage /> }, // Default page for '/'
      { path: 'flights', element: <FlightListPage /> },
      { path: 'confirm-flight', element: <FlightConfirmationPage /> },
      { path: 'bookings', element: <MyBookingsPage /> },
      { path: 'login', element: <LoginPage /> },
      { path: 'register', element: <RegisterPage /> },
      { path: '*', element: <NotFoundPage /> } // Catch-all for 404
    ],
  },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider> {/* Wrap with AuthProvider */}
        <RouterProvider router={router} />
      </AuthProvider>
    </ThemeProvider>
  </React.StrictMode>
);
