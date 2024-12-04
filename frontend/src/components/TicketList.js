import React from 'react';
import { Card, CardContent, Typography, Button } from '@mui/material';

const TicketList = ({ tickets, onAction, actionLabel }) => {
  return (
    <>
      {tickets.map((ticket) => (
        <Card key={ticket.id} sx={{ marginBottom: 2 }}>
          <CardContent>
            <Typography variant="h6">{ticket.name}</Typography>
            <Typography>Price: ${ticket.price}</Typography>
            <Typography>Available: {ticket.quantity}</Typography>
            {onAction && (
              <Button
                variant="contained"
                color="primary"
                onClick={() => onAction(ticket.id)}
                disabled={ticket.quantity === 0}
              >
                {actionLabel}
              </Button>
            )}
          </CardContent>
        </Card>
      ))}
    </>
  );
};

export default TicketList;
