import React, { useContext, useState, useEffect } from "react";
import { WebSocketContext } from "../components/context/WebSocketContext";
import { Box, Typography, Card, CardContent } from "@mui/material";
import { Bar, Pie, Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
} from "chart.js";

// Register required Chart.js components
ChartJS.register(CategoryScale, LinearScale, BarElement, ArcElement, PointElement, LineElement, Tooltip, Legend);

const AnalyticsDashboard = () => {
  const { simulationStatus = false } = useContext(WebSocketContext);

  // Dummy data for ticket details
  const ticketData = {
    availableTickets: 150,
    totalTicketsAdded: 500,
    totalTicketsRetrieved: 350,
    salesByType: {
      normal: 250,
      vip: 100,
    },
  };

  const [realTimeSales, setRealTimeSales] = useState([
    { time: "10:00 AM", sales: 5 },
    { time: "10:01 AM", sales: 8 },
  ]);

  // Simulate real-time data updates
  useEffect(() => {
    const interval = setInterval(() => {
      setRealTimeSales((prev) => [
        ...prev,
        { time: new Date().toLocaleTimeString(), sales: Math.floor(Math.random() * 20) },
      ]);
    }, 3000);

    return () => clearInterval(interval);
  }, []);

  const lineChartData = {
    labels: realTimeSales.map((point) => point.time),
    datasets: [
      {
        label: "Real-Time Sales",
        data: realTimeSales.map((point) => point.sales),
        borderColor: "#3f51b5",
        backgroundColor: "rgba(63, 81, 181, 0.1)",
        tension: 0.3,
        fill: true,
      },
    ],
  };

  const barChartData = {
    labels: ["Normal Customers", "VIP Customers"],
    datasets: [
      {
        label: "Sales",
        data: [ticketData.salesByType.normal, ticketData.salesByType.vip],
        backgroundColor: ["#4caf50", "#ff5722"],
      },
    ],
  };

  const pieChartData = {
    labels: ["Normal", "VIP"],
    datasets: [
      {
        data: [ticketData.salesByType.normal, ticketData.salesByType.vip],
        backgroundColor: ["#2196f3", "#ffc107"],
      },
    ],
  };

  return (
    <Box sx={{ maxWidth: "1200px", margin: "auto", marginTop: 4, padding: 2 }}>
      <Typography variant="h4" align="center" sx={{ marginBottom: 4 }}>
        Real-Time Analytics Dashboard
      </Typography>

      {/* Line Chart at the Top */}
      <Card sx={{ marginBottom: 4 }}>
        <CardContent>
          <Typography variant="h6" align="center" gutterBottom>
            Real-Time Sales (Line Chart)
          </Typography>
          <Box sx={{ height: "400px" }}>
            <Line data={lineChartData} />
          </Box>
        </CardContent>
      </Card>

      {/* Ticket Information */}
      <Card sx={{ marginBottom: 4 }}>
        <CardContent>
          <Typography variant="h5" align="center" gutterBottom>
            Simulation Data
          </Typography>
          <Box sx={{ display: "flex", justifyContent: "space-around", flexWrap: "wrap" }}>
            <Box sx={{ textAlign: "center", marginBottom: 2 }}>
              <Typography variant="body1">
                Status: <strong>{simulationStatus ? "Running" : "Stopped"}</strong>
              </Typography>
              <Typography variant="body1">
                Available Tickets: <strong>{ticketData.availableTickets}</strong>
              </Typography>
            </Box>
            <Box sx={{ textAlign: "center", marginBottom: 2 }}>
              <Typography variant="body1">
                Total Tickets Added: <strong>{ticketData.totalTicketsAdded}</strong>
              </Typography>
              <Typography variant="body1">
                Total Tickets Retrieved: <strong>{ticketData.totalTicketsRetrieved}</strong>
              </Typography>
            </Box>
          </Box>
        </CardContent>
      </Card>

      {/* Bar and Pie Charts */}
      <Box sx={{ display: "flex", flexWrap: "wrap", justifyContent: "space-between", gap: 4 }}>
        <Card sx={{ flex: "1 1 calc(50% - 16px)", minWidth: "300px" }}>
          <CardContent>
            <Typography variant="h6" align="center" gutterBottom>
              Sales by Customer Type (Bar Chart)
            </Typography>
            <Box sx={{ height: "300px" }}>
              <Bar data={barChartData} />
            </Box>
          </CardContent>
        </Card>
        <Card sx={{ flex: "1 1 calc(50% - 16px)", minWidth: "300px" }}>
          <CardContent>
            <Typography variant="h6" align="center" gutterBottom>
              Sales Distribution (Pie Chart)
            </Typography>
            <Box sx={{ height: "300px" }}>
              <Pie data={pieChartData} />
            </Box>
          </CardContent>
        </Card>
      </Box>
    </Box>
  );
};

export default AnalyticsDashboard;
