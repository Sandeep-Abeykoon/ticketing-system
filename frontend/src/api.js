import axios from 'axios';

const API_URL = 'http://localhost:8080/api'; // Update this to match your backend URL

// Fetch all tickets
export const getTickets = async () => {
  const response = await axios.get("http://localhost:8080/tickets");
  return response.data;
};

// Add a new ticket
export const addTicket = async (ticket) => {
  const response = await axios.post("http://localhost:8080/ticket");
  return response.data;
};

// Purchase a ticket
export const purchaseTicket = async (ticketId) => {
  const response = await axios.put(`${API_URL}/tickets/${ticketId}/purchase`);
  return response.data;
};
