import java.io.*;
import java.util.Scanner;

/**
 * Manages saving, loading, displaying, and creating the configuration settings
 * for the ticket management system.
 */
public class ConfigurationManager {
    private static final String CONFIG_FILE = "config.ser"; // File to store the configuration

    /**
     * Saves the given configuration to a file.
     *
     * @param config The configuration to save.
     */
    public static void saveConfiguration(Configuration config) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONFIG_FILE))) {
            oos.writeObject(config);
            System.out.println("Configuration saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }

    /**
     * Loads the configuration from a file.
     *
     * @return The loaded Configuration object, or null if the file does not exist
     *         or an error occurs.
     */
    public static Configuration loadConfiguration() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CONFIG_FILE))) {
            return (Configuration) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Configuration file not found. Please create a new configuration.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
        return null; // Return null if configuration cannot be loaded
    }

    /**
     * Displays the current configuration.
     *
     * @param config The configuration to display.
     */
    public static void displayConfiguration(Configuration config) {
        if (config != null) {
            System.out.println("\nCurrent Configuration:");
            System.out.println(config); // Uses Configuration's toString() method
        } else {
            System.out.println("No configuration available.\n");
        }
    }

    /**
     * Guides the user through creating a new configuration.
     *
     * @param scanner A Scanner object to read user input.
     * @return A new Configuration object based on user inputs.
     */
    public static Configuration configureSystem(Scanner scanner) {
        System.out.println("\nCreating new configuration...\n");

        // Prompt user for configuration settings
        int totalTickets = InputValidator.getPositiveInteger("Enter Total Tickets: ", scanner);
        int ticketReleaseRate = InputValidator.getPositiveInteger("Enter Ticket Release Rate: ", scanner);
        int ticketReleaseInterval = InputValidator.getPositiveInteger("Enter Ticket Release Interval: ", scanner);
        int customerRetrievalRate = InputValidator.getPositiveInteger("Enter Customer Retrieval Rate: ", scanner);
        int ticketRetrievalInterval = InputValidator.getPositiveInteger("Enter Ticket Retrieval Interval: ", scanner);
        int maxTicketCapacity = InputValidator.getPositiveInteger("Enter Max Ticket Capacity: ", scanner);

        // Create and return the new Configuration object
        return new Configuration(totalTickets, ticketReleaseRate, ticketReleaseInterval,
                customerRetrievalRate, ticketRetrievalInterval, maxTicketCapacity);
    }
}
