package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;

import java.util.Map;

public class Vendor extends Participant {
    private final int ticketsPerRelease;

    public Vendor(String vendorId,
                  ConfigurationData configurationData,
                  TicketService ticketService,
                  SimulationLogService logService) {
        super(vendorId, configurationData.getTicketReleaseInterval(), ticketService, logService);
        this.ticketsPerRelease = configurationData.getTicketReleaseRate();
    }

    @Override
    protected void performOperation() {
        // Generate and add tickets
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

    @Override
    protected String getType() {
        return "Vendor";
    }
}
