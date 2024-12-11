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
  const {
    logs,
    simulationStatus,
    availableTickets,
    totalTicketsAdded,
    totalTicketsRetrieved,
    totalVIPRetrievals,
    totalNormalRetrievals,
  } = useContext(WebSocketContext);

  const [groupedSales, setGroupedSales] = useState([]);

  // Process logs to extract VIP and Normal sales for line chart
  useEffect(() => {
    const salesData = {};

    logs.forEach((log) => {
      const { timestamp, action, details } = log;
      if (action === "TICKET_RETRIEVAL") {
        const time = new Date(timestamp);
        const minuteKey = `${time.getHours()}:${time.getMinutes()}`;

        if (!salesData[minuteKey]) {
          salesData[minuteKey] = { normal: 0, vip: 0 };
        }

        if (details.customerType === "VIP") {
          salesData[minuteKey].vip += details.retrievedTickets;
        } else {
          salesData[minuteKey].normal += details.retrievedTickets;
        }
      }
    });

    const grouped = Object.entries(salesData).map(([time, sales]) => ({
      time,
      vip: sales.vip,
      normal: sales.normal,
    }));

    setGroupedSales(grouped);
  }, [logs]);

  const lineChartData = {
    labels: groupedSales.map((point) => point.time),
    datasets: [
      {
        label: "VIP Sales",
        data: groupedSales.map((point) => point.vip),
        borderColor: "#FFC107",
        backgroundColor: "rgba(255, 193, 7, 0.1)",
        tension: 0.3,
        fill: true,
      },
      {
        label: "Normal Sales",
        data: groupedSales.map((point) => point.normal),
        borderColor: "#4CAF50",
        backgroundColor: "rgba(76, 175, 80, 0.1)",
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
        data: [totalNormalRetrievals, totalVIPRetrievals],
        backgroundColor: ["#4CAF50", "#FF5722"],
      },
    ],
  };

  const pieChartData = {
    labels: ["Normal", "VIP"],
    datasets: [
      {
        data: [totalNormalRetrievals, totalVIPRetrievals],
        backgroundColor: ["#2196F3", "#FFC107"],
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
                Available Tickets: <strong>{availableTickets}</strong>
              </Typography>
            </Box>
            <Box sx={{ textAlign: "center", marginBottom: 2 }}>
              <Typography variant="body1">
                Total Tickets Added: <strong>{totalTicketsAdded}</strong>
              </Typography>
              <Typography variant="body1">
                Total Tickets Retrieved: <strong>{totalTicketsRetrieved}</strong>
              </Typography>
            </Box>
            <Box sx={{ textAlign: "center", marginBottom: 2 }}>
              <Typography variant="body1">
                VIP Sales: <strong>{totalVIPRetrievals}</strong>
              </Typography>
              <Typography variant="body1">
                Normal Sales: <strong>{totalNormalRetrievals}</strong>
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
