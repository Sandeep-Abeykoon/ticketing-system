import React, { useContext, useState } from "react";
import { Button, TextField, Typography, Box, Select, MenuItem, Paper } from "@mui/material";
import { WebSocketContext } from "../context/WebSocketContext";
import { addVendor, removeVendor, addCustomer, removeCustomer } from "../api";

const UserManagementPage = () => {
  const { numberOfCustomers, numberOfVIPCustomers, numberOfVendors } = useContext(WebSocketContext);

  const [userType, setUserType] = useState("vendor");
  const [userId, setUserId] = useState("");
  const [error, setError] = useState(null);
  const [message, setMessage] = useState(null);

  // Validate input is a positive number
  const isValidId = (id) => /^[1-9][0-9]*$/.test(id);

  const handleAddParticipant = async () => {
    try {
      setError(null); // Clear previous errors
      if (userType === "vendor") {
        await addVendor();
        setMessage("Vendor added successfully.");
      } else {
        const isVIP = userType === "vipCustomer";
        await addCustomer(isVIP);
        setMessage(`${isVIP ? "VIP Customer" : "Customer"} added successfully.`);
      }
    } catch (err) {
      setError(err.response?.data || "Failed to add participant. Please try again.");
      setMessage(null);
    }
  };

  const handleRemoveParticipant = async () => {
    if (!isValidId(userId)) {
      setError("Participant ID must be a positive number and cannot be blank.");
      return;
    }

    try {
      setError(null); // Clear previous errors
      if (userType === "vendor") {
        await removeVendor(userId);
        setMessage("Vendor removed successfully.");
      } else {
        const isVIP = userType === "vipCustomer";
        await removeCustomer(userId, isVIP);
        setMessage(`${isVIP ? "VIP Customer" : "Customer"} removed successfully.`);
      }
    } catch (err) {
      setError(err.response?.data || "Failed to remove participant. Please check the ID and try again.");
      setMessage(null);
    }
  };

  return (
    <Box sx={{ maxWidth: 600, margin: "auto", mt: 4, p: 3 }}>
      <Typography variant="h4" align="center" gutterBottom>
        User Management
      </Typography>

      {message && <Typography sx={{ color: "green", mb: 2 }}>{message}</Typography>}
      {error && <Typography sx={{ color: "red", mb: 2 }}>{error}</Typography>}

      <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
        <Typography variant="h5" align="center" gutterBottom>
          Current Participant Counts
        </Typography>
        <Box sx={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 2 }}>
          <Typography>
            <strong>Vendors:</strong> {numberOfVendors}
          </Typography>
          <Typography>
            <strong>Customers:</strong> {numberOfCustomers}
          </Typography>
          <Typography>
            <strong>VIP Customers:</strong> {numberOfVIPCustomers}
          </Typography>
        </Box>
      </Paper>

      <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
        <Typography variant="h6">Add Participant</Typography>
        <Select value={userType} onChange={(e) => setUserType(e.target.value)} fullWidth>
          <MenuItem value="vendor">Vendor</MenuItem>
          <MenuItem value="customer">Customer</MenuItem>
          <MenuItem value="vipCustomer">VIP Customer</MenuItem>
        </Select>
        <Button variant="contained" onClick={handleAddParticipant}>
          Add Participant
        </Button>

        <Typography variant="h6" sx={{ mt: 4 }}>
          Remove Participant
        </Typography>
        <TextField
          label="Participant ID"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
          fullWidth
          error={!isValidId(userId) && userId !== ""}
          helperText={!isValidId(userId) && userId !== "" ? "Enter a valid positive number." : ""}
        />
        <Select value={userType} onChange={(e) => setUserType(e.target.value)} fullWidth>
          <MenuItem value="vendor">Vendor</MenuItem>
          <MenuItem value="customer">Customer</MenuItem>
          <MenuItem value="vipCustomer">VIP Customer</MenuItem>
        </Select>
        <Button variant="contained" color="error" onClick={handleRemoveParticipant}>
          Remove Participant
        </Button>
      </Box>
    </Box>
  );
};

export default UserManagementPage;
