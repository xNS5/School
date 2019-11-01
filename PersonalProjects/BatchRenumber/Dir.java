import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

class Dir {
    static void dir() {
        try {
            final Container cont = new Container("Default Directory");
            String init_path = "br_config/init.txt";
            Scanner sc = new Scanner(new File(init_path));
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Current Default: ");
            JTextField jt = new JTextField(40);
            JButton button1 = new JButton("Change"), button2 = new JButton("Save");

            cont.setLocationRelativeTo(null);
            cont.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            if (sc.hasNext()) {
                jt.setText(sc.next());
            }
            panel.add(label);
            panel.add(jt);
            panel.add(button1);
            panel.add(button2);
            cont.add(panel);
            cont.setSize(850, 80);
            cont.setResizable(false);
            cont.setVisible(true);

            button1.addActionListener(m -> jt.setText(Open.open(System.getProperty("user.home"))));

            button2.addActionListener(m -> {
                try {
                    String write_path = jt.getText();
                    File new_path = new File(write_path);

                    if (!new_path.isDirectory()) {
                        new Err("Inputted file isn't a valid directory");
                    } else {
                        FileWriter fw = new FileWriter(init_path);
                        fw.write(write_path);
                        fw.close();
                        cont.setVisible(false);
                        cont.dispose();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new Err("Dir: Save" + e.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            new Err("Dir: " + e.toString());
        }
    }
}