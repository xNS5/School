import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.FileNotFoundException;
/*
* Programming Assignment 1
* CSCI 405
* Michael Kennedy
*
* This program is designed to create a skyline from data points representing rectangular building dimensions.
* It made the most sense to utilize a merge sort style algorithm to complete this program.
* The program reads the input data into a 2D array, then passes it to the 'Divide' function.
* Once the divide function has reached its base case, it moves on to the 'Combine' function. The combine function compares the
* left and right X's values, whichever one is smaller gets added to the result. Then it checks the two list's values to determine which is greater.
*
* Once the program has combined all of the buildings and skylines to the point where only two lists are left, it tacks the right list to the end of the left list
* because each X value in the left sublist is guaranteed to be less than the right sublist.
*
* The number of input buildings can vary, so I opted to use arraylists to keep track of the different buildings and skylines.
* */


public class skyline {
    public static void main(String[] args) {
//        if (args[0].length() == 0) {
//            System.out.println("Incorrect number of arguments");
//            System.exit(1);
//        } else {
        try {
            int[][] dimensions = prep("testfiles/Test-1.txt");
            for (List<Integer> integers : divide(dimensions, 0, dimensions.length - 1)) {
                System.out.print(integers.toString() + " ");
            }
        } catch (FileNotFoundException | StackOverflowError | InputMismatchException e) {
            System.out.print("Error: ");
            e.printStackTrace();
        }
    }
//    }

    /*
     *  Simply parsing the input from the text file.
     * */
    public static int[][] prep(String args) throws FileNotFoundException, InputMismatchException {
        File buildings = new File(args);
        Scanner sc = new Scanner(buildings);
        String preFixedString = sc.nextLine().replaceAll("><", " ").replaceAll("<", "").replaceAll(">", "");
        String[] preFixedArray = preFixedString.split(" ");

        int numBuildings = preFixedArray.length / 3, counter = 0;
        int[][] dimensions = new int[numBuildings][3];

        for (int i = 0; i < numBuildings; i++) {
            for (int j = 0; j < 3; j++, counter++) {
                dimensions[i][j] = Integer.parseInt(preFixedArray[counter]);
            }
        }
        return dimensions;
    }

    /*
     * The Divide step of my program.
     * This divides the input array into a list of lists, with the beginning and end in one sublist and the
     * height in another.
     * */

    public static List<List<Integer>> divide(int[][] dimensions, int start, int end) throws StackOverflowError {
        if (start == end) { // If the divide step has reached the base case.
            List<List<Integer>> result = new ArrayList<>();
            List<Integer> temp1 = new ArrayList<>(), temp2 = new ArrayList<>(); // Created these two temp arraylists to hold both X values and the Y value.
            temp1.add(dimensions[start][0]);
            temp1.add(dimensions[start][2]);
            temp2.add(dimensions[start][1]);
            temp2.add(0);
            result.add(temp1);
            result.add(temp2);
            return result;
        }
        int midpoint = start + (end - start) / 2;
        List<List<Integer>> leftList = divide(dimensions, start, midpoint);
        List<List<Integer>> rightList = divide(dimensions, midpoint + 1, end);
        return merge(rightList, leftList);
    }


    /*
     * counterL and R keep track of the number of times the left or right side had the smallest value. At the end,
     * all of those elements are added to the result arraylist.
     * */

    public static List<List<Integer>> merge(List<List<Integer>> leftList, List<List<Integer>> rightList) {
        int counterL = 0, counterR = 0;
        int leftY = 0, rightY = 0;
        int currentX, currentY = 0;
        List<List<Integer>> result = new ArrayList<>();
        while (counterR < rightList.size() && counterL < leftList.size()) {
            int leftListX = leftList.get(counterL).get(0), leftListY = leftList.get(counterL).get(1); // Gets the values of the left and right list's x and y values.
            int rightListX = rightList.get(counterR).get(0), rightListY = rightList.get(counterR).get(1);

            /*
             * If the left list's X is less than the right list's X, the left list's X and Y values get saved
             * and the index gets increased. The same thing happens if the right list's X is less than the left's.
             * */
            if (leftListX < rightListX) {
                leftY = leftListY;
                currentX = leftListX;
                counterL++;
            } else if (rightListX < leftListX) {
                rightY = rightListY;
                currentX = rightListX;
                counterR++;
            } else { // If both x values are equal, set leftListX to currentX and set both Y values.
                leftY = leftListY;
                rightY = rightListY;
                currentX = leftListX;
                counterL++;
                counterR++;
            }
            /*
             * This adds the highest Y value to the result array.
             * */
            if (currentY != Math.max(leftY, rightY)) {
                currentY = Math.max(leftY, rightY);
                List<Integer> x = new ArrayList<>();
                x.add(currentX);
                x.add(currentY);
                result.add(x);
            }

        }

        /*
         * This block of code combines buildings and skylines. The values with the
         * smallest X's and largest Y's get added in the while loop, then the rest of the
         * buildings from the left and right sublists get combined to the final result.
         * */
        if (counterL < leftList.size()) {
            for (int i = counterL; i < leftList.size(); i++) {
                List<Integer> temp = new ArrayList<>();
                temp.add(leftList.get(i).get(0));
                temp.add(leftList.get(i).get(1));
                result.add(temp);
            }
        }
        if (counterR < leftList.size()) {
            for (int i = counterR; i < rightList.size(); i++) {
                List<Integer> temp = new ArrayList<>();
                temp.add(rightList.get(i).get(0));
                temp.add(rightList.get(i).get(1));
                result.add(temp);
            }
        }
        return result;
    }
}
