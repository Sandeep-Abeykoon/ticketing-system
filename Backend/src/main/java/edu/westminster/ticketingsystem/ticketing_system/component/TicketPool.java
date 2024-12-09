package edu.westminster.ticketingsystem.ticketing_system.component;

import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.Ticket;
import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketPool {
    private final List<Ticket> tickets = Collections.synchronizedList(new ArrayList<>());
    private final SystemConfiguration systemConfiguration;
    private final SimulationLogService logService;

    public synchronized boolean addTickets(List<Ticket> ticketsToAdd) {
        int maxCapacity = systemConfiguration.getConfigurationData().getMaxTicketCapacity();
        if (tickets.size() + ticketsToAdd.size() > maxCapacity) {
            logService.sendLog("Cannot add tickets: Pool has no space");
            return false;
        }
        tickets.addAll(ticketsToAdd);
        logService.sendTicketAvailability(tickets.size());
        return true;
    }

    public synchronized boolean retrieveTickets(String customerId, int ticketsPerRetrieval) {
        if (tickets.size() < ticketsPerRetrieval) {
            logService.sendLog("Not enough tickets available for Customer " + customerId);
            logService.sendTicketAvailability(tickets.size());
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

    public synchronized int getTicketCount() {
        return tickets.size();
    }
}

