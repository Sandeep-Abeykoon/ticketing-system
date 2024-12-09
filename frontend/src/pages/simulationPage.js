import React, { useContext, useRef, useEffect } from "react";
import { WebSocketContext } from "../components/context/WebSocketContext";
import { startSimulation, stopSimulation } from "../dummyApi";
import { validateField } from "../utils/validation";
import { TextField, Button, Box, Alert, Typography, Paper } from "@mui/material";

const SimulationPage = () => {
  const {
    logs,
    setLogs,
    ticketAvailability,
    setTicketAvailability,
    simulationStatus,
    numberOfCustomers,
    setNumberOfCustomers,
    numberOfVendors,
    setNumberOfVendors,
    isLoading,
  } = useContext(WebSocketContext);

  const [message, setMessage] = React.useState(null);
  const [errors, setErrors] = React.useState({});
  const [isSubmitting, setIsSubmitting] = React.useState(false);

  const logContainerRef = useRef(null);
  const [isScrolled, setIsScrolled] = React.useState(false);

  // Scroll to the bottom when new logs arrive unless the user has manually scrolled up
  useEffect(() => {
    if (logContainerRef.current && !isScrolled) {
      logContainerRef.current.scrollTop = logContainerRef.current.scrollHeight;
    }
  }, [logs, isScrolled]);

  const handleScroll = () => {
    if (logContainerRef.current) {
      const { scrollTop, scrollHeight, clientHeight } = logContainerRef.current;
      setIsScrolled(scrollTop + clientHeight < scrollHeight - 50);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    const parsedValue = value === "" ? "" : parseInt(value, 10);
    const error = validateField(name, parsedValue);

    setErrors((prevErrors) => ({ ...prevErrors, [name]: error }));

    if (name === "numberOfCustomers") setNumberOfCustomers(parsedValue);
    if (name === "numberOfVendors") setNumberOfVendors(parsedValue);
  };

  const handleStart = async () => {
    const customerError = validateField("numberOfCustomers", numberOfCustomers);
    const vendorError = validateField("numberOfVendors", numberOfVendors);

    if (customerError || vendorError) {
      setErrors({
        numberOfCustomers: customerError,
        numberOfVendors: vendorError,
      });
      return;
    }

    setIsSubmitting(true);
    setLogs([]); // Clear logs when starting the simulation

    try {
      await startSimulation(Number(numberOfCustomers), Number(numberOfVendors));
      setMessage({ type: "success", text: "Simulation started successfully." });
    } catch (error) {
      console.error("Failed to start simulation:", error);
      setMessage({
        type: "error",
        text: error.response?.data || "Failed to start simulation.",
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleStop = async () => {
    setIsSubmitting(true);

    try {
      await stopSimulation();
      setMessage({ type: "success", text: "Simulation stopped successfully." });
    } catch (error) {
      console.error("Failed to stop simulation:", error);
      setMessage({
        type: "error",
        text: error.response?.data || "Failed to stop simulation.",
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleClearLogs = () => {
    setLogs([]); // Clear logs
    setMessage({ type: "success", text: "Logs cleared successfully!" });
    dismissMessageAfterDelay();
  };

  const handleReset = () => {
    setNumberOfCustomers(0);
    setNumberOfVendors(0);
    setTicketAvailability(0);
    setLogs([]);
    setMessage({ type: "success", text: "Simulation reset successfully!" });
    dismissMessageAfterDelay();
  };

  const dismissMessageAfterDelay = () => {
    setTimeout(() => setMessage(null), 3000);
  };

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <div style={{ width: "60%", margin: "auto", marginTop: 20 }}>
      <h1>Simulation Dashboard</h1>
      {message && <Alert severity={message.type} sx={{ mb: 2 }}>{message.text}</Alert>}
      <Typography variant="h6">Simulation Status: {simulationStatus ? "Running" : "Stopped"}</Typography>
      <Typography variant="h6" sx={{ mb: 4 }}>Available Tickets: {ticketAvailability || 0}</Typography>
      
      <Box component="form" sx={{ mb: 2 }}>
        <TextField
          label="Number of Customers"
          name="numberOfCustomers"
          type="number"
          value={numberOfCustomers || ""}
          onChange={handleChange}
          fullWidth
          sx={{ mb: 2 }}
          disabled={simulationStatus || isSubmitting}
          error={!!errors.numberOfCustomers}
          helperText={errors.numberOfCustomers}
        />
        <TextField
          label="Number of Vendors"
          name="numberOfVendors"
          type="number"
          value={numberOfVendors || ""}
          onChange={handleChange}
          fullWidth
          sx={{ mb: 2 }}
          disabled={simulationStatus || isSubmitting}
          error={!!errors.numberOfVendors}
          helperText={errors.numberOfVendors}
        />
      </Box>
      
      <Box sx={{ display: "flex", gap: 2, marginBottom: 4 }}>
        <Button
          variant="contained"
          color="success"
          onClick={handleStart}
          disabled={simulationStatus || isSubmitting}
        >
          {isSubmitting ? "Starting..." : "Start Simulation"}
        </Button>
        <Button
          variant="contained"
          color="error"
          onClick={handleStop}
          disabled={!simulationStatus || isSubmitting}
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
        <Button
          variant="contained"
          color="warning"
          onClick={handleReset}
          disabled={simulationStatus || isSubmitting}
        >
          Reset
        </Button>
      </Box>
      
      <Paper
        ref={logContainerRef}
        onScroll={handleScroll}
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
