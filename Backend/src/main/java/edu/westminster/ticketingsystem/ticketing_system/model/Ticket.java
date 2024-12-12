package edu.westminster.ticketingsystem.ticketing_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The Ticket class represents a ticket in the ticketing system.
 * Each ticket is uniquely identified by its ID and is associated with a vendor.
 */
@Data
@AllArgsConstructor
public class Ticket {

    /**
     * Unique identifier for the ticket.
     */
    private String ticketId;

    /**
     * The ID of the vendor associated with this ticket.
     */
    private String vendorId;
}
