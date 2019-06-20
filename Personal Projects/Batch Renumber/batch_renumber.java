import javax.swing.*;

public class batch_renumber{

   public static void main(String[] args){
      final container cont = new container();
      cont.setResizable(false);
      JTextField jf1 = new JTextField(40);
      JTextField jf2 = new JTextField(36);
      JTextField jf3 = new JTextField(2);
      cont.setSize(750, 150);
      cont.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JButton button1 = new JButton("Choose Folder");
      JButton button2 = new JButton("Convert");

       cont.add(new JLabel("Import Directory"));
       cont.add(jf1);
       cont.add(button1);
       cont.add(new JLabel("New Name"));
       cont.add(jf2);
       cont.add(new JLabel("#"));
       cont.add(jf3);
       cont.add(button2);

       cont.setVisible(true);

      button1.addActionListener(e -> jf1.setText(open.open()));


      button2.addActionListener(e -> {
          if(jf1.getText().trim().length() == 0)
          {
              new err("Error: No file detected");
          }
          else {new convert(jf1.getText(), jf2.getText(), Integer.parseInt(jf3.getText()));}
      });

   }
}