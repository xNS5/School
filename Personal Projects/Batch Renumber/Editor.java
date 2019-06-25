import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.*;


public class Editor extends JFrame {
    private static String dir;
    private static JTextArea jt;
    private static JFrame mainFrame;

    Editor() {
        try {
            //File, Scanner and a StringBuilder
            dir = "/users/michaelkennedy/Computer Science/Github/CS/Personal Projects/Batch Renumber/br_config/filter";
            File filter_file = new File(dir);
            Scanner sc = new Scanner(filter_file);
            StringBuilder sb = new StringBuilder();

            //Creating a JFrame container, setting default close operation
            //Also creating a JPanel called ControlPanel, another container
            mainFrame = new JFrame("Editor");
            //mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            mainFrame.setSize(350, 400);
            mainFrame.add(controlPanel);
            mainFrame.setResizable(false);

            //Adding buttons and stuff
            JPanel panel = new JPanel();
            jt = new JTextArea(20, 20);
            JButton b1 = new JButton("Save");
            JButton b2 = new JButton("Close");
            JLabel label = new JLabel("File Extension Editor");
            GroupLayout layout = new GroupLayout(panel);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);

            //Adding info from filter file to a stringbuilder.
            while (sc.hasNextLine()) {
                sb.append(sc.next() + "\r\n");
            }
            jt.setText(sb.toString());

            //Here's the funky part, this chunk of code is how the different parts get aligned properly.
            //Honestly I just messed around with it until it worked properly.
            layout.setHorizontalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(jt).addComponent(label))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(b1).addComponent(b2)));

            layout.setVerticalGroup(layout.createSequentialGroup()
                    .addComponent(label)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jt)
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(b1).addComponent(b2))));

            panel.setLayout(layout);
            controlPanel.add(panel);
            mainFrame.setVisible(true);

            b1.addActionListener(e -> {
                save();
            });
            b2.addActionListener(e-> {
                mainFrame.setVisible(false);
                mainFrame.dispose();
            });

        } catch (Exception e) {
           new Err(e.getMessage());
        }
    }

    private static void save() {
        try {
            String input = jt.getText();
            FileWriter fw = new FileWriter(dir);
            fw.write(input);
            fw.close();
            mainFrame.setVisible(false);
            mainFrame.dispose();
        } catch (IOException e) {
            new Err(e.getMessage());
        }
    }
}