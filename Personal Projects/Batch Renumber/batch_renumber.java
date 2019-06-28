import javax.swing.*;
import javax.swing.GroupLayout;
import java.awt.*;
import java.util.Scanner;
import java.io.File;

public class batch_renumber{

    private static String default_dir_path = System.getProperty("user.dir")+"/br_config/init";
    private static String path;

   public static void main(String[] args){
           // Creating the container for the main window
           final Container cont = new Container("Batch Renumber");

           JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
           JPanel empty = new JPanel(); // This is sort of a placeholder to make it easier to place things in the groupLayout
           GroupLayout layout = new GroupLayout(panel);

           JMenuBar mainBar = new JMenuBar();
           JMenuBar menuBar = new JMenuBar();
           menuBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
           JMenu menu = new JMenu("File");
           JMenuItem filter = new JMenuItem("Edit Filter");
           JMenuItem default_dir = new JMenuItem("Change Default Directory");
           JMenu delimiters = new JMenu("Delimiters");
           JCheckBoxMenuItem delimiter1 = new JCheckBoxMenuItem("Delimiter: _ ", new ImageIcon("images/box.gif"));
           JCheckBoxMenuItem delimiter2 = new JCheckBoxMenuItem("Delimiter: . ");

           delimiters.add(delimiter1);
           delimiters.add(delimiter2);

           menu.add(filter);
           menu.add(default_dir);
           menu.addSeparator();
           menu.add(delimiters);


           menuBar.add(menu);

           JTextField jf1 = new JTextField(40);
           JTextField jf2 = new JTextField(30);
           JTextField jf3 = new JTextField(2);

           cont.setSize(900, 150);

           //Creating new JButtons
           JButton button1 = new JButton("Choose Folder");
           JButton button2 = new JButton("Convert");


           JLabel imp = new JLabel("Import Directory");
           JLabel name = new JLabel("New Name");
           JLabel num = new JLabel("#");

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
                   .addComponent(menuBar)
                   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                           .addComponent(imp).addComponent(jf1).addComponent(empty).addComponent(empty).addComponent(button1))
                   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                          .addComponent(name).addComponent(jf2).addComponent(num)
                           .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                               .addComponent(jf3, 0, 30, 40)
                               .addComponent(button2)))

           );

           layout.setAutoCreateGaps(true);
           layout.setAutoCreateContainerGaps(true);
           panel.setLayout(layout);
           cont.add(panel);
           cont.setVisible(true);

           //ActionListeners with lambda expressions.
           button1.addActionListener(e -> {
               try {
                   Scanner sc = new Scanner(new File(default_dir_path));
                   if (sc.hasNextLine()) {
                       String temp_str = sc.next();
                       File temp_file = new File(temp_str);
                       if (temp_file.isDirectory()) {
                           path = temp_str;
                       }
                   } else {
                       path = "~/";
                   }

                   jf1.setText(Open.Open(path));
               } catch (Exception v) {
                   v.printStackTrace();
                   new Err("Batch Renumber: " + v.toString());

               }
           }

           );
           button2.addActionListener(e -> {
               if (jf1.getText().trim().length() == 0) {
                   new Err("Error: No file detected\r\n");
               } else if ((jf2.getText().trim().length() == 0) || (jf3.getText().trim().length() == 0)){
                   new Err("Error: Please fill in both new name and number fields\r\n");
               } else if (Integer.parseInt(jf3.getText()) < 0){
                   new Err("Error: Number must be at least zero\r\n");
               } else {
                   new Convert(jf1.getText(), jf2.getText(), Integer.parseInt(jf3.getText()));
               }
           });
           filter.addActionListener(e-> new Editor());
           default_dir.addActionListener(e-> {
               Dir.dir();
           });
       }
   }