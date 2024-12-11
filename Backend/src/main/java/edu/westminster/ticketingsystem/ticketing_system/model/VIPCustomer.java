package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;

public class VIPCustomer extends Customer{
    public VIPCustomer(String customerId,
                       ConfigurationData configurationData,
                       TicketService ticketService,
                       SimulationLogService logService) {
        super(customerId, configurationData, ticketService, logService);
    }

    @Override
    protected void performOperation() {
        // Retrieve tickets as VIP
        ticketService.retrieveTickets(id, ticketsPerRetrieval, true);
    }

    @Override
    protected String getType() {
        return "VIP Customer";
    }
}
