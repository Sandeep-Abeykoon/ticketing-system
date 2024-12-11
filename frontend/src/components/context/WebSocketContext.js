import React, { createContext, useState, useEffect } from "react";
import { Client } from "@stomp/stompjs";
import { getSimulationStatus, getLogs } from "../../dummyApi";

export const WebSocketContext = createContext();

export const WebSocketProvider = ({ children }) => {
  const [logs, setLogs] = useState([]);
  const [simulationStatus, setSimulationStatus] = useState(null);
  const [availableTickets, setAvailableTickets] = useState(null);
  const [totalTicketsAdded, setTotalTicketsAdded] = useState(0);
  const [totalTicketsRetrieved, setTotalTicketsRetrieved] = useState(0);
  const [totalVIPRetrievals, setTotalVIPRetrievals] = useState(0);
  const [totalNormalRetrievals, setTotalNormalRetrievals] = useState(0);
  const [numberOfCustomers, setNumberOfCustomers] = useState(null);
  const [numberOfVendors, setNumberOfVendors] = useState(null);
  const [numberOfVIPCustomers, setNumberOfVIPCustomers] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [connectionStatus, setConnectionStatus] = useState("connecting");

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const [statusResponse, logsResponse] = await Promise.all([
          getSimulationStatus(),
          getLogs(),
        ]);

        // Set simulation status
        setSimulationStatus(statusResponse.isRunning);
        setAvailableTickets(statusResponse.ticketCount || 0);
        setTotalTicketsAdded(statusResponse.totalTicketsAdded || 0);
        setTotalTicketsRetrieved(statusResponse.totalTicketsRetrieved || 0);
        setTotalVIPRetrievals(statusResponse.totalVIPRetrievals || 0);
        setTotalNormalRetrievals(statusResponse.totalNormalRetrievals || 0);
        setNumberOfCustomers(statusResponse.numberOfCustomers);
        setNumberOfVIPCustomers(statusResponse.numberOfVIPCustomers);
        setNumberOfVendors(statusResponse.numberOfVendors);

        // Set initial logs
        setLogs(logsResponse || []);
      } catch (error) {
        console.error("Failed to fetch initial simulation data or logs:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchInitialData();

    const client = new Client({
      brokerURL: "ws://localhost:8080/ws-status",
      reconnectDelay: 5000,
      onConnect: () => {
        setConnectionStatus("connected");
        fetchInitialData();

        // Simulation Status Subscription
        client.subscribe("/topic/simulation-status", (message) => {
          const isRunning = JSON.parse(message.body);
          setSimulationStatus(isRunning);
        });

        // Real-time Logs Subscription
        client.subscribe("/topic/realtime-logs", (message) => {
          const log = JSON.parse(message.body);
          const { action, details } = log;

          // Update ticket-related data based on action type
          if (action === "TICKET_ADD" || action === "TICKET_RETRIEVAL" || action === "POOL_CLEARED") {
            setAvailableTickets(details.availableTickets || 0);
            setTotalTicketsAdded(details.totalTicketsAdded || 0);
            setTotalTicketsRetrieved(details.totalTicketsRetrieved || 0);
            setTotalVIPRetrievals(details.totalVIPRetrievals || 0);
            setTotalNormalRetrievals(details.totalNormalRetrievals || 0);
          }

          if (action === "SIMULATION_STARTED") {
            setNumberOfCustomers(details.numberOfCustomers || 0);
            setNumberOfVIPCustomers(details.numberOfVIPCustomers || 0);
            setNumberOfVendors(details.numberOfVendors || 0);
          }

          // Add log to logs array
          setLogs((prevLogs) => [...prevLogs, log]);
        });
      },
      onStompError: (error) => {
        console.error("STOMP error:", error);
        setConnectionStatus("not connected");
      },
      onWebSocketClose: () => {
        setConnectionStatus("not connected");
      },
      onWebSocketError: (error) => {
        console.error("WebSocket error:", error);
        setConnectionStatus("not connected");
      },
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  return (
    <WebSocketContext.Provider
      value={{
        logs,
        setLogs,
        simulationStatus,
        availableTickets,
        totalTicketsAdded,
        totalTicketsRetrieved,
        totalVIPRetrievals,
        totalNormalRetrievals,
        numberOfCustomers,
        setNumberOfCustomers,
        numberOfVIPCustomers,
        setNumberOfVIPCustomers,
        numberOfVendors,
        setNumberOfVendors,
        isLoading,
        connectionStatus,
      }}
    >
      {children}
    </WebSocketContext.Provider>
  );
};
