package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.component.TicketPool;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;

public class Customer implements Runnable{
    private String customerId;
    private int ticketsPerRetrieval;
    private int retrievalInterval;
    private TicketService ticketService;

    public Customer(String customerId, ConfigurationData configurationData, TicketService ticketService) {
        this.customerId = customerId;
        this.ticketsPerRetrieval = configurationData.getCustomerRetrievalRate();
        this.retrievalInterval = configurationData.getCustomerRetrievalInterval();
        this.ticketService = ticketService;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Retrieve tickets
                boolean retrieved = ticketService.retrieveTickets(customerId, ticketsPerRetrieval);

                if (!retrieved) {
                    System.out.println("Customer " + customerId + " could not retrieve tickets");
                } else {
                    System.out.println("Customer " + customerId + "retrieved " + ticketsPerRetrieval + " tickets");
                }

                // Wait for the retrieval interval
                Thread.sleep(retrievalInterval);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer " + customerId + " Interrupted");
            }
        }
    }
}
