import java.io.*;
import java.util.Scanner;

public class ConfigurationManager {
    private static final String CONFIG_FILE = "config.ser";

    // Save Configuration to a file using Serialization
    public static void saveConfiguration(Configuration config) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONFIG_FILE))) {
            oos.writeObject(config);
            System.out.println("Configuration saved successfully.\n");
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }

    // Load Configuration from a file using Serialization
    public static Configuration loadConfiguration() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CONFIG_FILE))) {
            return (Configuration) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Configuration file not found. Please configure the system.\n");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
        return null; // Return null if configuration is not found
    }

    // Display current configuration
    public static void displayConfiguration(Configuration config) {
        if (config == null) {
            System.out.println("No configuration loaded.");
        } else {
            System.out.println("Current Configuration:");
            System.out.println("Total Tickets: " + config.getTotalTickets());
            System.out.println("Ticket Release Rate: " + config.getTicketReleaseRate());
            System.out.println("Ticket Release Interval: " + config.getTicketReleaseInterval());
            System.out.println("Customer Retrieval Rate: " + config.getCustomerRetrievalRate());
            System.out.println("Ticket Retrieval Interval: " + config.getTicketRetrievalInterval());
            System.out.println("Max Ticket Capacity: " + config.getMaxTicketCapacity());
        }
    }

    // CLI to configure the system
    public static Configuration configureSystem() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Total Tickets: ");
        int totalTickets = getPositiveInteger(scanner);

        System.out.print("Enter Ticket Release Rate: ");
        int ticketReleaseRate = getPositiveInteger(scanner);

        System.out.print("Enter Ticket Release Interval: ");
        int ticketReleaseInterval = getPositiveInteger(scanner);

        System.out.print("Enter Customer Retrieval Rate: ");
        int customerRetrievalRate = getPositiveInteger(scanner);

        System.out.print("Enter Ticket Retrieval Interval: ");
        int ticketRetrievalInterval = getPositiveInteger(scanner);

        System.out.print("Enter Max Ticket Capacity: ");
        int maxTicketCapacity = getPositiveInteger(scanner);

        return new Configuration(totalTickets, ticketReleaseRate, ticketReleaseInterval,
                customerRetrievalRate, ticketRetrievalInterval, maxTicketCapacity);
    }

    // Helper method to validate positive integer input
    private static int getPositiveInteger(Scanner scanner) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Value must be positive. Try again:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number:");
            }
        }
    }
}
