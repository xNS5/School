import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
* Deadwood
*
* The entry point of the game, prompts players for their names and chosen colors. Once chosen,
* the game will start.
* */

public class Deadwood {
    public static void main(String[] args) {
        try {
            int numPlayers = 0;
            String colorStr = "";
            Hal driver = Hal.getInstance();
            Board board = Board.getInstance();
            BoardUI boardUI = BoardUI.getInstance();

            ArrayList<Player> players = new ArrayList<>();
            ArrayList<String> validColors = new ArrayList<>(Arrays.asList("blue","cyan","green","orange","pink","red","violet","yellow"));

            Scanner sc = new Scanner(new File("opening.txt"));
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
            // Instead of creating a new scanner, I just change the scanner input from File to System.in
            sc = new Scanner(System.in);

            // Parsing players
            while (numPlayers < 2 || numPlayers > 8) {
                System.out.print("Please enter the number of players [2-8]: ");
                while (!sc.hasNext("[2-8]")) {
                    System.out.print("Incorrect input. Please enter the number of players [2-8]: ");
                    sc.next();
                }
                numPlayers = sc.nextInt();
            }

            for (int i = 0; i < numPlayers; i++) {
                colorStr = "";
                System.out.printf("Player %d, what is your name? ", i + 1);
                String name = sc.next();
                sc = new Scanner(System.in);
                System.out.println(validColors.toString());
                while(!validColors.contains(colorStr)){
                    System.out.printf("%s, what color do you want? ", name);
                    colorStr = sc.next();
                    if(!validColors.contains(colorStr.toLowerCase())){
                        System.out.println("Invalid color. Please try again");
                    }

                }
                PlayerColor color = PlayerColor.valueOf(colorStr.toUpperCase());
                players.add(new Player(name, 1, 0, 0, color, Work.NULL));
                players.get(0).updateDollars(200);
            }

            driver.prepGame();
            driver.prepPlayers(players);
            board.switchPlayers();
            boardUI.setPlayers(players);
            boardUI.setVisible(true);


        } catch (Exception e) {
            System.out.println("Something went wrong:");
            e.printStackTrace();
        }
    }

}
