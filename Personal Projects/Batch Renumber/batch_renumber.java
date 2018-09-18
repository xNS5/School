
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class batch_renumber {
    public static void main(String[] arrstring) {
        try {
            boolean done = false;
            while (done == false) {
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
                String path = parent_child(string);
                File file = new File("/Users/michaelkennedy/Pictures/Photography/" + path + "/");
                File[] fileList = file.listFiles(Filter);
                Arrays.sort(fileList);

                if (fileList.length == 0) 
                  {
                       System.out.println();
                       System.out.println("This directory contains zero files. Please try again.");
                  }

                for (int i = 0; i < fileList.length; ++i) 
                   {
                       File oldFile = arrobject[i];
                       File  newFile= new File("/Users/michaelkennedy/Pictures/Photography/" + path + "/img_" + n + ".jpg");
                       ++n;
                       if (oldFile.getName().equals(newFile().getName())) {
                           System.out.println("***This Folder has already been converted***");
                           break;
                       }
                       if (object.renameTo(file2)) {
                           System.out.println(object.getName() + " ---> " + file2.getName() + "     Done");
                           continue;
                       }
                       System.out.println(object.getName() + " ---> " + file2.getName() + "     Conversion Failed");
                   }
                System.out.println();
                System.out.print("Would you like to convert another folder? [Y || N]: ");
                string = scanner.nextLine();
                if (string.equals("Y")) continue;
                System.exit(0);
            }
        }
        catch (Exception exception) {
            System.out.println("Something happened while trying to parse your responses. Please try again.");
            exception.printStackTrace();
            System.exit(-1);
        }
    }

    private static String parent_child(String string) {
        Object object = "";
        Scanner scanner = new Scanner(System.in);
        StringBuilder stringBuilder = new StringBuilder();
         v
        if (string.equals("Y")) {
            File[] arrfile;
            void var10_21;
            File file = new File("/Users/michaelkennedy/Pictures/Photography");
            for (File object22 : arrfile = file.listFiles((FileFilter)var4_4)) {
                System.out.print(" | " + object22.getName());
            }
            System.out.println(" | ");
            System.out.print("Parent Name: ");
            Object object3 = scanner.nextLine();
            while (!batch_renumber.verify((String)object3)) {
                System.out.println("This isn't a valid directory. Try again");
                System.out.println();
                System.out.print("Parent Name: ");
                object3 = scanner.nextLine();
            }
            File file2 = new File("/Users/michaelkennedy/Pictures/Photography/" + (String)object3);
            Object[] arrobject = file2.listFiles((FileFilter)var4_4);
            Arrays.sort(arrobject);
            for (Object object2 : arrobject) {
                System.out.print(" | " + object2.getName());
            }
            System.out.println(" | ");
            System.out.print("Child Name: ");
            String string2 = scanner.nextLine();
            while (!batch_renumber.verify((String)object3 + "/" + (String)var10_21)) {
                System.out.println("This isn't a valid directory. Try again");
                System.out.println();
                System.out.print("Child's Name: ");
                String string3 = scanner.nextLine();
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
}
