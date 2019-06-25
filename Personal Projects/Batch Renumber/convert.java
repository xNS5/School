import java.lang.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;


//Class for converting files
public class Convert {

    Convert (String dir, String name, int count) {
        try {
            File directory = new File(dir);
            filter f = new filter();
            File[] dir_list = directory.listFiles(f);
            if(dir_list.length == 0){
                throw new NoSuchFieldException("Error: No pictures in this directory\r\n");
            }
            Arrays.sort(dir_list);

//            for (int i = 0; i < dir_list.length; i++) {
//                String extension = getFileExtension(dir_list[i]);
//                File oldFile = dir_list[i];
//                File newFile = new File(dir + name + "_" + count);
//            }

        } catch (Exception e) {
            new Err(e.getMessage());
        }
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return null;
    }
}