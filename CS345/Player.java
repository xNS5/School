import javax.swing.*;

/*
* Player
*
* Player model with associated get and set functions.
* */
public class Player {
    private int dollars;
    private int credits;
    private int rank;
    private int setNo;
    private int score;
    private int rehearsalChips;
    private String location;
    private final String name;
    private Work status;
    private final PlayerColor color;
    private Role role;
    private JLabel die;

    public Player(String name, int rank, int dollars, int credits, PlayerColor color, Work status) {
        this.name = name;
        this.rank = rank;
        this.dollars = dollars;
        this.credits = credits;
        this.status = status;
        this.role = null;
        this.status = Work.NULL;
        this.rehearsalChips = 0;
        this.location = "";
        this.setNo = 0;
        this.color = color;
        this.die = null;
        this.score = 0;
        this.rehearsalChips = 0;
    }

    public int getDollars() {
        return this.dollars;
    }

    public int getCredits() {
        return this.credits;
    }

    public int getRank() {
        return this.rank;
    }

    public int getChips(){
        return this.rehearsalChips;
    }

    public int getScore(){
        this.score = this.credits + this.dollars + (6 * this.rank);
        return this.score;
    }

    public String getName() {
        return this.name;
    }

    public Work getStatus() {
        return this.status;
    }

    public Role getRole(){
        return this.role;
    }

    public JLabel getDie(){
        return this.die;
    }

    public PlayerColor getColor(){
        return this.color;
    }

    public int getSetNo(){
        return this.setNo;
    }

    public void setRank(int rank) { this.rank = rank; }

    public void setDie(JLabel die){
        this.die = die;
    }

    public void setLocation(String location, int setNo){
        this.location = location;
        this.setNo = setNo;
    }

    public void setRole(Role r){
        this.role = r;
    }

    public void updateCredits(int credits) {
        this.credits += credits;
    }

    public void updateDollars(int dollars) {
        this.dollars += dollars;
    }

    public void payDollars(int dollars){
        this.dollars -= dollars;
    }

    public void payCredits(int credits){
        this.credits -= credits;
    }

    public void setStatus(Work status) {
        this.status = status;
    }

    public void updateChip(){  this.rehearsalChips+=1; }

    public void resetChips(){
        this.rehearsalChips = 0;
    }

    public String printPlayer(){
        String var = "None";
        if(getRole() != null){
            var = this.role.getName();
        }
        return String.format("==============================\nPlayer: %s \nLocation: %s \nRole: %s\nRank: %d \nCredits: %d \nDollars: %d \nChips: %d\nStatus: %s\r\n==============================\n", this.name, this.location, var, this.rank, this.credits, this.dollars, this.rehearsalChips,this.status);
    }
}