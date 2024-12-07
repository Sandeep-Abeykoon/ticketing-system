package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import edu.westminster.ticketingsystem.ticketing_system.model.Vendor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VendorFactory {
    private final TicketService ticketService;
    private final ConfigurationData configurationData;

    public Vendor createVendor(String vendorId) {
        return new Vendor(vendorId, configurationData, ticketService);
    }
}
