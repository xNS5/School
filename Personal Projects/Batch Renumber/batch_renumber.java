import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class batch_renumber extends JFrame{

   public static void main(String[] args){
      container cont = new container();
      JTextField jf1 = new JTextField(12);
      cont.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      cont.setSize(500, 500);

      JFileChooser fc = new JFileChooser("~/Pictures/Photography");
      int returnValue = fc.showOpenDialog(null);
      JButton button1 = new JButton("Search for a folder");
      JButton button2 = new JButton("Convert");

      cont.add(button1);
      cont.add(button2);
      cont.setVisible(true);

      button1.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            if(returnValue == JFileChooser.APPROVE_OPTION)
            {

            }else{
               System.exit(0);
            }
         }
      });

      button2.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            System.out.println("Bell!");
         }
      });

   }
}