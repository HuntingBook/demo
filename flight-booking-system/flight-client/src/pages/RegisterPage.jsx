// src/pages/RegisterPage.jsx
import React, { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Container, Box, Typography, TextField, Button, CircularProgress, Alert, Grid } from '@mui/material';

export default function RegisterPage() {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    country: '',
    phone: '', // Optional
  });
  const [error, setError] = useState('');
  const { registerContext, isLoading } = useAuth();
  const navigate = useNavigate();

  const handleChange = (event) => {
    setFormData({ ...formData, [event.target.name]: event.target.value });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    // Basic password check (can be enhanced)
    if (formData.password.length < 8 || !/\d/.test(formData.password) || !/[a-zA-Z]/.test(formData.password)) {
      setError('Password must be at least 8 characters long and include at least one letter and one number.');
      return;
    }
    try {
      await registerContext(formData);
      navigate('/'); // Navigate to home on successful registration
    } catch (err) {
      setError(err.message || 'Failed to register. Please try again.');
    }
  };

  return (
    // Container uses MUI for max-width, Tailwind for padding/margin
    <Container component="main" maxWidth="xs" className="mt-8 p-2 sm:p-0">
      {/* Box uses MUI for structure, Tailwind for layout, padding, rounded corners */}
      <Box 
        className="mt-8 flex flex-col items-center p-4 sm:p-6 rounded-xl"
        sx={{borderRadius: '12px', bgcolor:'background.paper', boxShadow: {sm:3}}}
      >
        {/* Typography uses MUI variant */}
        <Typography component="h1" variant="h5">
          Sign Up
        </Typography>
        {/* Box for form uses Tailwind for margin and width */}
        <Box component="form" onSubmit={handleSubmit} noValidate className="mt-6 w-full"> 
          {error && <Alert severity="error" className="w-full mb-4">{error}</Alert>} {/* Tailwind: width, margin */}
          {/* Grid uses MUI for layout, Tailwind for responsive spacing (inherited from MUI theme or can be set) */}
          <Grid container spacing={2}> 
            <Grid item xs={12} sm={6}>
              <TextField
                autoComplete="given-name"
                name="firstName"
                required
                fullWidth
                id="firstName"
                label="First Name"
                autoFocus
                value={formData.firstName}
                onChange={handleChange}
                disabled={isLoading}
                aria-required="true"
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                id="lastName"
                label="Last Name"
                name="lastName"
                autoComplete="family-name"
                value={formData.lastName}
                onChange={handleChange}
                disabled={isLoading}
                aria-required="true"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
                autoComplete="email"
                value={formData.email}
                onChange={handleChange}
                disabled={isLoading}
                aria-required="true"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="new-password"
                value={formData.password}
                onChange={handleChange}
                disabled={isLoading}
                helperText="Min 8 chars, 1 letter, 1 number."
                aria-required="true"
                aria-describedby="password-helper-text"
              />
              <Typography variant="caption" id="password-helper-text" className="sr-only"> {/* Tailwind: screen-reader only */}
                Password must be at least 8 characters long and include at least one letter and one number.
              </Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="country"
                label="Country"
                id="country"
                autoComplete="country"
                value={formData.country}
                onChange={handleChange}
                disabled={isLoading}
                aria-required="true"
              />
            </Grid>
             <Grid item xs={12}>
              <TextField
                fullWidth
                name="phone"
                label="Phone Number (Optional)"
                id="phone"
                autoComplete="tel"
                value={formData.phone}
                onChange={handleChange}
                disabled={isLoading}
              />
            </Grid>
          </Grid>
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
            {isLoading ? <CircularProgress size={24} color="inherit" /> : 'Sign Up'}
          </Button>
          {/* Grid for link uses MUI structure, Tailwind for justification */}
          <Grid container className="justify-end">
            <Grid item>
              {/* Button link uses MUI variant */}
              <Button component={RouterLink} to="/login" variant="text" color="primary">
                Already have an account? Sign in
              </Button>
            </Grid>
          </Grid>
        </Box>
      </Box>
    </Container>
  );
}
