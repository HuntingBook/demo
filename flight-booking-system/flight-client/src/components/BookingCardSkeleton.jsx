// src/components/BookingCardSkeleton.jsx
import React from 'react';
import { Paper, Grid, Skeleton, Box } from '@mui/material';

export default function BookingCardSkeleton() {
  return (
    <Paper elevation={2} sx={{ p: 2, mb: 2, borderRadius: '12px' }}>
      <Grid container spacing={2} alignItems="center">
        <Grid item xs={12} sm={3}>
          <Skeleton variant="text" width="70%" height={20} />
          <Skeleton variant="text" width="50%" height={30} />
        </Grid>
        <Grid item xs={12} sm={6}>
          <Skeleton variant="text" width="60%" height={20} />
          <Skeleton variant="rectangular" width="90%" height={25} sx={{ mt: 0.5, mb: 0.5, borderRadius: '4px' }} />
          <Skeleton variant="rectangular" width="80%" height={20} sx={{ borderRadius: '4px' }}/>
        </Grid>
        <Grid item xs={12} sm={3} sx={{ textAlign: {xs: 'left', sm:'right'} }}>
          <Skeleton variant="text" width="50%" height={20} sx={{mb:0.5, ml:'auto'}}/>
          <Skeleton variant="rectangular" width={80} height={25} sx={{ ml:'auto', borderRadius: '16px' }}/> {/* Chip */}
          <Skeleton variant="text" width="70%" height={15} sx={{mt:1, ml:'auto'}}/>
        </Grid>
      </Grid>
    </Paper>
  );
}
