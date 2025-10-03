import java.util.Scanner;

public class BinarySearchTreeCLI {

    static class Node {
        int key;
        Node left, right;
        Node(int k) { key = k; }
    }

    static Node buildBalancedBST(int lo, int hi) {
        if (lo > hi) return null;
        int mid = (lo + hi) / 2;
        Node root = new Node(mid);
        root.left  = buildBalancedBST(lo, mid - 1);
        root.right = buildBalancedBST(mid + 1, hi);
        return root;
    }


    public static class BinaryTree {
        Node root;
        BinaryTree(int key) {
            root = new Node(key);
        }
        BinaryTree() {
            root = null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Binary Search Tree CLI ===");
            System.out.println("1. Create a binary search tree");
            System.out.println("2. Add a node");
            System.out.println("3. Delete a node");
            System.out.println("4. Print nodes (InOrder)");
            System.out.println("5. Print nodes (PreOrder)");
            System.out.println("6. Print nodes (PostOrder)");
            System.out.println("7. Exit program");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> createBST();
//                case 2 -> addNode();
//                case 3 -> deleteNode();
//                case 4 -> printInOrder();
//                case 5 -> printPreOrder();
//                case 6 -> printPostOrder();
                case 7 -> exit();
                default -> System.out.println("Invalid choice. Please try again.");
            }
            System.out.print("Press enter to continue...");
            scanner.nextLine();
        }
    }

    public static void createBST() {
        int minValue = 1;
        int maxValue = 7;

        try {
            Node root = buildBalancedBST(minValue, maxValue);
            System.out.println("\nBalanced BST created with values from " + minValue + " to " + maxValue);
        }
        catch (Exception e) {
            System.out.println("Error creating BST: " + e.getMessage());
        }
    }
//    public static void addNode() {
//    }
//    public static void deleteNode() {}
//    public static void printInOrder() {}
//    public static void printPreOrder() {}
//    public static void printPostOrder() {}
    public static void exit() {
        System.out.println("\nExiting...");
        System.exit(0);
    }
//    public static void CLI() {}
//    public static int readInt(Scanner scanner, String prompt) {}
}