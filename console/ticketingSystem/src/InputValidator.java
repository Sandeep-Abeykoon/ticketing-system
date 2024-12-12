import java.util.Scanner;

/**
 * Utility class for validating user input.
 */
public class InputValidator {

    /**
     * Prompts the user for a positive integer.
     *
     * @param prompt  Message displayed to the user.
     * @param scanner Scanner object for input.
     * @return A positive integer entered by the user.
     */
    public static int getPositiveInteger(String prompt, Scanner scanner) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be positive. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Prompts the user for an integer within a range.
     *
     * @param prompt  Message displayed to the user.
     * @param scanner Scanner object for input.
     * @param min     Minimum acceptable value (inclusive).
     * @param max     Maximum acceptable value (inclusive).
     * @return An integer within the specified range.
     */
    public static int getBoundedInteger(String prompt, Scanner scanner, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Value must be between " + min + " and " + max + ". Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
