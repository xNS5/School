import java.io.*;
import java.lang.String;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileFilter;

public class filter implements FileFilter {

   public boolean accept(File file) {
        if (file.getName().equals(".DS_Store")) {
            file.delete();
        } else if(file.isFile()) {
            try {
                String fileName = file.getName();
                HashMap<String, Boolean> hmap = new HashMap<>();
                Scanner sc = new Scanner(new File(System.getProperty("user.dir") + "/br_config/filter"));
                while (sc.hasNextLine()) {
                    hmap.put(sc.nextLine(), true);
                }

                return hmap.containsKey(fileName.substring(fileName.indexOf(".")));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                new Err("Extension: " + e.toString());
            }
        }
        return false;
    }
}


