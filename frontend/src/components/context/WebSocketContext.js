import React, { createContext, useState, useEffect } from "react";
import { Client } from "@stomp/stompjs";
import { getSimulationStatus, resetSimulation } from "../../dummyApi";

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

  // State mapper for dynamic updates
  const stateMapper = {
    isRunning: setSimulationStatus,
    ticketCount: setAvailableTickets,
    totalTicketsAdded: setTotalTicketsAdded,
    totalTicketsRetrieved: setTotalTicketsRetrieved,
    totalVIPRetrievals: setTotalVIPRetrievals,
    totalNormalRetrievals: setTotalNormalRetrievals,
    numberOfCustomers: setNumberOfCustomers,
    numberOfVendors: setNumberOfVendors,
    numberOfVIPCustomers: setNumberOfVIPCustomers,
  };

  const updateStateFromResponse = (response) => {
    Object.entries(stateMapper).forEach(([key, setter]) => {
      if (key in response) {
        setter(response[key] || 0); // Set state or default to 0
      }
    });
    setLogs(response.logs || []); // Handle logs separately
  };

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const statusResponse = await getSimulationStatus();
        updateStateFromResponse(statusResponse);
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

        // Simulation Status Subscription
        client.subscribe("/topic/simulation-status", (message) => {
          const isRunning = JSON.parse(message.body);
          setSimulationStatus(isRunning);
        });

        // Real-time Logs Subscription
        client.subscribe("/topic/realtime-logs", (message) => {
          const log = JSON.parse(message.body);
          const { action, details } = log;

          // Use mapper for real-time updates
          if (
            action === "TICKET_ADD" ||
            action === "TICKET_RETRIEVAL" ||
            action === "POOL_CLEARED"
          ) {
            Object.entries(stateMapper).forEach(([key, setter]) => {
              if (key in details) {
                setter(details[key] || 0); // Update states dynamically
              }
            });
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

  // Reset Simulation Function
  const resetSimulationData = async () => {
    setIsLoading(true); // Indicate reset is in progress
    try {
      const resetResponse = await resetSimulation(); // Call reset API
      updateStateFromResponse(resetResponse); // Update state with reset data
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
        resetSimulationData, // Expose reset function
      }}
    >
      {children}
    </WebSocketContext.Provider>
  );
};
