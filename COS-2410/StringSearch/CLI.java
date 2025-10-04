package StringSearch;

import java.util.Scanner;

/**
 * <h2>US States Search CLI</h2>
 *
 * <p>A tiny command-line interface that lets a user:</p>
 * <ol>
 *   <li>Display all 50 U.S. state names</li>
 *   <li>Search for a substring within the state names using the
 *       Boyer–Moore <em>bad-character</em> heuristic (case-insensitive)</li>
 *   <li>Exit the program</li>
 * </ol>
 *
 * <p>This class is intentionally minimal: it delegates all search/display logic
 * to {@link States} and only handles user interaction and control flow.</p>
 */
public class CLI {

    /** Private constructor to prevent instantiation. */
    private CLI() {}

    /**
     * Entry point for the CLI application.
     *
     * <p>Shows a 3-option menu in a loop until the user chooses to exit.
     * Option handlers call into {@link States} for the actual work.</p>
     *
     * @param args unused
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("===US States Search CLI===");
                System.out.println("1. Display all states");
                System.out.println("2. Search for a state (case-insensitive)");
                System.out.println("3. Exit");
                System.out.print("Please enter your choice (1-3): ");

                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> {
                        // Display all state names with simple word wrapping
                        States.display();
                    }
                    case "2" -> {
                        // Prompt user for a pattern and print the matching indexes
                        States.findStateIndexesByPattern(scanner);
                    }
                    case "3" -> {
                        System.out.println("\nGoodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }

                System.out.println();
            }
        }
    }
}
