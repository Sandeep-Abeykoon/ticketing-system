package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;
import lombok.Getter;

/**
 * The Customer class represents a customer participant in the ticketing system.
 *
 * Customers retrieve tickets from the ticket pool at a specified interval and rate.
 * This class extends the Participant class and implements customer-specific operations.
 */
public class Customer extends Participant {

    /**
     * Number of tickets this customer retrieves in each operation.
     */
    protected final int ticketsPerRetrieval;

    /**
     * Constructs a new Customer instance with the given parameters.
     *
     * @param customerId The unique identifier for the customer.
     * @param configurationData The configuration data containing customer retrieval settings.
     * @param ticketService The ticket service used for ticket operations.
     * @param logService The log service used for simulation logging.
     */
    public Customer(String customerId,
                    ConfigurationData configurationData,
                    TicketService ticketService,
                    SimulationLogService logService) {
        super(customerId, configurationData.getCustomerRetrievalInterval(), ticketService, logService);
        this.ticketsPerRetrieval = configurationData.getCustomerRetrievalRate();
    }

    /**
     * Performs the ticket retrieval operation for the customer.
     */
    @Override
    protected void performOperation() {
        ticketService.retrieveTickets(id, ticketsPerRetrieval, false);
    }

    /**
     * Returns the type of participant as a string.
     *
     * @return "Customer".
     */
    @Override
    protected String getType() {
        return "Customer";
    }
}
