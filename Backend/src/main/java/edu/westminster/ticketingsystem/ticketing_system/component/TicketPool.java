package edu.westminster.ticketingsystem.ticketing_system.component;

import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.Ticket;
import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@RequiredArgsConstructor
public class TicketPool {
    // Collections.synchronized list is not needed as ReentrantLock is used (otherwise it's redundant)
    private final List<Ticket> tickets = new ArrayList<>();
    private final SystemConfiguration systemConfiguration;
    private final SimulationLogService logService;
    private final Lock lock = new ReentrantLock();

    // Synchronized keyword is not needed as ReentrantLock is used
    public boolean addTickets(List<Ticket> ticketsToAdd) {
        lock.lock();
        try {
            int maxCapacity = systemConfiguration.getConfigurationData().getMaxTicketCapacity();
            if (tickets.size() + ticketsToAdd.size() > maxCapacity) {
                logService.sendLog("Cannot add tickets: Pool has no space");
                return false;
            }
            tickets.addAll(ticketsToAdd);
            logService.sendTicketAvailability(tickets.size());
            return true;
        } finally {
            lock.unlock();
        }
    }

    // Synchronized keyword is not needed as ReentrantLock is used
    public boolean retrieveTickets(String customerId, int ticketsPerRetrieval) {
        lock.lock();
        try {
            if (tickets.size() < ticketsPerRetrieval) {
                logService.sendLog("Not enough tickets available for Customer " + customerId);
                logService.sendTicketAvailability(tickets.size());
                return false;
            }
            if (ticketsPerRetrieval > 0) {
                tickets.subList(0, ticketsPerRetrieval).clear();
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    // Synchronized keyword is not needed as ReentrantLock is used
    public void clearPool() {
        lock.lock();
        try {
            tickets.clear();
        } finally {
            lock.unlock();
        }
    }

    // Synchronized keyword is not needed as ReentrantLock is used
    public int getTicketCount() {
        lock.lock();
        try {
            return tickets.size();
        } finally {
            lock.unlock();
        }
    }
}

