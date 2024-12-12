package edu.westminster.ticketingsystem.ticketing_system.component;

import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.Ticket;
import edu.westminster.ticketingsystem.ticketing_system.model.TicketRetrievalRequest;
import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import edu.westminster.ticketingsystem.ticketing_system.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@RequiredArgsConstructor
public class TicketPool {
    private final SystemConfiguration systemConfiguration;
    private final SimulationLogService logService;
    private final TransactionService transactionService;
    private final List<Ticket> tickets = new ArrayList<>();
    private final Lock lock = new ReentrantLock(true);
    private final Condition condition = lock.newCondition();
    private final PriorityQueue<TicketRetrievalRequest> requestQueue = new PriorityQueue<>();
    private int totalTicketsRetrieved = 0;
    private int totalTicketsAdded = 0;
    private int totalVIPRetrievals = 0;
    private int totalNormalRetrievals = 0;

    public boolean addTickets(List<Ticket> ticketsToAdd) {
        lock.lock();
        int ticketsToAddSize = ticketsToAdd.size();
        try {
            int maxCapacity = systemConfiguration.getConfigurationData().getMaxTicketCapacity();
            if (tickets.size() + ticketsToAddSize > maxCapacity) {
                return false;
            }
            tickets.addAll(ticketsToAdd);
            totalTicketsAdded += ticketsToAddSize;

            // Log transaction
            transactionService.logTransaction(
                    "ADD",
                    "VENDOR", // Default or replace with actual vendor ID if available
                    "VENDOR",
                    ticketsToAddSize,
                    "Added tickets to the pool."
            );

            logService.sendStructuredLog("TICKET_ADD", Map.of(
                    "ticketsAdded", ticketsToAddSize,
                    "availableTickets", tickets.size(),
                    "totalTicketsAdded", totalTicketsAdded,
                    "totalTicketsRetrieved", totalTicketsRetrieved,
                    "totalVIPRetrievals", totalVIPRetrievals,
                    "totalNormalRetrievals", totalNormalRetrievals
            ));
            condition.signalAll();
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void retrieveTickets(TicketRetrievalRequest request) throws InterruptedException {
        lock.lock();
        try {
            requestQueue.add(request);
            while (requestQueue.peek() != request || tickets.size() < request.getTicketsPerRetrieval()) {
                condition.await();
            }

            requestQueue.poll();
            if (tickets.size() < request.getTicketsPerRetrieval()) {
                logService.sendStructuredLog("TICKET_RETRIEVAL_FAILED", Map.of(
                        "customerId", request.getCustomerId(),
                        "customerType", request.isVIP() ? "VIP" : "Normal",
                        "reason", "Insufficient tickets"
                ));
                return;
            }

            if (request.getTicketsPerRetrieval() > 0) {
                tickets.subList(0, request.getTicketsPerRetrieval()).clear();
            }

            totalTicketsRetrieved += request.getTicketsPerRetrieval();
            if (request.isVIP()) {
                totalVIPRetrievals += request.getTicketsPerRetrieval();
            } else {
                totalNormalRetrievals += request.getTicketsPerRetrieval();
            }

            // Log transaction
            transactionService.logTransaction(
                    "RETRIEVE",
                    request.getCustomerId(),
                    request.isVIP() ? "VIP" : "NORMAL",
                    request.getTicketsPerRetrieval(),
                    "Retrieved tickets from the pool."
            );

            logService.sendStructuredLog("TICKET_RETRIEVAL", Map.of(
                    "customerId", request.getCustomerId(),
                    "customerType", request.isVIP() ? "VIP" : "Normal",
                    "retrievedTickets", request.getTicketsPerRetrieval(),
                    "availableTickets", tickets.size(),
                    "totalTicketsAdded", totalTicketsAdded,
                    "totalTicketsRetrieved", totalTicketsRetrieved,
                    "totalVIPRetrievals", totalVIPRetrievals,
                    "totalNormalRetrievals", totalNormalRetrievals
            ));
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void clearPoolData() {
        lock.lock();
        try {
            tickets.clear();
            requestQueue.clear();
            totalTicketsAdded = 0;
            totalTicketsRetrieved = 0;
            totalVIPRetrievals = 0;
            totalNormalRetrievals = 0;
            condition.signalAll();

            logService.sendStructuredLog("POOL_CLEARED", Map.of(
                    "availableTickets", tickets.size(),
                    "totalTicketsAdded", totalTicketsAdded,
                    "totalTicketsRetrieved", totalTicketsRetrieved,
                    "totalVIPRetrievals", totalVIPRetrievals,
                    "totalNormalRetrievals", totalNormalRetrievals
            ));

            // Log transaction
            transactionService.logTransaction(
                    "CLEAR",
                    "SYSTEM",
                    "SYSTEM",
                    tickets.size(),
                    "Cleared all tickets from the pool."
            );
        } finally {
            lock.unlock();
        }
    }

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

    public int getTotalVIPRetrievals() {
        lock.lock();
        try {
            return totalVIPRetrievals;
        } finally {
            lock.unlock();
        }
    }

    public int getTotalNormalRetrievals() {
        lock.lock();
        try {
            return totalNormalRetrievals;
        } finally {
            lock.unlock();
        }
    }
}
