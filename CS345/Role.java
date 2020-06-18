public class Role {
    private String roleName;
    private String roleLine;
    private int rank;
    private int[] location;
    private boolean taken = false;

    public Role(String name) {
        this.roleName = name;
        this.roleLine = "";
        this.rank = 0;
        this.taken= false;
        this.location = new int[2];
    }

    public String getName() {
        return this.roleName;
    }

    public String getLine() {
        return this.roleLine;
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

    public void setLine(String line) {
        this.roleLine = line;
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