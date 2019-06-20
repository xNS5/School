import java.io.*;

public class filter implements FileFilter{
    @Override
    public boolean accept (File file){
        if(file.getName().equals(".DS_Store")){
            file.delete();
        }
        else if(file.isFile()){
            return file.isFile();
        }
        return false;
    }
}


