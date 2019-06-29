import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import java.io.File;
import java.io.IOException;

//Class for converting files
public class Convert {
    //    public static void main(String[] args){
//        Convert("/Users/michaelkennedy/Pictures/Photography/Roll_2/", "img", "_", 0);
//    }
    /*static void*/ Convert(String dir, String name, String delim, int count) {
        try {
            Container mainFrame = new Container("Converter");
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)), panel = new JPanel();
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


            //Setting up layout, similar to Editor layout except with a few different buttons.
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
            Filter f = new Filter();
            File[] dir_list = Sort.mSort(directory.listFiles(f));

            if (dir_list.length == 0) {
                throw new NoSuchFieldException("Error: No pictures in this directory\r\n");
            }

            for (int i = count; i < dir_list.length; i++) {

                File oldFile = dir_list[i];
                String extension = oldFile.getName().substring(oldFile.getName().lastIndexOf("."));
                String oldFile_name = oldFile.getName(), newFile_name = (name + delim + i + extension);
                Path oldFile_path = Paths.get(oldFile.getAbsolutePath());
                Files.move(oldFile_path, oldFile_path.resolveSibling(newFile_name));
                jt.append(oldFile_name + " --> " + newFile_name);
                jt.append(System.getProperty("line.separator"));
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