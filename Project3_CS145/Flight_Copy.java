/*
* Author: Michael Kennedy
* Description: This project takes in data from text files and organizes them into a single linked list
* of flights.
*
*
*/

import java.util.*;
import java.io.*;

public class Flight
  {
    public static void main(String[] args) throws FileNotFoundException
    {
      try
        {
          if (args[0].equals("-l") && args[1].equals("ap_loc.txt")) // if user inputs -l and ap_loc then it goes to the airport class
            {
              Airport airport = new Airport();
              File f = new File(args[1]);
              File g = new File(args[2]);
              Scanner ac = new Scanner(f); // Scanner for Airport Coordinates
              Scanner dc = new Scanner(g); // Scanner for linked List of airport names

              Airport.airports(ac, read_file_2(dc));
            }
          else if(args[0].equals("-l") && !args[1].equals("ap_loc.txt"))
            {
              System.out.println("Please input -l to print airport locations");
            }
        else // Just creates a single linked list
         {
           File f = new File(args[0]);
           Scanner sc = new Scanner(f);
           read_file(sc);
         }

        }
        catch (Exception e)
          {
            System.out.println(e);
            System.out.printf("%s doesn't exist\n", args[0]);
          }
        }

        // Adds the hops to a linked list to create a linked list of linked lists
        public static void read_file(Scanner sc)
        {
          LinkedList<LinkedList<String>> flight = new LinkedList<LinkedList<String>>();
          while (sc.hasNext())
            {
              flight.add(read_Hop(sc));
            }
            LinkedList<String> container = addVal(flight);

            System.out.println("Number of hops: "+ container.size());
            for(int j = 0; j < container.size(); j++)
              {
                System.out.println(container.get(j));
              }
              System.out.println();
          }

          // Adds the hops to a linked list to create a linked list of linked lists
          public static LinkedList<String> read_file_2(Scanner sc)
          {
            LinkedList<LinkedList<String>> flight = new LinkedList<LinkedList<String>>();
            while (sc.hasNext())
              {
                flight.add(read_Hop(sc));
              }
              LinkedList<String> container = addVal(flight);
              return container;

            }

    // Reads the individual hops in the file
    public static LinkedList<String> read_Hop(Scanner sc)
        {
          LinkedList<String> hop = new LinkedList<String>();
          hop.add(sc.next());
          hop.add(sc.next());
          return hop;
        }

    /*
    * Combines the linked list into a signle linked list
    *
    */

    public static LinkedList<String> addVal(LinkedList<LinkedList<String>> flight)
    {
      LinkedList<String> container = new LinkedList<String>();
      container.add(flight.get(0).get(0));
      container.add(flight.get(0).get(1));

        for(LinkedList<String> hop : flight)
          {
            if(container.get(container.size()-1).equals(hop.get(0)))
              {
                container.add(hop.get(1));
                flight.remove(hop);
              }
           else if(container.get(0).equals(hop.get(1)))
              {
                container.push(hop.get(0));
                flight.remove(hop);
              }
          }
        return container;
      }
    }
