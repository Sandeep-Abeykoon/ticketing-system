import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages the ticket simulation, including vendor and customer threads.
 */
public class SimulationManager {
    private final List<Thread> threads = new ArrayList<>();
    private TicketPool ticketPool;

    /**
     * Starts the simulation by initializing vendors, customers, and the ticket pool.
     *
     * @param config  Simulation configuration.
     * @param scanner Scanner for user input.
     */
    public void startSimulation(Configuration config, Scanner scanner) {
        System.out.println("\nPlease provide simulation details:");

        // Get the number of vendors and customers
        int numberOfVendors = InputValidator.getPositiveInteger("Number of Vendors (must be greater than 0): ", scanner);
        int numberOfCustomers = InputValidator.getPositiveInteger("Number of Customers (must be greater than 0): ", scanner);

        System.out.println("\nSimulation Details:");
        System.out.println(config);
        System.out.println("Vendors: " + numberOfVendors + ", Customers: " + numberOfCustomers);

        ticketPool = new TicketPool(config.getMaxTicketCapacity());

        int maxThreads = Math.max(numberOfVendors, numberOfCustomers);

        for (int i = 1; i <= maxThreads; i++) {
            if (i <= numberOfVendors) {
                Thread vendorThread = new Thread(new Vendor("Vendor-" + i, config.getTicketReleaseInterval(),
                        config.getTicketReleaseRate(), ticketPool));
                threads.add(vendorThread);
                vendorThread.start();
            }

            if (i <= numberOfCustomers) {
                Thread customerThread = new Thread(new Customer("Customer-" + i, config.getTicketRetrievalInterval(),
                        config.getCustomerRetrievalRate(), ticketPool));
                threads.add(customerThread);
                customerThread.start();
            }
        }

        System.out.println("\nSimulation is running. Press Enter to stop.\n");
        scanner.nextLine(); // Wait for the user to press Enter before stopping the simulation
        stopSimulation();
    }

    /**
     * Stops the simulation by interrupting and joining all threads.
     */
    private void stopSimulation() {
        System.out.println("\nStopping simulation...");

        // Interrupt all threads to signal them to stop
        threads.forEach(Thread::interrupt);

        // Wait for all threads to finish their execution
        for (Thread thread : threads) {
            try {
                thread.join(); // Ensures the main thread waits for each thread to complete
            } catch (InterruptedException e) {
                System.err.println("Error while waiting for threads: " + e.getMessage());
            }
        }

        displaySummary();
    }

    /**
     * Displays the final summary of the simulation, including ticket statistics.
     */
    private void displaySummary() {
        System.out.println("\nSimulation Summary:");
        System.out.println("Tickets in Pool: " + ticketPool.getTicketCount());
        System.out.println("Tickets Added: " + ticketPool.getTotalAdded());
        System.out.println("Tickets Retrieved: " + ticketPool.getTotalRetrieved());
    }
}
