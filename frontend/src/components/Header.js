import React, { useContext } from "react";
import { AppBar, Toolbar, Typography, Button, Box, IconButton, Menu, MenuItem } from "@mui/material";
import { Link } from "react-router-dom";
import { WebSocketContext } from "./context/WebSocketContext";
import MenuIcon from "@mui/icons-material/Menu";
import { useState } from "react";

const Header = () => {
  const { connectionStatus } = useContext(WebSocketContext);
  const [anchorEl, setAnchorEl] = useState(null);

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const getStatusStyle = () => {
    if (connectionStatus === "connected") return { color: "#AAFF00" }; 
    if (connectionStatus === "connecting") return { color: "#FFEB3B" }; 
    return { color: "#EE4B2B" }; 
  };

  return (
    <AppBar position="static">
      <Toolbar sx={{ justifyContent: "space-between", flexWrap: "wrap" }}>
        <Typography variant="h6" sx={{ flexGrow: { xs: 1, sm: 0 } }}>
          Ticketing System
        </Typography>

        {/* Server Status */}
        <Box
          sx={{
            display: { xs: "none", sm: "flex" },
            alignItems: "center",
            marginRight: 4,
            flexGrow: 1,
          }}
        >
          <Typography
            variant="subtitle1"
            sx={{ fontWeight: "bold", display: "flex", alignItems: "center", gap: 1 }}
          >
            <span style={{ color: "white" }}>Server Status:</span>
            <span style={getStatusStyle()}>{connectionStatus.toUpperCase()}</span>
          </Typography>
        </Box>

        {/* Desktop Menu */}
        <Box sx={{ display: { xs: "none", sm: "flex" } }}>
          <Button color="inherit" component={Link} to="/config">
            Configuration
          </Button>
          <Button color="inherit" component={Link} to="/simulation">
            Simulation
          </Button>
          <Button color="inherit" component={Link} to="/dashboard">
            Dashboard
          </Button>
          <Button color="inherit" component={Link} to="/transactions">
            Transactions
          </Button>
        </Box>

        {/* Mobile Menu */}
        <Box sx={{ display: { xs: "flex", sm: "none" } }}>
          <IconButton color="inherit" onClick={handleMenuOpen}>
            <MenuIcon />
          </IconButton>
          <Menu
            anchorEl={anchorEl}
            open={Boolean(anchorEl)}
            onClose={handleMenuClose}
            sx={{ display: { xs: "block", sm: "none" } }}
          >
            <MenuItem onClick={handleMenuClose} component={Link} to="/config">
              Configuration
            </MenuItem>
            <MenuItem onClick={handleMenuClose} component={Link} to="/simulation">
              Simulation
            </MenuItem>
            <MenuItem onClick={handleMenuClose} component={Link} to="/dashboard">
              Dashboard
            </MenuItem>
            <MenuItem onClick={handleMenuClose} component={Link} to="/transactions">
              Transactions
            </MenuItem>
          </Menu>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
