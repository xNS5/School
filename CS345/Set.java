import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;


/*
* Set
*
* Has all information necessary for a player to act on a set, e.g. whether a set is active, number of shots on a specific scene, etc.
* */
public class Set{
    private Scene sceneCard;
    private String setName;
    private boolean visited, active;
    private int shots;
    private int shotCounter;
    private int setNumber;
    private final int[] location;
    private JLabel cardback;
    private final ArrayList<int[]> takeLocations;
    private final ArrayList<Role> extras;
    private final ArrayList<String> neighbors;
    private ArrayList<Player> players;
    private final ArrayList<JLabel> takeComponenets;


    public Set() {
        this.sceneCard = null;
        this.setName = "";
        this.shots = 0;
        this.setNumber = 0;
        this.location = new int[2];
        this.takeLocations = new ArrayList<>();
        this.takeComponenets = new ArrayList<>();
        this.neighbors = new ArrayList<>();
        this.extras = new ArrayList<>();
        this.players = new ArrayList<>();
        this.cardback = null;
        this.visited = false;
        this.active = true;
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public ArrayList<Role> getExtras() {
        if (this.extras.size() == 0) {
            return new ArrayList<>();
        }
        return this.extras;
    }

    public ArrayList<String> getNeighbors() {
        return this.neighbors;
    }

    public String getName(){
        return this.setName;
    }

    public Scene getScene() {
        return this.sceneCard;
    }

    public int getSetNumber() {
        return this.setNumber;
    }

    public int getShots() {
        return this.shotCounter;
    }

    public ArrayList<JLabel> getTakeComponenets(){
        return this.takeComponenets;
    }

    public JLabel getTake(int i){
        return this.takeComponenets.get(i);
    }

    public JLabel getCardBack(){
        return this.cardback;
    }

    public boolean hasPlayer(Player player) {
        for(Player p : players){
            if(p.getName().equals(player.getName())){
                return true;
            }
        }
        return false;
    }

    public boolean isVisited(){
        return this.visited;
    }

    public boolean isNeighbor(String s){
        for(String t : neighbors){
            if(t.equals(s)){
                return true;
            }
        }
        return false;
    }

    public boolean hasPlayers(){
        return this.players.size() != 0;
    }

    public void updateVisitedStatus(){
        this.visited = true;
    }

    public void resetVisitedStatus(){
        this.visited = false;
    }

    public void setScene(Scene scene) {
        this.sceneCard = scene;
    }

    public void setShots(int shots) {
        this.shots = shots;
        this.shotCounter = shots;
    }

    public void resetShots(){
        this.shotCounter = shots;
    }

    public void updateShots(){
        this.shotCounter -=1;
    }

    public void setLocation(int x, int y){
        this.location[0] = x;
        this.location[1] = y;
    }

    public void setTakeLocations(int[] location){
        this.takeLocations.add(location);
    }

    public int[] getLocation(){
        return this.location;
    }

    public void removePlayer(Player p){
        if(hasPlayer(p)){
            players.remove(p);
        }
    }

    public void setExtras(Role role) { this.extras.add(role); }

    public void setNeighbor(String neighbor) { this.neighbors.add(neighbor); }

    public void setName(String name) {
        this.setName = name;
    }

    public void deleteScene() throws IOException {
        this.active = false;
        BoardUI.getInstance().removeComponent(this.sceneCard.getCardLabel());
    }

    public void setShotComponent(JLabel shot){
        this.takeComponenets.add(shot);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void setNumber(int number) {
        this.setNumber = number;
    }

    public void setCardBack(JLabel c){
        this.cardback = c;
    }

    public void replacePlayer(Player player){
        for(int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(player.getName())) {
                players.set(i, player);
            }
        }
    }

    public void resetPlayers(){
        this.players = new ArrayList<>();
    }

    public void deactivate(){
        this.active = false;
    }

    public void reactivate(){
        this.active = true;
    }

    public boolean workingCard(){
        for(Player p : players){
            if(p.getStatus() == Work.MAIN){
                return true;
            }
        }
        return false;
    }

    public boolean hasScene(){
        return this.active;
    }

    public boolean isActive(){ return this.active; }
}