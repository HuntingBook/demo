// src/pages/FlightListPage.jsx
import React, { useEffect, useState, useMemo } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import flightService from '../services/flightService';
import FlightCard from '../components/FlightCard';
import FlightCardSkeleton from '../components/FlightCardSkeleton';
import { Container, Typography, Alert, Grid, Box, Pagination, Select, MenuItem, FormControl, InputLabel, Paper } from '@mui/material'; // Removed CircularProgress
import dayjs from 'dayjs';
import { toast } from 'react-toastify'; // Added toast

const FLIGHTS_PER_PAGE = 5;
const SKELETON_COUNT = 3; // Number of skeletons to display

export default function FlightListPage() {
  const location = useLocation();
  const navigate = useNavigate(); // Although not used in provided code, it's good practice to keep if navigation might be needed
  const [flights, setFlights] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchCriteria, setSearchCriteria] = useState({});
const [tripType, setTripType] = useState('ONE_WAY');
  const [returnDateFromParams, setReturnDateFromParams] = useState(null);
  const [selectedOutboundFlight, setSelectedOutboundFlight] = useState(null);
  const [currentLegSelection, setCurrentLegSelection] = useState('outbound'); // 'outbound' or 'return'
  
  const [currentPage, setCurrentPage] = useState(1);
  const [sortOrder, setSortOrder] = useState('price_asc'); // e.g., 'price_asc', 'time_asc'

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const fromParam = params.get('from');
    const toParam = params.get('to');
    const dateParam = params.get('date');
    const tripTypeParam = params.get('tripType');
    const returnDateParam = params.get('returnDate');

    setTripType(tripTypeParam || 'ONE_WAY');
    if (returnDateParam) {
      setReturnDateFromParams(returnDateParam);
    }
    
    // Determine which leg to fetch
    let effectiveFrom = fromParam;
    let effectiveTo = toParam;
    let effectiveDate = dateParam;

    if (tripTypeParam === 'ROUND_TRIP' && currentLegSelection === 'return' && selectedOutboundFlight) {
      // For return leg, swap from/to and use returnDate
      effectiveFrom = selectedOutboundFlight.destinationAirport.code; // Assuming structure from FlightDTO
      effectiveTo = selectedOutboundFlight.departureAirport.code;
      effectiveDate = returnDateParam;
      setSearchCriteria({ 
        from: effectiveFrom, 
        to: effectiveTo, 
        date: dayjs(effectiveDate).format('ddd, MMM D, YYYY'),
        tripType: 'ROUND_TRIP',
        leg: 'return'
      });
    } else {
      // For outbound leg or one-way
      setSearchCriteria({ 
        from: fromParam, 
        to: toParam, 
        date: dayjs(dateParam).format('ddd, MMM D, YYYY'),
        tripType: tripTypeParam || 'ONE_WAY',
        leg: 'outbound'
      });
    }

    if (effectiveFrom && effectiveTo && effectiveDate) {
      setIsLoading(true);
      setFlights([]); // Clear previous flights
      flightService.searchFlights({ fromAirportCode: effectiveFrom, toAirportCode: effectiveTo, departureDate: effectiveDate })
        .then(data => {
          setFlights(data || []);
          if (!data || data.length === 0) {
            toast.info(`No ${currentLegSelection === 'return' ? 'return' : 'outbound'} flights found.`);
          }
          setIsLoading(false);
        })
        .catch(err => {
          const message = err.message || `Failed to fetch ${currentLegSelection === 'return' ? 'return' : 'outbound'} flights.`;
          setError(message);
          toast.error(message);
          setIsLoading(false);
          setFlights([]);
        });
    } else {
      const message = 'No search criteria provided. Please start a new search.';
      setError(message);
      toast.warn(message);
      setIsLoading(false);
    }
  }, [location.search, currentLegSelection, selectedOutboundFlight, navigate]); // Added navigate
const handleFlightSelection = (flight) => {
    if (tripType === 'ROUND_TRIP' && currentLegSelection === 'outbound') {
      setSelectedOutboundFlight(flight);
      setCurrentLegSelection('return'); // Switch to selecting the return leg
      setCurrentPage(1); // Reset page for return flight results
      // The useEffect will re-trigger due to currentLegSelection change
      // and fetch flights for the return leg.
      toast.info('Please select your return flight.');
    } else {
      // For one-way or when selecting the return leg of a round trip
      if (tripType === 'ROUND_TRIP') {
        sessionStorage.setItem('selectedOutboundFlight', JSON.stringify(selectedOutboundFlight));
        sessionStorage.setItem('selectedReturnFlight', JSON.stringify(flight));
      } else {
        sessionStorage.setItem('selectedFlight', JSON.stringify(flight));
      }
      sessionStorage.setItem('tripType', tripType); // Store trip type for confirmation page
      navigate('/confirm-flight');
    }
  };

  const handleSortChange = (event) => {
    setSortOrder(event.target.value);
    setCurrentPage(1); // Reset to first page on sort change
  };

  const sortedFlights = useMemo(() => {
    let sortableFlights = [...flights];
    if (sortOrder === 'price_asc') {
      sortableFlights.sort((a, b) => a.price - b.price);
    } else if (sortOrder === 'price_desc') {
      sortableFlights.sort((a, b) => b.price - a.price);
    } else if (sortOrder === 'time_asc') { 
        sortableFlights.sort((a,b) => (a.departureTime || "").localeCompare(b.departureTime || ""));
    }
    // Example: Add duration sort (assuming duration is pre-calculated or calculable)
    // else if (sortOrder === 'duration_asc') {
    //   sortableFlights.sort((a, b) => {
    //     const durationA = calculateDuration(a.departureTime, a.arrivalTime); // Assuming calculateDuration is available
    //     const durationB = calculateDuration(b.departureTime, b.arrivalTime);
    //     // Handle "N/A" or errors from calculateDuration if necessary
    //     if (durationA === "N/A") return 1;
    //     if (durationB === "N/A") return -1;
    //     // Simple string comparison for "Xh Ym" might not be robust, convert to minutes for proper sort
    //     const toMinutes = (durStr) => {
    //         const parts = durStr.match(/(\d+)h\s*(\d+)m/);
    //         if (!parts) return Infinity;
    //         return parseInt(parts[1]) * 60 + parseInt(parts[2]);
    //     };
    //     return toMinutes(durationA) - toMinutes(durationB);
    //   });
    // }
    return sortableFlights;
  }, [flights, sortOrder]);

  const paginatedFlights = useMemo(() => {
    const startIndex = (currentPage - 1) * FLIGHTS_PER_PAGE;
    return sortedFlights.slice(startIndex, startIndex + FLIGHTS_PER_PAGE);
  }, [sortedFlights, currentPage]);

  const handlePageChange = (event, value) => {
    setCurrentPage(value);
  };

  // if (isLoading) { // Modified to show skeletons if flights array is empty during load
  //   return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 5 }}><CircularProgress /></Box>;
  // }

  return (
    // Container uses MUI for max-width, Tailwind for padding/margin
    <Container maxWidth="lg" className="mt-4 sm:mt-8 p-2 sm:p-4">
      {/* Typography uses MUI variant, Tailwind for responsive size */}
      <Typography variant="h4" component="h1" gutterBottom className="text-2xl sm:text-3xl md:text-4xl font-semibold mb-2"> 
        Flight Search Results
      </Typography>
      {searchCriteria.from && (
        // Typography uses MUI variant, Tailwind for responsive size and margin
        <Typography variant="subtitle1" gutterBottom className="mb-4 text-sm sm:text-base"> 
          Showing flights from <strong>{searchCriteria.from}</strong> to <strong>{searchCriteria.to}</strong> on <strong>{searchCriteria.date}</strong>
        </Typography>
      )}

      {error && <Alert severity="error" className="mb-4">{error}</Alert>} {/* Tailwind margin */}

      {!isLoading && !error && flights.length > 0 && (
        // Paper uses MUI elevation/theming, Tailwind for padding, margin, flex layout, rounded corners
        <Paper elevation={1} className="p-3 mb-6 flex flex-col sm:flex-row justify-center sm:justify-end rounded-xl">
          {/* FormControl uses MUI structure, Tailwind for responsive width */}
          <FormControl size="small" className="w-full sm:w-auto sm:min-w-[200px]"> 
            <InputLabel id="sort-by-label">Sort By</InputLabel>
            <Select
              labelId="sort-by-label"
              id="sort-by-select"
              value={sortOrder}
              label="Sort By"
              onChange={handleSortChange}
            >
              <MenuItem value="price_asc">Price: Low to High</MenuItem>
              <MenuItem value="price_desc">Price: High to Low</MenuItem>
              <MenuItem value="time_asc">Departure Time: Earliest</MenuItem>
              {/* <MenuItem value="duration_asc">Duration: Shortest</MenuItem> */}
            </Select>
          </FormControl>
        </Paper>
      )}
      
      {!isLoading && !error && flights.length > 0 && paginatedFlights.length === 0 && (
         <Typography variant="subtitle1" sx={{mt:2}}>No flights match current page. Try adjusting page number or sort criteria.</Typography>
      )}

      {!isLoading && !error && flights.length === 0 && !searchCriteria.from && ( // Only show if no criteria was set initially
         <Alert severity="info">Please initiate a search from the Home page.</Alert>
      )}
      
      {!isLoading && !error && flights.length === 0 && searchCriteria.from && ( // Show if search was done but no results
         <Alert severity="info">No flights found matching your criteria. Please try a different search.</Alert>
      )}

      <Grid container spacing={3}>
        {isLoading && flights.length === 0 ? ( // Display skeletons only when loading and no flights are yet available
          Array.from(new Array(SKELETON_COUNT)).map((_, index) => (
            <Grid item xs={12} key={`skeleton-${index}`}>
              <FlightCardSkeleton />
            </Grid>
          ))
        ) : !isLoading && paginatedFlights.length === 0 ? ( // This condition is for when loading is done and no flights to show on the current page
          <Grid item xs={12}>
            {/* This message is now shown here instead of below the grid, which is better if grid is empty */}
          </Grid>
        ) : ( // Display actual flight cards
          paginatedFlights.map(flight => (
            <Grid item xs={12} key={flight.flightId}>
              <FlightCard flight={flight} onSelectFlight={handleFlightSelection} />
            </Grid>
          ))
        )}
      </Grid>
      
      {/* Informational messages based on state */}
      {!isLoading && !error && flights.length === 0 && searchCriteria.from && ( // Initial search yielded no results
        <Alert severity="info" sx={{ mt: 3 }}>No flights found matching your criteria. Please try a different search.</Alert>
      )}
      {!isLoading && !error && flights.length > 0 && paginatedFlights.length === 0 && ( // Results exist, but not on this page (e.g. after sort/page change)
         <Typography variant="subtitle1" sx={{mt:3, textAlign:'center'}}>No flights match the current page or filter criteria.</Typography>
      )}
      {!isLoading && !error && flights.length === 0 && !searchCriteria.from && ( // Page loaded without search params
         <Alert severity="info" sx={{ mt: 3 }}>Please initiate a search from the Home page.</Alert>
      )}

      {sortedFlights.length > FLIGHTS_PER_PAGE && !isLoading && paginatedFlights.length > 0 && ( // Hide pagination if no flights are displayed
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4, mb:2 }}>
          <Pagination
            count={Math.ceil(sortedFlights.length / FLIGHTS_PER_PAGE)}
            page={currentPage}
            onChange={handlePageChange}
            color="primary"
            showFirstButton
            showLastButton
          />
        </Box>
      )}
    </Container>
  );
}
