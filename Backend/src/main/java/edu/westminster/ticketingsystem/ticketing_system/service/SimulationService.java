package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.Vendor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulationService {
    private final TicketService ticketService;
    private final SystemConfiguration systemConfiguration;
    private final List<Thread> vendorThreads;

    public SimulationService(TicketService ticketService, SystemConfiguration systemConfiguration){
        this.ticketService = ticketService;
        this.systemConfiguration = systemConfiguration;
        this.vendorThreads = new ArrayList<>();
    }
    public void startSimulation(int numberOfVendors) {
        if (!vendorThreads.isEmpty()) {
            throw new IllegalStateException(("Simulation is already running"));
        }

        int ticketsPerRelease = systemConfiguration.getConfigurationData().getTicketReleaseRate();
        int releaseInterval = systemConfiguration.getConfigurationData().getTicketReleaseInterval();

        for (int i = 0; i < numberOfVendors; i++) {
            String vendorId = String.valueOf(i + 1);

            Vendor vendor = new Vendor(vendorId, ticketsPerRelease, releaseInterval, ticketService);
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }
    }

    public void stopSimulation() {
        for (Thread thread : vendorThreads) {
            thread.interrupt();
        }
        vendorThreads.clear();
    }
}
