import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;


/*
* Board
*
* This is the Board model of the game. Maintains a list of players, controls player movement, switches player at the end of a turn,
* assigns roles, adds currency, etc.
* */

public class Board{
    private static Board instance = null;
    private Player current;
    private final Queue<Player> playerQueue;
    private ArrayList<Set> sets;
    private final HashMap<String, Integer> setLocations;
    private int numScenes;

    /*
    * Board Constructor
    * */
    private Board() {
        this.current = null;
        this.sets = new ArrayList<>();
        this.setLocations = new HashMap<>();
        this.numScenes = 0;
        this.playerQueue = new LinkedList<>();
    }

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    /*
    * Moving player
    * */
    public void movePlayer(String destination) {
        Set current = sets.get(this.current.getSetNo());
        Set Destination = sets.get(setLocations.get(destination));
        this.current.setLocation(Destination.getName(), setLocations.get(destination));
        Destination.addPlayer(this.current);
        current.removePlayer(this.current);
    }

    /*
    * Sets the current player.
    * */
    public void switchPlayers() throws IOException {
        if (playerQueue.size() != 0) {
            Player next = playerQueue.remove();
            if (this.current != null) {
                sets.get(this.current.getSetNo()).replacePlayer(this.current);
                playerQueue.add(this.current);
                this.current = next;
            }
            BoardUI.getInstance().updateCurrentPlayerLabel(this.current.getName());
        }
    }

    /*
     *  Gets the current player.
     * */
    public Player getCurrent(){
        return this.current;
    }

    // Changing status of current player
    public void changeStatus(Work status){
        this.current.setStatus(status);
    }

    // Putting player into their set's arraylist
    public void replacePlayer(){
        sets.get(this.current.getSetNo()).replacePlayer(this.current);
    }

    public void replacePlayer(Player player){
        sets.get(player.getSetNo()).replacePlayer(player);
    }

    // Giving the current player a role
    public void setRole(Role role){
        this.current.setRole(role);
    }

    //Setting current player's rank
    public void setRank(int rank){
        this.current.setRank(rank);
    }

    //Removing credits from player
    public void payCredits(int credits){
        this.current.payCredits(credits);
    }

    //Removing dollars from player
    public void payDollars(int dollars){
        this.current.payDollars(dollars);
    }

    /*
    * Getting the set location from the hashmap. Hashmaps have O(1) retrieval so I figured this would be the easiest way to keep track of set indexes.
    * */
    public int getSetLocation(String name) {
        return this.setLocations.get(name);
    }

    /*
     * Returns the set arraylist
     * */
    public ArrayList<Set> getSets() {
        return this.sets;
    }

    /*
    *  Gets the set at a specific index
    * */
    public Set getSet(int x){
        return this.sets.get(x);
    }

    /*
    * Player decides to work. I opted to put this in the "Board" class because board has the ArrayList of sets and manipulates player objects within the set objects
    * */
    public void work(int choice) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        Hal driver = Hal.getInstance();
        BoardUI boardUI = BoardUI.getInstance();

        if (choice == 0) {
            act();
        } else {
            rehearse();
        }
        if (driver.gameOver()) {
            Player winner = null;
            int score = Integer.MIN_VALUE;
            playerQueue.add(this.current);
            for(int i = 0; i < playerQueue.size(); i++){
                Player curr = playerQueue.remove();
                boardUI.removeComponent(curr.getDie());
                if(curr.getScore() > score){
                    winner = curr;
                }
            }
            assert winner != null;
            boardUI.finishGame(winner);
        } else if(numScenes == 1) {
            driver.resetBoard();
        } else{
            postWorkHelper();
        }
    }

    // Scene bonus only applies the top rolled numbers because the rules written in the game description were confusing.
    private void act() throws IOException, ParserConfigurationException, SAXException {
        BoardUI boardUI = BoardUI.getInstance();
        Set currSet = sets.get(this.current.getSetNo());
        Scene currScene = currSet.getScene();
        int budget = currScene.getBudget();
        int randVal = Hal.getInstance().rollDie(1)[0];
        if (randVal + this.current.getChips() >= budget) {
            boardUI.updateStatusMessage(String.format("You rolled: %d. Success!",randVal));
            boardUI.removeComponent(currSet.getTake(currSet.getShots() - 1));
            currSet.updateShots();
            if (this.current.getStatus() == Work.MAIN) {
                this.current.updateCredits(2);
            } else if (this.current.getStatus() == Work.EXTRA) {
                this.current.updateDollars(1);
                this.current.updateCredits(1);
            }
        } else {
            boardUI.updateStatusMessage(String.format("You rolled: %d. Oops!", randVal));
            if (this.current.getStatus() == Work.EXTRA) {
                this.current.updateDollars(1);
            }
        }
        ArrayList<Player> workingPlayers = currSet.getPlayers();
        if (currSet.getShots() == 0) {
            boardUI.updateStatusMessage("And that's a wrap!");
            boardUI.removeComponent(currScene.getCardLabel());


            //Bonus
            if (currSet.workingCard()) {
                int[] bonusDie = Hal.getInstance().rollDie(budget);
                int counter = budget - 1;
                Arrays.sort(bonusDie);
                for (int i = workingPlayers.size() - 1; i >= 0; i--, counter--) {
                    Player curr = workingPlayers.get(i);
                    if (curr.getName().equals(this.current.getName())) {
                        if(this.current.getStatus() == Work.MAIN || this.current.getStatus() == Work.POSTMAIN){
                            this.current.updateDollars(bonusDie[counter]);
                        } else if (this.current.getStatus() == Work.EXTRA || this.current.getStatus() == Work.POSTEXTRA) {
                            this.current.updateDollars(curr.getRole().getRank());
                        }
                        workingPlayers.set(i, this.current);
                    } else {
                        if (curr.getStatus() == Work.MAIN || curr.getStatus() == Work.POSTMAIN) {
                            curr.updateDollars(bonusDie[counter]);
                        } else if (curr.getStatus() == Work.EXTRA || curr.getStatus() == Work.POSTEXTRA) {
                            curr.updateDollars(curr.getRole().getRank());
                        }
                    }

                }
            }
            for (int i = 0; i < workingPlayers.size(); i++) {
                Player workingPlayer = workingPlayers.get(i);
                workingPlayer.resetChips();
                workingPlayer.setStatus(Work.NULL);
                workingPlayer.setRole(null);
                workingPlayers.set(i, workingPlayer);
                boardUI.placePlayerDie(workingPlayer, 0);
            }
            for(int i = 0; i < currSet.getExtras().size(); i++){
                currSet.getExtras().get(i).finishRole();
            }
            currSet.deleteScene();
            updateSceneCount();
            currSet.deactivate();
        }
        boardUI.setPlayerInfo(this.current);
        this.replaceSet(currSet);
        this.replacePlayer();

        if(this.numScenes == 1){
            Hal.getInstance().resetBoard();
        }
    }

    // Adds rehearsal chip to current player
    private void rehearse() throws IOException {
        BoardUI boardUI = BoardUI.getInstance();
        this.current.updateChip();
        boardUI.updateStatusMessage(String.format("Added 1 Rehearsal Chip to: %s", this.current.getName()));
        boardUI.setPlayerInfo(this.current);
    }

    // Changes the current player's enum
    private void postWorkHelper() {
        if(this.current.getStatus() == Work.MAIN){
            this.current.setStatus(Work.POSTMAIN);
        } else if(this.current.getStatus() == Work.EXTRA) {
            this.current.setStatus(Work.POSTEXTRA);
        }
    }

    /*
    * This moves the players to the trailer set at the start of the game
    * */
    public void resetPlayers(ArrayList<Player> players) {
        for (Player p : players) {
            p.setLocation("Trailers", 0);
            playerQueue.add(p);
            this.sets.get(0).addPlayer(p);
        }
        this.current = playerQueue.remove();
    }

    // Moving players back to trailers at start of game.
    public void resetPlayers() throws IOException {
        BoardUI boardUI = BoardUI.getInstance();
        playerQueue.add(this.current);
        int size = playerQueue.size();
        for(int i = 0; i < size; i++){
            Player curr = playerQueue.remove();
            sets.get(curr.getSetNo()).resetPlayers();
            curr.setStatus(Work.NULL);
            curr.setRole(null);
            curr.resetChips();
            curr.setLocation("Trailers", 0);
            replacePlayer(curr);
            playerQueue.add(curr);
            boardUI.placePlayerDie(curr, 0);
        }
        this.current = playerQueue.remove();
    }

    /*
    * Adding sets to the board
    *  */
    public void setSet(ArrayList<Set> newSet) {
        this.sets = newSet;
        this.numScenes = newSet.size()-2;
    }

    // Replaces a given set with a new one.
    public void replaceSet(Set s){
        sets.set(s.getSetNumber(), s);
    }

    /*
    * Updates number of scenes
    * */
    public void updateSceneCount(){
        this.numScenes = this.numScenes - 1;
    }

    /*
     * This adds the set number and name to the setLocations hashmap.
     * */
    public void addSetLocation(String name, int num) {
        this.setLocations.put(name, num);
    }

}