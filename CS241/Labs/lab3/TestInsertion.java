import java.util.*;
/*
* Author: Michael Kennedy
* Assignment: Lab 3
* Description: This program tests the InsertionSort class. If the program is run from the command line without any integers after, it jumps to the
* "run from command prompt" method, which allows the user to custom imput their own lists. If the user inputted-- for example-- the number 10, the program
* would generate a list from 0 to 9, scramble the order, re-sort it, then compare it against the original list.
*
*/

public class TestInsertion
  {
    /*
    * Main method. Please note that all of the commented out arrays are just for testing
    * individual methods. I'm keeping them for the final result in order to test that each method works.
    */
    public static void main(String[] args)
      {
        // int[] array1 = {4, 1, 6, 3, 3, 2};
        // int[] array2 = {2, 4, 1, 6, 3, 3};
        // int[] array3 = {1, 1, 1, 3, 2, 5, 9};
        // int[] array4 = {1, 1, 1, 3, 6, 5, 9};
        // InsertionSort i_s = new InsertionSort();

        /*
        * Sorting all 4 test arrays
        */
        // int[] array_1 = i_s.insertionSort(array1);
        // int[] array_2 = i_s.insertionSort(array2);
        // int[] array_3 = i_s.insertionSort(array3);
        // int[] array_4 = i_s.insertionSort(array4);


        /*
        * Testing both the sameElements and isSorted method. Un-comment the arrays/sorted arrays to test.
        */
        // System.out.println(isSorted(array_1));
        // System.out.println(sameElements(array3, array4));

        try                                                                     // Using a try-catch blocks in case the user inputs something that isn't an integer.
          {
            if(args.length != 0)                                                //  If the length of the user input is 0-- meaning no input, shuffletest gets called.
              {
                for(String s : args)                                            // From the console, each value inputted gets passed to shuffle test with a for loop.
                  {
                  int n = Integer.parseInt(s);                                  // Converts the user input into integer values
                    shuffleTest(n);
                  }
              }
            else
              {
                testFromConsole();                                              // If args.length == 0, test from console will be called
              }
            }
          catch(NumberFormatException e)                                        // If the user were to input an invalid input, a character or a non-integer symbol, the program will print out an error message
            {
              System.out.println("ERROR User input contains invalid characters. Please try again." );
            }


      }

/*
* Checks for whether the test arrays are sorted.
* In this case, it iterates through the array to see if the next element is greater
* than the current. If it does encounter a value that is greater than the current,
* the method returns false.
*/
    private static boolean isSorted(int[] array)
      {
        for(int i = 0; i < array.length-1; i++)
          {
            if (array[i] > array[i+1])
              {
                return false;
              }
        }
        return true;
      }
  /*
  * This method takes in two arrays to determine if they contain the same number of elements.
  * Uses two hash tables to determine if there are the same counts of numbers in the arrays.
  * If there are multiple occurances of integers in one of the hashmaps, the count will increase by 1.
  */
    private static boolean sameElements(int[] array1, int[] array2)
      {
        HashMap<Integer, Integer> map1 = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> map2 = new HashMap<Integer, Integer>();

        for(Integer number : array1)                                            // Using a for-each loop to extract the integers stored in the array and put them in map1.
          {
            Integer count = map1.get(number);                                   // Getting the value of what the key "number" can access.
            map1.put(number, (count == null) ? 1 : count +1);                   // Using the ? operator to increase the count of numbers in the hashmap.
          }

        for(Integer number : array2)                                            // Using a for-each loop to extract the integers stored in the array and then put them in map2.
          {
            Integer count = map2.get(number);                                   // Getting the value of what the key "number" can access.
            map2.put(number, (count == null) ? 1 : count +1);                   // Using the ? operator to increase the count of numbers in the hashmap.
          }

        for(int key : map1.keySet())                                            // For-each loop getting the keys from the map1 keyset.
          {
            if (map1.get(key) != map2.get(key)) // If the maps don't have the same values or keys, this method returns false.
              {
                return false;
              }
            }
          return true;
        }
/*
* Void method that allows the user to test the InsertionSort from the console.
* Note: my arrays are named arr_[state], which are essentially array_sorted, array_integers, etc.
*/
private static void testFromConsole()
  {
    while(true)                                                                 // In order for this method to loop, all I had to do was do while(true)
    {
      InsertionSort i_s = new InsertionSort();                                  // Creating an instance of the InsertionSort class
      Scanner sc = new Scanner(System.in);                                      // Creating a scanner
      System.out.print("String to parse: ");
      String input = sc.next();                                                 // User inputs the values from the console.
      try                                                                       // If the user were to input a non-integer value, the catch will state that the user made an error
        {
          String[] arr_strings = input.split("");                               // Splitting the string in between the characters, and making an array of Strings
          int[] arr_ints = new int[arr_strings.length];                         // Creating an array to store the integers
          int[] arr_initial = new int[arr_strings.length];                      // In order to store the intial list, I had to create a new array
          for(int i = 0; i < arr_strings.length; i++)                           // For loop to add all of the integers to the two arrays
            {
              arr_ints[i] = Integer.parseInt(arr_strings[i]);                   // Converting the values from the String array to integers
              arr_initial[i] = Integer.parseInt(arr_strings[i]);
            }

            int[] arr_sorted = i_s.insertionSort(arr_ints);                     // Assigning the sorted array to the variable arr_sorted
            if(isSorted(arr_sorted))
              {
                System.out.println("Array successfully sorted");                // If the array is sorted, the program will say so.
              }
            if(sameElements(arr_initial, arr_sorted))                           // If the sorted array has the same values of the intial array, it would have passed the test
              {
                System.out.println("Passed Test");
              }
            else
              {
                System.out.println("FAILED Test");
              }
        }
        catch(NumberFormatException e)                                          // If the user were to input anything other than an int, the program will catch the exception
          {
            System.out.println("ERROR. User input contains invalid characters. Please try again." );
          }
    }
  }

/*
* Creates an array of N numbers long, shuffles it, then sorts it.
* Note: This method also works for large numbers within range N.
* Un-comment the commented sections to test this. I added spaces in between each output
* to test this.
*/
    private static void shuffleTest(int n)                                      // Method that takes in an int n.
      {
        System.out.println("Creating an array of length: " + n);
        int[] storage = new int[n];                                             // Creating an array of size n.
        int[] shuffled = new int[n];                                            // Creating another arrray of size n.
        InsertionSort i_s = new InsertionSort();                                // Creating an instance of the InsertionSort class.
        Random rand = new Random();

        System.out.print("Original list: ");
        for(int i = 0; i < n; i++)                                              // This loop prints out the initial pre-sorted list.
          {
            storage[i] = i;                                                     // Assigns the value of i to a location within the storage array
            shuffled[i] = i;                                                    // Assigns the value of i to a location within the shuffled array
            System.out.print(storage[i]);
          }
        System.out.println();

        System.out.print("Shuffled list: ");                                    // Shuffling List.
        for(int i = shuffled.length-1; i > 0; i--)                              // Starting from the end of the list and iterating backwards.
          {
            int t = rand.nextInt(shuffled.length);                              // Generating a random integer between 0 to N, which will be the index to swap.
            int temp = shuffled[i];                                             // Creating a "storage" variable for the contents of shuffled at location i
            shuffled[i] = shuffled[t];                                          // Swapping shuffled at location i with location t
            shuffled[t] = temp;                                                 // Assigning the value of temp to location t
          }
        for(int v : shuffled)                                                   // Added this extra for loop to print out the shuffled loop. Wasn't printing out properly, only noticed after my last commit 
          {
            System.out.print(v);
          }
          System.out.println();

        int[] sorted = i_s.insertionSort(shuffled);                              // Insertion sorting the array

        System.out.print("Sorted List: ");
        for(int num : sorted)                                                    // Prints out the sorted array
          {
            System.out.print(num);
          }
        System.out.println(sorted.length);

        if(Arrays.equals(sorted, storage))                                      // Checks for whether the arrays are equal
          {
            System.out.println("Passed Test");
          }
        else
          {
            System.out.println("FAILED Test");
          }
        System.out.println();
      }
}
