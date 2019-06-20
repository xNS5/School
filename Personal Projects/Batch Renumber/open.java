import javax.swing.JFileChooser;

public class open {

     static String open() {
        JFileChooser jfc = new JFileChooser("/users/michaelkennedy/Pictures/Photography/");
        jfc.setDialogTitle("Choose a directory to save your file: ");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().isDirectory()) {
                return jfc.getSelectedFile().getAbsolutePath();
            }
        }
        return null;
    }
}
