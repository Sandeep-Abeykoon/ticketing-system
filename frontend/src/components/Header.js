import React from 'react';
import { AppBar, Toolbar, Typography, Button } from '@mui/material';
import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          Ticketing System
        </Typography>
        <Button color="inherit" component={Link} to="/vendor">
          Vendor
        </Button>
        <Button color="inherit" component={Link} to="/customer">
          Customer
        </Button>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
