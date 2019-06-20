import javax.swing.*;
import java.awt.*;

public class err{

     err (String err){
         final container frame = new container();
         JButton close = new JButton("Back");
         JButton exit = new JButton("Exit");
         JLabel label = new JLabel(err);
         frame.add(label);
         frame.add(close);
         frame.add(exit);
         frame.setVisible(true);
         frame.pack();

         close.addActionListener(e-> { frame.setVisible(false); frame.dispose(); });
         exit.addActionListener(e -> System.exit(-1));
     }
}