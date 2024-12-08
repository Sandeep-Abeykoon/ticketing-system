package edu.westminster.ticketingsystem.ticketing_system.service;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SimulationLogService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendLog(String logMessage) {
        System.out.println(logMessage);
        messagingTemplate.convertAndSend("/topic/simulation-logs", logMessage);
    }

    public void sendTicketAvailability(int availableTickets) {
        messagingTemplate.convertAndSend("/topic/ticket-availability", availableTickets);
    }

    public void sendSimulationStatus(boolean isRunning) {
        System.out.println("Simulation started");
        messagingTemplate.convertAndSend("/topic/simulation-status", isRunning);
    }
}
