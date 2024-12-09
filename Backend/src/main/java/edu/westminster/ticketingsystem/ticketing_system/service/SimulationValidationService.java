package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SimulationValidationService {

    private SystemConfiguration systemConfiguration;

    public void validateSimulationStart(boolean simulationStatus, int numberOfVendors, int numberOfCustomers) {
        if (!systemConfiguration.isSystemConfigured()) {
            throw new IllegalStateException("The system is not configured");
        }
        if (simulationStatus) {
            throw new IllegalStateException("Simulation is already running");
        }
        if (numberOfVendors <= 0) {
            throw new IllegalArgumentException("Number of vendors must be greater than 0");
        }
        if (numberOfCustomers <= 0) {
            throw new IllegalArgumentException("Number of customers should be greater than 0");
        }
    }
}
