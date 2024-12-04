import React, { useState } from 'react';
import { TextField, Button, Box } from '@mui/material';

const AddTicketForm = ({ onAdd }) => {
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [quantity, setQuantity] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onAdd({ name, price: parseFloat(price), quantity: parseInt(quantity) });
    setName('');
    setPrice('');
    setQuantity('');
  };

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ marginBottom: 4 }}>
      <TextField
        label="Ticket Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        fullWidth
        required
        sx={{ marginBottom: 2 }}
      />
      <TextField
        label="Price"
        value={price}
        onChange={(e) => setPrice(e.target.value)}
        type="number"
        fullWidth
        required
        sx={{ marginBottom: 2 }}
      />
      <TextField
        label="Quantity"
        value={quantity}
        onChange={(e) => setQuantity(e.target.value)}
        type="number"
        fullWidth
        required
        sx={{ marginBottom: 2 }}
      />
      <Button type="submit" variant="contained" color="primary">
        Add Ticket
      </Button>
    </Box>
  );
};

export default AddTicketForm;
