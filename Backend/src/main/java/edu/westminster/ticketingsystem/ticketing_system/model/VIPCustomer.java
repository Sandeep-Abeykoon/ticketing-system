package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;

/**
 * The VIPCustomer class represents a VIP customer participant in the ticketing system.
 * VIP customers have higher priority when retrieving tickets from the pool.
 * This class extends the Customer class and overrides the operation to mark retrievals as VIP.
 */
public class VIPCustomer extends Customer {

    /**
     * Constructs a new VIPCustomer instance with the given parameters.
     *
     * @param customerId The unique identifier for the VIP customer.
     * @param configurationData The configuration data containing customer retrieval settings.
     * @param ticketService The ticket service used for ticket operations.
     * @param logService The log service used for simulation logging.
     */
    public VIPCustomer(String customerId,
                       ConfigurationData configurationData,
                       TicketService ticketService,
                       SimulationLogService logService) {
        super(customerId, configurationData, ticketService, logService);
    }

    /**
     * Performs the ticket retrieval operation for the VIP customer.
     * VIP customers retrieve tickets with higher priority.
     */
    @Override
    protected void performOperation() {
        ticketService.retrieveTickets(id, ticketsPerRetrieval, true);
    }

    /**
     * Returns the type of participant as a string.
     *
     * @return "VIP Customer".
     */
    @Override
    protected String getType() {
        return "VIP Customer";
    }
}
