import java.io.*;
import java.lang.String;
import java.util.HashMap;
import java.util.Scanner;

public class filter implements FileFilter{

    public boolean accept (File file){
        if(file.getName().equals(".DS_Store")){
            file.delete();
        }
        else if(file.isFile()){
            return file.isFile();
        }
        return false;
    }

    private boolean extension(File file){
        try {
            String fileName = file.getName();
            HashMap<String, Boolean> hmap = new HashMap<>();
            Scanner sc = new Scanner(new File("files.txt"));
            while (sc.hasNextLine()) {
                hmap.put(sc.nextLine(), true);
            }
            if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0){
                return hmap.get(fileName.substring(fileName.lastIndexOf(".") + 1));
            }
            else return false;
        }
        catch(FileNotFoundException e){
            new Err(e.getMessage());
        }
        return false;
    }


}


