import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;


/*
* Test
*
* Test file where I can test various aspects of the game, e.g. movement, currency, etc. without having to input a name, color every time I need to run the game.
* */
public class test{
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, InterruptedException {
        Player p = new Player("Michael", 1, 100, 0, PlayerColor.BLUE, Work.NULL);
        Player n = new Player("Bubbles", 1, 100, 0, PlayerColor.BLUE, Work.NULL);
        p.setRank(6);
        n.setRank(6);
        for(int i = 0; i < 6; i++){
            p.updateChip();
            n.updateChip();
        }
        ArrayList<Player> pl = new ArrayList<>();
        pl.add(p);
        pl.add(n);

        Hal h = Hal.getInstance();
        Board b = Board.getInstance();
        BoardUI bui = BoardUI.getInstance();

        h.prepGame();
        h.prepPlayers(pl);
        b.switchPlayers();
        bui.setPlayers(pl);
        bui.setVisible(true);
    }
}