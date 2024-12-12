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

/**
 * The TicketPool class manages the pool of tickets in a thread-safe manner.
 * It handles operations such as adding tickets, retrieving tickets, and clearing the pool.
 * The class uses locking mechanisms to ensure concurrent safety and PriorityQueues
 * to handle ticket retrieval requests in a prioritized manner.
 */
@Component
@RequiredArgsConstructor
public class TicketPool {
    private final SystemConfiguration systemConfiguration;
    private final SimulationLogService logService;
    private final TransactionService transactionService;
    private final List<Ticket> tickets = new ArrayList<>();
    private final Lock lock = new ReentrantLock(true); // Ensures fair locking for threads
    private final Condition condition = lock.newCondition();
    private final PriorityQueue<TicketRetrievalRequest> requestQueue = new PriorityQueue<>();
    private int totalTicketsRetrieved = 0;
    private int totalTicketsAdded = 0;
    private int totalVIPRetrievals = 0;
    private int totalNormalRetrievals = 0;

    /**
     * Adds tickets to the pool if there is sufficient capacity.
     *
     * @param ticketsToAdd List of tickets to add to the pool.
     * @return true if tickets were added successfully, false if there was insufficient capacity.
     */
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

            // Logs the ticket addition operation to the database
            transactionService.logTransaction(
                    "ADD",
                    "VENDOR",
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

    /**
     * Retrieves tickets from the pool based on the given request.
     *
     * @param request The retrieval request containing customer details and ticket count.
     * @throws InterruptedException If the thread is interrupted while waiting for tickets.
     */
    public void retrieveTickets(TicketRetrievalRequest request) throws InterruptedException {
        lock.lock();
        try {
            requestQueue.add(request);
            // Wait until the current request is the highest priority and there are enough tickets
            if (request != null) {
                while (requestQueue.peek() != request || tickets.size() < request.getTicketsPerRetrieval()) {
                    condition.await();
                }
            }

            requestQueue.poll();
            assert request != null;
            if (tickets.size() < request.getTicketsPerRetrieval()) {
                logService.sendStructuredLog("TICKET_RETRIEVAL_FAILED", Map.of(
                        "customerId", request.getCustomerId(),
                        "customerType", request.isVIP() ? "VIP" : "Normal",
                        "reason", "Insufficient tickets"
                ));
                return;
            }

            // Remove tickets for the request
            if (request.getTicketsPerRetrieval() > 0) {
                tickets.subList(0, request.getTicketsPerRetrieval()).clear();
            }

            // Updating the counter variable states
            totalTicketsRetrieved += request.getTicketsPerRetrieval();
            if (request.isVIP()) {
                totalVIPRetrievals += request.getTicketsPerRetrieval();
            } else {
                totalNormalRetrievals += request.getTicketsPerRetrieval();
            }

            // Logs the ticket retrieval operation to the database
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

    /**
     * Clears all tickets and resets the pool data.
     */
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

            // logs the transaction to the database
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

    /**
     * Retrieves the current number of available tickets in the pool.
     *
     * @return The number of tickets in the pool.
     */
    public int getTicketCount() {
        lock.lock();
        try {
            return tickets.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves the total number of tickets retrieved from the pool.
     *
     * @return Total number of retrieved tickets.
     */
    public int getTotalTicketsRetrieved() {
        lock.lock();
        try {
            return totalTicketsRetrieved;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves the total number of tickets added to the pool.
     *
     * @return Total number of added tickets.
     */
    public int getTotalTicketsAdded() {
        lock.lock();
        try {
            return totalTicketsAdded;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves the total number of VIP tickets retrieved.
     *
     * @return Total number of VIP ticket retrievals.
     */
    public int getTotalVIPRetrievals() {
        lock.lock();
        try {
            return totalVIPRetrievals;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves the total number of normal tickets retrieved.
     *
     * @return Total number of normal ticket retrievals.
     */
    public int getTotalNormalRetrievals() {
        lock.lock();
        try {
            return totalNormalRetrievals;
        } finally {
            lock.unlock();
        }
    }
}
