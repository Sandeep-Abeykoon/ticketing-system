package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.component.TicketPool;
import edu.westminster.ticketingsystem.ticketing_system.model.Ticket;
import edu.westminster.ticketingsystem.ticketing_system.model.TicketRetrievalRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TicketService provides operations for ticket management in the simulation.
 * This service handles generating and adding tickets to the pool, as well as
 * retrieving tickets based on customer requests.
 */
@Service
@AllArgsConstructor
public class TicketService {

    private TicketPool ticketPool;

    /**
     * Generates tickets and adds them to the ticket pool.
     *
     * @param vendorId The ID of the vendor generating the tickets.
     * @param ticketsPerRelease The number of tickets to generate and add to the pool.
     * @return true if the tickets were successfully added, false otherwise.
     */
    public boolean generateAndAddTickets(String vendorId, int ticketsPerRelease) {
        // Generating the tickets
        List<Ticket> ticketsToAdd = new ArrayList<>();

        for (int i = 0; i < ticketsPerRelease; i++) {
            String ticketId = vendorId + "-" + (i + 1);
            ticketsToAdd.add(new Ticket(ticketId, vendorId));
        }

        return ticketPool.addTickets(ticketsToAdd);
    }

    /**
     * Retrieves tickets from the ticket pool based on a customer's request.
     *
     * @param customerId The ID of the customer retrieving tickets.
     * @param ticketsPerRetrieval The number of tickets requested.
     * @param isVIP Whether the customer is a VIP.
     */
    public void retrieveTickets(String customerId, int ticketsPerRetrieval, boolean isVIP) {
        try {
            TicketRetrievalRequest request = new TicketRetrievalRequest(customerId, ticketsPerRetrieval, isVIP);
            ticketPool.retrieveTickets(request);
        } catch (Exception e) {
            // TODO: Add error logging for ticket retrieval failures (Can use error logging)
        }
    }
}
