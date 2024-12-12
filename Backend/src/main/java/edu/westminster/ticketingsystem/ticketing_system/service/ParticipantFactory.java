package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import edu.westminster.ticketingsystem.ticketing_system.model.Customer;
import edu.westminster.ticketingsystem.ticketing_system.model.VIPCustomer;
import edu.westminster.ticketingsystem.ticketing_system.model.Vendor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * ParticipantFactory is responsible for creating instances of participants in the ticketing system.
 * This factory simplifies the creation of different types of participants such as vendors, customers,
 * and VIP customers by encapsulating their dependencies.
 */
@Component
@AllArgsConstructor
public class ParticipantFactory {

    private final TicketService ticketService;
    private final ConfigurationData configurationData;
    private final SimulationLogService logService;

    /**
     * Creates a new Vendor instance.
     *
     * @param vendorId The unique identifier for the vendor.
     * @return A new Vendor instance.
     */
    public Vendor createVendor(String vendorId) {
        return new Vendor(vendorId, configurationData, ticketService, logService);
    }

    /**
     * Creates a new Customer instance.
     *
     * @param customerId The unique identifier for the customer.
     * @return A new Customer instance.
     */
    public Customer createCustomer(String customerId) {
        return new Customer(customerId, configurationData, ticketService, logService);
    }

    /**
     * Creates a new VIPCustomer instance.
     *
     * @param customerId The unique identifier for the VIP customer.
     * @return A new VIPCustomer instance.
     */
    public VIPCustomer createVIPCustomer(String customerId) {
        return new VIPCustomer(customerId, configurationData, ticketService, logService);
    }
}
