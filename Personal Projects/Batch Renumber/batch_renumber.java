import javax.swing.*;

public class batch_renumber{

   public static void main(String[] args){

       try {
           final Container cont = new Container("Batch Renumber");
           cont.setResizable(false);
           JTextField jf1 = new JTextField(40);
           JTextField jf2 = new JTextField(36);
           JTextField jf3 = new JTextField(2);
           cont.setSize(750, 150);

           JButton button1 = new JButton("Choose Folder");
           JButton button2 = new JButton("Convert");
           JButton button3 = new JButton("Edit Filter");

           cont.add(new JLabel("Import Directory"));
           cont.add(jf1);
           cont.add(button1);
           cont.add(new JLabel("New Name"));
           cont.add(jf2);
           cont.add(new JLabel("#"));
           cont.add(jf3);
           cont.add(button2);
           cont.add(button3);
           cont.setVisible(true);

           button1.addActionListener(e -> jf1.setText(Open.Open()));
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
           button3.addActionListener(e->{new Editor();});
       }
       catch(Exception e){
          System.out.println(e.getMessage());
       }
   }
}