import React, { useEffect, useState } from "react";
import { fetchConfiguration, updateConfiguration, updateSystemStatus } from "../dummyApi";
import { TextField, Button, Box, Alert, Typography } from "@mui/material";

const AdminPage = () => {
  const [formData, setFormData] = useState({
    totalTickets: "",
    ticketReleaseRate: "",
    customerRetrievalRate: "",
    maxTicketCapacity: "",
  });

  const [systemConfigured, setSystemConfigured] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await fetchConfiguration();
        setFormData(data.configurationData);
        setSystemConfigured(data.systemConfigured);
      } catch (error) {
        console.error("Failed to fetch configuration:", error);
        setMessage({ type: "error", text: "Failed to fetch configuration data." });
      }
    };

    fetchData();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await updateConfiguration({
        ...formData,
        totalTickets: parseInt(formData.totalTickets),
        ticketReleaseRate: parseInt(formData.ticketReleaseRate),
        customerRetrievalRate: parseInt(formData.customerRetrievalRate),
        maxTicketCapacity: parseInt(formData.maxTicketCapacity),
      });
      setMessage({ type: "success", text: response });
    } catch (error) {
      setMessage({ type: "error", text: "Failed to update configuration." });
    }
  };

  const handleSystemToggle = async () => {
    try {
      const newStatus = !systemConfigured; // Toggle the current status
      const response = await updateSystemStatus(newStatus);
      setSystemConfigured(newStatus);
      setMessage({ type: "success", text: response });
    } catch (error) {
      setMessage({ type: "error", text: "Failed to update system status." });
    }
  };

  return (
    <div style={{ width: "60%", margin: "auto", marginTop: 20 }}>
      <h1>Admin Dashboard</h1>
      {message && <Alert severity={message.type}>{message.text}</Alert>}
      <Typography variant="h6" sx={{ marginBottom: 2 }}>
        <strong>System Status:</strong>{" "}
        {systemConfigured ? "Configured and Running" : "Not Configured / Halted"}
      </Typography>
      <Button
        variant="contained"
        color={systemConfigured ? "secondary" : "primary"}
        onClick={handleSystemToggle}
        sx={{ marginBottom: 4 }}
      >
        {systemConfigured ? "Halt System" : "Run System"}
      </Button>
      <Box component="form" onSubmit={handleSubmit}>
        <TextField
          label="Total Tickets"
          name="totalTickets"
          value={formData.totalTickets}
          onChange={handleChange}
          fullWidth
          required
          sx={{ marginBottom: 2 }}
        />
        <TextField
          label="Ticket Release Rate"
          name="ticketReleaseRate"
          value={formData.ticketReleaseRate}
          onChange={handleChange}
          fullWidth
          required
          sx={{ marginBottom: 2 }}
        />
        <TextField
          label="Customer Retrieval Rate"
          name="customerRetrievalRate"
          value={formData.customerRetrievalRate}
          onChange={handleChange}
          fullWidth
          required
          sx={{ marginBottom: 2 }}
        />
        <TextField
          label="Max Ticket Capacity"
          name="maxTicketCapacity"
          value={formData.maxTicketCapacity}
          onChange={handleChange}
          fullWidth
          required
          sx={{ marginBottom: 2 }}
        />
        <Button type="submit" variant="contained" color="primary" fullWidth>
          Update Configuration
        </Button>
      </Box>
    </div>
  );
};

export default AdminPage;
