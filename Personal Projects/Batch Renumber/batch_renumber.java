import java.util.*;
import java.io.*;

public class batch_renumber{
   FileFilter filter{
      public boolean accept(File file) {
         if (file.getName().equals(".DS_Store"){
               file.delete();
         }
         else if(file.isFile()){
            return file.isFile();
         }
         return file.isDirectory();
      }
   };

   public static void main(String[] args){
      try{
         boolean done = false;
         while (done == false)
         {
            int n = 1;
            Scanner sc = new Scanner(System.in);
            System.out.println("Is this a subpath in Photography? [Y || N]");
            String answer = sc.nextLine();

            while(!answer.equalsIgnoreCase("Y") || !answer.equalsIgnoreCase("N"))
            {
               System.out.println("That isn't a valid input. Try again");
               System.out.print("Is this a subpath in Photography? [Y || N]");
               String answer = sc.nextLine();
            }

            String path = parent_child(answer);
            File file = new File(getFile() + path);

         }
      }
   }

  private static String parent_child(String answer){
     Scanner sc = new Scanner(System.in);
     StringBuilder sb = new StringBuilder();
     String path;

     if (answer.equalsIgnoreCase("Y"))


  }

  private static void listFiles(String path){
     File f = new File("/User/michaelkennedy/Pictures/Photography/"+path);
     File[] arrList = path.listFiles(fileFilter);
     
  }
  
   private static String getDir(){  
      return "/Users/michaelkennedy/Pictures/Photography";
   }

