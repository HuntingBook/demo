// src/components/FlightCardSkeleton.jsx
import React from 'react';
import { Paper, Grid, Skeleton, Box } from '@mui/material';

export default function FlightCardSkeleton() {
  return (
    <Paper elevation={3} sx={{ p: 3, mb: 3, borderRadius: '12px' }}> {/* Added borderRadius from theme */}
      <Grid container spacing={2} alignItems="center">
        <Grid item xs={12} md={2}>
          <Skeleton variant="text" width="80%" height={30} sx={{ mb: 0.5 }} />
          <Skeleton variant="text" width="60%" />
        </Grid>
        <Grid item xs={12} md={5}>
          <Skeleton variant="rectangular" width="90%" height={40} sx={{ mb: 1, borderRadius: '4px' }} />
          <Skeleton variant="text" width="70%" />
          <Skeleton variant="text" width="50%" sx={{mt: 0.5}} />
          <Skeleton variant="rectangular" width="90%" height={40} sx={{ mt:1, mb: 1, borderRadius: '4px' }} />
          <Skeleton variant="text" width="70%" />
        </Grid>
        <Grid item xs={12} md={2} sx={{ textAlign: { xs: 'left', md: 'center' } }}>
          <Skeleton variant="rectangular" width={60} height={25} sx={{ borderRadius: '16px' }}/> {/* For Chip */}
        </Grid>
        <Grid item xs={12} md={3} sx={{ textAlign: 'right' }}>
          <Skeleton variant="text" width={80} height={40} sx={{ mb: 1, ml:'auto' }} /> {/* ml:auto to align right */}
          <Skeleton variant="rectangular" width={120} height={40} sx={{ ml:'auto', borderRadius: '8px' }} /> {/* For Button */}
        </Grid>
      </Grid>
    </Paper>
  );
}
