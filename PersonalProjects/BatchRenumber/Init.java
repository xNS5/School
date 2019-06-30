import java.io.File;
import java.io.IOException;

class Init {

    Init() {
        try {
            String br_path = System.getProperty("user.home") + "/br_config/";
            String init_path = System.getProperty("user.home") + "/br_config/init";
            String filter_path = System.getProperty("user.home") + "/br_config/filter";

            if (!new File(br_path).isDirectory()) {
                new File(br_path).mkdir();
                new File(br_path + "/init").createNewFile();
                new File(br_path + "/filter").createNewFile();
            } else if (!new File(init_path).exists()) {
                new File(br_path + "/init").createNewFile();
            } else if (!new File(filter_path).exists()) {
                new File(br_path + "/filter").createNewFile();
            }

        } catch (IOException i) {
            new Err("Error creating init files");
        }
    }
}