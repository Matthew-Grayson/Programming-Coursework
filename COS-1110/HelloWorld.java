/*
    A simple Java program that prints "Hello, World" to the terminal.
 */
public class HelloWorld {

    public static void main(String[] args) {
        // Declare a byte variable with an integer < 128
        byte smallNumber = 5;

        // Countdown from the byte variable to 1 with a 1 second delay between each number
        for (int i = 0; i < smallNumber; i++) {
            System.out.print(smallNumber - i);
            // Try-catch block to handle  InterruptedException
            try {
                // Delay for 1 second and display a dot for each third of a second
                System.out.print(".");
                Thread.sleep(333);
                System.out.print(".");
                Thread.sleep(333);
                System.out.print(".");
                Thread.sleep(334);
                System.out.println("");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Prints "Hello, World" in the terminal.
        System.out.println("Hello, World");
    }

}