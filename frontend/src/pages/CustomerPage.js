import React, { useEffect, useState } from 'react';
import { getTickets, purchaseTicket } from '../dummyApi';
import TicketList from '../components/TicketList';

const CustomerPage = () => {
  const [tickets, setTickets] = useState([]);

  useEffect(() => {
    fetchTickets();
  }, []);

  const fetchTickets = async () => {
    const data = await getTickets();
    setTickets(data);
  };

  const handlePurchase = async (ticketId) => {
    await purchaseTicket(ticketId);
    fetchTickets();
  };

  return (
    <div>
      <h2>Available Tickets</h2>
      <TicketList tickets={tickets} onAction={handlePurchase} actionLabel="Buy" />
    </div>
  );
};

export default CustomerPage;
