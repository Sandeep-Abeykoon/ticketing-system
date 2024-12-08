import React, { useState, useEffect, useRef } from "react";
import useWebSocket from "../hooks/webSocket";
import { startSimulation, stopSimulation, getSimulationStatus } from "../dummyApi";
import { TextField, Button, Box, Alert, Typography, Paper } from "@mui/material";

const SimulationPage = () => {
  const { logs, ticketAvailability: wsTicketAvailability, simulationStatus: wsSimulationStatus } = useWebSocket();
  const [numberOfCustomers, setNumberOfCustomers] = useState(0);
  const [numberOfVendors, setNumberOfVendors] = useState(0);
  const [simulationStatus, setSimulationStatus] = useState(false); // REST API status
  const [initialTicketAvailability, setInitialTicketAvailability] = useState(0);
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const logContainerRef = useRef(null); // Reference for the log container

  // Fetch initial simulation status and ticket count
  useEffect(() => {
    const fetchInitialStatus = async () => {
      try {
        const response = await getSimulationStatus();
        console.log("Initial Simulation Status:", response);
        setSimulationStatus(response.isRunning); // Use REST API response
        setInitialTicketAvailability(response.ticketCount);
        setNumberOfVendors(response.numberOfVendors);
        setNumberOfCustomers(response.numberOfCustomers);
      } catch (error) {
        console.error("Failed to fetch simulation status:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchInitialStatus();
  }, []);

  // Auto-scroll to the latest log when logs change
  useEffect(() => {
    if (logContainerRef.current) {
      logContainerRef.current.scrollTop = logContainerRef.current.scrollHeight;
    }
  }, [logs]);

  const handleStart = async () => {
    try {
      await startSimulation(numberOfCustomers, numberOfVendors);
      setMessage({ type: "success", text: "Simulation started successfully." });
      setSimulationStatus(true); // Optimistically set the simulation as running
    } catch (error) {
      setMessage({ type: "error", text: "Failed to start simulation." });
    }
  };

  const handleStop = async () => {
    try {
      await stopSimulation();
      setMessage({ type: "success", text: "Simulation stopped successfully." });
      setSimulationStatus(false); // Optimistically set the simulation as stopped
    } catch (error) {
      setMessage({ type: "error", text: "Failed to stop simulation." });
    }
  };

  // Determine the current simulation status
  const isSimulationRunning =
    wsSimulationStatus !== null ? wsSimulationStatus : simulationStatus; // Prioritize WebSocket updates if available
  const currentTicketAvailability =
    wsTicketAvailability !== null ? wsTicketAvailability : initialTicketAvailability; // Prioritize WebSocket updates if available

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <div style={{ width: "60%", margin: "auto", marginTop: 20 }}>
      <h1>Simulation Dashboard</h1>
      {message && <Alert severity={message.type} sx={{ mb: 2 }}>{message.text}</Alert>}
      <Typography variant="h6">Simulation Status: {isSimulationRunning ? "Running" : "Stopped"}</Typography>
      <Typography variant="h6" sx={{ mb: 4 }}>Available Tickets: {currentTicketAvailability}</Typography>
      
      <Box component="form" sx={{ mb: 2 }}>
        <TextField
          label="Number of Customers"
          type="number"
          value={numberOfCustomers}
          onChange={(e) => setNumberOfCustomers(Number(e.target.value))}
          fullWidth
          sx={{ mb: 2 }}
          disabled={isSimulationRunning} // Disable when simulation is running
        />
        <TextField
          label="Number of Vendors"
          type="number"
          value={numberOfVendors}
          onChange={(e) => setNumberOfVendors(Number(e.target.value))}
          fullWidth
          sx={{ mb: 2 }}
          disabled={isSimulationRunning} // Disable when simulation is running
        />
      </Box>
      
      <Box sx={{ display: "flex", gap: 2, marginBottom: 4 }}>
        <Button
          variant="contained"
          color="success"
          onClick={handleStart}
          disabled={isSimulationRunning}
        >
          Start Simulation
        </Button>
        <Button
          variant="contained"
          color="error"
          onClick={handleStop}
          disabled={!isSimulationRunning}
        >
          Stop Simulation
        </Button>
      </Box>
      
      <Paper
        ref={logContainerRef} // Attach the log container ref here
        style={{
          height: "200px",
          overflowY: "scroll",
          marginTop: 20,
          border: "1px solid black",
          padding: "10px",
        }}
      >
        <Typography variant="h6" sx={{ mb: 1 }}>Logs</Typography>
        {logs.map((log, index) => (
          <Typography key={index} variant="body2" sx={{ mt: 1 }}>
            {log}
          </Typography>
        ))}
      </Paper>
    </div>
  );
};

export default SimulationPage;
