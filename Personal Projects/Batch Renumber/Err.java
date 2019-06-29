import javax.swing.*;

class Err {
    //Error popup with a custom message.
    Err(String err) {
        final Container frame = new Container("Error");
        JButton close = new JButton("Back");
        JButton exit = new JButton("Exit");
        JLabel label = new JLabel(err);
        frame.add(label);
        frame.add(close);
        frame.add(exit);
        frame.setVisible(true);
        frame.pack();

        close.addActionListener(e -> {
            frame.setVisible(false);
            frame.dispose();
        });
        exit.addActionListener(e -> System.exit(-1));
    }
}