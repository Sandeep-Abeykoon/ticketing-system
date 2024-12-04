import React, { useEffect, useState } from 'react';
import { getTickets, addTicket } from '../dummyApi';
import AddTicketForm from '../components/AddTicketForm';
import TicketList from '../components/TicketList';

const VendorPage = () => {
  const [tickets, setTickets] = useState([]);

  useEffect(() => {
    fetchTickets();
  }, []);

  const fetchTickets = async () => {
    const data = await getTickets();
    setTickets(data);
  };

  const handleAddTicket = async (ticket) => {
    await addTicket(ticket);
    fetchTickets();
  };

  return (
    <div>
      <h2>Vendor Dashboard</h2>
      <AddTicketForm onAdd={handleAddTicket} />
      <TicketList tickets={tickets} />
    </div>
  );
};

export default VendorPage;
