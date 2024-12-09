import React, { createContext, useState, useEffect } from "react";
import { Client } from "@stomp/stompjs";
import { getSimulationStatus } from "../../dummyApi";

export const WebSocketContext = createContext();

export const WebSocketProvider = ({ children }) => {
  const [logs, setLogs] = useState([]);
  const [ticketAvailability, setTicketAvailability] = useState(null);
  const [simulationStatus, setSimulationStatus] = useState(null);
  const [numberOfCustomers, setNumberOfCustomers] = useState(null);
  const [numberOfVendors, setNumberOfVendors] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const response = await getSimulationStatus();
        setSimulationStatus(response.isRunning);
        setTicketAvailability(response.ticketCount);
        setNumberOfCustomers(response.numberOfCustomers);
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
        console.log("WebSocket connected");

        client.subscribe("/topic/simulation-status", (message) => {
          const isRunning = JSON.parse(message.body);
          setSimulationStatus(isRunning);
        });

        client.subscribe("/topic/simulation-logs", (message) => {
          const log = message.body;
          setLogs((prevLogs) => [...prevLogs, log]);
        });

        client.subscribe("/topic/ticket-availability", (message) => {
          const ticketCount = parseInt(message.body, 10);
          setTicketAvailability(ticketCount);
        });

        client.subscribe("/topic/number-of-customers", (message) => {
          const customers = parseInt(message.body, 10);
          setNumberOfCustomers(customers);
        });

        client.subscribe("/topic/number-of-vendors", (message) => {
          const vendors = parseInt(message.body, 10);
          setNumberOfVendors(vendors);
        });
      },
      onDisconnect: () => {
        console.log("WebSocket disconnected");
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
        ticketAvailability,
        setTicketAvailability,
        simulationStatus,
        numberOfCustomers,
        setNumberOfCustomers,
        numberOfVendors,
        setNumberOfVendors,
        isLoading,
      }}
    >
      {children}
    </WebSocketContext.Provider>
  );
};
