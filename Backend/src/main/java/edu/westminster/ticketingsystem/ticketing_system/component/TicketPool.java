package edu.westminster.ticketingsystem.ticketing_system.component;

import edu.westminster.ticketingsystem.ticketing_system.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TicketPool {
    private final List<Ticket> tickets = Collections.synchronizedList(new ArrayList<>());

    public synchronized void addTickets(List<Ticket> ticketsToAdd) {
        tickets.addAll(ticketsToAdd);
        System.out.println(ticketsToAdd.size() + " tickets added to the pool.");
    }

    public synchronized int getTotalTicketCount() {
        return tickets.size();
    }
}
