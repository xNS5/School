import java.io.FileWriter;

public class Dir {
    private static String new_path;
    static String dir(){
        String init_path = System.getProperty("user.dir") + "/br_config/init";

        try{
            new_path = Open.Open("~/");
            FileWriter fw = new FileWriter(init_path);
            if(new_path != null){
                fw.write(new_path);
             }
            else{
                new_path = "~/";
            }
            fw.close();

        }
        catch(Exception e){
            e.printStackTrace();
            new Err("Dir: " + e.toString());
        }
        return new_path;
    }
}