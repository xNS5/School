import java.io.File;
import java.io.IOException;

class Init {

    Init() {
        try {
            File home = new File(System.getProperty("user.home"));
            File br_path = new File(home + "/br_config/"), init_path = new File(home + "/br_config/init.txt"), filter_path = new File(home + "/br_config/filter.txt");
            if (!br_path.isDirectory()) {
                br_path.mkdir();
                init_path.createNewFile();
                filter_path.createNewFile();
            } else if (!init_path.exists()) {
                init_path.createNewFile();
            } else if (!filter_path.exists()) {
                filter_path.createNewFile();
            }
        } catch (IOException i) {
            new Err("Error creating init.txt files");
        }
    }
}