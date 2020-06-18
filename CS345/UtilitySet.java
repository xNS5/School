import java.util.ArrayList;

public class UtilitySet{
    private static ArrayList<ArrayList<Integer>> castingOffice;
    private static UtilitySet instance = null;

    private UtilitySet() {
        this.castingOffice = new ArrayList<>();
    }

    public static UtilitySet getInstance(){
        if(instance == null){
            instance = new UtilitySet();
        }
        return instance;
    }

    public void castingUpgrade(int rank, int dollars, int credits) {
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(rank);
        temp.add(dollars);
        temp.add(credits);
        castingOffice.add(temp);
    }

    public int[] getUpgradeCurrency(int upgrade){
        int[] ret = new int[2];
        for(ArrayList<Integer> arr : castingOffice){
            if(arr.get(0) == upgrade){
                ret[0] = arr.get(1);
                ret[1] = arr.get(2);
            }
        }
        return ret;
    }

    public String upgradesToString(){
        StringBuilder sb = new StringBuilder();
        for(ArrayList<Integer> upgrades : castingOffice){
            sb.append(upgrades.toString()).append("\n");
        }
        return sb.toString();
    }

    public static ArrayList<ArrayList<Integer>> getUpgrades(){
        return castingOffice;
    }
}
