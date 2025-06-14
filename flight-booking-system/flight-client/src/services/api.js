// src/services/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  // baseURL: 'http://localhost:8080/api', // Your backend API base URL
});

// Request interceptor to add token to headers
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Optional: Response interceptor for global error handling
api.interceptors.response.use(
  (response) => response, // Simply return response if successful
  (error) => {
    // Example: Handle 401 Unauthorized (e.g., token expired)
    if (error.response && error.response.status === 401) {
      // Here you could trigger a logout action from AuthContext
      // For now, just log or alert
      console.error('Unauthorized access - 401. Token might be expired.');
      // Potentially: localStorage.removeItem('authToken'); localStorage.removeItem('authUser'); window.location.href = '/login';
    }
    // You can add more global error handling here
    return Promise.reject(error);
  }
);

export default api;
