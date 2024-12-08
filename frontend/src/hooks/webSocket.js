import { Client } from "@stomp/stompjs";
import { useState, useEffect } from "react";

const useWebSocket = () => {
  const [logs, setLogs] = useState([]);
  const [ticketAvailability, setTicketAvailability] = useState(0);
  const [systemStatus, setSystemStatus] = useState(false);

  useEffect(() => {
      
    const client = new Client({
      brokerURL: "ws://localhost:8080/ws-status", // Update with your WebSocket endpoint
      reconnectDelay: 5000, // Auto-reconnect delay
      onConnect: () => {
        console.log("WebSocket connected");

        // Subscribe to system status
        client.subscribe("/topic/simulation-status", (message) => {
          const status = JSON.parse(message.body);
          setSystemStatus(status.running);
          setTicketAvailability(status.ticketCount);
        });

        // Subscribe to logs
        client.subscribe("/topic/simulation-logs", (message) => {
          const log = message.body;
          setLogs((prevLogs) => [...prevLogs, log]);
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

  return { logs, ticketAvailability, systemStatus };
};

export default useWebSocket;
