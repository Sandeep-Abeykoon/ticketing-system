import { Client } from "@stomp/stompjs";
import { useState, useEffect } from "react";

const useWebSocket = () => {
  const [logs, setLogs] = useState([]);
  const [ticketAvailability, setTicketAvailability] = useState(null);
  const [simulationStatus, setSimulationStatus] = useState(null);

  useEffect(() => {
    const client = new Client({
      brokerURL: "ws://localhost:8080/ws-status", // WebSocket endpoint
      reconnectDelay: 5000, // Auto-reconnect delay
      onConnect: () => {
        console.log("WebSocket connected");

        // Subscribe to system status
        client.subscribe("/topic/simulation-status", (message) => {
          const isRunning = JSON.parse(message.body);
          setSimulationStatus(isRunning);
        });

        // Subscribe to logs
        client.subscribe("/topic/simulation-logs", (message) => {
          const log = message.body;
          setLogs((prevLogs) => [...prevLogs, log]);
        });

        // Subscribe to ticket availability
        client.subscribe("/topic/ticket-availability", (message) => {
          const ticketCount = parseInt(message.body, 10);
          setTicketAvailability(ticketCount);
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

  return { logs, ticketAvailability, simulationStatus };
};

export default useWebSocket;
