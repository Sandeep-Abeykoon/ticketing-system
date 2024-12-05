// src/api.js

import { tickets } from "./dummyData";

import axios from "axios";

const API_URL = "http://localhost:8080/api"; // Backend base URL

// Check system configuration status
export const checkSystemConfigured = async () => {
  const response = await axios.get(`${API_URL}/configuration/status`);
  return response.data;
};

// Update system status (run/halt)
export const updateSystemStatus = async (status) => {
  const response = await axios.put(`${API_URL}/configuration/status`, { systemConfigured: status });
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

// Fetch all tickets
export const getTickets = async () => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(tickets), 500); // Simulate network latency
  });
};

// Add a new ticket
export const addTicket = async (ticket) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      const newTicket = { id: tickets.length + 1, ...ticket };
      tickets.push(newTicket);
      resolve(newTicket);
    }, 500);
  });
};

// Purchase a ticket
export const purchaseTicket = async (ticketId) => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const ticket = tickets.find((t) => t.id === ticketId);
      if (ticket && ticket.quantity > 0) {
        ticket.quantity -= 1;
        resolve(ticket);
      } else {
        reject(new Error("Ticket not available"));
      }
    }, 500);
  });
};
