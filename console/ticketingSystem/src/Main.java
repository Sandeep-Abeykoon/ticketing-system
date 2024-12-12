import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            displayHeading();
            Configuration config = ConfigurationManager.loadConfiguration();

            if (config != null) {
                ConfigurationManager.displayConfiguration(config);
                keepRunning = showMenuWithConfig(scanner, config);
            } else {
                System.out.println("No configuration found. Please create a new configuration.\n");
                Configuration newConfig = ConfigurationManager.configureSystem();
                ConfigurationManager.saveConfiguration(newConfig);
            }
        }

        System.out.println("Exiting the program. Goodbye!");
    }

    // Displays a nice heading for the application
    private static void displayHeading() {
        System.out.println("=======================================");
        System.out.println("     TICKET MANAGEMENT SYSTEM");
        System.out.println("=======================================\n");
    }

    // Shows the menu if a configuration is already present
    private static boolean showMenuWithConfig(Scanner scanner, Configuration config) {
        while (true) {
            System.out.println("\nMenu Options:");
            System.out.println("1. Edit Configuration");
            System.out.println("2. Proceed to Simulation");
            System.out.println("3. Exit");
            System.out.print("\nEnter your choice: ");

            int choice = getValidatedMenuChoice(scanner);

            switch (choice) {
                case 1 -> {
                    System.out.println("\nEditing Configuration...");
                    Configuration updatedConfig = ConfigurationManager.configureSystem();
                    ConfigurationManager.saveConfiguration(updatedConfig);
                    System.out.println("Configuration updated successfully.\n");
                    return true; // Continue to the main loop
                }
                case 2 -> {
                    System.out.println("\nProceeding to Simulation...");
                    startSimulation(config);
                    return true; // Return to the main loop after simulation
                }
                case 3 -> {
                    return false; // Exit the program
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Starts the simulation by invoking the SimulationManager
    private static void startSimulation(Configuration config) {
        SimulationManager simulationManager = new SimulationManager();
        simulationManager.startSimulation(config);
    }

    // Helper method to validate menu choice input
    private static int getValidatedMenuChoice(Scanner scanner) {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 3) {
                    return choice;
                } else {
                    System.out.print("Invalid choice. Please enter a number between 1 and 3: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}
