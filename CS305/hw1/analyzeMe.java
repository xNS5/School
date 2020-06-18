import java.lang.NumberFormatException;
import java.lang.Math;
import java.util.Random;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;

public class analyzeMe {
    public static void main(String[] args) {
        String argType;
        int key = 0, arrLength = 0, numMixed = 0, swaps;
        float degreeSorted; // Needs to be a float because it's the percent of numbers that are mixed
        // I opted for a try...catch block because even though I was supposed to assume that the user would input correctly
        // formatted arguments, I still wanted to add some sort of error handling/checking.
        try {
            String inputArgs[] = args[0].split("=");
            if (args[0].length() > 2 && args[0].contains("=")) {
                argType = inputArgs[0];
                key = Integer.parseInt(inputArgs[1]);
            } else {
                argType = inputArgs[0];
            }

            arrLength = Integer.parseInt(args[1]); // Length of the array
            degreeSorted = (1 - (Float.parseFloat(args[2]) / 100)); // Percent of inputs to be unsorted/mixed
            numMixed = (int)(degreeSorted * arrLength); // Number of indexes to be unsorted/mixed casted as an int
            int[] sortedArr = insertionSort(arrayGenerator(arrLength));
            int[] sortedCopy = Arrays.copyOfRange(sortedArr, 1, sortedArr.length-1);
            swaps = sortedArr[0];

            if (argType.equals("IS")) {

                if (degreeSorted == 0) { // 100% sorted -> 0% mixed
                    isOutput("A Initial: ", "A Final: ", sortedCopy, sortedCopy, 0);
                } else if (degreeSorted == 1) { // 0% sorted -> 100% unsorted -> reversed array
                    isOutput("A Initial: ", "A Final: ", sortedCopy, arrayInverter(sortedCopy), (arrLength * (arrLength - 1)) / 2 );
                } else { // Greater than zero, less than 100% sorted
                    int[] mixedArr = arrayMixer(sortedCopy, numMixed);
                    isOutput("A Initial: ", "A Final: ", sortedCopy, Arrays.copyOfRange(mixedArr, 1, mixedArr.length), swaps-(insertionSort(mixedArr)[0]));
                }
            } else if (argType.equals("LS")) {
                if (degreeSorted == 0) {
                    lsOutput(sortedArr, linearSearch(sortedCopy, key));
                } else if (degreeSorted == 1) {
                    int[] reversedArr = arrayInverter(sortedCopy);
                    lsOutput(reversedArr, linearSearch(reversedArr, key));
                } else {
                    int[] mixedArr = Arrays.copyOfRange(arrayMixer(sortedCopy, numMixed), 1, sortedArr.length);
                    lsOutput(mixedArr, linearSearch(mixedArr, key));
                }
            }
        } catch (Exception e) {
            System.out.println(e + ". Please check your input and try again");
            System.exit(1);
        }
    }

    /*
    *  This function generates an array populated with random integers from 1 to 2*(array length)
    *  if the value is already in the array, it re-generates a new value.
    */
   public static int[] arrayGenerator(int length) {
        HashSet<Integer> valSet = new HashSet<Integer>();
        int[] arr = new int[length];
        Random rand = new Random();

        for (int i = 0; i < length; i++) {
            int randVal = rand.nextInt(2 * length);
            while (!valSet.add(randVal)) {
                randVal = rand.nextInt(2 * length);
            }
            arr[i] = randVal;
        }

        return arr;
    }
    // This function inverts a pre-sorted array
    public static int[] arrayInverter(int[] arr) {
        int length = arr.length;
        int[] arrInv = new int[length];
        for (int i = 0, j = length - 1; i < length; i++, j--) {
            arrInv[j] = arr[i];
        }
        return arrInv;
    }
    /*
    * This function takes a pre-sorted array, randomly selects n indexes, and changes the value at that index.
    * I had to use two HashSets for this instead of a HashMap because I need to know which indexes the program has already visited
    * and the values already in the array to avoid adding duplicates.
    */
    public static int[] arrayMixer(int[] arr, int num) {
        int counter = 0, length = arr.length, randVal = 0, randIndex = 0, mixedSwaps = 0;
        int[] mixed = new int[length + 1], arrCopy = Arrays.copyOfRange(arr, 0, length);
        HashSet<Integer> indexSet = new HashSet<>(), valueSet = new HashSet<>();
        Random rand = new Random();

        for (int i : arr) {
            valueSet.add(i);
        }

        for (int i = 0; i < num; i++) {
            randIndex = rand.nextInt(length);
            randVal = rand.nextInt(2 * length); // I changed randVal to be 5 * length for a wider range of values
            // If the generated index has been visited, re-generates another index.
            while (!indexSet.add(randIndex)) {
                randIndex = rand.nextInt(length);
            }
            // If the newly generated value is already in arrCopy, re-generates another value.
            while (!valueSet.add(randVal)) {
                randVal = rand.nextInt(2 * length);
            }

            mixedSwaps += (randIndex - 1); //Counting the number of times the value at randIndex would have needed to be swapped
            arrCopy[randIndex] = randVal;
        }
        mixed[0] = mixedSwaps; // Setting the value at 0 to the number of swaps to be subtracted from the overall number of swaps
        for (int i = 1; i < length + 1; i++) {
            mixed[i] = arrCopy[i - 1];
        }
        return mixed;
    }
    // Prints out arrays
    public static void arrayPrint(String message, int[] arr) {
        System.out.print(message);
        for (int i : arr) {
            System.out.printf("%d ", i);
        }
        System.out.println();
    }
    public static int linearSearch(int[] arr, int key) {
        int length = arr.length, counter = 0;
        for (int i = 0; i < length; i++) {
            if(arr[i] == key){
                return counter;
            }
            counter += 1;
        }
        return counter;
    }
    // Insertion sort algorithm. I didn't do any modifications to it.
    public static int[] insertionSort(int[] arr) {
        int[] arrCopy = new int[arr.length+1];
        int counter=0;
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j -= 1;
                counter+=1;
            }
            arr[j + 1] = key;
        }
        arrCopy[0] = counter;
        for(int i = 1; i < arr.length; i++){
            arrCopy[i] = arr[i-1];
        }
        return arrCopy;
    }

    //Insertion Sort Output. It takes the Initial and Final arrays and prints them to the console.
    public static void isOutput(String message1, String message2, int[] arr1, int[] arr2 , int swaps) {
        if (arr1.length < 20 && arr2.length < 20) {
            arrayPrint(message1, arr1);
            arrayPrint(message2, arr2);
            System.out.printf("Num Swaps: %d\r\n", swaps);
        } else {
            System.out.printf("Num Swaps: %d\r\n", swaps);
        }
    }
    // Linear Search Output. Does the same thing for Insertion Sort Output but for the Linear Search argument.
    public static void lsOutput(int[] arr, int counter) {
        if (arr.length < 20) {
            arrayPrint("A: ", arr);
            String result = counter == arr.length ? "Entries Searched: %d, not found\r\n" : "Entries Searched: %d\r\n";
            System.out.printf(result, counter);
        } else {
            String result = counter == arr.length ? "Entries Searched: %d, not found\r\n" : "Entries Searched: %d\r\n";
            System.out.printf(result, counter);
        }
    }
}