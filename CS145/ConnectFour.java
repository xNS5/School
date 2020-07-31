import java.util.*;


public class ConnectFour{
  public static void main(String[] args){
    System.out.println("This is a game of Connect Four! Please coose a row to drop a tile into.");
    displayBoard();
    beginPlaying();
}

  public static void displayBoard(){
    char[][] playBoard = new char[6][7];
    for (int x = 0; x < 6; x++){
      for (int y = 0; y < 7; y++){
        playBoard[x][y] = ' ';
        System.out.print("|  "+playBoard[x][y]+"  ");
      }
      System.out.print("|");
      System.out.println();
    }
    System.out.print("+-----+-----+-----+-----+-----+-----+-----+");
    System.out.println();
    for (int x = 0; x < 7; x++){
      System.out.print("   "+x+"  ");
    }
    System.out.println();
  }


  public static void beginPlaying(){
    Scanner sc = new Scanner(System.in);
    int x = 0;
    while (true){
      System.out.print("Player "+playerCheck(x)+"'s turn: ");
      int t = sc.nextInt();
      if (t < 0 || t > 7){
        System.out.println("I'm sorry, that isn a valid input. Please try again.");
        System.out.print("Player "+playerCheck(x)+"'s turn: ");
        t = sc.nextInt();
      }
      x++;
    }

  }

  public static int playerCheck(int x){
    int t = 0;
      if(x%2 != 0){
        t = 1;
      }
      else{
        t = 2;
      }
    return t;
  }
}
