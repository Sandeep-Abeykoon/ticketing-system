import React, { useContext } from "react";
import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";
import { Link } from "react-router-dom";
import { WebSocketContext } from "./context/WebSocketContext";

const Header = () => {
  const { connectionStatus } = useContext(WebSocketContext);

  const getStatusStyle = () => {
    if (connectionStatus === "connected") return { color: "#AAFF00" }; 
    if (connectionStatus === "connecting") return { color: "#FFEB3B" }; 
    return { color: "#EE4B2B" }; 
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          Ticketing System
        </Typography>
        <Box sx={{ display: "flex", alignItems: "center", marginRight: 4 }}>
          <Typography
            variant="subtitle1"
            sx={{ fontWeight: "bold", display: "flex", alignItems: "center", gap: 1 }}
          >
            <span style={{ color: "white" }}>Server Status:</span>
            <span style={getStatusStyle()}>{connectionStatus.toUpperCase()}</span>
          </Typography>
        </Box>
        <Button color="inherit" component={Link} to="/config">
          Configuration
        </Button>
        <Button color="inherit" component={Link} to="/simulation">
          Simulation
        </Button>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
