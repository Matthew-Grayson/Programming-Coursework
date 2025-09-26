/**
 * Methods for finding and printing perfect numbers.
 *
 * A perfect number is a positive integer that equals the sum of its proper divisors.
 * For example, 6 is a perfect number because its proper divisors (1, 2, 3) add up to 6.
 *
 * Complexity:
 *   Time complexity O(n^2) for naive approach, O(n * sqrt(n)) for optimized approach.
 *   Space complexity O(1).
 *
 * References (APA):
 * Ochem, P., & Rao, M. (2012). Odd perfect numbers are greater than 10^1500.
 *   Mathematics of Computation, 81(279), 1869–1877.
 *   https://www.ams.org/journals/mcom/2012-81-279/S0025-5718-2012-02563-4/S0025-5718-2012-02563-4.pdf
 * Oracle. (n.d.). java.util (Java Platform SE 8). In Java Platform, Standard Edition 8 API Specification.
 *   Retrieved September 26, 2025, from https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html
 */


import java.util.Scanner;
import java.util.InputMismatchException;

public class PerfectNumbers {
    public static void main(String[] args) {
        /**
         * Prints perfect numbers between 1 and 200 then prompts user for a new upper limit.
         *
         * Uses naive algorithm that checks all divisors up to each number.
         *
         */
        int limit = 200;
        System.out.println("Perfect numbers up to " + limit + ":");
        // naive approach checking all divisors up to num
        for (int num = 2; num <= limit; num++) {
            int sum = 1;
            for (int div = 2; div <= num; div++) {
                if (num % div == 0){
                    sum += div;
                };
            }
            if (sum == num){
                System.out.println(num);
            };
        }
        findPerfectNumbers();
    }

    static void findPerfectNumbers() {
        /** Prints perfect numbers up to a user-defined limit.
         *
         * Uses an optimized algorithm that adds divisors in complementary pairs
         * reducing the search range requirement to the square root of each number;
         * skips odd numbers since the theoretical lower bound of an odd perfect number is > 10^1500.
         */

        // prompt user for limit and validate input
        Scanner scanner = new Scanner(System.in);
        long limit = -1;
        while (limit < 2) {
            try {
                System.out.print("Enter a numeric limit to print all perfect numbers up to that limit: ");
                limit = scanner.nextLong();
                if (limit < 2) {
                    System.out.println("Please enter an integer greater than or equal to 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive integer <= 9,223,372,036,854,775,807.");
                scanner.next(); // Clear invalid input
            }
        }
        System.out.println("Perfect numbers up to " + limit + ":");
        // optimized approach checking divisors up to sqrt(num) and skipping odd numbers
        for (long num = 2; num <= limit; num+=2) {
            long sum = 1;
            long squareRoot = (long)Math.sqrt(num);
            for (long div = 2; div <= squareRoot; div++) {
                if (num % div == 0) {
                    long quo = num / div;
                    sum += div;
                    // add paired divisor if not duplicate (as with a perfect square)
                    if (quo != div) {
                        sum += quo;
                    }
                    // exit early if sum exceeds num
                    if (sum > num) break;
                }
            }
            if (sum == num) {
                System.out.println(num);
            }
        }
        System.out.println("Done.");
    }
}

