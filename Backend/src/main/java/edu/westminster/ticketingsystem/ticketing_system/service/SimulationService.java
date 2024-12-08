package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.component.TicketPool;
import edu.westminster.ticketingsystem.ticketing_system.model.Customer;
import edu.westminster.ticketingsystem.ticketing_system.model.Vendor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulationService {
    private final ParticipantFactory participantFactory;
    private final TicketPool ticketPool;
    private final SimulationLogService logService;
    private final List<Thread> vendorThreads;
    private final List<Thread> customerThreads;
    private boolean isSimulationRunning;

    public SimulationService(ParticipantFactory participantFactory,
                             TicketPool ticketPool,
                             SimulationLogService logService){
        this.participantFactory = participantFactory;
        this.ticketPool = ticketPool;
        this.logService = logService;
        this.vendorThreads = new ArrayList<>();
        this.customerThreads = new ArrayList<>();
        this.isSimulationRunning = false;
    }

    public void startSimulation(int numberOfVendors, int numberOfCustomers) {
        if (isSimulationRunning) {
            throw new IllegalStateException(("Simulation is already running"));
        }
        isSimulationRunning = true;
        logService.sendSimulationStatus(true);


        /* Starting one Vendor thread and one Customer thread in each thread loop to balance the
           thread starting of vendor and customers
         */
        int maxThreads = Math.max(numberOfVendors, numberOfCustomers);
        for (int i = 0; i < maxThreads; i++) {
            if (i < numberOfVendors) {
                String vendorId = String.valueOf(i + 1);
                Vendor vendor = participantFactory.createVendor(vendorId);
                Thread vendorThread = new Thread(vendor);
                vendorThreads.add(vendorThread);
                vendorThread.start();
                logService.sendLog("Vendor " + vendorId + " started");
            }

            if (i < numberOfCustomers) {
                String customerId = String.valueOf(i + 1);
                Customer customer = participantFactory.createCustomer(customerId);
                Thread customerThread = new Thread(customer);
                customerThreads.add(customerThread);
                customerThread.start();
                logService.sendLog("Customer " + customerId + " started");
            }
        }
    }

    public void stopSimulation() {
        vendorThreads.forEach(Thread::interrupt);
        customerThreads.forEach(Thread::interrupt);

        vendorThreads.clear();
        customerThreads.clear();

        ticketPool.clearPool();
        isSimulationRunning = false;
        logService.sendSimulationStatus(false);
        logService.sendLog("Simulation stopped");
    }

    public boolean getSimulationStatus() {
        return isSimulationRunning;
    }
}
