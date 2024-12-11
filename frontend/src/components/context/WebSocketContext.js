import React, { createContext, useState, useEffect } from "react";
import { Client } from "@stomp/stompjs";
import { getSimulationStatus, resetSimulation } from "../../dummyApi";
import { createStateMapper, updateStateFromResponse } from "../../utils/stateMapper";

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

  // Create the state mapper dynamically
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

  // Reset Simulation Function
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
