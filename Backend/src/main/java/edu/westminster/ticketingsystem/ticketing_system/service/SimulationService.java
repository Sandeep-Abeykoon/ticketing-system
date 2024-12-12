package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.component.TicketPool;
import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.Customer;
import edu.westminster.ticketingsystem.ticketing_system.model.Vendor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SimulationService manages the core logic of the ticketing system simulation.
 * This service handles starting, stopping, resetting the simulation, and managing
 * participants (vendors, customers, and VIP customers). It also maintains simulation
 * status and sends logs to clients.
 */
@Service
@RequiredArgsConstructor
public class SimulationService {

    private final ParticipantFactory participantFactory;
    private final TicketPool ticketPool;
    private final SimulationLogService logService;
    private final SimulationValidationService validationService;
    private final ParticipantManagementService participantManagementService;
    private final SystemConfiguration systemConfiguration;

    private final List<Thread> vendorThreads = new ArrayList<>();
    private final List<Thread> customerThreads = new ArrayList<>();
    private final List<Thread> vipCustomerThreads = new ArrayList<>();

    private boolean isSimulationRunning = false;

    /**
     * Starts the simulation with the specified number of vendors, customers, and VIP customers.
     *
     * @param numberOfVendors Number of vendors to add.
     * @param numberOfCustomers Number of normal customers to add.
     * @param numberOfVIPCustomers Number of VIP customers to add.
     * @throws IllegalStateException if the system is not configured or if a simulation is already running.
     */
    public void startSimulation(int numberOfVendors, int numberOfCustomers, int numberOfVIPCustomers) {
        if (!systemConfiguration.isSystemConfigured()) {
            throw new IllegalStateException("The system is not configured");
        }
        if (isSimulationRunning) {
            throw new IllegalStateException("Simulation is already running");
        }

        validationService.validateSimulationStart(numberOfVendors, numberOfCustomers, numberOfVIPCustomers);

        isSimulationRunning = true;
        logService.sendSimulationStatus(true);
        logService.sendStructuredLog("SIMULATION_STARTED", Map.of(
                "numberOfVendors", numberOfVendors,
                "numberOfCustomers", numberOfCustomers,
                "numberOfVIPCustomers", numberOfVIPCustomers
        ));

        int maxThreads = Math.max(numberOfVendors, Math.max(numberOfCustomers, numberOfVIPCustomers));

        for (int i = 0; i < maxThreads; i++) {
            if (i < numberOfVendors) {
                String vendorId = String.valueOf(i + 1);
                Vendor vendor = participantFactory.createVendor(vendorId);
                Thread vendorThread = new Thread(vendor, vendorId);
                vendorThreads.add(vendorThread);
                logService.sendStructuredLog("THREAD_STARTED", Map.of("id", vendorId, "type", "Vendor"));
                vendorThread.start();
            }

            if (i < numberOfVIPCustomers) {
                String vipCustomerId = String.valueOf(i + 1);
                Customer vipCustomer = participantFactory.createVIPCustomer(vipCustomerId);
                Thread vipCustomerThread = new Thread(vipCustomer, vipCustomerId);
                vipCustomerThreads.add(vipCustomerThread);
                logService.sendStructuredLog("THREAD_STARTED", Map.of("id", vipCustomerId, "type", "VIP Customer"));
                vipCustomerThread.start();
            }

            if (i < numberOfCustomers) {
                String customerId = String.valueOf(i + 1);
                Customer customer = participantFactory.createCustomer(customerId);
                Thread customerThread = new Thread(customer, customerId);
                customerThreads.add(customerThread);
                logService.sendStructuredLog("THREAD_STARTED", Map.of("id", customerId, "type", "Customer"));
                customerThread.start();
            }
        }
    }

    /**
     * Stops the currently running simulation by interrupting all participant threads.
     *
     * @throws IllegalStateException if no simulation is running.
     */
    public void stopSimulation() {
        if (!isSimulationRunning) {
            throw new IllegalStateException("No currently any simulation running");
        }

        vendorThreads.forEach(Thread::interrupt);
        customerThreads.forEach(Thread::interrupt);
        vipCustomerThreads.forEach(Thread::interrupt);

        vendorThreads.clear();
        customerThreads.clear();
        vipCustomerThreads.clear();

        isSimulationRunning = false;

        logService.sendSimulationStatus(false);
        logService.sendStructuredLog("SIMULATION_STOPPED", Map.of("message", "Simulation stopped"));
    }

    /**
     * Resets the ticket pool data and clears logs. This operation can only be performed
     * when the simulation is not running.
     *
     * @return A map containing the current simulation status details.
     * @throws IllegalStateException if a simulation is running.
     */
    public Map<String, Object> resetTicketPoolData() {
        if (isSimulationRunning) {
            throw new IllegalStateException("Can't reset when a simulation is running");
        }

        ticketPool.clearPoolData();
        logService.clearLogs();
        return getSimulationStatusDetails();
    }

    /**
     * Retrieves the current simulation status.
     *
     * @return true if the simulation is running, false otherwise.
     */
    public boolean getSimulationStatus() {
        return isSimulationRunning;
    }

    /**
     * Retrieves detailed simulation status information, including participant counts, ticket data, and logs.
     *
     * @return A map containing the simulation status details.
     */
    public Map<String, Object> getSimulationStatusDetails() {
        Map<String, Object> response = new HashMap<>();
        response.put("isRunning", getSimulationStatus());
        response.put("ticketCount", ticketPool.getTicketCount());
        response.put("totalTicketsAdded", ticketPool.getTotalTicketsAdded());
        response.put("totalTicketsRetrieved", ticketPool.getTotalTicketsRetrieved());
        response.put("totalVIPRetrievals", ticketPool.getTotalVIPRetrievals());
        response.put("totalNormalRetrievals", ticketPool.getTotalNormalRetrievals());
        response.put("numberOfVendors", vendorThreads.size());
        response.put("numberOfCustomers", customerThreads.size());
        response.put("numberOfVIPCustomers", vipCustomerThreads.size());
        response.put("logs", logService.getLogs());
        return response;
    }

    /**
     * Adds a new vendor to the simulation.
     */
    public void addVendor() {
        participantManagementService.addVendor(vendorThreads, isSimulationRunning);
        sendUserUpdateLog();
    }

    /**
     * Adds a new customer to the simulation.
     *
     * @param isVIP Whether the customer is a VIP.
     */
    public void addCustomer(boolean isVIP) {
        participantManagementService.addCustomer(
                isVIP ? vipCustomerThreads : customerThreads, isVIP, isSimulationRunning);
        sendUserUpdateLog();
    }

    /**
     * Removes a vendor from the simulation by ID.
     *
     * @param vendorId The ID of the vendor to remove.
     */
    public void removeVendor(String vendorId) {
        participantManagementService.removeVendor(vendorThreads, vendorId, isSimulationRunning);
        sendUserUpdateLog();
    }

    /**
     * Removes a customer from the simulation by ID.
     *
     * @param customerId The ID of the customer to remove.
     * @param isVIP Whether the customer is a VIP.
     */
    public void removeCustomer(String customerId, boolean isVIP) {
        participantManagementService.removeCustomer(
                isVIP ? vipCustomerThreads : customerThreads, customerId, isSimulationRunning
        );
        sendUserUpdateLog();
    }

    /**
     * Sends a log update with the current counts of vendors and customers.
     */
    private void sendUserUpdateLog() {
        logService.sendStructuredLog("USER_UPDATE", Map.of(
                "numberOfCustomers", customerThreads.size(),
                "numberOfVIPCustomers", vipCustomerThreads.size(),
                "numberOfVendors", vendorThreads.size()
        ));
    }
}
