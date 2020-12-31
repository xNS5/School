import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;


/*
* UtilitySet
*
* Contains the "Casting Office" set and functions associated with changing ranks + die in game.
* */
public class UtilitySet{
    private static ArrayList<ArrayList<Integer>> castingOffice;
    private static UtilitySet instance = null;

    private UtilitySet() {
        castingOffice = new ArrayList<>();
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

    public String upgradesToString() {
        StringBuilder sb = new StringBuilder();
        for (ArrayList<Integer> upgrades : castingOffice) {
            sb.append(upgrades.toString()).append("\n");
        }
        return sb.toString();
    }

    public void dieUpgrade() throws IOException {
        Board board = Board.getInstance();
        Player current = board.getCurrent();
        String prefix_pre = current.getColor().name().toLowerCase(Locale.ROOT);
        String prefix = String.valueOf(prefix_pre.charAt(0));
        int rank = current.getRank();
        ImageIcon die = new ImageIcon(String.format("./dice/%s%d.png", prefix, rank));
        JLabel dieLabel = new JLabel();
        dieLabel.setIcon(die);
        dieLabel.setBounds(current.getDie().getBounds());
        BoardUI.getInstance().removeComponent(current.getDie());
        current.setDie(dieLabel);
        BoardUI.getInstance().placePlayerDie(current, 0);
    }

    public static ArrayList<ArrayList<Integer>> getUpgrades(){
        return castingOffice;
    }
}
