// src/context/AuthContext.jsx
import React, { createContext, useContext, useState, useEffect } from 'react';
import authService from '../services/authService';
import { toast } from 'react-toastify'; // Added toast

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('authToken') || null);
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('authToken'));
  const [isLoading, setIsLoading] = useState(false); // For login/register loading state

  useEffect(() => {
    const storedToken = localStorage.getItem('authToken');
    const storedUser = localStorage.getItem('authUser');
    if (storedToken && storedUser) {
      try {
        const parsedUser = JSON.parse(storedUser);
        setToken(storedToken);
        setUser(parsedUser);
        setIsAuthenticated(true);
        // Set token for API headers (already handled by interceptor reading from localStorage)
      } catch (error) {
        console.error("Failed to parse stored user:", error);
        // Clear invalid stored data
        localStorage.removeItem('authToken');
        localStorage.removeItem('authUser');
        setToken(null);
        setUser(null);
        setIsAuthenticated(false);
      }
    }
  }, []);

  const loginContext = async (email, password) => {
    setIsLoading(true);
    try {
      const response = await authService.login(email, password);
      if (response.success && response.data.token && response.data.user) {
        setToken(response.data.token);
        setUser(response.data.user);
        setIsAuthenticated(true);
        toast.success('Login successful!');
        // localStorage is handled by authService.login
        return true;
      } else {
        // Handle login failure (e.g., display error message from response.message)
        setIsAuthenticated(false);
        setUser(null);
        setToken(null);
        toast.error(response.message || 'Login failed. Please check your credentials.');
        throw new Error(response.message || 'Login failed');
      }
    } catch (error) {
      console.error('Login context error:', error);
      toast.error(error.message || 'Login failed. Please check your credentials.');
      setIsAuthenticated(false);
      setUser(null);
      setToken(null);
      localStorage.removeItem('authToken'); // Ensure cleanup
      localStorage.removeItem('authUser');
      throw error; // Re-throw for page to handle
    } finally {
      setIsLoading(false);
    }
  };

  const registerContext = async (userData) => {
    setIsLoading(true);
    try {
      const response = await authService.register(userData);
      if (response.success && response.data.token && response.data.user) {
        setToken(response.data.token);
        setUser(response.data.user);
        setIsAuthenticated(true);
        toast.success('Registration successful!');
        // localStorage handled by authService.register
        return true;
      } else {
        toast.error(response.message || 'Registration failed. Please try again.');
        throw new Error(response.message || 'Registration failed');
      }
    } catch (error) {
      console.error('Register context error:', error);
      toast.error(error.message || 'Registration failed. Please try again.');
      setIsAuthenticated(false);
      setUser(null);
      setToken(null);
      localStorage.removeItem('authToken');
      localStorage.removeItem('authUser');
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const logoutContext = () => {
    authService.logout(); // Clears localStorage
    setUser(null);
    setToken(null);
    setIsAuthenticated(false);
    toast.info('You have been logged out.');
    // Optionally redirect here or let component handle redirect
    // window.location.href = '/login';
  };

  return (
    <AuthContext.Provider value={{ user, token, isAuthenticated, isLoading, loginContext, registerContext, logoutContext }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
