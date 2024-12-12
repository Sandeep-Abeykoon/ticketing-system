package edu.westminster.ticketingsystem.ticketing_system.model;

import lombok.Getter;

/**
 * The TicketRetrievalRequest class represents a request made by a customer to retrieve tickets.
 * Requests are prioritized based on whether the customer is a VIP and the timestamp of the request.
 * VIP customers are given higher priority, and within the same priority, requests are handled in FIFO order.
 */
@Getter
public class TicketRetrievalRequest implements Comparable<TicketRetrievalRequest> {

    /**
     * Unique identifier for the customer making the request.
     */
    private final String customerId;

    /**
     * Number of tickets requested for retrieval.
     */
    private final int ticketsPerRetrieval;

    /**
     * Indicates whether the customer is a VIP.
     */
    private final boolean isVIP;

    /**
     * Timestamp of the request, used to maintain FIFO order within the same priority.
     */
    private final long timestamp;

    /**
     * Constructs a new TicketRetrievalRequest.
     *
     * @param customerId The ID of the customer making the request.
     * @param ticketsPerRetrieval The number of tickets requested.
     * @param isVIP Whether the customer is a VIP.
     */
    public TicketRetrievalRequest(String customerId, int ticketsPerRetrieval, boolean isVIP) {
        this.customerId = customerId;
        this.ticketsPerRetrieval = ticketsPerRetrieval;
        this.isVIP = isVIP;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Compares this request with another based on priority.
     * - VIP customers have higher priority.
     * - Requests of the same type are handled in FIFO order based on timestamp.
     *
     * @param other The other TicketRetrievalRequest to compare to.
     * @return -1 if this request has higher priority, 1 if lower, or 0 if equal.
     */
    @Override
    public int compareTo(TicketRetrievalRequest other) {
        // VIP has higher priority
        if (this.isVIP != other.isVIP) {
            return this.isVIP ? -1 : 1;
        }
        // FIFO within the same priority
        return Long.compare(this.timestamp, other.timestamp);
    }
}
