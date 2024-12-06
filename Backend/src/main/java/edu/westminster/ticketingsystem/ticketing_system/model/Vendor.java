package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.component.TicketPool;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Vendor implements Runnable {
    private final String vendorId;
    private final int ticketsPerRelease;
    private final int releaseInterval; // in milliseconds
    private final TicketPool ticketPool;

    public Vendor(String vendorId, int ticketsPerRelease, int releaseInterval, TicketPool ticketPool) {
        this.vendorId = vendorId;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Create tickets
                List<Ticket> ticketsToAdd = new ArrayList<>();
                for (int i = 0; i < ticketsPerRelease; i++) {
                    String ticketId = UUID.randomUUID().toString();
                    ticketsToAdd.add(new Ticket(ticketId, vendorId));
                }

                // Add tickets to the pool
                ticketPool.addTickets(ticketsToAdd);
                System.out.println("Vendor " + vendorId + " added " + ticketsPerRelease + " tickets.");

                // Wait for the release interval
                Thread.sleep(releaseInterval);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorId + " interrupted.");
                break;
            }
        }
    }
}
