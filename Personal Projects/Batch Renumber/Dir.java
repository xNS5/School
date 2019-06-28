import java.io.FileWriter;

public class Dir {
    private static String new_path;

    static void dir() {
        String init_path = System.getProperty("user.dir") + "/br_config/init";
        try {
            new_path = Open.Open("~/");
            if (!new_path.equals("")) {
                System.out.println(new_path);
                FileWriter fw = new FileWriter(init_path);
                fw.write(new_path);
                fw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Err("Dir: " + e.toString());
        }
    }
}