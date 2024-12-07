package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.component.TicketPool;
import edu.westminster.ticketingsystem.ticketing_system.model.Vendor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulationService {
    private final ParticipantFactory participantFactory;
    private final List<Thread> vendorThreads;
    private final TicketPool ticketPool;

    public SimulationService(ParticipantFactory participantFactory, TicketPool ticketPool){
        this.participantFactory = participantFactory;
        this.ticketPool = ticketPool;
        this.vendorThreads = new ArrayList<>();

    }
    public void startSimulation(int numberOfVendors) {
        if (!vendorThreads.isEmpty()) {
            throw new IllegalStateException(("Simulation is already running"));
        }

        for (int i = 0; i < numberOfVendors; i++) {
            String vendorId = String.valueOf(i + 1);

            Vendor vendor = participantFactory.createVendor(vendorId);
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

        ticketPool.clearPool();
    }
}
