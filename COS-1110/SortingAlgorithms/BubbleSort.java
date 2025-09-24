package SortingAlgorithms;

public class BubbleSort {
    void sort(int arr[]) {
        int n = arr.length;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }
    void printArray(int arr[]) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide integers as command-line arguments.");
            return;
        }

        int[] arr = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            try {
                arr[i] = Integer.parseInt(args[i]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + args[i] + " is not an integer.");
                return;
            }
        }
        BubbleSort bubbleSort = new BubbleSort();
        System.out.println("Unsorted array:");
        bubbleSort.printArray(arr);
        bubbleSort.sort(arr);
        System.out.println("Sorted array:");
        bubbleSort.printArray(arr);
    }
}