package edu.westminster.ticketingsystem.ticketing_system.model;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * ConfigurationData represents the configuration settings for the ticketing system.
 * This class defines parameters related to ticket release, customer retrieval rates, and
 * other system-specific limits.
 */
@Component
@Data
public class ConfigurationData {

    /**
     * The initial number of tickets in the system.
     */
    private int totalTickets;

    /**
     * The rate at which tickets are released into the pool.
     */
    private int ticketReleaseRate;

    /**
     * The interval (in seconds) between consecutive ticket releases.
     */
    private int ticketReleaseInterval;

    /**
     * The rate at which customers retrieve tickets from the pool.
     */
    private int customerRetrievalRate;

    /**
     * The interval (in seconds) between consecutive customer ticket retrievals.
     */
    private int customerRetrievalInterval;

    /**
     * The maximum capacity of tickets the system can hold.
     */
    private int maxTicketCapacity;
}
