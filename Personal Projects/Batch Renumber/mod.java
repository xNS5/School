public class mod{

   public static File[] sort(File[] unsorted){
      int arrLength = unsorted.length;
      File[] sorted = new File[arrLength];
      String temp;
    
      for(int i = 0; i < arrLength; i++){i
         String name = unsorted[i].getName();
         StringBuilder sb = new StringBuilder();
         int nameLength = name.length();
         for(int j = 0; j < nameLength; j++){
            char t = name.charAt(j);
            if(Integer.parseInt(t))
                  sb.append(t);
            else if(t.equals('.');
                  int index = Integer.parseInt(sb.toString());
                  sorted[index] = unsorted[index];
                  break;
            else
               continue;
         }
      }
            

         
