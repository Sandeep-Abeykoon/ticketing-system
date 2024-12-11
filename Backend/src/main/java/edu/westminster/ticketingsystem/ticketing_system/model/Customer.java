package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;
import lombok.Getter;

public class Customer extends Participant{
    protected final int ticketsPerRetrieval;

    public Customer(String customerId,
                    ConfigurationData configurationData,
                    TicketService ticketService,
                    SimulationLogService logService) {
        super(customerId, configurationData.getCustomerRetrievalInterval(), ticketService, logService);
        this.ticketsPerRetrieval = configurationData.getCustomerRetrievalRate();
    }

    @Override
    protected void performOperation() {
        // Retrieve tickets
        ticketService.retrieveTickets(id, ticketsPerRetrieval, false);
    }

    @Override
    protected String getType() {
        return "Customer";
    }
}

