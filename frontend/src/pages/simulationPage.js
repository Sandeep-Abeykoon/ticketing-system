import React, { useEffect, useState, useRef } from "react";
import useWebSocket from "../hooks/webSocket";
import { startSimulation, stopSimulation, getSimulationStatus } from "../dummyApi";
import { validateField } from "../utils/validation";
import { TextField, Button, Box, Alert, Typography, Paper } from "@mui/material";

const SimulationPage = () => {
  const { logs, ticketAvailability: wsTicketAvailability, simulationStatus: wsSimulationStatus } = useWebSocket();
  const [numberOfCustomers, setNumberOfCustomers] = useState("");
  const [numberOfVendors, setNumberOfVendors] = useState("");
  const [simulationStatus, setSimulationStatus] = useState(false);
  const [initialTicketAvailability, setInitialTicketAvailability] = useState(0);
  const [message, setMessage] = useState(null);
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const [localLogs, setLocalLogs] = useState([]);
  const logContainerRef = useRef(null);

  useEffect(() => {
    const fetchInitialStatus = async () => {
      try {
        const response = await getSimulationStatus();
        setSimulationStatus(response.isRunning);
        setInitialTicketAvailability(response.ticketCount);
        setNumberOfVendors(response.numberOfVendors?.toString() || "");
        setNumberOfCustomers(response.numberOfCustomers?.toString() || "");
      } catch (error) {
        console.error("Failed to fetch simulation status:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchInitialStatus();
  }, []);

  useEffect(() => {
    if (logContainerRef.current) {
      logContainerRef.current.scrollTop = logContainerRef.current.scrollHeight;
    }
  }, [logs]);

  const handleStart = async () => {
    // Validate fields
    const customerError = validateField("numberOfCustomers", numberOfCustomers);
    const vendorError = validateField("numberOfVendors", numberOfVendors);

    if (customerError || vendorError) {
      setErrors({
        numberOfCustomers: customerError,
        numberOfVendors: vendorError,
      });
      return;
    }

    try {
      await startSimulation(Number(numberOfCustomers), Number(numberOfVendors));
      setMessage({ type: "success", text: "Simulation started successfully." });
      setSimulationStatus(true);
      dismissMessageAfterDelay();
    } catch (error) {
      setMessage({ type: "error", text: "Failed to start simulation." });
      dismissMessageAfterDelay();
    }
  };

  const handleStop = async () => {
    try {
      await stopSimulation();
      setMessage({ type: "success", text: "Simulation stopped successfully." });
      setSimulationStatus(false);
      dismissMessageAfterDelay();
    } catch (error) {
      setMessage({ type: "error", text: "Failed to stop simulation." });
      dismissMessageAfterDelay();
    }
  };

  const handleClearLogs = () => {
    setLocalLogs([]);
    setMessage({ type: "success", text: "Logs cleared successfully!" });
    dismissMessageAfterDelay();
  };

  const dismissMessageAfterDelay = () => {
    setTimeout(() => setMessage(null), 3000); // Message disappears after 3 seconds
  };

  const isSimulationRunning =
    wsSimulationStatus !== null ? wsSimulationStatus : simulationStatus;
  const currentTicketAvailability =
    wsTicketAvailability !== null ? wsTicketAvailability : initialTicketAvailability;

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
          onChange={(e) => setNumberOfCustomers(e.target.value)}
          fullWidth
          sx={{ mb: 2 }}
          disabled={isSimulationRunning}
          error={!!errors.numberOfCustomers}
          helperText={errors.numberOfCustomers}
        />
        <TextField
          label="Number of Vendors"
          type="number"
          value={numberOfVendors}
          onChange={(e) => setNumberOfVendors(e.target.value)}
          fullWidth
          sx={{ mb: 2 }}
          disabled={isSimulationRunning}
          error={!!errors.numberOfVendors}
          helperText={errors.numberOfVendors}
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
        <Button
          variant="contained"
          color="secondary"
          onClick={handleClearLogs}
        >
          Clear Logs
        </Button>
      </Box>
      
      <Paper
        ref={logContainerRef}
        style={{
          height: "200px",
          overflowY: "scroll",
          marginTop: 20,
          border: "1px solid black",
          padding: "10px",
        }}
      >
        <Typography variant="h6" sx={{ mb: 1 }}>Logs</Typography>
        {(localLogs.length > 0 ? localLogs : logs).map((log, index) => (
          <Typography key={index} variant="body2" sx={{ mt: 1 }}>
            {log}
          </Typography>
        ))}
      </Paper>
    </div>
  );
};

export default SimulationPage;
