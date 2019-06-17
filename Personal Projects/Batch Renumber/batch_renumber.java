import java.util.*;
import java.io.*;
import javax.swing.*;



public class batch_renumber{

   public static void main(String[] args){

      JFrame frame = new JFrame();
      JFileChooser jfc = new JFileChooser("/users/michaelkennedy/Pictures/Photography/");
      jfc.setDialogTitle("Open a Folder to Convert: ");
      int returnValue = jfc.showOpenDialog(null);
      button = new JButton("Test");

      if (returnValue == JFileChooser.APPROVE_OPTION) {
         File selectedFile = jfc.getSelectedFile();

      }

      if (returnValue == JFileChooser.CANCEL_OPTION){
         System.exit(0);
      }




   }
}