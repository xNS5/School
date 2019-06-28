import java.lang.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

//Class for converting files
public class Convert{

    public static void main(String[] args){
        convert("/users/michaelkennedy/Pictures/Photography/Roll_1", "img", "_", 0);
    }
    static void convert (String dir, String name, String delim, int count) {
        try {
            Container mainFrame = new Container("Converter");
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel panel = new JPanel();
            JTextArea jt = new JTextArea(20, 20);
            JScrollPane scrollPane = new JScrollPane(jt);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            JButton b1 = new JButton("Done"), b2 = new JButton("Open");
            JLabel label = new JLabel("Conversion Progress");

            mainFrame.setSize(350, 400);
            mainFrame.add(controlPanel);
            mainFrame.setResizable(false);

            GroupLayout layout = new GroupLayout(panel);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);

            layout.setHorizontalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(label).addComponent(scrollPane))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(b1).addComponent(b2)));

            layout.setVerticalGroup(layout.createSequentialGroup()
                    .addComponent(label)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPane)
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

            for (int i = 0; i < dir_list.length; i++) {
               File oldFile = dir_list[i];
                String oldFile_path = oldFile.getAbsolutePath();
               String extension = oldFile.getName().substring(oldFile.getName().indexOf("."));
                String oldFile_name = oldFile.getName(), newFile_name = (name + delim + count + extension);
               File newFile = new File(dir + "/" + newFile_name);

                jt.append(oldFile_name + "       Status: " + (oldFile.renameTo(newFile) == true ? " Completed\r\n" : " Failed\r\n"));
               count++;
            }

            b1.addActionListener(e -> {
                mainFrame.setVisible(false);
                mainFrame.dispose();
            });
            b2.addActionListener(e -> {
                try {
                    Desktop.getDesktop().open(directory);
                } catch (IOException g) {
                    g.printStackTrace();
                    new Err("Convert - Open: " + g.toString());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            new Err("Convert: " + e.toString());
        }
    }

}