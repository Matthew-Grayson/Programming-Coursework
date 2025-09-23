package SortingAlgorithms;
public class SelectionSort {
    void sort(int arr[]) {
        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min])
                    min = j;
            }
            int temp = arr[min];
            arr[min] = arr[i];
            arr[i] = temp;
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

        SelectionSort selectionSort = new SelectionSort();
        System.out.println("Unsorted array:");
        selectionSort.printArray(arr);

        selectionSort.sort(arr);
        System.out.println("Sorted array:");
        selectionSort.printArray(arr);
    }
}