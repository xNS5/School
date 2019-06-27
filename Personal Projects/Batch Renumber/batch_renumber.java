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
           GroupLayout layout = new GroupLayout(panel);
           layout.setAutoCreateGaps(true);
           layout.setAutoCreateContainerGaps(true);

           JMenuBar menuBar = new JMenuBar();
           JMenu menu = new JMenu("File");

           menuBar.add(menu);

           JTextField jf1 = new JTextField(40);
           JTextField jf2 = new JTextField(36);
           JTextField jf3 = new JTextField(2);
           cont.setSize(900, 150);

           //Creating new JButtons
           JButton button1 = new JButton("Choose Folder");
           JButton button2 = new JButton("Convert");
           JButton button3 = new JButton("Edit Filter");
           JButton button4 = new JButton("Default Directory");

           JLabel imp = new JLabel("Import Directory");
           JLabel name = new JLabel("New Name");
           JLabel num = new JLabel("#");


           layout.setHorizontalGroup(layout.createSequentialGroup()
                   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(imp).addComponent(name))
                   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(jf1).addComponent(jf2))
                   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                       .addComponent(button1).addComponent(num))
                   .addComponent(jf3)
                   .addComponent(button2)
           );

           layout.setVerticalGroup(layout.createSequentialGroup()
                   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                           .addComponent(imp).addComponent(jf1).addComponent(button1))
                   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                           .addComponent(name).addComponent(jf2).addComponent(num).addComponent(jf3)
                           .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                   .addComponent(button2)))

           );

           panel.setLayout(layout);
           cont.add(panel);
           cont.setVisible(true);


           //ActionListeners with lambda expressions.
           button1.addActionListener(e -> {
               try {
                   Scanner sc = new Scanner(new File(default_dir_path));
                   if (sc.hasNextLine()) {
                       String temp_str = sc.next();
                       System.out.println(temp_str);
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
           button3.addActionListener(e-> new Editor());
           button4.addActionListener(e-> {
               Dir.dir();
           });
       }
   }