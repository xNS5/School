import java.lang.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;


//Class for converting files
public class Convert{

    public static void main(String[] args){
        convert()
    }
    static void convert (String dir, String name, String delim, int count) {
        try {
            Container mainFrame = new Container("Converter");
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel panel = new JPanel();
            JTextArea jt = new JTextArea(20, 20);
            JButton b1 = new JButton("Done"), b2 = new JButton("Open Output Directory");
            JLabel label = new JLabel("Conversion Progress");

            mainFrame.setSize(350, 400);
            mainFrame.add(controlPanel);
            mainFrame.setResizable(false);

            GroupLayout layout = new GroupLayout(panel);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);

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

            panel.setLayout(layout);
            controlPanel.add(panel);
            mainFrame.setVisible(true);

            File directory = new File(dir);
            filter f = new filter();
            File[] dir_list = directory.listFiles(f);

            if(dir_list.length == 0){
                throw new NoSuchFieldException("Error: No pictures in this directory\r\n");
            }
            Arrays.sort(dir_list);

            for (int i = 0; i < dir_list.length; i++) {
                String extension = getFileExtension(dir_list[i]);
                File oldFile = dir_list[i];
                File newFile = new File(dir + name + "_" + count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Err("Convert: " + e.toString());
        }
    }

}