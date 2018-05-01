import java.util.*;

public class Airport
  {
    public static void airports(Scanner ac, Scanner dc)
      {
        LinkedList<LinkedList<String>> coordinates = new LinkedList<LinkedList<String>>();
        LinkedList<String> airports = new LinkedList<String>();
        while(ac.hasNext())
          {
            coordinates.add(coordinates(ac));
          }
        while(dc.hasNext())
          {
            airports.add(dc.next());
          }

        airportCoordinates(airports, coordinates);

        }

      public static LinkedList<String> coordinates(Scanner ac)
          {
            LinkedList<String> location = new LinkedList<String>();
            location.add(ac.next());
            location.add(ac.next());
            location.add(ac.next());
            return location;
          }

      public static void airportCoordinates(LinkedList<String> airports, LinkedList<LinkedList<String>> coordinates)
        {
          int i = 0;
          for(LinkedList<String> locations : coordinates)
            {
              if(locations.get(0).equals(airports.get(i)))
                {
                  System.out.printf("%s, %s,  %s\n", locations.get(1), locations.get(2), airports.get(i));
                  i++;
                }
              }
            }
      }
