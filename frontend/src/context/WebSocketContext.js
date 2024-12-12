import React, { createContext, useState, useEffect } from "react";
import { Client } from "@stomp/stompjs";
import { getSimulationStatus, resetSimulation } from "../api";
import { createStateMapper, updateStateFromResponse } from "../utils/stateMapper";

// Context for managing WebSocket-related state and functionality
export const WebSocketContext = createContext();

export const WebSocketProvider = ({ children }) => {
  // State variables for managing logs, simulation status, participants, and tickets
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

  // Dynamically map state setters for updating state based on responses
  const stateMapper = createStateMapper({
    setSimulationStatus,
    setAvailableTickets,
    setTotalTicketsAdded,
    setTotalTicketsRetrieved,
    setTotalVIPRetrievals,
    setTotalNormalRetrievals,
    setNumberOfCustomers,
    setNumberOfVendors,
    setNumberOfVIPCustomers,
  });

  useEffect(() => {
    // Fetch initial simulation data and establish WebSocket connection
    const fetchInitialData = async () => {
      try {
        const statusResponse = await getSimulationStatus();
        updateStateFromResponse(statusResponse, stateMapper, setLogs);
      } catch (error) {
        console.error("Failed to fetch initial simulation data:", error);
      } finally {
        setIsLoading(false);
      }
    };

    // Configure WebSocket client
    const client = new Client({
      brokerURL: "ws://localhost:8080/ws-status",
      reconnectDelay: 5000, // Reconnect delay in milliseconds
      onConnect: () => {
        setConnectionStatus("connected");
        fetchInitialData();

        // Subscribe to simulation status updates
        client.subscribe("/topic/simulation-status", (message) => {
          const isRunning = JSON.parse(message.body);
          setSimulationStatus(isRunning);
        });

        // Subscribe to real-time logs
        client.subscribe("/topic/realtime-logs", (message) => {
          const log = JSON.parse(message.body);
          const { action, details } = log;

          // Update state based on actions
          if (
            action === "TICKET_ADD" ||
            action === "TICKET_RETRIEVAL" ||
            action === "POOL_CLEARED"
          ) {
            updateStateFromResponse(details, stateMapper);
          }

          if (action === "SIMULATION_STARTED" || action === "USER_UPDATE") {
            setNumberOfCustomers(details.numberOfCustomers || 0);
            setNumberOfVIPCustomers(details.numberOfVIPCustomers || 0);
            setNumberOfVendors(details.numberOfVendors || 0);
          }

          setLogs((prevLogs) => [...prevLogs, log]);
        });
      },
      onStompError: (error) => {
        console.error("STOMP error:", error);
        setConnectionStatus("not connected");
        setIsLoading(false);
      },
      onWebSocketClose: () => {
        setConnectionStatus("not connected");
        setIsLoading(false);
      },
      onWebSocketError: (error) => {
        console.error("WebSocket error:", error);
        setConnectionStatus("not connected");
        setIsLoading(false);
      },
    });

    // Activate WebSocket client
    client.activate();

    // Clean up on component unmount
    return () => {
      client.deactivate();
    };
  }, []);

  // Reset simulation data to initial state
  const resetSimulationData = async () => {
    setIsLoading(true);
    try {
      const resetResponse = await resetSimulation();
      updateStateFromResponse(resetResponse, stateMapper, setLogs);
    } catch (error) {
      console.error("Failed to reset simulation:", error);
      alert("Error resetting simulation. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    // Provide WebSocket state and functionality to child components
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
        resetSimulationData,
      }}
    >
      {children}
    </WebSocketContext.Provider>
  );
};
