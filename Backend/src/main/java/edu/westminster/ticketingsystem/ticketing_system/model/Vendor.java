package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;

public class Vendor implements Runnable {
    private final String vendorId;
    private final int ticketsPerRelease;
    private final int releaseInterval; // in milliseconds
    private final TicketService ticketService;

    public Vendor(String vendorId, ConfigurationData configurationData, TicketService ticketService) {
        this.vendorId = vendorId;
        this.ticketsPerRelease = configurationData.getTicketReleaseRate();
        this.releaseInterval = configurationData.getTicketReleaseInterval();
        this.ticketService = ticketService;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Generate and add tickets
                boolean added = ticketService.generateAndAddTickets(vendorId, ticketsPerRelease);

                if (!added) {
                    System.out.println("Vendor " + vendorId + " could not add tickets to the pool");
                } else {
                    System.out.println("Vendor " + vendorId + " added " + ticketsPerRelease + " tickets to the pool");
                }

                // Wait for the release interval
                Thread.sleep(releaseInterval);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorId + " interrupted.");
            }
        }
    }
}
