import java.util.Random;

public class LLTest {

  public static void main (String[] args) {
    if( !processArgs(args))
      System.err.println("Usage: java LLTest\n");
    else
      runTests();
  }

  public static boolean processArgs(String[] args) {
    return true;
  }

  public static boolean  runTests () {
    return
      //defaultConstructor() &&
      listAddGet() &&
      //listSize() &&
      //listRemove() &&
      //listOrder() &&
      true;
  }


  public static boolean defaultConstructor() {
    LinkedList<Integer> ll = new LinkedList<Integer>();
    if(ll == null)
      System.err.println("Default constructor failed to build a LinkedList object.");
    return ll != null;
  }

  // List Size
  // Test that the length of the linked list is correct.  There are really two
  // cases here: empty and 100 items.
  /*public static boolean listSize() {
    LinkedList<Double> ll = new LinkedList<Double>();
    boolean didPass = true;

    if(ll.size() != 0) {
      System.err.println("Default LinkedList has non-zero size.");
      didPass = false;
    }

    Random rnd = new Random();
    int i = 0;
    while(i < 100) {
      ll.add(rnd.nextDouble());
      ++i;
    }

    if(ll.size() != i) {
      System.err.printf("Added %d Integers to LinkedList, but size returned %d\n",
                        i, ll.size());
      didPass = false;
    }

    return didPass;
  }*/


  // List Remove
  // Does remove actually work?  This test has three cases: first item, middle
  // item, and an item that is not in the last.
  /*public static boolean listRemove() {
    LinkedList<Integer> ll = new LinkedList<Integer>();
    boolean didPass = true;

    int i = 0;
    while(i < 10) {
      ll.add(i);
      ++i;
    }

    Integer rem = ll.get(0);
    return true;
    ll.remove(rem);

    i = 0;
    for(Integer item : ll) {
      if(rem.equals(item)) {
        System.err.format("Removed %d, but found %d at position %d\n",
                          rem, item, i);
        didPass = false;
      }
      ++i;
    }

    rem = ll.get(4);
    ll.remove(rem);

    i = 0;
    for(Integer item : ll) {
      if(rem.equals(item)) {
        System.err.format("Removed %d, but found %d at position %d\n",
                          rem, item, i);
        didPass = false;
      }
      ++i;
    }

    rem = -100;
    ll.remove(rem);

    i = 0;
    for(Integer item : ll) {
      if(rem.equals(item)) {
        System.err.format("Removed %d, but found %d at position %d\n",
                          rem, item, i);
        didPass = false;
      }
      ++i;
    }
    return didPass;
  }*/


  // List Order
  // Linked lists are sequential containers like arrays.  As we add things to
  // the list, they should stay in that order.  To pass this test case, your
  // Linked List must have an iterator and be iterable.
  /*
  public static boolean listOrder() {
    LinkedList<Integer> ll = new LinkedList<Integer>();
    boolean didPass = true;

    Random rnd = new Random(0);
    int i = 0;
    while(i < 10) {
      ll.add(rnd.nextInt());
      ++i;
    }

    rnd = new Random(0);
    i = 0;
    for(Integer item : ll) {
      Integer expected = rnd.nextInt();
      if(!item.equals(expected)) {
        System.err.format("Expected %d, but got %d in position %d\n",
                          expected, item, i);
        didPass = false;
      }
      ++i;
    }

    return didPass;
  }
  */
  // Function that tests the add and get functions
  //
  //
  public static boolean listAddGet()
    {
      LinkedList<Integer> ll = new LinkedList<Integer>();
      for(int i = 0; i < 3; i++)
        {
          ll.add(i);
        }
        for(int i = 0; i < 3; i++)
          {
            System.out.println(ll.get(i));
          }
        return true;
    }


}
