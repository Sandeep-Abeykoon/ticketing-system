package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.model.Customer;
import edu.westminster.ticketingsystem.ticketing_system.model.Vendor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ParticipantManagementService handles the addition and removal of participants
 * (customers and vendors) in the simulation.
 * This service manages participant threads, ensuring proper validation and logging
 * during their lifecycle in the simulation.
 */
@Service
@RequiredArgsConstructor
public class ParticipantManagementService {

    private final ParticipantFactory participantFactory;
    private final SimulationLogService logService;

    /**
     * Adds a customer (VIP or normal) to the provided list of customer threads.
     *
     * @param customerThreads The list of customer threads.
     * @param isVIP Whether the customer is a VIP.
     * @param isSimulationRunning Whether the simulation is currently running.
     */
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

    /**
     * Removes a customer (VIP or normal) from the provided list of customer threads by ID.
     *
     * @param customerThreads The list of customer threads.
     * @param customerId The ID of the customer to be removed.
     * @param isSimulationRunning Whether the simulation is currently running.
     */
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

    /**
     * Adds a vendor to the provided list of vendor threads.
     *
     * @param vendorThreads The list of vendor threads.
     * @param isSimulationRunning Whether the simulation is currently running.
     */
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

    /**
     * Removes a vendor from the provided list of vendor threads by ID.
     *
     * @param vendorThreads The list of vendor threads.
     * @param vendorId The ID of the vendor to be removed.
     * @param isSimulationRunning Whether the simulation is currently running.
     */
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

    /**
     * Validates whether the simulation is currently running.
     *
     * @param isSimulationRunning Whether the simulation is running.
     * @throws IllegalStateException if the simulation is not running.
     */
    private void validateSimulationRunning(boolean isSimulationRunning) {
        if (!isSimulationRunning) {
            throw new IllegalStateException("Simulation is not running.");
        }
    }

    /**
     * Calculates the next unique ID for a participant based on the current list of threads.
     *
     * @param threads The list of threads.
     * @return The next unique ID as an integer.
     */
    private int getNextId(List<Thread> threads) {
        return threads.stream()
                .mapToInt(thread -> Integer.parseInt(thread.getName()))
                .max()
                .orElse(0) + 1;
    }

    /**
     * Creates and starts a thread for the given participant.
     *
     * @param participant The participant to run in the thread.
     * @param id The ID to assign to the thread.
     * @return The created and started thread.
     */
    private Thread createAndStartThread(Runnable participant, String id) {
        Thread thread = new Thread(participant);
        thread.setName(id);
        thread.start();
        return thread;
    }
}
