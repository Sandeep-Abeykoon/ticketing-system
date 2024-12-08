import React, { useEffect, useState } from "react";
import { fetchConfiguration, updateConfiguration } from "../dummyApi";
import { TextField, Button, Box, Alert, Typography } from "@mui/material";

const ConfigurationPage = () => {
  const [formData, setFormData] = useState({
    totalTickets: 0,
    ticketReleaseRate: 0,
    ticketReleaseInterval: 0,
    customerRetrievalRate: 0,
    customerRetrievalInterval: 0,
    maxTicketCapacity: 0,
  });

  const [systemConfigured, setSystemConfigured] = useState(false);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(true);

  // Fetch configuration on page load
  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await fetchConfiguration();
        if (data && data.configurationData) {
          setFormData(data.configurationData);
          setSystemConfigured(data.systemConfigured);
        } else {
          setMessage({ type: "error", text: "Failed to fetch configuration data." });
        }
      } catch (error) {
        console.error("Failed to fetch configuration data:", error);
        setMessage({ type: "error", text: "Failed to fetch configuration data." });
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  // Handle form input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await updateConfiguration({
        ...formData,
        totalTickets: parseInt(formData.totalTickets),
        ticketReleaseRate: parseInt(formData.ticketReleaseRate),
        ticketReleaseInterval: parseInt(formData.ticketReleaseInterval),
        customerRetrievalRate: parseInt(formData.customerRetrievalRate),
        customerRetrievalInterval: parseInt(formData.customerRetrievalInterval),
        maxTicketCapacity: parseInt(formData.maxTicketCapacity),
      });

      if (response && response.configurationData) {
        setFormData(response.configurationData);
        setSystemConfigured(response.systemConfigured);
        setMessage({ type: "success", text: "Configuration updated successfully." });
      } else {
        setMessage({ type: "error", text: "Failed to update configuration." });
      }
    } catch (error) {
      console.error("Failed to update configuration:", error);
      setMessage({
        type: "error",
        text: error.response?.data || "Failed to update configuration.",
      });
    }
  };

  // Automatically dismiss messages after a few seconds
  useEffect(() => {
    if (message) {
      const timeout = setTimeout(() => setMessage(""), 5000);
      return () => clearTimeout(timeout);
    }
  }, [message]);

  if (loading) {
    return <div>Loading configuration...</div>;
  }

  return (
    <div style={{ width: "60%", margin: "auto", marginTop: 20 }}>
      <h1>Configuration Page</h1>
      {message && (
        <Alert severity={message.type} sx={{ mb: 2 }}>
          {message.text}
        </Alert>
      )}
      <Typography variant="h6" sx={{ marginBottom: 2 }}>
        <strong style={{ color: "black" }}>Configuration Status:</strong>{" "}
        <span style={{ color: systemConfigured ? "green" : "red" }}>
          {systemConfigured ? "Configured" : "Not Configured"}
        </span>
      </Typography>
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
          label="Ticket Release Interval (ms)"
          name="ticketReleaseInterval"
          value={formData.ticketReleaseInterval}
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
          label="Customer Retrieval Interval (ms)"
          name="customerRetrievalInterval"
          value={formData.customerRetrievalInterval}
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

export default ConfigurationPage;
