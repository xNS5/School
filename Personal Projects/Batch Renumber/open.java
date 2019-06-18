import java.swing.*;

public class open extends JFrame{
    public open()
    {
        JFileChooser fc = new JFileChooser("~/Pictures/Photography");
        int returnValue = jfc.showOpenDialog(null);

      if (returnValue == JFileChooser.APPROVE_OPTION) {
         return jfc.getSelectedFile();
      }
      else if (returnValue == JFileChooser.CANCEL_OPTION)
      {
          System.exit(0);
      }
}