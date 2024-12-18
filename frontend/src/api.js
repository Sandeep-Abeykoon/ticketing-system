// src/api.js
import axios from "axios";

const API_URL = "http://localhost:8080/api"; // Backend base URL

// Check system configuration status
export const checkSystemConfigured = async () => {
  const response = await axios.get(`${API_URL}/configuration/status`);
  return response.data;
};

// Fetch current configuration
export const fetchConfiguration = async () => {
  const response = await axios.get(`${API_URL}/configuration`);
  return response.data;
};

// Update configuration
export const updateConfiguration = async (config) => {
  const response = await axios.put(`${API_URL}/configuration`, config);
  return response.data;
};

export const getSimulationStatus = async () => {
  const response = await axios.get(`${API_URL}/simulation/status`);
  return response.data;
};

// Start simulation
export const startSimulation = async (numberOfCustomers, numberOfVendors, numberOfVIPCustomers) => {
  const response = await axios.post(
    `${API_URL}/simulation/start`,
    null, // No request body
    {
      params: {
        numberOfCustomers,
        numberOfVendors,
        numberOfVIPCustomers,
      },
    }
  );
  return response.data;
};

// Stop simulation
export const stopSimulation = async () => {
  const response = await axios.post(`${API_URL}/simulation/stop`);
  return response.data;
};

// Reset Simulation
export const resetSimulation = async () => {
  const response = await axios.post(`${API_URL}/simulation/reset`);
  return response.data;
};

//Get all transactions (Which are saved in the database)
export const getAllTransactions = async () => {
  const response = await axios.get(`${API_URL}/transactions`);
  return response.data;
};

// Add a vendor
export const addVendor = async () => {
  const response = await axios.post(`${API_URL}/participants/vendor/add`);
  return response.data;
};

// Remove a vendor
export const removeVendor = async (vendorId) => {
  const response = await axios.delete(`${API_URL}/participants/vendor/remove/${vendorId}`);
  return response.data;
};

// Add a customer (Normal or VIP)
export const addCustomer = async (isVIP) => {
  const response = await axios.post(`${API_URL}/participants/customer/add`, null, {
    params: { isVIP },
  });
  return response.data;
};

// Remove a customer (Normal or VIP)
export const removeCustomer = async (customerId, isVIP) => {
  const response = await axios.delete(`${API_URL}/participants/customer/remove/${customerId}`, {
    params: { isVIP },
  });
  return response.data;
};