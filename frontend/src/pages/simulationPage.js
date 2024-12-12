import React, { useContext, useRef, useEffect } from "react";
import { WebSocketContext } from "../context/WebSocketContext";
import { startSimulation, stopSimulation } from "../api";
import { validateField } from "../utils/validation";
import { formatLogMessage } from "../utils/logFormatter";
import { TextField, Button, Box, Alert, Typography, Paper, Card, CardContent } from "@mui/material";

const SimulationPage = () => {
  const {
    logs,
    setLogs,
    availableTickets,
    totalTicketsAdded,
    totalTicketsRetrieved,
    totalVIPRetrievals,
    totalNormalRetrievals,
    simulationStatus,
    numberOfCustomers,
    setNumberOfCustomers,
    numberOfVIPCustomers,
    setNumberOfVIPCustomers,
    numberOfVendors,
    setNumberOfVendors,
    isLoading,
    resetSimulationData,
  } = useContext(WebSocketContext);

  const [message, setMessage] = React.useState(null);
  const [errors, setErrors] = React.useState({});
  const [isSubmitting, setIsSubmitting] = React.useState(false);

  const logContainerRef = useRef(null);
  const [isScrolled, setIsScrolled] = React.useState(false);

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
    if (name === "numberOfVIPCustomers") setNumberOfVIPCustomers(parsedValue);
    if (name === "numberOfVendors") setNumberOfVendors(parsedValue);
  };

  const handleStart = async () => {
    const customerError = validateField("numberOfCustomers", numberOfCustomers);
    const vipCustomerError = validateField("numberOfVIPCustomers", numberOfVIPCustomers);
    const vendorError = validateField("numberOfVendors", numberOfVendors);

    if (customerError || vipCustomerError || vendorError) {
      setErrors({
        numberOfCustomers: customerError,
        numberOfVIPCustomers: vipCustomerError,
        numberOfVendors: vendorError,
      });
      return;
    }

    setIsSubmitting(true);
    setLogs([]);

    try {
      await startSimulation(
        Number(numberOfCustomers),
        Number(numberOfVendors),
        Number(numberOfVIPCustomers)
      );
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
    setLogs([]);
    setMessage({ type: "success", text: "Logs cleared successfully!" });
    dismissMessageAfterDelay();
  };

  const handleReset = async () => {
    try {
      await resetSimulationData();
      setMessage({ type: "success", text: "Simulation reset successfully!" });
    } catch (error) {
      setMessage({ type: "error", text: "Failed to reset simulation." });
    }
  };

  const dismissMessageAfterDelay = () => {
    setTimeout(() => setMessage(null), 3000);
  };

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <Box sx={{ maxWidth: "1200px", margin: "auto", marginTop: 4, padding: 2 }}>
      <Typography variant="h4" align="center" sx={{ marginBottom: 4 }}>
        Simulation Dashboard
      </Typography>
      {message && <Alert severity={message.type} sx={{ mb: 2 }}>{message.text}</Alert>}

      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          gap: 3,
          alignItems: "center",
        }}
      >
        <Card sx={{ width: "100%" }}>
          <CardContent>
            <Typography variant="h5" gutterBottom align="center">
              Simulation Data
            </Typography>
            <Box
              sx={{
                display: "grid",
                gridTemplateColumns: "1fr 1fr",
                gap: 2,
                justifyItems: "center",
                alignItems: "center",
                marginTop: 2,
              }}
            >
              <Box>
                <Typography variant="body1">
                  Status: <strong>{simulationStatus ? "Running" : "Stopped"}</strong>
                </Typography>
              </Box>
              <Box>
                <Typography variant="body1">
                  Available Tickets: <strong>{availableTickets}</strong>
                </Typography>
              </Box>
              <Box>
                <Typography variant="body1">
                  Tickets Added: <strong>{totalTicketsAdded}</strong>
                </Typography>
              </Box>
              <Box>
                <Typography variant="body1">
                  Tickets Retrieved: <strong>{totalTicketsRetrieved}</strong>
                </Typography>
              </Box>
              <Box>
                <Typography variant="body1">
                  VIP Retrievals: <strong>{totalVIPRetrievals}</strong>
                </Typography>
              </Box>
              <Box>
                <Typography variant="body1">
                  Normal Retrievals: <strong>{totalNormalRetrievals}</strong>
                </Typography>
              </Box>
            </Box>
          </CardContent>
        </Card>

        <Box component="form" sx={{ maxWidth: "600px" }}>
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
            label="Number of VIP Customers"
            name="numberOfVIPCustomers"
            type="number"
            value={numberOfVIPCustomers || ""}
            onChange={handleChange}
            fullWidth
            sx={{ mb: 2 }}
            disabled={simulationStatus || isSubmitting}
            error={!!errors.numberOfVIPCustomers}
            helperText={errors.numberOfVIPCustomers}
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

        <Box sx={{ display: "flex", flexWrap: "wrap", gap: 2, justifyContent: "center" }}>
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
      </Box>

      <Paper
        ref={logContainerRef}
        onScroll={handleScroll}
        sx={{
          height: "200px",
          overflowY: "scroll",
          marginTop: 4,
          border: "1px solid black",
          padding: 2,
        }}
      >
        <Typography variant="h6" sx={{ mb: 1 }}>Logs</Typography>
        {logs.map((log, index) => {
          const formattedLog = formatLogMessage(log); // Call the formatter
          if (!formattedLog) return null; // Skip if the log is null

          const { message, color } = formattedLog; // Safely destructure
          return (
            <Typography key={index} variant="body2" sx={{ mt: 1, color }}>
              {message}
            </Typography>
          );
        })}
      </Paper>
    </Box>
  );
};

export default SimulationPage;
