package edu.westminster.ticketingsystem.ticketing_system.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ConfigurationData {
    private int totalTickets; // Initial Number of tickets
    private int ticketReleaseRate;
    private int ticketReleaseInterval;
    private int customerRetrievalRate;
    private int customerRetrievalInterval;
    private int maxTicketCapacity;
}
