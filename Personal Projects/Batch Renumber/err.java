import javax.swing.*;

public class err{

     err (String err){
         final container frame = new container();
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         JButton close = new JButton("Close");
         close.addActionListener(e-> System.exit(0));

         frame.add(new JLabel(err));
         frame.add(close);
         frame.setSize(200,100);

         frame.setVisible(true);
     }
}