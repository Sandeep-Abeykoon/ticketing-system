package edu.westminster.ticketingsystem.ticketing_system.service;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public void sendTicketAvailability(int availableTickets) {
        messagingTemplate.convertAndSend("/topic/ticket-availability", availableTickets);
    }

    public void sendSimulationStatus(boolean isRunning) {
        System.out.println("Simulation started");
        messagingTemplate.convertAndSend("/topic/simulation-status", isRunning);
    }
}
