package Recursion;

import java.util.Scanner;

public class Fibonacci {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = readInt(scanner, "Enter a non-negative integer, n, to compute nth Fibonacci number: ");
        System.out.println("Fibonacci number at position " + n + " is: " + n);
    }

    public static long listNthFibonacci(int n) {
        if (n <= 1) {
            return n;
        } else {
            return listNthFibonacci(n - 1) + listNthFibonacci(n - 2);
        }
    }

    public static long readInt(Scanner scanner, String prompt) {
        int n;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                n = scanner.nextInt();
                if (n >= 0 && n <= 92) { // Limiting to 92 to prevent overflow
                    return listNthFibonacci(n);
                } else {
                    System.out.println("Please enter a non-negative integer <= 92.");
                }
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Clear invalid input
            }
        }
    }
}