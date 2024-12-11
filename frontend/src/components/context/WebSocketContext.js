import React, { createContext, useState, useEffect } from "react";
import { Client } from "@stomp/stompjs";
import { getSimulationStatus } from "../../dummyApi";

export const WebSocketContext = createContext();

export const WebSocketProvider = ({ children }) => {
  const [logs, setLogs] = useState([]);
  const [ticketData, setTicketData] = useState({
    availableTickets: null,
    totalTicketsAdded: 0,
    totalTicketsRetrieved: 0,
  });
  const [simulationStatus, setSimulationStatus] = useState(null);
  const [numberOfCustomers, setNumberOfCustomers] = useState(null);
  const [numberOfVendors, setNumberOfVendors] = useState(null);
  const [numberOfVIPCustomers, setNumberOfVIPCustomers] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [connectionStatus, setConnectionStatus] = useState("connecting"); // New state for connection status

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const response = await getSimulationStatus();
        setSimulationStatus(response.isRunning);
        setTicketData({
          availableTickets: response.ticketCount || 0,
          totalTicketsAdded: response.totalTicketsAdded || 0,
          totalTicketsRetrieved: response.totalTicketsRetrieved || 0,
        });
        setNumberOfCustomers(response.numberOfCustomers);
        setNumberOfVIPCustomers(response.numberOfVIPCustomers);
        setNumberOfVendors(response.numberOfVendors);
      } catch (error) {
        console.error("Failed to fetch initial simulation data:", error);
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

        client.subscribe("/topic/simulation-status", (message) => {
          const isRunning = JSON.parse(message.body);
          setSimulationStatus(isRunning);
        });

        client.subscribe("/topic/simulation-logs", (message) => {
          const log = message.body;
          setLogs((prevLogs) => [...prevLogs, log]);
        });

        client.subscribe("/topic/ticket-data", (message) => {
          const ticketData = JSON.parse(message.body);
          setTicketData({
            availableTickets: ticketData.availableTickets || 0,
            totalTicketsAdded: ticketData.totalTicketsAdded || 0,
            totalTicketsRetrieved: ticketData.totalTicketsRetrieved || 0,
          });
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
        ticketData,
        setTicketData,
        simulationStatus,
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
