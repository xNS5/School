import java.util.*;
import java.lang.*;

public class test{
    public static void main(String[] args)
    {
        System.out.println(myAtoi("42"));
    }
    public static int myAtoi(String str) {
        List<Integer> arr_list = new ArrayList<Integer>();
        char[] arr = str.toCharArray();
        boolean neg = false;
        int result = 0;
        for(int i = 0; i < arr.length; i++)
        {
            int num_c = arr[i];
            if(num_c == 45)
            {
                neg = true;
            }

            if(num_c >= 47 || num_c <= 57)
            {
                arr_list.add(Character.getNumericValue(arr[i]));
            }
        }

        if(arr_list.size() > 8)
        {
            return (neg == true) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }

        for(int i = 0; i < arr_list.size(); i++)
        {
            result += (arr_list.get(i) * (10 * i));
        }

        return result;
    }
}