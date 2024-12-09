import React, { useEffect, useState, useRef } from "react";
import useWebSocket from "../hooks/webSocket";
import { startSimulation, stopSimulation, getSimulationStatus } from "../dummyApi";
import { validateField } from "../utils/validation";
import { TextField, Button, Box, Alert, Typography, Paper } from "@mui/material";

const SimulationPage = () => {
  const { logs: wsLogs, ticketAvailability: wsTicketAvailability, simulationStatus: wsSimulationStatus } =
    useWebSocket();
  const [numberOfCustomers, setNumberOfCustomers] = useState("");
  const [numberOfVendors, setNumberOfVendors] = useState("");
  const [simulationStatus, setSimulationStatus] = useState(false);
  const [initialTicketAvailability, setInitialTicketAvailability] = useState(0);
  const [message, setMessage] = useState(null);
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const [logs, setLogs] = useState([]); // Manage displayed logs
  const [isSubmitting, setIsSubmitting] = useState(false); // Disable buttons during submission
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

  // Sync WebSocket logs with the displayed logs
  useEffect(() => {
    setLogs(wsLogs);
  }, [wsLogs]);

  useEffect(() => {
    if (logContainerRef.current) {
      logContainerRef.current.scrollTop = logContainerRef.current.scrollHeight;
    }
  }, [logs]);

  // Real-time validation on input change
  const handleChange = (e) => {
    const { name, value } = e.target;
    const parsedValue = value === "" ? "" : parseInt(value, 10);
    const error = validateField(name, parsedValue);

    setErrors((prevErrors) => ({ ...prevErrors, [name]: error }));
    if (name === "numberOfCustomers") setNumberOfCustomers(parsedValue);
    if (name === "numberOfVendors") setNumberOfVendors(parsedValue);
  };

  const handleStart = async () => {
    // Check for validation errors before starting
    const customerError = validateField("numberOfCustomers", numberOfCustomers);
    const vendorError = validateField("numberOfVendors", numberOfVendors);

    if (customerError || vendorError) {
      setErrors({
        numberOfCustomers: customerError,
        numberOfVendors: vendorError,
      });
      return;
    }

    setIsSubmitting(true); // Disable buttons while submitting
    setLogs([]); // Clear the logs

    try {
      await startSimulation(Number(numberOfCustomers), Number(numberOfVendors));
      setMessage({ type: "success", text: "Simulation started successfully." });
      setSimulationStatus(true);
    } catch (error) {
      console.error("Failed to start simulation:", error);
      setMessage({
        type: "error",
        text: error.response?.data || "Failed to start simulation.",
      });
    } finally {
      setIsSubmitting(false); // Re-enable buttons after submission
    }
  };

  const handleStop = async () => {
    setIsSubmitting(true); // Disable buttons while submitting

    try {
      await stopSimulation();
      setMessage({ type: "success", text: "Simulation stopped successfully." });
      setSimulationStatus(false);
    } catch (error) {
      console.error("Failed to stop simulation:", error);
      setMessage({
        type: "error",
        text: error.response?.data || "Failed to stop simulation.",
      });
    } finally {
      setIsSubmitting(false); // Re-enable buttons after submission
    }
  };

  const handleClearLogs = () => {
    setLogs([]); // Clear the displayed logs
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
          name="numberOfCustomers"
          type="number"
          value={numberOfCustomers}
          onChange={handleChange}
          fullWidth
          sx={{ mb: 2 }}
          disabled={isSimulationRunning || isSubmitting} // Disable during running or submission
          error={!!errors.numberOfCustomers}
          helperText={errors.numberOfCustomers}
        />
        <TextField
          label="Number of Vendors"
          name="numberOfVendors"
          type="number"
          value={numberOfVendors}
          onChange={handleChange}
          fullWidth
          sx={{ mb: 2 }}
          disabled={isSimulationRunning || isSubmitting} // Disable during running or submission
          error={!!errors.numberOfVendors}
          helperText={errors.numberOfVendors}
        />
      </Box>
      
      <Box sx={{ display: "flex", gap: 2, marginBottom: 4 }}>
        <Button
          variant="contained"
          color="success"
          onClick={handleStart}
          disabled={isSimulationRunning || isSubmitting} // Disable during running or submission
        >
          {isSubmitting ? "Starting..." : "Start Simulation"}
        </Button>
        <Button
          variant="contained"
          color="error"
          onClick={handleStop}
          disabled={!isSimulationRunning || isSubmitting} // Disable if not running or during submission
        >
          {isSubmitting ? "Stopping..." : "Stop Simulation"}
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
