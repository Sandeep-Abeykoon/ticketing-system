package edu.westminster.ticketingsystem.ticketing_system.component;

import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.Ticket;
import edu.westminster.ticketingsystem.ticketing_system.model.TicketRetrievalRequest;
import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@RequiredArgsConstructor
public class TicketPool {
    private final SystemConfiguration systemConfiguration;
    private final SimulationLogService logService;
    // Collections.synchronized list is not needed as ReentrantLock is used (otherwise it's redundant)
    private final List<Ticket> tickets = new ArrayList<>(); // Ticket pool array
    private final Lock lock = new ReentrantLock(true); // Fair lock for FIFO behaviour
    private final Condition condition = lock.newCondition(); // Condition for waiting threads
    private final PriorityQueue<TicketRetrievalRequest> requestQueue = new PriorityQueue<>();
    private int totalTicketsRetrieved = 0;
    private int totalTicketsAdded = 0;

    // Synchronized keyword is not needed as ReentrantLock is used
    public boolean addTickets(List<Ticket> ticketsToAdd) {
        lock.lock();
        int ticketsToAddSize = ticketsToAdd.size();
        try {
            int maxCapacity = systemConfiguration.getConfigurationData().getMaxTicketCapacity();
            if (tickets.size() + ticketsToAddSize > maxCapacity) {
                logService.sendLog("Cannot add tickets: Pool has no space");
                return false;
            }
            tickets.addAll(ticketsToAdd);
            totalTicketsAdded += ticketsToAddSize;
            logService.sendTicketData(tickets.size(), totalTicketsAdded, totalTicketsRetrieved);
            condition.signalAll(); // Notify waiting threads (VIPs or normals)
            return true;
        } catch (Exception e) {
            logService.sendLog("Error while adding tickets: " + e.getMessage());
            return false;
        } finally {
            lock.unlock();
        }
    }

    // Synchronized keyword is not needed as ReentrantLock is used
    public void retrieveTickets(TicketRetrievalRequest request) {
        lock.lock();
        try {
            requestQueue.add(request);
            while (requestQueue.peek() != request || tickets.size() < request.getTicketsPerRetrieval()) {
                condition.await();
            }

            requestQueue.poll();
            if (tickets.size() < request.getTicketsPerRetrieval()) {
                logService.sendLog((request.isVIP() ? "VIP " : "") + "Customer " + request.getCustomerId() + " could not retrieve tickets");
                return;
            }

            tickets.subList(0, request.getTicketsPerRetrieval()).clear();
            logService.sendLog((request.isVIP() ? "VIP " : "") + "Customer " + request.getCustomerId() + " retrieved " + request.getTicketsPerRetrieval() + " tickets");
            totalTicketsRetrieved += request.getTicketsPerRetrieval();
            logService.sendTicketData(tickets.size(), totalTicketsAdded, totalTicketsRetrieved);
            condition.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logService.sendLog((request.isVIP() ? "VIP " : "") + "Customer " + request.getCustomerId() + " was interrupted.");
        } finally {
            lock.unlock();
        }
    }

    // Synchronized keyword is not needed as ReentrantLock is used
    public void clearPoolData() {
        lock.lock();
        try {
            tickets.clear(); // Clear all tickets in the pool
            requestQueue.clear(); // Clear all pending requests
            condition.signalAll(); // Notify all waiting threads
            totalTicketsAdded = 0;
            totalTicketsRetrieved = 0;
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

    public int getTotalTicketsRetrieved() {
        lock.lock();
        try {
            return totalTicketsRetrieved;
        } finally {
            lock.unlock();
        }
    }

    public int getTotalTicketsAdded() {
        lock.lock();
        try {
            return totalTicketsAdded;
        } finally {
            lock.unlock();
        }
    }
}

