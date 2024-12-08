package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.component.TicketPool;
import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;

public class Customer implements Runnable{
    private final String customerId;
    private final int ticketsPerRetrieval;
    private final int retrievalInterval;
    private final TicketService ticketService;
    private final SimulationLogService logService;

    public Customer(String customerId,
                    ConfigurationData configurationData,
                    TicketService ticketService,
                    SimulationLogService logService) {
        this.customerId = customerId;
        this.ticketsPerRetrieval = configurationData.getCustomerRetrievalRate();
        this.retrievalInterval = configurationData.getCustomerRetrievalInterval();
        this.ticketService = ticketService;
        this.logService = logService;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Retrieve tickets
                boolean retrieved = ticketService.retrieveTickets(customerId, ticketsPerRetrieval);

                if (!retrieved) {
                    logService.sendLog("Customer " + customerId + " could not retrieve tickets");
                } else {
                    logService.sendLog("Customer " + customerId + " retrieved " + ticketsPerRetrieval + " tickets");
                }

                // Wait for the retrieval interval
                Thread.sleep(retrievalInterval);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logService.sendLog("Customer " + customerId + " Interrupted");
            }
        }
    }
}
