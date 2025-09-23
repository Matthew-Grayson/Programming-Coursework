/**
  This program finds and prints all perfect numbers up to the the limit entered by the user.
  A perfect number is a positive integer that is equal to the sum of its divisors.
  For example, 6 is a perfect number because its divisors (1, 2, 3) sum to 6.
 */
import java.util.Scanner;

public class PerfectNumbers {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the upper limit you would like to search find perfect numbers: ");
        long limit = scanner.nextLong();

        System.out.println("Perfect numbers up to " + limit + ":");
        for (long num = 1; num <= limit; num++) {
            if (isPerfect(num)) {
                System.out.println(num);
            }
        }
    }

    // Function to check if a number is perfect
    public static boolean isPerfect(long number) {
        long sum = 0;
        // Find all divisors and sum them up
        for (long i = 1; i <= number / 2; i++) {
            if (number % i == 0) {
                sum += i;
            }
        }
        // A number is perfect if the sum of its divisors equals the number itself
        return sum == number && number != 0;
    }
}