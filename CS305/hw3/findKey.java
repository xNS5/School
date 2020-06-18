import java.text.DecimalFormat;

public class findKey{
    public static void main(String[] args) {
        int key = Integer.parseInt(args[0]), length = 0, index = 1;

        /*
        * The input is separated by commas, so I'm removing all of the commas
        * */
        for (int i = 1; i < args.length; i++) {
            length += 1;
            if (args[i].contains(",")) {
                length = 0;
                args[i] = args[i].replace(",", "");
            }
        }

        int[][] arr = new int[length][length];

        /*
        * Adding each value to a 2D array
        * */
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if(args[index].equals("I")){
                    arr[i][j] = Integer.MAX_VALUE; // Otherwise I'd have to create an array of doubles.
                } else {
                    arr[i][j] = Integer.parseInt(args[index]);
                }
                index++; // Increasing index of args[]
            }
        }

        /*
        * Printing out the values in the 2D array
        * */
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if(arr[i][j] == Integer.MAX_VALUE){
                    System.out.print("I ");
                    continue;
                }
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
        /*
        * keyFinder returns a 2 length array containing the indexes where the matching key is.
        * */
        int[] indexes = keyFinder(arr, length, key);
        if(indexes[0] < 0){
            System.out.println("Not found");
        } else {
            System.out.printf("Found at: %d, %d\r\n", indexes[0], indexes[1]);
        }
    }

    /*
    * KeyFinder finds the corresponding value to the key in the array
    * */
    public static int[] keyFinder(int[][] arr, int length, int key) {
        int row = 0, col = length-1, count = 0; // Starting at the last index at the first array
        System.out.println("Looking at:");
        while (row < length  && col >= 0){
            count+=1;
            if (arr[row][col] == key) { //If the value matches the key, it returns an array containing the indexes
                System.out.printf("Looked at: %d indexes\r\n", count);
                return new int[]{row, col};
            }
            if (arr[row][col] < key) { // If the value is less than the key, it drops down another row
                row++;
            } else { //If the value is greater than the key, it moves down a column
                col--;
            }
        }
        System.out.printf("Looked at: %d indexes\r\n", count);
        return new int[] {-1, -1}; // If the value matching the key isn't found, it returns an array with -1 in both indexes.
    }
}