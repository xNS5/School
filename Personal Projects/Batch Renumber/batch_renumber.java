import javax.swing.*;
import java.util.Scanner;
import java.io.File;

public class batch_renumber{

    private static String default_dir_path = System.getProperty("user.dir")+"/br_config/init";
    private static String path;

   public static void main(String[] args){

       try {
           // Creating the container for the main window
           final Container cont = new Container("Batch Renumber");
           JPanel panel = new JPanel();
           cont.setResizable(false);
           Scanner sc = new Scanner(new File(default_dir_path));

           if(sc.hasNextLine()){
               String temp_str =  sc.next();
               File temp_file = new File(temp_str);
               if(temp_file.isDirectory()){
                   path = temp_str;
               }
           }
           else{
               path = "~/";
           }

           // jf1 = folder to be converted, jf2 = name to rename the files to, jf3 is the number to iterate up from.
           JTextField jf1 = new JTextField(40);
           JTextField jf2 = new JTextField(36);
           JTextField jf3 = new JTextField(2);
           cont.setSize(750, 150);

           //Creating new JButtons
           JButton button1 = new JButton("Choose Folder");
           JButton button2 = new JButton("Convert");
           JButton button3 = new JButton("Edit Filter");
           JButton button4 = new JButton("Default Directory");

           cont.add(new JLabel("Import Directory"));
           cont.add(jf1);
           cont.add(button1);
           cont.add(new JLabel("New Name"));
           cont.add(jf2);
           cont.add(new JLabel("#"));
           cont.add(jf3);
           cont.add(button2);
           cont.add(button3);
           cont.add(button4);
           cont.setVisible(true);

           //ActionListeners with lambda expressions.
           button1.addActionListener(e -> jf1.setText(Open.Open(path)));
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
               path = Dir.dir();
           });
       }
       catch(Exception e){
           e.printStackTrace();
         new Err("Batch Renumber: " + e.toString());

       }
   }
}