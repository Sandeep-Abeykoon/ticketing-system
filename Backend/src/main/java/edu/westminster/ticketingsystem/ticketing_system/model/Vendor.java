package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;

import java.util.Map;

/**
 * The Vendor class represents a vendor participant in the ticketing system.
 * Vendors are responsible for generating and adding tickets to the ticket pool
 * at specified intervals and rates defined in the system configuration.
 */
public class Vendor extends Participant {

    /**
     * Number of tickets the vendor releases into the pool in each operation.
     */
    private final int ticketsPerRelease;

    /**
     * Constructs a new Vendor instance with the given parameters.
     *
     * @param vendorId The unique identifier for the vendor.
     * @param configurationData The configuration data containing ticket release settings.
     * @param ticketService The ticket service used for ticket operations.
     * @param logService The log service used for simulation logging.
     */
    public Vendor(String vendorId,
                  ConfigurationData configurationData,
                  TicketService ticketService,
                  SimulationLogService logService) {
        super(vendorId, configurationData.getTicketReleaseInterval(), ticketService, logService);
        this.ticketsPerRelease = configurationData.getTicketReleaseRate();
    }

    /**
     * Performs the ticket release operation for the vendor.
     * Generates and adds tickets to the ticket pool. Logs success or failure based
     * on the pool's capacity.
     */
    @Override
    protected void performOperation() {
        boolean added = ticketService.generateAndAddTickets(id, ticketsPerRelease);

        if (!added) {
            logService.sendStructuredLog("TICKET_ADD_FAILED", Map.of(
                    "id", id,
                    "reason", "Pool has no space",
                    "ticketsPerRelease", ticketsPerRelease
            ));
        } else {
            logService.sendStructuredLog("TICKET_ADD_VENDOR", Map.of(
                    "id", id,
                    "ticketsAdded", ticketsPerRelease,
                    "operation", "Add tickets"
            ));
        }
    }

    /**
     * Returns the type of participant as a string.
     *
     * @return "Vendor".
     */
    @Override
    protected String getType() {
        return "Vendor";
    }
}
