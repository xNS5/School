import javax.swing.*;

class Err {
    //Error popup with a custom message.
    Err(String err) {
        final Container cont = new Container("Error");
        cont.setLocationRelativeTo(null);
        cont.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JButton close = new JButton("Back");
        JButton exit = new JButton("Exit");
        JLabel label = new JLabel(err);
        cont.add(label);
        cont.add(close);
        cont.add(exit);
        cont.setVisible(true);
        cont.pack();

        close.addActionListener(e -> {
            cont.setVisible(false);
            cont.dispose();
        });
        exit.addActionListener(e -> System.exit(0));
    }
}