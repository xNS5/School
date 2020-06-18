import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

/*
* Author: Michael Kennedy
* CSCI 405
*  This is a dynamic programming problem with the goal of reaching the final castle at distance
* a_n while minimizing the total penalty which is the sum of daily penalties over all travel days.
* First on line 5 in the above algorithm it calculates the penalty for the value at index $i$.
* Then it checks the penalties for indexes 0...i. If one of those penalties is less than the original,
* that new value becomes the minimum penalty and gets marked as a stop in the trip.
 * */

public class wasteland{
    public static void main(String[] args){
        // Validating input length
        if(args.length == 0){
            System.out.println("Incorrect number of arguments");
            System.exit(-1);
        }
        try {
            // input is the text file numbers
            // numMiles is the number of miles travelled in a day
            // finalInput is a copy of input from index 1...n
            // penalty is an array to keep track of the penalties as it iterates through finalInput
            // stops is the locations of the castles one would stop at.
            int[] input = prep(args[0]);
            int numMiles = input[0];
            int[] finalInput = new int[input.length-1], penalty = new int[input.length-1], stops = new int[input.length-1];

            // Copying the input from input from indexes 1...n to finalInput at 0...n-1.
            System.arraycopy(input, 1, finalInput, 0, input.length - 1);

            // Iterating through the finalInput array to calculate the minimum penalty
            for(int i = 0; i < finalInput.length; i++){
                penalty[i] = (int) Math.pow((numMiles-finalInput[i]), 2);   // Calculating the penalty for index i
                stops[i] = 0;
                for(int j = 0; j < i; j++){
                    int var = (int)(penalty[j] + Math.pow((numMiles - (finalInput[i] - finalInput[j])),2)); // Calculating the cost of previous penalties
                    if(penalty[i] > var){ // If the cost of the penalty is greater than the cost of getting from index i to j, the penalty for i is changed.
                        penalty[i] = var;
                        stops[i] = j+1; // Adds j+1 to the stop counter.
                    }
                }
            }

            // Using a stack to reverse the indexes so they increment as opposed to decrement.
            Stack<Integer> st = new Stack<>();
            ArrayList<String> arr = new ArrayList<>();
            int index = stops.length-1;
            while(index>=0){
                st.push(index+1);
                index = stops[index]-1;
            }
            while(!st.empty()){
                arr.add(String.valueOf(st.pop()));
            }
            System.out.printf("Castles: %s\r\nPenalty: %d\r\n", Arrays.asList(arr.toArray()), penalty[finalInput.length - 1]);
            System.out.println(Arrays.toString(penalty));

        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    /*
    * Parsing the strings and returning a single dimension array.
    * */
    private static int[] prep(String args) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        File input = new File(args);
        Scanner sc = new Scanner(input);

        while(sc.hasNextLine()){
            sb.append(sc.nextLine()).append(" ");
        }

        String argsInput = sb.toString();
        String[] argsStrArray = argsInput.replaceAll("\\s", " ").split(" ");
        int[] argsNumsArr = new int[argsStrArray.length];
        for(int i = 0; i < argsStrArray.length; i++){
            argsNumsArr[i] = Integer.parseInt(argsStrArray[i]);
        }
        return argsNumsArr;
    }
}