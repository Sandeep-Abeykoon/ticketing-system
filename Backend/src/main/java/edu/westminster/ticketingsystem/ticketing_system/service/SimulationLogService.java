package edu.westminster.ticketingsystem.ticketing_system.service;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SimulationLogService {
    private final SimpMessagingTemplate messagingTemplate;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<Map<String, Object>> logsMemory = new ArrayList<>();

    public void sendStructuredLog(String action, Map<String, Object> details) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        Map<String, Object> log = Map.of(
                "timestamp", timestamp,
                "action", action,
                "details", details
        );
        // Storing the log in the log memory
        logsMemory.add(log);
        messagingTemplate.convertAndSend("/topic/realtime-logs", log);
    }

    public void sendSimulationStatus(boolean isRunning) {
        messagingTemplate.convertAndSend("/topic/simulation-status", isRunning);
    }

    public List<Map<String, Object>> getLogs() {
        return logsMemory;
    }

    public void clearLogs() {
        logsMemory.clear();
    }
}
