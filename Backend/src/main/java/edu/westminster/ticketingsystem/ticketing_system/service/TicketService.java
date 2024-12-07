package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.component.TicketPool;
import edu.westminster.ticketingsystem.ticketing_system.model.Ticket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketService {
    private TicketPool ticketPool;
    public boolean generateAndAddTickets(String vendorId, int ticketsPerRelease) {
        // Generating the tickets
        List<Ticket> ticketsToAdd = new ArrayList<>();

        for (int i = 0; i < ticketsPerRelease; i++) {
            String ticketId = vendorId + "-" + (i + 1);
            ticketsToAdd.add(new Ticket(ticketId, vendorId));
        }
        return ticketPool.addTickets(ticketsToAdd);
    }

    public boolean retrieveTickets(String customerId, int ticketsPerRetrieval) {
    }
}
