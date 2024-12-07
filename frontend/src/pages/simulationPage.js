import React, { useState } from "react";
import { checkSystemConfigured, startSimulation, stopSimulation } from "../dummyApi";
import { TextField, Button, Box, Alert, Typography } from "@mui/material";

const SimulationPage = () => {
  const [numberOfCustomers, setNumberOfCustomers] = useState(0);
  const [numberOfVendors, setNumberOfVendors] = useState(0);
  const [isRunning, setIsRunning] = useState(false);
  const [message, setMessage] = useState("");

  const handleStartSimulation = async () => {
    try {
      // Check if the system is configured
      const isConfigured = await checkSystemConfigured();
      if (!isConfigured) {
        setMessage({
          type: "error",
          text: "System is not configured. Please configure the system before running the simulation.",
        });
        return;
      }

      // Start the simulation
      await startSimulation(numberOfCustomers, numberOfVendors);
      setIsRunning(true);
      setMessage({ type: "success", text: "Simulation started successfully." });
    } catch (error) {
      console.error("Failed to start simulation:", error);
      setMessage({ type: "error", text: "Failed to start simulation." });
    }
  };

  const handleStopSimulation = async () => {
    try {
      // Stop the simulation
      await stopSimulation();
      setIsRunning(false);
      setMessage({ type: "success", text: "Simulation stopped successfully." });
    } catch (error) {
      console.error("Failed to stop simulation:", error);
      setMessage({ type: "error", text: "Failed to stop simulation." });
    }
  };

  return (
    <div style={{ width: "60%", margin: "auto", marginTop: 20 }}>
      <h1>Simulation Dashboard</h1>
      {message && <Alert severity={message.type}>{message.text}</Alert>}
      <Box component="form" sx={{ marginBottom: 4 }}>
        <Typography variant="h6" sx={{ marginBottom: 2 }}>
          Configure Simulation
        </Typography>
        <TextField
          label="Number of Customers"
          type="number"
          value={numberOfCustomers}
          onChange={(e) => setNumberOfCustomers(Number(e.target.value))}
          fullWidth
          required
          sx={{ marginBottom: 2 }}
        />
        <TextField
          label="Number of Vendors"
          type="number"
          value={numberOfVendors}
          onChange={(e) => setNumberOfVendors(Number(e.target.value))}
          fullWidth
          required
          sx={{ marginBottom: 2 }}
        />
      </Box>
      <Box sx={{ display: "flex", gap: 2 }}>
        <Button
          variant="contained"
          color="success"
          onClick={handleStartSimulation}
          disabled={isRunning}
        >
          Start Simulation
        </Button>
        <Button
          variant="contained"
          color="error"
          onClick={handleStopSimulation}
          disabled={!isRunning}
        >
          Stop Simulation
        </Button>
      </Box>
    </div>
  );
};

export default SimulationPage;
