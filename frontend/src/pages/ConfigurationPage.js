import React, { useEffect, useState } from "react";
import { fetchConfiguration, updateConfiguration } from "../api";
import { validateField } from "../utils/validation";
import { TextField, Button, Box, Alert, Typography } from "@mui/material";

const ConfigurationPage = () => {
  const [formData, setFormData] = useState({
    totalTickets: "",
    ticketReleaseRate: "",
    ticketReleaseInterval: "",
    customerRetrievalRate: "",
    customerRetrievalInterval: "",
    maxTicketCapacity: "",
  });

  const [systemConfigured, setSystemConfigured] = useState(false);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false); 
  const [errors, setErrors] = useState({}); 

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

  // Handle form input changes with validation
  const handleChange = (e) => {
    const { name, value } = e.target;
    const parsedValue = value === "" ? "" : parseInt(value, 10);
    const error = validateField(name, parsedValue);

    setErrors((prevErrors) => ({ ...prevErrors, [name]: error }));
    setFormData((prevData) => ({
      ...prevData,
      [name]: parsedValue,
    }));
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validate all fields
    const formErrors = {};
    Object.keys(formData).forEach((key) => {
      const error = validateField(key, formData[key]);
      if (error) formErrors[key] = error;
    });

    if (Object.keys(formErrors).length > 0) {
      setErrors(formErrors);
      return;
    }

    setIsSubmitting(true); // Disable the button while submitting

    try {
      const response = await updateConfiguration(formData);
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
    } finally {
      setIsSubmitting(false); // Re-enable the button after submission
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
        {Object.keys(formData).map((key) => (
          <TextField
            key={key}
            label={key
              .replace(/([A-Z])/g, " $1")
              .replace(/^./, (str) => str.toUpperCase())}
            name={key}
            value={formData[key]}
            onChange={handleChange}
            fullWidth
            required
            sx={{ marginBottom: 2 }}
            error={!!errors[key]}
            helperText={errors[key]}
          />
        ))}
        <Button
          type="submit"
          variant="contained"
          color="primary"
          fullWidth
          disabled={isSubmitting} // Button is disabled while submitting
        >
          {isSubmitting ? "Updating..." : "Update Configuration"}
        </Button>
      </Box>
    </div>
  );
};

export default ConfigurationPage;
