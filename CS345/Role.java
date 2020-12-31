/*
* Role
*
* Contains all pertinent information to a 'role' a player might take.
* */

public class Role {
    private String roleName;
    private int rank;
    private final int[] location;
    private boolean taken;

    public Role(String name) {
        this.roleName = name;
        this.rank = 0;
        this.taken= false;
        this.location = new int[2];
    }

    public String getName() {
        return this.roleName;
    }

    public boolean isTaken(){
        return this.taken;
    }

    public void setName(String name) {
        this.roleName = name;
    }

    public int[] getLocation(){
        return this.location;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void takeRole() {
        this.taken = true;
    }

    public void finishRole(){this.taken = false;}

    public void setLocation(int x, int y){
        location[0] = x;
        location[1] = y;
    }

}