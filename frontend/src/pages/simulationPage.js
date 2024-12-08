import React, { useState } from "react"; // Added useState here
import { startSimulation, stopSimulation } from "../dummyApi";
import useWebSocket from "../hooks/webSocket";
import { TextField, Button, Box, Alert, Typography } from "@mui/material";

const SimulationPage = () => {
  const { logs, ticketAvailability, systemStatus } = useWebSocket();
  const [numberOfCustomers, setNumberOfCustomers] = useState(0);
  const [numberOfVendors, setNumberOfVendors] = useState(0);
  const [message, setMessage] = useState("");


  const handleStart = async () => {
    try {
      await startSimulation(numberOfCustomers, numberOfVendors);
      setMessage({ type: "success", text: "Simulation started successfully." });
    } catch (error) {
      setMessage({ type: "error", text: "Failed to start simulation." });
    }
  };

  const handleStop = async () => {
    try {
      await stopSimulation();
      setMessage({ type: "success", text: "Simulation stopped successfully." });
    } catch (error) {
      setMessage({ type: "error", text: "Failed to stop simulation." });
    }
  };

  return (
    <div style={{ width: "60%", margin: "auto", marginTop: 20 }}>
      <h1>Simulation Dashboard</h1>
      {message && <Alert severity={message.type}>{message.text}</Alert>}
      <Typography variant="h6">System Status: {systemStatus ? "Running" : "Stopped"}</Typography>
      <Typography variant="h6">Ticket Availability: {ticketAvailability}</Typography>
      <Box component="form">
        <TextField
          label="Number of Customers"
          type="number"
          value={numberOfCustomers}
          onChange={(e) => setNumberOfCustomers(Number(e.target.value))}
        />
        <TextField
          label="Number of Vendors"
          type="number"
          value={numberOfVendors}
          onChange={(e) => setNumberOfVendors(Number(e.target.value))}
        />
      </Box>
      <Box sx={{ display: "flex", gap: 2, marginTop: 2 }}>
        <Button variant="contained" color="success" onClick={handleStart} disabled={systemStatus}>
          Start Simulation
        </Button>
        <Button variant="contained" color="error" onClick={handleStop} disabled={!systemStatus}>
          Stop Simulation
        </Button>
      </Box>
      <div style={{ height: "200px", overflowY: "scroll", marginTop: 20, border: "1px solid black" }}>
        <Typography variant="h6">Logs</Typography>
        {logs.map((log, index) => (
          <p key={index}>{log}</p>
        ))}
      </div>
    </div>
  );
};

export default SimulationPage;
