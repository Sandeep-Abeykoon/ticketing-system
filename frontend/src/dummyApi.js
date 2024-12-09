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
export const startSimulation = async (numberOfCustomers, numberOfVendors) => {
  const response = await axios.post(
    `${API_URL}/simulation/start`,
    null, // No request body
    {
      params: {
        numberOfCustomers,
        numberOfVendors,
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


