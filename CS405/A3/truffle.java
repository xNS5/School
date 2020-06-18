/*
 * Author: Michael Kennedy
 * Class: CSCI 405
 * This algorithm is written using the saddleback search algorithm as a basis.
 * It first finds the maximum value in array 0, then moves to find the next max values for levels 0...n.
 * If the value is at index n-1 or 0, it just checks the available indexes. If the value is not at n-1 or 0, it checks
 * index [i+1][j-1], [i+][j], [i+1][j+1] to locate the maximum value.
 *
 * This algorithm is most like the saddleback search algorithm, so the overal running time of this algorithm is O(m+n) for a mxn array.
 * */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class truffle {
    public static void main(String[] args) {
        // Looking for textfile
        if (args.length == 0) {
            System.out.println("Incorrect number of arguments");
            System.exit(-1);
        } else {
            try {
                int[][] input = parseFile(args[0]);
                ArrayList<int[]> index = new ArrayList<>();
                int one, two, three, sum = 0, maxVal = Integer.MIN_VALUE, width = input[0].length, height = input.length;

                // Locating the max value in column 0
                for (int i = width - 1; i >= 0; i--) {
                    if (input[0][i] > maxVal) {
                        maxVal = input[0][i];
                        sum = maxVal;
                        if (index.size() == 0) {
                            index.add(new int[]{0, i});
                        } else {
                            index.set(0, new int[]{0, i});
                        }
                    }
                }

                int i = 0, j = index.get(0)[1];
                //Checks for Case 4: If the array is a mx1 array.
                // Case 5 will pass the first if statment but fail in the while loop, moving onto printing the values.
                if(width != 1){
                while (i < height-1) {
                        // Case 1: max value is at the end of the row
                        if (j == width - 1) {
                            one = input[i + 1][j - 1];
                            two = input[i + 1][j];
                            if (one > two || one == two) {
                                i += 1;
                                j -= 1;
                            } else if (two > one) {
                                i += 1;
                            }
                            sum += input[i][j];
                            index.add(new int[]{i, j});
                        // Case 2: max value is at the beginning of the row
                        } else if (j == 0) {
                            two = input[i + 1][j];
                            three = input[i + 1][j + 1];
                            if (two > three) {
                                i += 1;
                            } else if (two == three || three > two) {
                                i += 1;
                                j += 1;
                            }
                            sum += input[i][j];
                            index.add(new int[]{i, j});
                        // Case 3: max value is somewhere between 0 and len-1.
                        } else {
                            one = input[i + 1][j - 1];
                            two = input[i + 1][j];
                            three = input[i + 1][j + 1];
                            if (one > two && one > three) {
                                i += 1;
                                j -= 1;
                            } else if ((two > one && two > three) || (one == two && one > three) || (two == three && two > one)) {
                                i += 1;
                            } else if ((three > one && three > two) || (one == three && one > two) || (one == two && one == three)) {
                                i += 1;
                                j += 1;
                            }
                            sum += input[i][j];
                            index.add(new int[]{i, j});
                        }
                    }
                // Case 4: Input is an mx1 array. input starts at 1 because index 0 contains the max value index, the starting point.
                } else {
                    for(int x = 1; x < input.length; x++){
                        sum+=input[x][0];
                        index.add(new int[]{x,0});
                    }
                }

                for (int[] arr : index) {
                    int x = arr[0], y = arr[1], val = input[x][y];
                    System.out.printf("[%d,%d] = %d\r\n", x, y, val);
                }

                System.out.printf("%d total truffles in this path!\r\n", sum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Parses input file
     * */
    private static int[][] parseFile(String fileName) throws FileNotFoundException, NumberFormatException, ArrayIndexOutOfBoundsException{
        File inputFile = new File(fileName);
        Scanner sc = new Scanner(inputFile);
        ArrayList<String> tempStr = new ArrayList<>();
        while (sc.hasNextLine()) {
            tempStr.add(sc.nextLine());
        }
        int width = tempStr.get(0).split("\\t").length, height = tempStr.size();
        int[][] parsed = new int[height][width];
        for (int i = 0; i < height; i++) {
            String[] arr = tempStr.get(i).split("\\t");
            for(int j = 0; j < width; j++){
                parsed[i][j] = Integer.parseInt(arr[j]);
            }
        }
        return parsed;
    }
}