import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class Renumber {
    private static class Vars {
        private String path, delim = "";
    }

    public static void main(String[] args) {

        // Creating the container for the main window
        final Container cont = new Container("Batch Renumber");
        cont.setSize(900, 150);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)), empty = new JPanel(); // This is sort of a placeholder to make it easier to place things in the groupLayout
        GroupLayout layout = new GroupLayout(panel);
        cont.setResizable(false);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JMenu menu = new JMenu("Settings"), delimiters = new JMenu("Delimiters");
        JMenuItem filter = new JMenuItem("Edit Filter"), default_dir = new JMenuItem("Change Default Directory");
        JCheckBox delimiter1 = new JCheckBox("Delimiter: \" \" "), delimiter2 = new JCheckBox("Delimiter: . ");
        delimiter2.setSelected(true);

        delimiters.add(delimiter1);
        delimiters.add(delimiter2);

        menu.add(filter);
        menu.add(default_dir);
        menu.addSeparator();
        menu.add(delimiters);
        menuBar.add(menu);

        JTextField jf1 = new JTextField(40), jf2 = new JTextField(30), jf3 = new JTextField(2);

        //Creating new JButtons
        JButton button1 = new JButton("Choose Folder"), button2 = new JButton("Convert");
        //Creating new JLabels
        JLabel imp = new JLabel("Import Directory"), name = new JLabel("New Name"), num = new JLabel("#");

        //I fucking hate group layout. So much of a pain.
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(menuBar)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(imp).addComponent(name))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(jf1).addComponent(jf2))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(empty).addComponent(num))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(empty).addComponent(jf3, 0, 40, 40))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(button1).addComponent(button2))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(menuBar).addComponent(imp).addComponent(jf1).addComponent(empty).addComponent(empty).addComponent(button1))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(name).addComponent(jf2).addComponent(num)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(jf3, 0, 30, 40).addComponent(button2)))
        );

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        panel.setLayout(layout);
        cont.add(panel);
        cont.setVisible(true);

        Vars v = new Vars();


        //ActionListeners with lambda expressions.
        button1.addActionListener(e -> {
                    try {
                        Scanner sc = new Scanner(new File(System.getProperty("user.home") + "/br_config/init"));
                        if (sc.hasNextLine()) {
                            String temp_str = sc.next();
                            File temp_file = new File(temp_str);
                            if (temp_file.isDirectory()) {
                                v.path = temp_str;
                            }
                        } else {
                            v.path = "~/";
                        }

                        jf1.setText(Open.open(v.path));
                    } catch (Exception n) {
                        n.printStackTrace();
                        new Err("Batch Renumber: " + n.toString());
                    }
                }
        );

        button2.addActionListener(e -> {
            if (jf1.getText().trim().length() == 0) {
                new Err("Error: No file detected\r\n");
            } else if ((jf2.getText().trim().length() == 0) || (jf3.getText().trim().length() == 0)) {
                new Err("Error: Please fill in both new name and number fields\r\n");
            } else if (Integer.parseInt(jf3.getText().replaceAll("[^0-9]", "")) < 0) {
                new Err("Error: Number must be at least zero\r\n");
            } else {
                new Convert(jf1.getText(), jf2.getText(), v.delim, Integer.parseInt(jf3.getText()));
            }
        });

        delimiter1.addActionListener(e -> {
            v.delim = " ";
            delimiter2.setSelected(false);
        });
        delimiter2.addActionListener(e -> {
            v.delim = ".";
            delimiter1.setSelected(false);
        });

        filter.addActionListener(e -> new Editor());
        default_dir.addActionListener(e -> Dir.dir());

    }

}