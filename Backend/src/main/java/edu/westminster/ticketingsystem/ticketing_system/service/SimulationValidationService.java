package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SimulationValidationService {

    public void validateSimulationStart(int numberOfVendors, int numberOfCustomers, int numberOfVIPCustomers) {
        if (numberOfVendors <= 0) {
            throw new IllegalArgumentException("Number of vendors must be greater than 0");
        }
        if (numberOfCustomers <= 0) {
            throw new IllegalArgumentException("Number of customers should be greater than 0");
        }
        if (numberOfVIPCustomers < 0) {
            throw new IllegalArgumentException("Number of VIP customers cannot be less than 0");
        }
    }
}
