import java.io.FileWriter;

class Dir {
    static void dir() {
        String init_path = System.getProperty("user.dir") + "/br_config/init";
        try {
            String new_path = Open.open(System.getProperty("user.home"));
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