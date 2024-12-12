import java.util.Scanner;

/**
 * Entry point for the Ticket Management System.
 * Handles the main menu and user interaction for configuration and simulation.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            displayHeading();
            Configuration config = ConfigurationManager.loadConfiguration();

            if (config != null) {
                ConfigurationManager.displayConfiguration(config);
                keepRunning = showMenu(scanner, config);
            } else {
                System.out.println("No configuration found.");
                config = ConfigurationManager.configureSystem(scanner);
                ConfigurationManager.saveConfiguration(config);
            }
        }

        System.out.println("Exiting program. Goodbye!");
    }

    /**
     * Displays the program heading.
     */
    private static void displayHeading() {
        System.out.println("=======================================");
        System.out.println("     TICKET MANAGEMENT SYSTEM");
        System.out.println("=======================================");
    }

    /**
     * Displays the menu and handles user choices.
     *
     * @param scanner Scanner object for input.
     * @param config  The current system configuration.
     * @return True to continue running, false to exit.
     */
    private static boolean showMenu(Scanner scanner, Configuration config) {
        System.out.println("\nMenu Options:");
        System.out.println("1. Edit Configuration");
        System.out.println("2. Start Simulation");
        System.out.println("3. Exit\n");

        int choice = InputValidator.getBoundedInteger("Enter your choice: ", scanner, 1, 3);

        switch (choice) {
            case 1 -> {
                // Update and save new configuration
                Configuration newConfig = ConfigurationManager.configureSystem(scanner);
                ConfigurationManager.saveConfiguration(newConfig);
                System.out.println("Configuration updated.");
            }
            case 2 -> {
                // Start simulation
                new SimulationManager().startSimulation(config, scanner);
            }
            case 3 -> {
                // Exit the program
                return false;
            }
        }
        return true;
    }
}
