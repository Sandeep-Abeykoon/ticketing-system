import React, { useEffect, useState } from "react";
import { getAllTransactions } from "../dummyApi";
import {
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Box,
  Grid,
  CircularProgress,
} from "@mui/material";

const TransactionsPage = () => {
  const [transactions, setTransactions] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [stats, setStats] = useState({
    totalSales: 0,
    vipSales: 0,
    ticketAdds: 0,
  });

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const data = await getAllTransactions();
        setTransactions(data);

        // Calculate aggregate statistics
        const stats = data.reduce(
          (acc, transaction) => {
            if (transaction.type === "ADD") {
              acc.ticketAdds += transaction.ticketCount;
            } else if (transaction.type === "RETRIEVE") {
              acc.totalSales += transaction.ticketCount;
              if (transaction.userType === "VIP") {
                acc.vipSales += transaction.ticketCount;
              }
            }
            return acc;
          },
          { totalSales: 0, vipSales: 0, ticketAdds: 0 }
        );
        setStats(stats);
      } catch (error) {
        console.error("Failed to fetch transactions:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchTransactions();
  }, []);

  if (isLoading) {
    return <CircularProgress />;
  }

  return (
    <Box sx={{ padding: 4 }}>
      <Typography variant="h4" align="center" gutterBottom>
        Transactions Overview
      </Typography>

      <Grid container spacing={4} sx={{ marginBottom: 4 }}>
        <Grid item xs={12} sm={4}>
          <Paper sx={{ padding: 2 }}>
            <Typography variant="h6" align="center">
              Total Sales
            </Typography>
            <Typography variant="h5" align="center">
              {stats.totalSales} Tickets
            </Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} sm={4}>
          <Paper sx={{ padding: 2 }}>
            <Typography variant="h6" align="center">
              VIP Sales
            </Typography>
            <Typography variant="h5" align="center">
              {stats.vipSales} Tickets
            </Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} sm={4}>
          <Paper sx={{ padding: 2 }}>
            <Typography variant="h6" align="center">
              Ticket Additions
            </Typography>
            <Typography variant="h5" align="center">
              {stats.ticketAdds} Tickets
            </Typography>
          </Paper>
        </Grid>
      </Grid>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Type</TableCell>
              <TableCell>User ID</TableCell>
              <TableCell>User Type</TableCell>
              <TableCell>Ticket Count</TableCell>
              <TableCell>Timestamp</TableCell>
              <TableCell>Details</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {transactions.map((transaction) => (
              <TableRow key={transaction.id}>
                <TableCell>{transaction.id}</TableCell>
                <TableCell>{transaction.type}</TableCell>
                <TableCell>{transaction.userId}</TableCell>
                <TableCell>{transaction.userType}</TableCell>
                <TableCell>{transaction.ticketCount}</TableCell>
                <TableCell>
                  {new Date(transaction.timestamp).toLocaleString()}
                </TableCell>
                <TableCell>{transaction.details}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default TransactionsPage;
