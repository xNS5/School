import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


//Class for converting files
public class Convert {

    private static File directory;
    private static File[] dir_list;
    private static String name, delim;
    private static JTextArea jt;

    Convert(String dir, String name, String delim, int count) {
        jt = new JTextArea(20, 20);
        this.name = name;
        this.delim = delim;

        final Container mainFrame = new Container("Converter");
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)), panel = new JPanel();

        JScrollPane scrollPane = new JScrollPane(jt);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JButton b1 = new JButton("Done"), b2 = new JButton("Open");
        JLabel label = new JLabel("Conversion Progress");

        mainFrame.setSize(350, 400);
        mainFrame.setLocationRelativeTo(null);
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

        directory = new File(dir);
        Filter f = new Filter();
        dir_list = Sort.mSort(directory.listFiles(f));

        if (dir_list.length == 0) {
            new Err("Empty Directory");
        }

        rename(dir_list, 0, count);

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
    }
    private static void rename(File[] dir, int i, int count){
        try {

            if (i == dir.length) {
                return;
            }

            File oldFile = dir[i];
            String extension = oldFile.getName().substring(oldFile.getName().lastIndexOf("."));
            String oldFile_name = oldFile.getName(), newFile_name = (name + delim + count + extension);
            Path oldFile_path = Paths.get(oldFile.getAbsolutePath());
            jt.append(oldFile_name + " --> " + newFile_name);
            jt.append(System.getProperty("line.separator"));

            rename(dir, i += 1, count += 1);

        } catch (Exception e) {
            e.printStackTrace();
            new Err("Convert: " + e.toString());
        }
    }

}