package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;

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
            logService.sendLog("Vendor " + id + " could not add tickets to the pool");
        } else {
            logService.sendLog("Vendor " + id + " added " + ticketsPerRelease + " tickets to the pool");
        }
    }

    @Override
    protected String getType() {
        return "Vendor";
    }
}
