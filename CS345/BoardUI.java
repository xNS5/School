import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.*;
import javax.swing.text.StyleContext;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoardUI extends JFrame {
    private static BoardUI instance = null;
    private JLabel image;
    private JPanel mainPanel;
    private JLabel currentPlayerLabel;
    private JPanel buttonPane;
    private JButton moveButton;
    private JButton workButton;
    private JButton upgradeButton;
    private JButton endTurnButton;
    private JTable playerTable;
    private JLabel statusLabel;
    public JLabel dayLabel;
    public JPanel gameInfoPanel;
    public JLayeredPane imagePane;
    public JButton takeRoleButton;
    public JLabel totalDayLabel;
    private final String[] col = {"Name", "Rank", "Dollars", "Credits", "Chips", "Score"};
    private final String[][] initData = {{"", "", "", "", "", ""}};

    /*
     * TODO:
     *  Test 2 players working same scene
     * */
    private BoardUI() {
        super("Deadwood");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon boardImage = new ImageIcon("board.jpg");
        image = new JLabel();
        image.setIcon(boardImage);
        imagePane = getLayeredPane();
        image.setBorder(new LineBorder(Color.black));
        image.setOpaque(true);
        image.setBounds(25, 50, boardImage.getIconWidth(), boardImage.getIconHeight());
        imagePane.add(image, JLayeredPane.DEFAULT_LAYER);
        updatePlayers(initData, col);
        mainPanel.setPreferredSize(new Dimension(1760, boardImage.getIconHeight() + 75));

        playerTable.getTableHeader().setResizingAllowed(false);
        playerTable.getTableHeader().setReorderingAllowed(false);
        moveButton.addMouseListener(new mouseListener());
        workButton.addMouseListener(new mouseListener());
        takeRoleButton.addMouseListener(new mouseListener());
        upgradeButton.addMouseListener(new mouseListener());
        endTurnButton.addMouseListener(new mouseListener());

        setContentPane(mainPanel);
        setResizable(false);
        pack();
    }

    public static BoardUI getInstance() throws IOException {
        if (instance == null) {
            instance = new BoardUI();
        }
        return instance;
    }

    // Updating current player
    public void updateCurrentPlayerLabel(String name) {
        this.currentPlayerLabel.setText(name);
    }

    // Updating the total day label
    public void updateTotalDayLabel(int num) {
        totalDayLabel.setText(String.valueOf(num));
    }

    // updating the status of the player's action
    public void updateStatusMessage(String message) {
        statusLabel.setText(message);
        repaint();
    }

    //Updating Day label
    public void updateDayLabel(int i) {
        this.dayLabel.setText(String.valueOf(i));
        this.repaint();
    }

    // Setting players in the PlayerInfo table
    public void setPlayers(ArrayList<Player> players) {
        String[][] playerInfo = new String[players.size()][6];
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            playerInfo[i][0] = p.getName();
            playerInfo[i][1] = String.valueOf(p.getRank());
            playerInfo[i][2] = String.valueOf(p.getDollars());
            playerInfo[i][3] = String.valueOf(p.getCredits());
            playerInfo[i][4] = String.valueOf(p.getChips());
            playerInfo[i][5] = String.valueOf(p.getScore());
        }
        updatePlayers(playerInfo, col);
    }

    // Method for changing data in the player JTable
    public void setPlayerInfo(Player player) {
        for (int i = 0; i < playerTable.getRowCount(); i++) {
            if (playerTable.getValueAt(i, 0).equals(player.getName())) {
                playerTable.setValueAt(player.getRank(), i, 1);
                playerTable.setValueAt(player.getDollars(), i, 2);
                playerTable.setValueAt(player.getCredits(), i, 3);
                playerTable.setValueAt(player.getChips(), i, 4);
                playerTable.setValueAt(player.getScore(), i, 5);
            }
        }
    }

    // Helper method to add players to PlayerInfo table
    private void updatePlayers(String[][] data, String[] col) {
        DefaultTableModel model = new DefaultTableModel(data, col);
        playerTable.setModel(model);
        playerTable.setSize(new Dimension(100, 100));
        repaint();
    }

    // Moving player die
    public void placePlayerDie(Player player, int num) {
        JLabel dice = player.getDie();
        Set currSet = Board.getInstance().getSet(player.getSetNo());
        int x = 0, y = 0;
        if (num == 0) {
            // Moving locations on a set
            int[] dimensions = currSet.getLocation();
            x = dimensions[0];
            y = dimensions[1];
        } else if (num == 1) {
            // Main Character
            int[] rdimensions = player.getRole().getLocation();
            int[] cdimensions = currSet.getLocation();
            x = rdimensions[0] + cdimensions[0];
            y = rdimensions[1] + cdimensions[1];
        } else if (num == 2) {
            // Extras
            int[] dimensions = player.getRole().getLocation();
            x = dimensions[0] + 4;
            y = dimensions[1] + 3;
        }
        dice.setBounds(x, y, dice.getIcon().getIconWidth(), dice.getIcon().getIconHeight());
        imagePane.add(dice, 2, 0);
        if (!currSet.getName().equals("Trailers") && !currSet.getName().equals("Casting Office")) {
            currSet.updateVisitedStatus();
            removeComponent(currSet.getCardBack());
            Board.getInstance().replaceSet(currSet);
        }
        this.repaint();
    }

    // Setting cardbacks on each set
    public void deployCardBacks() {
        ArrayList<Set> sets = Board.getInstance().getSets();
        for (int i = 0; i < sets.size(); i++) {
            if (!sets.get(i).getName().equals("Trailers") && !sets.get(i).getName().equals("Casting Office")) {
                Set currSet = sets.get(i);
                imagePane.add(currSet.getCardBack(), 2, 1);
            }
        }
        repaint();
    }

    // Setting the corresponding cards to each set
    public void deployCards() {
        ArrayList<Set> sets = Board.getInstance().getSets();
        for (Set set : sets) {
            if (!set.getName().equals("Casting Office") && !set.getName().equals("Trailers")) {
                JLabel sceneCard = set.getScene().getCardLabel();
                ImageIcon sceneCardImage = new ImageIcon("./cards/" + set.getScene().getFilename());
                int x = set.getLocation()[0], y = set.getLocation()[1];
                sceneCard.setIcon(sceneCardImage);
                sceneCard.setBounds(x, y, sceneCardImage.getIconWidth(), sceneCardImage.getIconHeight());
                imagePane.add(sceneCard, 2, 11);
            }
        }
        setTake(sets);
        repaint();
    }

    public void clearBoard() throws IOException {
        Board board = Board.getInstance();
        ArrayList<Set> sets = board.getSets();
        for (Set set : sets) {
            ArrayList<JLabel> takes = set.getTakeComponenets();
            if (set.isActive() && !set.isVisited() && !set.getName().equals("Trailers") && !set.getName().equals("Casting Office")) {
                for (JLabel take : takes) {
                    this.removeComponent(take);
                }
                if (set.hasPlayers()) {
                    for (Player player : set.getPlayers()) {
                        this.removeComponent(player.getDie());
                    }
                }
                this.removeComponent(set.getCardBack());
                this.removeComponent(set.getScene().getCardLabel());
            } else if (set.isActive() && set.isVisited() && !set.getName().equals("Trailers") && !set.getName().equals("Casting Office")) {
                for (JLabel take : takes) {
                    this.removeComponent(take);
                }
                if (set.hasPlayers()) {
                    for (Player player : set.getPlayers()) {
                        this.removeComponent(player.getDie());
                    }
                }
                this.removeComponent(set.getScene().getCardLabel());
            }
        }
        repaint();
    }

    // Adding take images to each set
    private void setTake(ArrayList<Set> sets) {
        for (Set set : sets) {
            if (!set.getName().equals("Casting Office") && !set.getName().equals("Trailers")) {
                ArrayList<JLabel> takes = set.getTakeComponenets();
                for (JLabel t : takes) {
                    imagePane.add(t, 2, 3);
                }
            }
        }
    }

    public void finishGame(Player player) throws IOException {
        clearBoard();
        updateStatusMessage(String.format("Winner: %s with a score of %d!", player.getName(), player.getScore()));
    }

    // Removing a component from the board.
    public void removeComponent(Component c) {
        imagePane.remove(c);
        imagePane.revalidate();
        imagePane.repaint();
    }

    // Helper function to create headers for the Take Role button
    private JComponent createHeader(String header) {
        JLabel label = new JLabel(header);
        label.setFont(label.getFont().deriveFont(Font.ITALIC));
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }

    // MouseListener class for the Jbuttons
    class mouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            Board b = Board.getInstance();
            JPopupMenu sets = new JPopupMenu(), work = new JPopupMenu(), roles = new JPopupMenu();
            Set set = b.getSet(b.getCurrent().getSetNo());

            // If the source is the "move" button.
            if (e.getSource() == moveButton && moveButton.isEnabled()) {
                updateStatusMessage("");
                // If the current player isn't working and hasn't moved (moved -> ended turn).
                if (b.getCurrent().getStatus() == Work.UPGRADED) {
                    updateStatusMessage("Error: Please End Your Turn");
                }
                if (b.getCurrent().getStatus() == Work.NULL) {
                    updateStatusMessage("");
                    Set current = b.getSet(b.getCurrent().getSetNo());
                    ArrayList<String> arr = current.getNeighbors();
                    for (String s : arr) {
                        JMenuItem item = new JMenuItem(s);
                        item.addActionListener(e1 -> {
                            String setName = e1.getActionCommand();
                            if (!current.isVisited()) {
                                b.getSet(b.getCurrent().getSetNo()).updateVisitedStatus();
                            }
                            if (!b.getSet(b.getCurrent().getSetNo()).isNeighbor(setName)) {
                                updateStatusMessage(String.format("Cannot Move to: %s", e1.getActionCommand()));
                            } else {
                                updateStatusMessage(String.format("Moved %s to %s", b.getCurrent().getName(), setName));
                                b.changeStatus(Work.MOVED);
                                b.movePlayer(setName);
                                placePlayerDie(b.getCurrent(), 0);
                            }
                        });
                        sets.add(item);
                    }
                    sets.show(moveButton, moveButton.getWidth(), moveButton.getHeight() / 2);
                    repaint();
                } else if (b.getCurrent().getStatus() == Work.PREEXTRA || b.getCurrent().getStatus() == Work.PREMAIN) {
                    updateStatusMessage("Error: Cannot Leave a Role");
                } else if (b.getCurrent().getStatus() == Work.EXTRA || b.getCurrent().getStatus() == Work.MAIN) {
                    updateStatusMessage("Error: You Already Have a Role");
                } else {
                    updateStatusMessage("Error: Please Take A Role or End Turn");
                }
            } else if (e.getSource() == workButton && workButton.isEnabled()) {
                Work status = b.getCurrent().getStatus();
                if (status == Work.MOVED || status == Work.NULL || !set.hasScene()) {
                    updateStatusMessage("Error: Role Not Selected");
                } else if (status == Work.POSTMAIN || status == Work.POSTEXTRA) {
                    updateStatusMessage("Error: You Cannot Work Again This Turn");
                } else if (status == Work.PREMAIN || status == Work.PREEXTRA) {
                    updateStatusMessage("Error: You Cannot Work This Turn");
                } else if ((status == Work.EXTRA || status == Work.MAIN) && set.hasScene()) {
                    String[] workItems = {"Act", "Rehearse"};
                    for (String s : workItems) {
                        JMenuItem item = new JMenuItem(s);
                        item.addActionListener(e1 -> {
                            String action = e1.getActionCommand();
                            try {
                                if (action.equals("Act")) {
                                    b.work(0);
                                } else if (action.equals("Rehearse")) {
                                    b.work(1);
                                }
                            } catch (IOException | InterruptedException | ParserConfigurationException | SAXException exception) {
                                exception.printStackTrace();
                            }
                        });
                        work.add(item);
                    }
                    work.show(workButton, workButton.getWidth(), workButton.getHeight() / 2);
                    repaint();
                } else if (!set.hasScene()) {
                    updateStatusMessage("Error: This Set Does Not Have a Scene");
                }
                setPlayerInfo(b.getCurrent());
            } else if (e.getSource() == takeRoleButton && takeRoleButton.isEnabled()) {
                int setNo = b.getCurrent().getSetNo();
                Work status = b.getCurrent().getStatus();
                if ((status == Work.NULL || status == Work.MOVED) && !set.hasScene()) {
                    updateStatusMessage("Error: No Scene is Present");
                } else if (setNo == b.getSetLocation("Trailers") || setNo == b.getSetLocation("Casting Office") || !set.hasScene()) {
                    updateStatusMessage("Error: There Are No Roles in This Set");
                } else if (status != Work.EXTRA && status != Work.MAIN && status != Work.POSTEXTRA && status != Work.POSTMAIN && status != Work.PREEXTRA && status != Work.PREMAIN) {
                    Set currentSet = b.getSet(b.getCurrent().getSetNo());
                    ArrayList<Role> extras = currentSet.getExtras(), mainChar = currentSet.getScene().getRoles(), roleList;
                    String[] roleType = {"Main Characters", "Extras"};
                    roles.add(createHeader(currentSet.getScene().getTitle()));
                    for (String type : roleType) {
                        roles.add(createHeader(type));
                        if (type.equals("Main Characters")) {
                            roleList = mainChar;
                        } else {
                            roleList = extras;
                        }
                        for (Role r : roleList) {
                            if (!r.isTaken()) {
                                JMenuItem role = new JMenuItem(String.format("%s, Rank: %d", r.getName(), r.getRank()));
                                ArrayList<Role> finalRoleList = roleList;
                                role.addActionListener(e1 -> {
                                    if (b.getCurrent().getRank() < r.getRank()) {
                                        updateStatusMessage("Error: Insufficient Rank");
                                    } else {
                                        String var = e1.getActionCommand().split(",")[0];
                                        updateStatusMessage(String.format("%s is playing %s", b.getCurrent().getName(), var));
                                        for (Role role1 : finalRoleList) {
                                            if (role1.getName().equals(var)) {
                                                int num;
                                                if (type.equals("Main Characters")) {
                                                    num = 1;
                                                    b.changeStatus(Work.PREMAIN);
                                                } else {
                                                    num = 2;
                                                    b.changeStatus(Work.PREEXTRA);
                                                }
                                                role1.takeRole();
                                                b.setRole(role1);
                                                placePlayerDie(b.getCurrent(), num);
                                            }
                                        }
                                    }
                                });
                                roles.add(role);
                            }
                        }
                    }
                    roles.show(workButton, workButton.getWidth(), workButton.getHeight() / 2);
                    repaint();
                } else {
                    updateStatusMessage("Error: Cannot Take Another Role");
                }
            } else if (e.getSource() == upgradeButton && upgradeButton.isEnabled()) {
                if (b.getCurrent().getSetNo() == b.getSetLocation("Casting Office") && (b.getCurrent().getStatus() == Work.NULL || b.getCurrent().getStatus() == Work.MOVED)) {
                    ButtonGroup radioButtonGroup = new ButtonGroup();
                    JDialog upgradePanel = new JDialog();
                    JPanel buttonPanel = new JPanel(), buttonGroup = new JPanel();

                    upgradePanel.setBounds(900, 500, 250, 350);
                    upgradePanel.setLayout(new GridLayout(0, 1));
                    buttonGroup.setLayout(new GridLayout(0, 1));
                    buttonGroup.setBorder(new TitledBorder("Please Select an Upgrade"));
                    buttonGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
                    upgradePanel.setResizable(false);

                    JButton creditButton = new JButton("Credits"), dollarButton = new JButton("Dollars");
                    String rankPattern = "Rank: ", dollarsPattern = "Dollars: ", creditsPattern = "Credits: ";
                    Pattern p = Pattern.compile(Pattern.quote(rankPattern) + "(.*?)" + Pattern.quote(dollarsPattern));

                    ArrayList<ArrayList<Integer>> upgradeList = UtilitySet.getUpgrades();
                    for (ArrayList<Integer> arr : upgradeList) {
                        int rank = arr.get(0), dollars = arr.get(1), credits = arr.get(2);
                        JRadioButton item = new JRadioButton(String.format("%s%d %s%d  %s%d ", rankPattern, rank, dollarsPattern, dollars, creditsPattern, credits));
                        item.setBorder(new EmptyBorder(1, 0, 0, 0));
                        radioButtonGroup.add(item);
                        buttonGroup.add(item);
                    }

                    creditButton.addActionListener(e1 -> {
                        int rank = 0, creditCost;
                        Matcher m = p.matcher(parseButtons(radioButtonGroup));
                        if (m.find()) {
                            rank = Integer.parseInt(m.group(1).trim());
                        }
                        creditCost = UtilitySet.getInstance().getUpgradeCurrency(rank)[1];
                        if (b.getCurrent().getCredits() < creditCost) {
                            updateStatusMessage("Error: Insufficient Credits");
                            upgradePanel.dispose();
                        } else {
                            updateStatusMessage(String.format("Upgraded %s to Rank %d!", b.getCurrent().getName(), rank));
                            b.setRank(rank);
                            b.payCredits(creditCost);
                            try {
                                UtilitySet.getInstance().dieUpgrade();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            upgradePanel.dispose();
                        }
                        setPlayerInfo(b.getCurrent());
                    });

                    dollarButton.addActionListener(e1 -> {
                        int rank = 0, dollarCost;
                        Matcher m = p.matcher(parseButtons(radioButtonGroup));
                        if (m.find()) {
                            rank = Integer.parseInt(m.group(1).trim());
                        }
                        dollarCost = UtilitySet.getInstance().getUpgradeCurrency(rank)[1];
                        if (b.getCurrent().getRank() == rank) {
                            updateStatusMessage("Error: Rank Already Achieved");
                        } else if (b.getCurrent().getDollars() < dollarCost) {
                            updateStatusMessage("Error: Insufficient Dollars");
                            upgradePanel.dispose();
                        } else {
                            updateStatusMessage(String.format("Upgraded %s to Rank %d!", b.getCurrent().getName(), rank));
                            b.setRank(rank);
                            b.payDollars(dollarCost);
                            try {
                                UtilitySet.getInstance().dieUpgrade();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            upgradePanel.dispose();
                        }
                        setPlayerInfo(b.getCurrent());
                    });

                    buttonPanel.add(dollarButton);
                    buttonPanel.add(creditButton);
                    upgradePanel.add(buttonGroup);
                    upgradePanel.add(buttonPanel);
                    upgradePanel.setLocationRelativeTo(null);
                    upgradePanel.setAlwaysOnTop(true);
                    upgradePanel.setVisible(true);
                    b.changeStatus(Work.UPGRADED);
                    b.replacePlayer();
                    repaint();
                } else if (b.getCurrent().getSetNo() != b.getSetLocation("Casting Office")) {
                    updateStatusMessage("Error: You Aren't At The Casting Office");
                } else {
                    updateStatusMessage("Error: Illegal Action");
                }
            } else if (e.getSource() == endTurnButton) {
                updateStatusMessage("");

                if (b.getCurrent().getStatus() == Work.MOVED || b.getCurrent().getStatus() == Work.UPGRADED) {
                    b.changeStatus(Work.NULL);
                } else if (b.getCurrent().getStatus() == Work.PREMAIN || b.getCurrent().getStatus() == Work.POSTMAIN) {
                    b.changeStatus(Work.MAIN);
                } else if (b.getCurrent().getStatus() == Work.PREEXTRA || b.getCurrent().getStatus() == Work.POSTEXTRA) {
                    b.changeStatus(Work.EXTRA);
                }
                b.replacePlayer();
                try {
                    b.switchPlayers();
                    updateStatusMessage(String.format("Switched to Player: %s", b.getCurrent().getName()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println(Board.getInstance().getCurrent().printPlayer());
        }

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        private String parseButtons(ButtonGroup buttonGroup) {
            AbstractButton ret = null;
            for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements(); ) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    ret = button;
                }
            }
            assert ret != null;
            return ret.getActionCommand();
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setOpaque(false);
        final JPanel spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.weightx = 2.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.weighty = 5.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weighty = 6.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer3, gbc);
        buttonPane = new JPanel();
        buttonPane.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.gridheight = 4;
        mainPanel.add(buttonPane, gbc);
        moveButton = new JButton();
        Font moveButtonFont = this.$$$getFont$$$(null, -1, 18, moveButton.getFont());
        if (moveButtonFont != null) moveButton.setFont(moveButtonFont);
        moveButton.setText("Move");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPane.add(moveButton, gbc);
        workButton = new JButton();
        Font workButtonFont = this.$$$getFont$$$(null, -1, 18, workButton.getFont());
        if (workButtonFont != null) workButton.setFont(workButtonFont);
        workButton.setText("Work");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPane.add(workButton, gbc);
        upgradeButton = new JButton();
        Font upgradeButtonFont = this.$$$getFont$$$(null, -1, 18, upgradeButton.getFont());
        if (upgradeButtonFont != null) upgradeButton.setFont(upgradeButtonFont);
        upgradeButton.setText("Upgrade");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPane.add(upgradeButton, gbc);
        endTurnButton = new JButton();
        Font endTurnButtonFont = this.$$$getFont$$$(null, -1, 18, endTurnButton.getFont());
        if (endTurnButtonFont != null) endTurnButton.setFont(endTurnButtonFont);
        endTurnButton.setText("End Turn");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPane.add(endTurnButton, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPane.add(spacer4, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPane.add(panel1, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setVerticalScrollBarPolicy(21);
        scrollPane1.setWheelScrollingEnabled(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(scrollPane1, gbc);
        playerTable = new JTable();
        playerTable.setEnabled(false);
        playerTable.setShowVerticalLines(false);
        playerTable.setUpdateSelectionOnSort(false);
        scrollPane1.setViewportView(playerTable);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPane.add(spacer5, gbc);
        takeRoleButton = new JButton();
        Font takeRoleButtonFont = this.$$$getFont$$$(null, -1, 18, takeRoleButton.getFont());
        if (takeRoleButtonFont != null) takeRoleButton.setFont(takeRoleButtonFont);
        takeRoleButton.setForeground(new Color(-16777216));
        takeRoleButton.setHideActionText(false);
        takeRoleButton.setText("Take Role");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPane.add(takeRoleButton, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPane.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 3.0;
        gbc.weighty = 3.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 6;
        gbc.weightx = 0.125;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer8, gbc);
        gameInfoPanel = new JPanel();
        gameInfoPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(gameInfoPanel, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gameInfoPanel.add(panel2, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel3, gbc);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer9, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, 18, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-16777216));
        label1.setText("Current Player:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel3.add(label1, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer10, gbc);
        currentPlayerLabel = new JLabel();
        Font currentPlayerLabelFont = this.$$$getFont$$$(null, -1, 18, currentPlayerLabel.getFont());
        if (currentPlayerLabelFont != null) currentPlayerLabel.setFont(currentPlayerLabelFont);
        currentPlayerLabel.setText("    ");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        panel3.add(currentPlayerLabel, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer11, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gameInfoPanel.add(spacer12, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gameInfoPanel.add(spacer13, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gameInfoPanel.add(spacer14, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gameInfoPanel.add(panel4, gbc);
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, -1, 18, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-16777216));
        label2.setText("Day:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(label2, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer15, gbc);
        dayLabel = new JLabel();
        Font dayLabelFont = this.$$$getFont$$$(null, -1, 18, dayLabel.getFont());
        if (dayLabelFont != null) dayLabel.setFont(dayLabelFont);
        dayLabel.setForeground(new Color(-1766144));
        dayLabel.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        panel4.add(dayLabel, gbc);
        final JPanel spacer16 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer16, gbc);
        final JPanel spacer17 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer17, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gameInfoPanel.add(panel5, gbc);
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, -1, 18, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setForeground(new Color(-16777216));
        label3.setText("Total Days:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel5.add(label3, gbc);
        final JPanel spacer18 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(spacer18, gbc);
        totalDayLabel = new JLabel();
        Font totalDayLabelFont = this.$$$getFont$$$(null, -1, 18, totalDayLabel.getFont());
        if (totalDayLabelFont != null) totalDayLabel.setFont(totalDayLabelFont);
        totalDayLabel.setForeground(new Color(-1766144));
        totalDayLabel.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        panel5.add(totalDayLabel, gbc);
        final JPanel spacer19 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(spacer19, gbc);
        final JPanel spacer20 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(spacer20, gbc);
        imagePane = new JLayeredPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(imagePane, gbc);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(panel6, gbc);
        statusLabel = new JLabel();
        Font statusLabelFont = this.$$$getFont$$$(null, -1, 24, statusLabel.getFont());
        if (statusLabelFont != null) statusLabel.setFont(statusLabelFont);
        statusLabel.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel6.add(statusLabel, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
