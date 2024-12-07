package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import edu.westminster.ticketingsystem.ticketing_system.model.Customer;
import edu.westminster.ticketingsystem.ticketing_system.model.Vendor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ParticipantFactory {
    private final TicketService ticketService;
    private final ConfigurationData configurationData;

    public Vendor createVendor(String vendorId) {
        return new Vendor(vendorId, configurationData, ticketService);
    }

    public Customer createCustomer(String customerId) {
        return new Customer(customerId, configurationData, ticketService);
    }
}
