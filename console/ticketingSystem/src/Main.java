import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayHeading();
            Configuration config = ConfigurationManager.loadConfiguration();

            if (config != null) {
                ConfigurationManager.displayConfiguration(config);
                showMenuWithConfig(scanner, config);
            } else {
                System.out.println("No configuration found. Please create a new configuration.\n");
                Configuration newConfig = ConfigurationManager.configureSystem();
                ConfigurationManager.saveConfiguration(newConfig);
            }
        }
    }

    // Displays a nice heading for the application
    private static void displayHeading() {
        System.out.println("=======================================");
        System.out.println("     TICKET MANAGEMENT SYSTEM");
        System.out.println("=======================================\n");
    }

    // Shows the menu if a configuration is already present
    private static void showMenuWithConfig(Scanner scanner, Configuration config) {
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
                    return;
                }
                case 2 -> {
                    System.out.println("\nProceeding to Simulation...");
                    startSimulation(config);
                    return;
                }
                case 3 -> {
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Starts the simulation (stub implementation for now)
    private static void startSimulation(Configuration config) {
        System.out.println("\nStarting simulation with the following configuration:");
        System.out.println(config + "\n");
        // Add simulation logic here
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
