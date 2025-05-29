// src/services/authService.js
import api from './api';

const login = async (email, password) => {
  const response = await api.post('/auth/login', { email, password });
  // Assuming backend returns { success: true, data: { token: '...', user: {...} } }
  if (response.data && response.data.success && response.data.data.token) {
    localStorage.setItem('authToken', response.data.data.token);
    localStorage.setItem('authUser', JSON.stringify(response.data.data.user)); // Store user info
  }
  return response.data; // Return the whole response for context to handle
};

const register = async (userData) => {
  // userData: { email, password, firstName, lastName, country, phone }
  const response = await api.post('/auth/register', userData);
  if (response.data && response.data.success && response.data.data.token) {
    localStorage.setItem('authToken', response.data.data.token);
    localStorage.setItem('authUser', JSON.stringify(response.data.data.user));
  }
  return response.data;
};

const logout = () => {
  localStorage.removeItem('authToken');
  localStorage.removeItem('authUser');
  // Any backend call for logout can be added here if needed
};

export default {
  login,
  register,
  logout,
};
