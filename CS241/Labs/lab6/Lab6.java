// Lab6.java
//
// Skeleton for Lab6


// TODO: Add proper import java.*s here

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class Lab6 extends JFrame implements ActionListener{


   JButton cutButton, copyButton, pasteButton, openButton, saveButton;
   JScrollPane scroller;
   JTextArea text;

    // Constructor
    public Lab6() {
        // Follow instructions for Stage1
        super("CSCI 241 Lab6");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        // Follow instructions for Stage2 to call initialize()
        initialize();
    }

    // Initialize the GUI
    // Follow instructions for Stage 2
    private void initialize() {
        // Add a panel for the toolbar
        JPanel toolbar = new JPanel();
        add(toolbar, BorderLayout.NORTH);
        // Add buttons to the toolbar
        // Example: addButton(toolbar, "Cut");
        // Add button for "Cut"
        // Add button for "Copy"
        addButton(toolbar, "Cut");
        addButton(toolbar, "Copy");
        addButton(toolbar, "Paste");

        // Add a text area within a scrolling pane by following instructions for Stage 4
        text = new JTextArea();
        scroller = new JScrollPane(text);
        add(scroller, BorderLayout.CENTER);

        // Follow instructions for Stage 6
        // Add button for "Open"
        addButton(toolbar, "Open");

        // Follow instructions for Stage 7
        // Add button for "Save"
        addButton(toolbar, "Save");


    }

    // Add a button to a panel
    // Follow instructions for Stage 3
    private void addButton(JPanel panel, String label) {
        // Follow instructions for Stage 3
      JButton button = new JButton(label);
      panel.add(button);
      button.addActionListener(this);
    }

    // Event-handler for button-press
    // //Follow instructions for Stage 3
    public void actionPerformed(ActionEvent e) {
        // Follow instructions for Stage 3
        System.out.println(e.getActionCommand() + " Pressed");

        // Follow instructions for Stage 5
        if (e.getActionCommand() == "Cut") {
            text.cut();
        } else if (e.getActionCommand() == "Copy") { //--> Follow instructions for Stage 5
            text.copy();
        } else if (e.getActionCommand() == "Paste") { //--> Follow instructions for Stage 5
            text.paste();
        } else if (e.getActionCommand() == "Open") { // ---> Follow instructions for Stage 6
            readFile();
        } else if (e.getActionCommand() == "Save") { // ---> Follow instructions for Stage 7
            writeFile();
        }

    }

    // Read a file in Stage 6
    private void readFile() {
        // Follow instructions for Stage 6
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showOpenDialog(this);

        if(option == JFileChooser.APPROVE_OPTION)
          {
            try
              {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                text.setText(new String(Files.readAllBytes(Paths.get(filename))));
              }
            catch(IOException e)
              {
                System.out.println("Cannot read the file " + e);
              }
          }
    }

    // Write to a file in Stage 7
    private void writeFile() {
        // Follow instructions for Stage 7
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showSaveDialog(this);
        if(option == JFileChooser.APPROVE_OPTION)
          {
            try
              {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                Files.write(Paths.get(filename), text.getText().getBytes());
              }
            catch(IOException e)
              {
                System.out.println("Cannot write to file " + e);
              }
          }


    }

    public static void main(String[] args) {
        Lab6 frame = new Lab6();
    }

}
