import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Filter implements FileFilter {

    public boolean accept(File file) {
        if (file.getName().equals(".DS_Store")) {
            file.delete();
        } else if (file.isFile()) {
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


