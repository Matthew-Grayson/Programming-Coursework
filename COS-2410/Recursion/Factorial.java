package Recursion;

import java.util.Scanner;

public class Factorial {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        byte num = readByte(scanner, "Enter a non-negative integer to compute its factorial: ");
        long result = factorial(num);
        System.out.println("Factorial of " + num + " is: " + result);
    }

    static long factorial(int n) {
        // Base case
        if (n <= 1) {
            return 1;
        }
        // Recursive case
        return n * factorial(n - 1);
    }
    static byte readByte(Scanner scanner, String prompt) {
        byte num;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextByte()) {
                num = scanner.nextByte();
                if (num >= 0 && num <= 20) { // Limiting to 20 to prevent overflow
                    return num;
                } else {
                    System.out.println("Please enter a non-negative integer <= 20.");
                }
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Clear invalid input
            }
        }
    }
}