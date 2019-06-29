import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Filter implements FileFilter {
    public boolean accept(File file) {
        if (file.isFile() && !file.getName().equals(".DS_Store")) {
            try {
                String fileName = file.getName();
                HashMap<String, Boolean> hmap = new HashMap<>();
                Scanner sc = new Scanner(new File(System.getProperty("user.dir") + "/br_config/filter"));
                while (sc.hasNextLine()) {
                    hmap.put(sc.nextLine(), true);
                }
                return hmap.containsKey(fileName.substring(fileName.lastIndexOf(".")));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                new Err("Extension: " + e.toString());
            }
        }
        return false;
    }
}


