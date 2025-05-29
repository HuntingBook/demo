// src/pages/LoginPage.jsx
import React, { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Container, Box, Typography, TextField, Button, CircularProgress, Alert, Grid } from '@mui/material';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { loginContext, isLoading, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      await loginContext(email, password);
      // No need to check isAuthenticated here, loginContext throws on failure.
      // If successful, AuthContext's useEffect or a redirect in loginContext might handle navigation,
      // or we navigate explicitly after success.
      navigate('/'); // Navigate to home on successful login
    } catch (err) {
      setError(err.message || 'Failed to login. Please check your credentials.');
    }
  };
  
  // If already authenticated, redirect to home (optional, can be handled by ProtectedRoutes later)
  // This can cause issues if navigate is called during render. A better approach is a ProtectedRoute
  // or a useEffect hook. For now, we'll rely on the form success navigation.
  // React.useEffect(() => {
  //   if (isAuthenticated) {
  //     navigate('/');
  //   }
  // }, [isAuthenticated, navigate]);


  return (
    // Container uses MUI for max-width, Tailwind for padding/margin
    <Container component="main" maxWidth="xs" className="mt-8 p-2 sm:p-0"> 
      {/* Box uses MUI for structure, Tailwind for layout, padding, rounded corners */}
      <Box 
        className="mt-8 flex flex-col items-center p-4 sm:p-6 rounded-xl" 
        sx={{borderRadius: '12px' /* from theme */, bgcolor:'background.paper', boxShadow: {sm:3} /* theme shadow on sm+ */}}
      >
        {/* Typography uses MUI variant */}
        <Typography component="h1" variant="h5"> 
          Sign In
        </Typography>
        {/* Box for form uses Tailwind for margin and width */}
        <Box component="form" onSubmit={handleSubmit} noValidate className="mt-2 w-full"> 
          {error && <Alert severity="error" className="w-full mb-4">{error}</Alert>} {/* Tailwind: width, margin */}
          <TextField
            margin="normal" // MUI margin for consistency between TextFields
            required
            fullWidth
            id="email"
            label="Email Address"
            name="email"
            autoComplete="email"
            autoFocus
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            disabled={isLoading}
            aria-required="true"
            // className="mb-4" // Can use Tailwind for bottom margin instead of "normal" if preferred
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="current-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            disabled={isLoading}
            aria-required="true"
            // className="mb-4"
          />
          {/* Button uses MUI for structure/variant, Tailwind for margin and full-width */}
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            className="mt-6 mb-4 py-2" // Tailwind: margin, padding
            disabled={isLoading}
            aria-live="polite"
          >
            {isLoading ? <CircularProgress size={24} color="inherit" /> : 'Sign In'}
          </Button>
          {/* Grid for link uses MUI structure, Tailwind for justification */}
          <Grid container className="justify-end"> 
            <Grid item>
              {/* Button link uses MUI variant, Tailwind for text color (if not using MUI color prop) */}
              <Button component={RouterLink} to="/register" variant="text" color="primary"> 
                {"Don't have an account? Sign Up"}
              </Button>
            </Grid>
          </Grid>
        </Box>
      </Box>
    </Container>
  );
}
