package edu.westminster.ticketingsystem.ticketing_system.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ConfigurationData {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
}
