package edu.westminster.ticketingsystem.ticketing_system.component;

import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TicketPool {
    private final List<Ticket> tickets;
    private final SystemConfiguration systemConfiguration;

    public TicketPool(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
        this.tickets = Collections.synchronizedList(new ArrayList<>());
    }

    public synchronized boolean addTickets(List<Ticket> ticketsToAdd) {
        int maxCapacity = systemConfiguration.getConfigurationData().getMaxTicketCapacity();
        if (tickets.size() + ticketsToAdd.size() > maxCapacity) {
            System.out.println("Cannot add tickets: Pool has no space");
            return false;
        }
        tickets.addAll(ticketsToAdd);
        System.out.println("Pool current size: " + tickets.size());
        return true;
    }

    public synchronized boolean retrieveTickets(String customerId, int ticketsPerRetrieval) {
        if (tickets.size() < ticketsPerRetrieval) {
            System.out.println("Not enough tickets available for Customer " + customerId);
            return false;
        }
        if (ticketsPerRetrieval > 0) {
            tickets.subList(0, ticketsPerRetrieval).clear();
        }
        return true;
    }

    public synchronized void clearPool() {
        tickets.clear();
    }
}

