import java.io.*;
import java.util.HashSet;

public class Filter implements FileFilter {
    public boolean accept(File file) {
        if (file.isFile() && !file.getName().equals(".DS_Store")) {
            try {
                String fileName = file.getName();
                HashSet<String> hmap = new HashSet<>();
                InputStream is = Filter.class.getResourceAsStream("/br_config/filter.txt");
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
//                Scanner sc = new Scanner(new File("br_config/filter.txt"));
                while (bf.ready()) {
                    hmap.add(bf.readLine());
                }
                return hmap.contains(fileName.substring(fileName.lastIndexOf(".")));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                new Err("Filter: " + e.toString());
            } catch (IOException e){
                e.printStackTrace();
                new Err("Filter: " + e.toString());
            }
        }
        return false;
    }
}


