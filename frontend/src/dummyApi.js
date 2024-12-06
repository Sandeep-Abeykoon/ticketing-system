// src/api.js

import { tickets } from "./dummyData";

import axios from "axios";

const API_URL = "http://localhost:8080/api"; // Backend base URL

// Check system configuration status
export const checkSystemConfigured = async () => {
  const response = await axios.get(`${API_URL}/configuration/status`);
  return response.data;
};

export const stopSimulation = async () => {
}

export const startSimulation = async () => {
}


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


