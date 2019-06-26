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

           JTextField jf1 = new JTextField(40);
           JTextField jf2 = new JTextField(36);
           JTextField jf3 = new JTextField(2);
           cont.setSize(750, 150);

           //Creating new JButtons
           JButton button1 = new JButton("Choose Folder");
           JButton button2 = new JButton("Convert");
           JButton button3 = new JButton("Edit Filter");
           JButton button4 = new JButton("Default Directory");

           JLabel imp = new JLabel("Import Directory");
           JLabel name = new JLabel("New Name");
           JLabel num = new JLabel("#");


           layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                   .addGroup(layout.createParallelGroup()
                           .addComponent(imp)
                           .addComponent(jf1)
                           .addComponent(button1)
                   )
                   .addGroup(layout.createParallelGroup()
                           .addComponent(name)
                           .addComponent(jf2)
                           .addComponent(num)
                           .addComponent(jf3)
                           .addComponent(button2)
                   )
           );

           layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                   .addComponent(imp)
                   .addComponent(jf1)
                   .addComponent(button1)
                   .addComponent(name)
                   .addComponent(jf2)
                   .addComponent(num)
                   .addComponent(jf3)
                   .addComponent(button2)
           );

           panel.setLayout(layout);
           cont.add(panel);
           cont.setVisible(true);

//           cont.add(new JLabel("Import Directory"));
//           cont.add(jf1);
//           cont.add(button1);
//           cont.add(new JLabel("New Name"));
//           cont.add(jf2);
//           cont.add(new JLabel("#"));
//           cont.add(jf3);
//           cont.add(button2);
//           cont.add(button3);
//           cont.add(button4);
//           cont.setVisible(true);

       try{
           Scanner sc = new Scanner(new File(default_dir_path));

           //ActionListeners with lambda expressions.
           button1.addActionListener(e -> {
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
       catch(Exception e){
           e.printStackTrace();
         new Err("Batch Renumber: " + e.toString());

       }
   }
}