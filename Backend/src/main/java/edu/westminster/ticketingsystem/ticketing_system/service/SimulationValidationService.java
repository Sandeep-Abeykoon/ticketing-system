package edu.westminster.ticketingsystem.ticketing_system.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * SimulationValidationService validates input parameters for starting the simulation.
 * This service ensures that the simulation starts with valid values for vendors, customers,
 * and VIP customers.
 */
@Service
@AllArgsConstructor
public class SimulationValidationService {

    /**
     * Validates the input parameters for starting the simulation.
     *
     * @param numberOfVendors Number of vendors to start with.
     * @param numberOfCustomers Number of normal customers to start with.
     * @param numberOfVIPCustomers Number of VIP customers to start with.
     * @throws IllegalArgumentException if any parameter is invalid.
     */
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
