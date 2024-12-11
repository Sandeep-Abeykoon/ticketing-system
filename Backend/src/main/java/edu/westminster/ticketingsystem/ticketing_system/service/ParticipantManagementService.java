package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.model.Customer;
import edu.westminster.ticketingsystem.ticketing_system.model.Vendor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ParticipantManagementService {

    private final ParticipantFactory participantFactory;
    private final SimulationLogService logService;

    // Add a customer (VIP or Normal) to the given list
    public void addCustomer(List<Thread> customerThreads, boolean isVIP, boolean isSimulationRunning) {
        validateSimulationRunning(isSimulationRunning);

        String customerId = String.valueOf(getNextId(customerThreads));
        Customer customer = isVIP
                ? participantFactory.createVIPCustomer(customerId)
                : participantFactory.createCustomer(customerId);

        Thread customerThread = createAndStartThread(customer, customerId);
        customerThreads.add(customerThread);

        logService.sendStructuredLog("CUSTOMER_ADDED", Map.of(
                "id", customerId,
                "type", isVIP ? "VIP Customer" : "Customer"
        ));
    }

    // Remove a customer (VIP or Normal) from the given list by ID
    public void removeCustomer(List<Thread> customerThreads, String customerId, boolean isSimulationRunning) {
        validateSimulationRunning(isSimulationRunning);

        Thread targetThread = customerThreads.stream()
                .filter(thread -> thread.getName().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Customer with ID " + customerId + " not found."));

        targetThread.interrupt();
        customerThreads.remove(targetThread);

        logService.sendStructuredLog("CUSTOMER_REMOVED", Map.of(
                "id", customerId,
                "type", "Customer"
        ));
    }

    // Add a vendor to the given list
    public void addVendor(List<Thread> vendorThreads, boolean isSimulationRunning) {
        validateSimulationRunning(isSimulationRunning);

        String vendorId = String.valueOf(getNextId(vendorThreads));
        Vendor vendor = participantFactory.createVendor(vendorId);

        Thread vendorThread = createAndStartThread(vendor, vendorId);
        vendorThreads.add(vendorThread);

        logService.sendStructuredLog("VENDOR_ADDED", Map.of(
                "id", vendorId,
                "type", "Vendor"
        ));
    }

    // Remove a vendor from the given list by ID
    public void removeVendor(List<Thread> vendorThreads, String vendorId, boolean isSimulationRunning) {
        validateSimulationRunning(isSimulationRunning);

        Thread targetThread = vendorThreads.stream()
                .filter(thread -> thread.getName().equals(vendorId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Vendor with ID " + vendorId + " not found."));

        targetThread.interrupt();
        vendorThreads.remove(targetThread);

        logService.sendStructuredLog("VENDOR_REMOVED", Map.of(
                "id", vendorId,
                "type", "Vendor"
        ));
    }

    // Utility to validate if the simulation is running
    private void validateSimulationRunning(boolean isSimulationRunning) {
        if (!isSimulationRunning) {
            throw new IllegalStateException("Simulation is not running.");
        }
    }

    // Utility to get the next ID based on the current list
    private int getNextId(List<Thread> threads) {
        return threads.stream()
                .mapToInt(thread -> Integer.parseInt(thread.getName()))
                .max()
                .orElse(0) + 1;
    }

    // Utility to create and start a thread
    private Thread createAndStartThread(Runnable participant, String id) {
        Thread thread = new Thread(participant);
        thread.setName(id);
        thread.start();
        return thread;
    }
}
