import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;
import java.util.stream.Collectors;


class Editor extends JFrame {
    private static String dir = "br_config/filter.txt";
    private static JTextArea jt;
    private static Container mainFrame;

    Editor() {
        try {
            //File, Scanner and a StringBuilder
           File filter_file = new File(dir);
            Scanner sc = new Scanner(filter_file);
            StringBuilder sb = new StringBuilder();

            //Creating a JFrame container, setting default close operation
            //Also creating a JPanel called ControlPanel, another container
            mainFrame = new Container("Editor");
            jt = new JTextArea(20, 20);
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)), panel = new JPanel();
            JButton b1 = new JButton("Save"), b2 = new JButton("Close");
            JLabel label = new JLabel("File Extension Editor");
            GroupLayout layout = new GroupLayout(panel);

            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);

            mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            mainFrame.setSize(350, 400);
            mainFrame.add(controlPanel);
            mainFrame.setResizable(false);

            //Adding info from filter.txt file to a string builder.
            while(sc.hasNextLine()){
                sb.append(sc.next()).append(System.getProperty("line.separator"));
            }

            jt.setText(sb.toString());

//======================================================================================================================
            //Here's the funky part, this chunk of code is how the different parts get aligned properly.
            //Honestly I just messed around with it until it worked properly.
            layout.setHorizontalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(label).addComponent(jt))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(b1).addComponent(b2)));

            layout.setVerticalGroup(layout.createSequentialGroup()
                    .addComponent(label)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jt)
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(b1).addComponent(b2))));
//======================================================================================================================

            panel.setLayout(layout);
            controlPanel.add(panel);
            mainFrame.setVisible(true);

            b1.addActionListener(e -> save());
            b2.addActionListener(e -> {
                mainFrame.setVisible(false);
                mainFrame.dispose();
            });

        } catch (Exception e) {
            e.printStackTrace();
            new Err("Editor: " + e.toString() + e.getStackTrace()[0].getLineNumber());
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