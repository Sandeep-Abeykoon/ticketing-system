package edu.westminster.ticketingsystem.ticketing_system.service;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SimulationLogService provides functionalities for logging simulation activities
 * and broadcasting them to connected clients in real-time.
 * It also maintains an in-memory log history for retrieval and management.
 */
@Service
@AllArgsConstructor
public class SimulationLogService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Formatter for log timestamps.
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * In-memory storage for simulation logs.
     */
    private final List<Map<String, Object>> logsMemory = new ArrayList<>();

    /**
     * Sends a structured log message to all connected clients and stores it in memory.
     *
     * @param action The action being logged (e.g., "TICKET_ADD", "THREAD_INTERRUPTED").
     * @param details Additional details about the action.
     */
    public void sendStructuredLog(String action, Map<String, Object> details) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        Map<String, Object> log = Map.of(
                "timestamp", timestamp,
                "action", action,
                "details", details
        );

        // Storing the log in memory
        logsMemory.add(log);

        // Broadcasting the log to all clients subscribed to the "/topic/realtime-logs" topic
        messagingTemplate.convertAndSend("/topic/realtime-logs", log);
    }

    /**
     * Sends the current simulation status (running or stopped) to connected clients.
     *
     * @param isRunning Boolean indicating whether the simulation is currently running.
     */
    public void sendSimulationStatus(boolean isRunning) {
        messagingTemplate.convertAndSend("/topic/simulation-status", isRunning);
    }

    /**
     * Retrieves all logs stored in memory.
     *
     * @return A list of all structured logs.
     */
    public List<Map<String, Object>> getLogs() {
        return logsMemory;
    }

    /**
     * Clears all logs from memory.
     */
    public void clearLogs() {
        logsMemory.clear();
    }
}
