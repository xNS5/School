import javax.swing.*;

public class Open {
    //Open dialogue with jFileChooser
    static String Open(String dir) {
        JFileChooser jfc = new JFileChooser(dir);
        jfc.setDialogTitle("Choose a directory");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().isDirectory()) {
                return jfc.getSelectedFile().getAbsolutePath();
            }
        }
        return "";
    }
}
