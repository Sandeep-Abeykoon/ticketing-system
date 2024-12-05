package edu.westminster.ticketingsystem.ticketing_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticket {
    private String ticketId;
    private String vendorId;
    private String name;
    private double price;
}
