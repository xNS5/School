import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import javax.swing.*;

public class defaultDir {

    private static String init_file = System.getProperty("user.dir") + "/br_config/init";

    public static void main(String[] args) {
        dir();
    }

    public static void dir() {
        try {
            JFrame frame = new JFrame("Default Directory");
            JButton open = new JButton("Open");
            JButton close = new JButton("Close");
            JButton save = new JButton ("Save");
            JTextField jf = new JTextField();
            Scanner sc = new Scanner(new File(init_file));

        } catch (FileNotFoundException e) {
            new Err(e.getMessage());
        }
    }
}