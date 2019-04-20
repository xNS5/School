import java.util.*;

/*
* Author: Michael Kennedy
* Assignment: RecordCollection lab
* Description: See Writeup file
*
*
*
*
*
*
*
*
*/

public class lab2
  {
    public ArrayList<RecordCollection> container = new ArrayList<RecordCollection>();
    public static void main(String[] args)
      {
        boolean done = false;
        while(done == false)
          {
            Scanner sc = new Scanner(System.in);
            System.out.println("1. Add a record");
            System.out.println("2. Update information");
            System.out.println("3. Search");
            System.out.println("4. Print entire list sorted by year\n");
            System.out.println("Press 0 to quit");
            System.out.print("Please make a selection: ");
            int input = sc.nextInt();
            while (input < 0 || input > 4)
              {
                System.out.print("I'm sorry, that is an invalid option. Please try again: ");
                input = sc.nextInt();
              }

            switch(input)
              {
                case 0:
                  done = true;
                  break;
                case 1:
                  records();
                  break;
                case 2:
                  updateList();
                  break;
                case 3:
                  searchList();
                  break;
                case 4:
                  sortList();
                  break;
              }

        }

      }

    public static void records()
      {
        System.out.println("Calls method records, which adds information to the class ArrayList 'container'");
      }
    public static void updateList()
      {
        System.out.println("User inputs artist name or album, method prints out information and user is allowed to update specific fields. If number of records is 0, the record is deleted");
      }
    public static void searchList()
      {
        System.out.println("Called method searchList, which will allow the user to search for an artist/album and will display the information associated with that album/artist ");
      }
    public static void sortList()
      {
        System.out.println("You called the sort method, which will sort by year");
      }

  }
