import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimulationManager {
    private final List<Thread> threads = new ArrayList<>(); // List to keep track of all threads
    private TicketPool ticketPool; // Shared TicketPool

    public void startSimulation(Configuration config) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please provide the following details:");
        System.out.println("---------------------------------------\n");

        int numberOfVendors = getValidInput(scanner, "Number of Vendors (must be greater than 0): ", 1);
        int numberOfCustomers = getValidInput(scanner, "Number of Customers (must be greater than 0): ", 1);

        System.out.println("\nSimulation Details:");
        System.out.println("----------------------\n");
        System.out.println("Configuration: " + config);
        System.out.println("Number of Vendors: " + numberOfVendors);
        System.out.println("Number of Customers: " + numberOfCustomers);

        System.out.println("\nStarting Simulation...");

        // Create the shared TicketPool
        ticketPool = new TicketPool(config.getMaxTicketCapacity());

        // Create vendor threads
        for (int i = 1; i <= numberOfVendors; i++) {
            Thread vendorThread = new Thread(new Vendor("Vendor-" + i, config.getTicketReleaseInterval(),
                    config.getTicketReleaseRate(), ticketPool));
            threads.add(vendorThread);
            vendorThread.start();
        }

        // Create customer threads
        for (int i = 1; i <= numberOfCustomers; i++) {
            Thread customerThread = new Thread(new Customer("Customer-" + i, config.getTicketRetrievalInterval(),
                    config.getCustomerRetrievalRate(), ticketPool));
            threads.add(customerThread);
            customerThread.start();
        }

        System.out.println("\nSimulation is now running...");
        System.out.println("Press Enter to stop the simulation.\n");

        // Monitor for Enter key press to stop the simulation
        listenForEnterKey(scanner);
    }

    private int getValidInput(Scanner scanner, String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min) {
                    return value;
                } else {
                    System.out.println("Input must be greater than or equal to " + min + ". Try again.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.\n");
            }
        }
    }

    private void listenForEnterKey(Scanner scanner) {
        try {
            scanner.nextLine(); // Wait for the Enter key press
            stopSimulation();
        } catch (Exception e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    // Stops the simulation
    private void stopSimulation() {
        System.out.println("\nStopping simulation...");

        // Interrupt all threads
        for (Thread thread : threads) {
            thread.interrupt();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join(); // Wait for the thread to terminate
            } catch (InterruptedException e) {
                System.err.println("Error while waiting for thread termination: " + e.getMessage());
            }
        }

        threads.clear(); // Clear the thread list after ensuring all threads are stopped
        displaySummary(); // Display the simulation summary
        System.out.println("Simulation has been stopped.");
    }

    // Displays the simulation summary
    private void displaySummary() {
        System.out.println("\nSimulation Summary:");
        System.out.println("----------------------");
        System.out.println("Total Tickets in Pool: " + ticketPool.getTicketCount());
        System.out.println("Total Tickets Added: " + ticketPool.getTotalAdded());
        System.out.println("Total Tickets Retrieved: " + ticketPool.getTotalRetrieved());
        System.out.println("----------------------\n");
    }
}
