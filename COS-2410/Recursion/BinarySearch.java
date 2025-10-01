package Recursion;

import java.util.Scanner;

public class BinarySearch{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int target = readInt(scanner, "Enter a target integer to search for in the array: ");
        int[] arr = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41, 43, 45, 47, 49};
        int result = binarySearch(arr, 0, arr.length - 1, target);
        if (result != -1) {
            System.out.println("Element found at index: " + result);
        } else {
            System.out.println("Element not found in the array.");
        }

    }
    public static int binarySearch (int[] arr, int left, int right, int target) {
        while (left <= right) {
            int mid = left + (right - left) / 2; // prevent integer overflow vs (left + right) / 2
            if (target < arr[mid]) {
                return binarySearch(arr, left, mid - 1, target);
            }
            if (target > arr[mid]) {
                return binarySearch(arr, mid + 1, right, target);
            } else {
                return mid; // target found
            }
        }
        return -1; // target not found
    }

    public static int readInt(Scanner scanner, String prompt) {
        int n;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                n = scanner.nextInt();
                return n;
            } else {
                System.out.println("Invalid input. Please enter a postive integer.");
                scanner.next(); // Clear invalid input
            }
        }
    }
}