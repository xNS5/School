public class InsertionSort
  {
    public static int[] insertionSort(int[] array)
      {
        int len = array.length;
        for(int i = 0; i < len; i++)
          {
            int number = array[i];
            int j = i-1;
            while(j >=0 && array[j] > number)
              {
                array[j+1] = array[j];
                j = j-1;
                array[j+1] = number;
              }
          }
          return array;
      }
  }
