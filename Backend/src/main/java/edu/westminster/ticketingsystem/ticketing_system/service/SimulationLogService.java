package edu.westminster.ticketingsystem.ticketing_system.service;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class SimulationLogService {
    private final SimpMessagingTemplate messagingTemplate;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void sendLog(String logMessage) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String logMessageWithTimeStamp = String.format("[%s] %s", timestamp, logMessage);
        messagingTemplate.convertAndSend("/topic/simulation-logs", logMessageWithTimeStamp);
    }

    public void sendTicketData(int availableTickets, int totalTicketsAdded, int totalTicketsRetrieved) {
        Map<String, Integer> ticketData = new HashMap<>();
        ticketData.put("availableTickets", availableTickets);
        ticketData.put("totalTicketsAdded", totalTicketsAdded);
        ticketData.put("totalTicketsRetrieved", totalTicketsRetrieved);

        messagingTemplate.convertAndSend("/topic/ticket-data", ticketData);
    }

    public void sendSimulationStatus(boolean isRunning) {
        messagingTemplate.convertAndSend("/topic/simulation-status", isRunning);
    }
}
