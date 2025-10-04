package BinarySearchTreeCLI;

import java.util.List;
import java.util.Scanner;

/**
 * Simple command-line interface for Binary Search Tree operations.
 *
 * <p>Provides a text-based menu to:
 * <ul>
 *   <li>Create a balanced tree (with values 1–7 by default)</li>
 *   <li>Add or delete nodes</li>
 *   <li>Print traversals: in-order, pre-order, post-order</li>
 * </ul>
 */
public final class BinarySearchTreeCLI {

    /** Prevent instantiation; this is a static-only utility class. */
    private BinarySearchTreeCLI() {}

    private static BinarySearchTree<Integer> tree;

    /**
     * Entry point for the CLI application. Continuously displays a menu and
     * executes user-selected operations until the user chooses to exit.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                printMenu();
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1" -> createTree(sc);
                    case "2" -> addNode(sc);
                    case "3" -> deleteNode(sc);
                    case "4" -> printTraversal(BinarySearchTree.Traversal.InOrder);
                    case "5" -> printTraversal(BinarySearchTree.Traversal.PreOrder);
                    case "6" -> printTraversal(BinarySearchTree.Traversal.PostOrder);
                    case "7" -> { System.out.println("\nGoodbye!"); return; }
                    default  -> System.out.println("Invalid choice.");
                }
                pause(sc);
            }
        }
    }

    /** Prints the available menu options. */
    private static void printMenu() {
        System.out.println("\n=== Binary Search Tree CLI ===");
        System.out.println("  1. Create a balanced BST from range");
        System.out.println("  2. Add a node");
        System.out.println("  3. Delete a node");
        System.out.println("  4. Print InOrder traversal");
        System.out.println("  5. Print PreOrder traversal");
        System.out.println("  6. Print PostOrder traversal");
        System.out.println("  7. Exit");
        System.out.print("Enter choice: ");
    }

    /**
     * Creates a balanced BST with default values 1 through 7.
     *
     * @param sc scanner used for input (not currently used here)
     */
    private static void createTree(Scanner sc) {
        int minVal = 1;
        int maxVal = 7;
        tree = BinarySearchTree.fromInclusiveRange(minVal, maxVal);
        System.out.printf("\nCreated balanced BST with values %d through %d%n", minVal, maxVal);
    }

    /**
     * Prompts the user for a value and inserts it into the tree.
     *
     * @param sc scanner for reading the integer to insert
     */
    private static void addNode(Scanner sc) {
        ensureTree();
        int val = readInt(sc, "\nEnter value to insert: ");
        System.out.println("Inserting node " + val + "... ");
        tree.add(val);
        System.out.println("Done.");
    }

    /**
     * Prompts the user for a value and deletes it from the tree.
     *
     * @param sc scanner for reading the integer to delete
     */
    private static void deleteNode(Scanner sc) {
        ensureTree();
        int val = readInt(sc, "\nEnter value to delete: ");
        System.out.println("Deleting node " + val + "... ");
        tree.delete(val);
        System.out.println("Done.");
    }

    /**
     * Prints the tree in the specified traversal order.
     *
     * @param t traversal type (in-order, pre-order, post-order)
     */
    private static void printTraversal(BinarySearchTree.Traversal t) {
        ensureTree();
        List<Integer> vals = tree.traverse(t);
        System.out.println("\n" + t + ": " + String.join(", ",
                vals.stream().map(String::valueOf).toList()));
    }

    /** Ensures a tree exists; if not, creates an empty one. */
    private static void ensureTree() {
        if (tree == null) tree = new BinarySearchTree<>();
    }

    /**
     * Prompts until the user enters a valid integer.
     *
     * @param sc     scanner for reading input
     * @param prompt text displayed to the user
     * @return the integer entered by the user
     */
    private static int readInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            sc.nextLine();
            System.out.print("Enter an integer: ");
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    /**
     * Reads an integer with a default fallback if the input is blank or invalid.
     *
     * @param sc     scanner for reading input
     * @param prompt text displayed to the user
     * @param def    default value to return on blank/invalid input
     * @return the parsed integer, or {@code def} if input is blank/invalid
     */
    private static int readInt(Scanner sc, String prompt, int def) {
        System.out.print(prompt);
        String line = sc.nextLine().trim();
        if (line.isEmpty()) return def;
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Pauses program execution until the user presses Enter.
     *
     * @param sc scanner for reading input
     */
    private static void pause(Scanner sc) {
        System.out.print("\nPress Enter to continue...");
        sc.nextLine();
    }
}
