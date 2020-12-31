import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;



/*
* Hal
*
* The game manager of the game. Maintains data on the current day, generates die values,
* parses game and card info from the .xml files.
* */
public class Hal {
    private static Hal instance = null;
    private int days;
    private int currentDay;
    private final ArrayList<Scene> scenes;
    private final ArrayList<Set> sets;

    /*
    * Constructor
    * */
    private Hal() {
        this.days = 0;
        this.currentDay = 1;
        this.scenes = new ArrayList<>();
        this.sets = new ArrayList<>();
    }

    public static Hal getInstance() {
        if (instance == null) {
            instance = new Hal();
        }
        return instance;
    }

    // This increases the current day value
    public void updateDay() throws IOException {
        this.currentDay += 1;
        BoardUI.getInstance().updateDayLabel(currentDay);
    }

    // This gets the total number of days, how many days total one game will last
    public int getDay() {
        return this.days;
    }

    // This determines if the game is over. If the current day is one greater than the total number of days, it will return true.
    public boolean gameOver(){
        return currentDay > days;
    }

    // Rolls die, returns int[] array. Seemed easier to do that instead of running rollDie n times.
    public int[] rollDie(int val) {
        Random rm = new Random();
        int[] ret = new int[val];
        for(int i = 0; i < val; i++){
            ret[i] = rm.nextInt(6-1)+1;
        }
        return ret;
    }

    // This function sets the total number of days in the game.
    private void setDays(int days) {
        this.days = days;
    }

    // This function resets the board at the end of the day.
    public void resetBoard() throws IOException, SAXException, ParserConfigurationException {
        Random rand = new Random();
        Board board = Board.getInstance();
        BoardUI bui = BoardUI.getInstance();
        if(board.getSets().size() != 0){
            bui.updateStatusMessage("Day concluded!");
            updateDay();
            board.resetPlayers();
            bui.clearBoard();
        }
        if(scenes.size() < sets.size()){
            parseCards();
        }
        for(Set set : sets) {
            if (!set.getName().equals("Trailers") && !set.getName().equals("Casting Office")) {
                int index = rand.nextInt(sets.size());
                Scene temp = scenes.get(index);
                set.resetVisitedStatus();
                set.reactivate();
                set.setScene(temp);
                scenes.remove(index);
            }
            for (int i = 0; i < board.getSets().size(); i++) {
                board.getSets().get(i).resetShots();
            }
        }
        board.setSet(sets);
        bui.deployCardBacks();
        bui.deployCards();

    }

    /*
    * Sets the number of days and credits based on the number of players.
    * */
    public void prepPlayers(ArrayList<Player> players) throws IOException {
        Board board = Board.getInstance();
        BoardUI boardUI = BoardUI.getInstance();
        int numPlayers = players.size();
        setDays(4);
        if (numPlayers == 2 || numPlayers == 3) {
            setDays(3);
        } else if (numPlayers == 5) {
            for (Player p : players) {
                p.updateCredits(2);
            }
        } else if (numPlayers == 6) {
            for (Player p : players) {
                p.updateCredits(4);
            }
        } else if (numPlayers == 7 || numPlayers == 8) {
            for (Player p : players) {
                p.setRank(2);
            }
        }

       boardUI.updateTotalDayLabel(getDay());
        players = setPlayerColors(players);
        for(Player player : players){
            boardUI.placePlayerDie(player, 0);
        }
        board.resetPlayers(players);
    }

    // Pretty self explanatory, sets the player's colors.
    private ArrayList<Player> setPlayerColors(ArrayList<Player> players){
        HashMap<String, Integer> colorMap = new HashMap<>();
        ArrayList<Player> playerArr = new ArrayList<>();
        int count = 1;
        for(Player p : players){
            String prefix = dieHelper(p.getColor());
            if(colorMap.containsKey(prefix)){
                count+=1;
                colorMap.replace(prefix, count);
            }
            colorMap.put(prefix, count);
            ImageIcon die = new ImageIcon(String.format("./dice/%s%d.png", prefix, count));
            JLabel dieLabel = new JLabel();
            dieLabel.setIcon(die);
            int[] dimensions = Board.getInstance().getSet(p.getSetNo()).getLocation();
            dieLabel.setBounds(dimensions[0], dimensions[1], die.getIconWidth(), die.getIconHeight());
            p.setDie(dieLabel);
            playerArr.add(p);
        }
        Collections.reverse(playerArr);
        return playerArr;
    }

    /*
    * Starts parses the board.xml file and gets the board class ready.
    * */
    public void prepGame() throws IOException, NullPointerException, ParserConfigurationException, SAXException {
        Board board = Board.getInstance();
        BoardUI.getInstance().updateDayLabel(1);
        parseCards();
        File xmlBoard = new File("board.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document d = db.parse(xmlBoard);
        d.getDocumentElement().normalize();
        NodeList setList = d.getElementsByTagName("set");
        for (int i = 0; i < setList.getLength(); i++) {
            Node setNode = setList.item(i);
            Set set = new Set();
            if (setNode.getNodeType() == Node.ELEMENT_NODE) {
                Element setElement = (Element) setNode;
                String setName = setElement.getAttribute("name");
                int setNumber = Integer.parseInt(setElement.getAttribute("number"));
                int x = Integer.parseInt(setElement.getAttribute("x")) + 25, y = Integer.parseInt(setElement.getAttribute("y")) + 50;
                set.setName(setName);
                set.setNumber(setNumber);
                set.setLocation(x, y);
                board.addSetLocation(setName, setNumber);

                if (setName.equals("Casting Office")) {
                    NodeList upgradeList = setElement.getElementsByTagName("upgrade");
                    UtilitySet us = UtilitySet.getInstance();
                    for (int j = 0; j < upgradeList.getLength(); j++) {
                        Element upgrade = (Element) upgradeList.item(j);
                        us.castingUpgrade(Integer.parseInt(upgrade.getAttribute("rank")), Integer.parseInt(upgrade.getAttribute("dollars")), Integer.parseInt(upgrade.getAttribute("credits")));
                    }
                } else if (!setName.equals("Trailers")) {
                    ImageIcon cardBack = new ImageIcon("./cards/cardback.jpg");
                    JLabel card = new JLabel();
                    card.setIcon(cardBack);
                    card.setBounds(x, y, cardBack.getIconWidth(), cardBack.getIconHeight());
                    set.setCardBack(card);

                    // Getting list of extras
                    NodeList extras = setElement.getElementsByTagName("extra");
                    for (int j = 0; j < extras.getLength(); j++) {
                        set.setExtras(roleHelper((Element) extras.item(j), 0));
                    }

                    // getting count of takes.
                    // temp[0] is the take number, rest are location information
                    // Also adding shot components to the Set object.
                    NodeList takes = setElement.getElementsByTagName("take");
                    set.setShots(takes.getLength());
                    ImageIcon shotIcon = new ImageIcon("./cards/shot.png");
                    for (int j = 0; j < takes.getLength(); j++) {
                        Element takeElem = (Element) takes.item(j);
                        int[] temp = new int[2];
                        temp[0] = Integer.parseInt(takeElem.getAttribute("x")) + 27;
                        temp[1] = Integer.parseInt(takeElem.getAttribute("y")) + 51;
                        JLabel takeLabel = new JLabel(shotIcon);
                        takeLabel.setBounds(temp[0], temp[1], shotIcon.getIconWidth(), shotIcon.getIconHeight());
                        set.setShotComponent(takeLabel);
                        set.setTakeLocations(temp);
                    }
                }
                // Getting all of the elements for current set under tag "neighbor"
                NodeList neighbors = setElement.getElementsByTagName("neighbor");
                for (int j = 0; j < neighbors.getLength(); j++) {
                    set.setNeighbor(neighbors.item(j).getTextContent()); // setting each neighbor to set object
                }
                sets.add(set);
            }
        }
        resetBoard();
    }

    /*
    * Parses newCards.xml file
    * */
    private void parseCards() throws ParserConfigurationException, IOException, SAXException {
        File xmlCards = new File("newCards.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document d = db.parse(xmlCards);
        d.getDocumentElement().normalize();
        // List of Cards
        NodeList cardList = d.getElementsByTagName("card");
        for (int i = 0; i < cardList.getLength(); i++) {
            Node cardNode = cardList.item(i);
            Scene scene = new Scene();
            if (cardNode.getNodeType() == Node.ELEMENT_NODE) {
                Element cardElement = (Element) cardNode;
                // Title of the scene
                scene.setTitle(cardElement.getAttribute("name"));
                // Filename
                scene.setFileName(cardElement.getAttribute("img"));
                // Budget of the scene
                scene.setBudget(Integer.parseInt(cardElement.getAttribute("budget")));
                // Scene's Caption
                scene.setCaption(cardElement.getElementsByTagName("scene").item(0).getTextContent());

                // Adding card Icon elements to the scene
                scene.setCardLabel(new JLabel());

                // List of characters
                NodeList roleList = cardElement.getElementsByTagName("part");
                for (int j = 0; j < roleList.getLength(); j++) {
                    scene.setRole(roleHelper((Element) roleList.item(j), 1));
                }
                scenes.add(scene);
            }
        }
    }

    /*
    * I created this helper because both the Scene and the Set have roles -- main characters and extras
    * */
    private Role roleHelper(Element roleElement, int state){
        Role character = new Role(roleElement.getAttribute("name"));
        character.setRank(Integer.parseInt(roleElement.getAttribute("rank")));
        if(state == 0) {
            character.setLocation(Integer.parseInt(roleElement.getAttribute("x")) + 25, Integer.parseInt(roleElement.getAttribute("y")) + 50);
        } else {
            character.setLocation(Integer.parseInt(roleElement.getAttribute("x")), Integer.parseInt(roleElement.getAttribute("y")));
        }
        return character;
    }

    // Helper to parse die colors
    private String dieHelper(PlayerColor p) {
        switch (p) {
            case BLUE:
                return "b";
            case CYAN:
                return "c";
            case GREEN:
                return "g";
            case ORANGE:
                return "o";
            case PINK:
                return "p";
            case RED:
                return "r";
            case VIOLET:
                return "v";
            case YELLOW:
                return "y";
        }
        return "";
    }
}