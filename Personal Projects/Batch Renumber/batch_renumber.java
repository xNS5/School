
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class batch_renumber {
   FileFilter filter{
               public boolean accept(File file) {
                  if (file.getName().equals(".DS_Store")) {
                     file.delete();
                  }
                  else if(file.isFile())
                  {
                     return file.isFile();
                  }
                  return file.isDirectory();
               }
            };

   public static void main(String[] arrstring) {
      try {
         boolean done = false;
         while (done == false) {
            int n = 1;
            Scanner sc = new Scanner(System.in);
            System.out.print("Is this a subpath in Photography? [Y || N]: ");
            String answer= sc.nextLine();

            while (!answer.equals("Y") && !answer.equals("N")) 
            {
               System.out.println("That isn't a valid input. Try again");
               System.out.print("Is this a subpath in Pictures? [Y || N]: ");
               answer = sc.nextLine();
            }

            String path = parent_child(answer);
            File file = new File("/Users/michaelkennedy/Pictures/Photography/" + path + "/");
            File[] fileList = file.listFiles(filter);
            Arrays.sort(fileList);

            if (fileList.length == 0) 
            {
               System.out.println();
               System.out.println("This directory contains zero files. Please try again.");
            }

            for (int i = 0; i < fileList.length; ++i) 
            {
               File oldFile = fileList[i];
               File newFile= new File("/Users/michaelkennedy/Pictures/Photography/" + path + "/img_" + n + ".jpg");
               n++;
               if (oldFile.getName().equals(newFile().getName())) {
                  System.out.println("***This Folder has already been converted***");
                  break;
               }
               else if (oldFile.renameTo(newFile)) {
                  System.out.println(oldFile.getName() + " ---> " + newFile.getName() + "     Done");
                  continue;
               }
               System.out.println(oldFile.getName() + " ---> " + newFile.getName() + "     Conversion Failed");
            }

            System.out.println();
            System.out.print("Would you like to convert another folder? [Y || N]: ");
            answer = sc.nextLine();
            if (answer.equals("Y")) 
            {
               continue;
            }
            System.exit(0);
         }
      }
      catch (Exception e) {
         System.out.println("Something happened while trying to parse your responses. Please try again.");
         e.printStackTrace();
         System.exit(-1);
      }
   }

   private static String parent_child(String answer) {
      
      Scanner sc = new Scanner(System.in);
      StringBuilder sb = new StringBuilder();

         if (answer.equals("Y"))
            File fileParent = new File("/Users/michaelkennedy/Pictures/Photography");
            File[] fileList_1 = file.listFiles(filter);

            for (File f  : fileParent) {
               System.out.print(" | " + f.getName());
            }

            System.out.println(" | ");
            System.out.print("Parent Name: ");
            String parent= sc.nextLine();

            while (!verify(parent)) {
               System.out.println("This isn't a valid directory. Try again");
               System.out.println();
               System.out.print("Parent Name: ");
               parent = sc.nextLine();
            }

            File fileChild = new File("/Users/michaelkennedy/Pictures/Photography/" + parent);
            File[] fileList_2  = fileChild.listFiles(filter);
            Arrays.sort(fileList_2);

            for (File f : fileList_2) {
               System.out.print(" | " + f.getName());
            }

            System.out.println(" | ");
            System.out.print("Child Name: ");
            String child = sc.nextLine();

            while (!verify(parent + "/" + child) {
               System.out.println("This isn't a valid directory. Try again");
               System.out.println();
               System.out.print("Child's Name: ");
               child = sc.nextLine();
            }
            System.out.println();
            object = (String)object3 + "/" + (String)var10_21;

         } else if (string.equals("N")) {
            System.out.println("Please note that selecting this means that this is a folder within /Pictures/Photography");
            File file = new File("/Users/michaelkennedy/Pictures/Photography");
            Object[] arrobject = file.listFiles();
            Arrays.sort(arrobject);
            for (Object object3 : arrobject) {
               if (object3.getName().equals(".DS_Store")) continue;
               System.out.print(" | " + object3.getName());
            }
            System.out.print(" | ");
            System.out.println();
            System.out.print("Folder Name: ");
            Object object6 = scanner.nextLine();
            File file3 = new File("/Users/michaelkennedy/Pictures/Photography/" + (String)object6);
            while (!file3.isDirectory()) {
               System.out.println("This isn't a valid directory. Try again");
               System.out.println();
               System.out.print("Folder Name: ");
               object6 = scanner.nextLine();
            }
            object = object6;
         }
      return object;
   }

   private static boolean verify(String string) {
      File file = new File("/Users/michaelkennedy/Pictures/Photography/" + string);
      return file.exists();
   }
   
   private static getDir()
      {
         return 
      }
}
