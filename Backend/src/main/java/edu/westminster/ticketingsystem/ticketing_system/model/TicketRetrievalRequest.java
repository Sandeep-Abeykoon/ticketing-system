package edu.westminster.ticketingsystem.ticketing_system.model;

import lombok.Getter;

@Getter
public class TicketRetrievalRequest implements Comparable<TicketRetrievalRequest> {
    private final String customerId;
    private final int ticketsPerRetrieval;
    private final boolean isVIP;
    private final long timestamp;  //Fifo within the same priority

    public TicketRetrievalRequest(String customerId, int ticketsPerRetrieval, boolean isVIP) {
        this.customerId = customerId;
        this.ticketsPerRetrieval = ticketsPerRetrieval;
        this.isVIP = isVIP;
        this.timestamp = System.currentTimeMillis();
    }


    @Override
    public int compareTo(TicketRetrievalRequest other) {
        // VIP has higher priority for requests of the same type
        if(this.isVIP != other.isVIP) {
            return this.isVIP ? -1 : 1;
        }
        return Long.compare(this.timestamp, other.timestamp);
    }
}
