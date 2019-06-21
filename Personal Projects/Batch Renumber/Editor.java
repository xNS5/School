import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.awt.*;

public class Editor{

    public static void main(String[] args){
        editor();
    }
    public static void editor(){
        String init_dir = "/users/michaelkennedy/Computer Science/Github/CS/Personal Projects/Batch Renumber/br_config/filter";
        final Container frame = new Container("Editor");
        File filter = new File(init_dir);

        JButton button1 = new JButton("Save");
        JTextArea panel = new JTextArea("Taco!");

        panel.setBounds(15, 12, 300, 300);
        frame.setSize(400, 350);

        frame.add(panel);
        frame.add(button1);
        frame.setVisible(true);

    }
}