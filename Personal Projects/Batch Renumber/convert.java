import java.lang.*;
import java.io.*;

public class convert{

    convert (String dir, String name, int count){
        File directory = new File(dir);
        filter f = new filter();
        File[] dir_list = directory.listFiles(f);
        System.out.println("Yep!");
    }
}