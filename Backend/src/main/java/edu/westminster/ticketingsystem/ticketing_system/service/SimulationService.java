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
                Thread vendorThread = new Thread(vendor);
                vendorThread.setName(vendorId);
                vendorThreads.add(vendorThread);
                logService.sendStructuredLog("THREAD_STARTED", Map.of(
                        "id", vendorId,
                        "type", "Vendor"
                ));
                vendorThread.start();
            }

            if (i < numberOfVIPCustomers) {
                String vipCustomerId = String.valueOf(i + 1);
                Customer vipCustomer = participantFactory.createVIPCustomer(vipCustomerId);
                Thread vipCustomerThread = new Thread(vipCustomer);
                vipCustomerThread.setName(vipCustomerId);
                vipCustomerThreads.add(vipCustomerThread);
                logService.sendStructuredLog("THREAD_STARTED", Map.of(
                        "id", vipCustomerId,
                        "type", "VIP Customer"
                ));
                vipCustomerThread.start();
            }

            if (i < numberOfCustomers) {
                String customerId = String.valueOf(i + 1);
                Customer customer = participantFactory.createCustomer(customerId);
                Thread customerThread = new Thread(customer);
                customerThread.setName(customerId);
                customerThreads.add(customerThread);
                logService.sendStructuredLog("THREAD_STARTED", Map.of(
                        "id", customerId,
                        "type", "Customer"
                ));
                customerThread.start();
            }
        }
    }

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
        logService.sendStructuredLog("SIMULATION_STOPPED", Map.of(
                "message", "Simulation stopped"
        ));
    }

    public Map<String, Object> resetTicketPoolData() {
        if (isSimulationRunning) {
            throw new IllegalStateException("Can't reset when a simulation is running");
        }
        ticketPool.clearPoolData();
        logService.clearLogs();
        return getSimulationStatusDetails();
    }

    public boolean getSimulationStatus() {
        return isSimulationRunning;
    }

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
        response.put("logs", logService.getLogs()); // Add logs to the response
        return response;
    }

    // Participant Management
    public void addVendor() {
        participantManagementService.addVendor(vendorThreads, isSimulationRunning);
        sendUserUpdateLog();
    }

    public void addCustomer(boolean isVIP) {
        participantManagementService.addCustomer(
                isVIP ? vipCustomerThreads : customerThreads, isVIP, isSimulationRunning);
        sendUserUpdateLog();
    }

    public void removeVendor(String vendorId) {
        participantManagementService.removeVendor(vendorThreads, vendorId, isSimulationRunning);
        sendUserUpdateLog();
    }

    public void removeCustomer(String customerId, boolean isVIP) {
        participantManagementService.removeCustomer(
                isVIP ? vipCustomerThreads : customerThreads, customerId, isSimulationRunning
        );
        sendUserUpdateLog();
    }

    // Utility to send user update log
    private void sendUserUpdateLog() {
        logService.sendStructuredLog("USER_UPDATE", Map.of(
                "numberOfCustomers", customerThreads.size(),
                "numberOfVIPCustomers", vipCustomerThreads.size(),
                "numberOfVendors", vendorThreads.size()
        ));
    }
}


