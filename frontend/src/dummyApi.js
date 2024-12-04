// src/api.js

import { tickets } from "./dummyData";

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
