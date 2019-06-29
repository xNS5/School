import javax.swing.*;
import java.awt.*;

class Container extends JFrame {
    //Creating the container for the different frames.
    Container(String title) {
        super(title);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER));
    }
}