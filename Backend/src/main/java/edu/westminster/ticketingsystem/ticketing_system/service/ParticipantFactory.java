package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import edu.westminster.ticketingsystem.ticketing_system.model.Customer;
import edu.westminster.ticketingsystem.ticketing_system.model.VIPCustomer;
import edu.westminster.ticketingsystem.ticketing_system.model.Vendor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ParticipantFactory {
    private final TicketService ticketService;
    private final ConfigurationData configurationData;
    private final SimulationLogService logService;

    public Vendor createVendor(String vendorId) {
        return new Vendor(vendorId, configurationData, ticketService, logService);
    }

    public Customer createCustomer(String customerId) {
        return new Customer(customerId, configurationData, ticketService, logService);
    }

    public VIPCustomer createVIPCustomer(String customerId) {
        return new VIPCustomer(customerId, configurationData, ticketService, logService);
    }
}
