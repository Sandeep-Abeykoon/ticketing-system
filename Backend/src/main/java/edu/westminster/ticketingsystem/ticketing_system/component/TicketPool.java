package edu.westminster.ticketingsystem.ticketing_system.component;

import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.Ticket;
import edu.westminster.ticketingsystem.ticketing_system.model.TicketRetrievalRequest;
import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
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
            // tickets.size() < request.getTicketsPerRetrieval() is essential for priority for waiting for tickets to get added
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
            logService.sendStructuredLog("POOL_CLEARED", Map.of(
                    "availableTickets", 0,
                    "totalTicketsAdded", totalTicketsAdded,
                    "totalTicketsRetrieved", totalTicketsRetrieved,
                    "totalVIPRetrievals", totalVIPRetrievals,
                    "totalNormalRetrievals", totalNormalRetrievals
            ));

            tickets.clear();
            requestQueue.clear();
            totalTicketsAdded = 0;
            totalTicketsRetrieved = 0;
            totalVIPRetrievals = 0;
            totalNormalRetrievals = 0;
            condition.signalAll();
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
