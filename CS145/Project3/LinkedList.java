import java.util.NoSuchElementException;
import java.util.Iterator;

/** Generic Linked List.
 *  This class uses a slightly different linked-list pattern than the textbook.
 *  The most significant change is in the {@link ListNode} class.
 *
 *  @author Aran Clauson
 *  @version 1.0
 */
public class LinkedList<T> implements Iterable<T> {

  ListNode<T> head;

  /** Default Constructor.
   *  This is my personal coding style.  This constructor performs the same
   *  operations as the compiler provided default, but I like the explicitness.
   */
  public LinkedList() {
    head = null;
  }

  /** Adds the specified <code>item</code> to the end of the list.
   * @param item   The item to be added at the end of the list.
   */
  public void add(T item)
    {
      if(head == null)
        {
          head = new ListNode<T>(item);
        }
      else
        {
          head.rest.add(item);
        }
    }

  public void push(T item)
    {
      ListNode<T> current = head;
      ListNode<T> head = new ListNode<T>(item);
      if(current == null)
        {
          head.next = current;
        }
      else
        {
          current = head;
        }
    }



  /** Gets and item by index.
   * @param i   The index of the item to retreive
   * @return item and index <code>i</code> or null if there is no such item.
   */
  public T get (int i) {
    if(i < 0 || head == null)
      return null;
    else if (i == 0)
      return head.payload;
    else
      return head.rest.get(i - 1);
  }

  /** Removes the first instance that is object-equivalent (<code>==</code>) to
   * <code>item</code>.
   * @param item   The specific object to remove from the list.
   */
  public void remove (T item) {
    if(head != null) {
      if(head.payload == item)
        head = head.rest.head;
      else
        head.rest.remove(item);
    }
  }

  /** Get the size of the list.
   * @return the number of items in the list.
   */
  public int size() {
    if(head == null)
      return 0;
    else
      return 1 + head.rest.size();
  }

  /** Gets an iterator.
   * This is the only method from the <code>Iterable</code> interface.
   *
   * @return an iterator to the first item.
   */
  public ListIterator<T> iterator() {
    return new ListIterator<T>(this);
  }
}

/** ListNode.
 * This is where the biggest deviation from the textbook's implementation lies.
 * Where the book has a reference to the next <code>ListNode</code> in the list,
 * this implementation has a nested <code>LinkedList</code>.  This change makes
 * implementation of the <code>LinkedList</code> methods far easier.
 * Specifically, there is no special case for the first item added to a list.
 * <p>
 * One thing to take special note of is that <code>rest</code> is never
 * <code>null</code>.
 *
 * @author Aran Clauson
 * @version 1.0
 */
class ListNode<T> {
  T payload;
  LinkedList<T> rest;
  ListNode<T> next;
  ListNode<T> prev;

  /** Constructs a new <code>ListNode</code> with the specified values.
   * @param item  The object that this <code>ListNode</code> references.
   * @param rest  The rest of the list.
   */
  ListNode(T item, LinkedList<T> rest) {
    this.payload = item;
    this.rest = rest;
  }

  /** Constructs a new <code>ListNode</code> with the specified payload and a
   * new, empty list as the rest.
   * @param item  The object that this <code>ListNode</code> references.
   */
  ListNode(T item) {
    this(item, new LinkedList<T>());
  }
}

/** Iterator for <code>LinkedList</code>
 * <p>
 * Notice that the <code>iterator</code> references a <code>LinkedList</code>
 * not a <code>ListNode</code>.
 *
 * @author Aran Clauson
 * @version 1.0
 */
class ListIterator<T>
  implements Iterator<T> {

  LinkedList<T> list;

  /** Constructs a new <code>ListIterator</code> referencing the specified
   * <code>LinkedList</code>.
   *
   * @param list  Indirectly, the item currently referenced by the iterator.
   */
  public ListIterator(LinkedList<T> list) {
    this.list = list;
  }

  /** Returns the item currently referred to by the iterator and moves the
   * iterator to the next item.
   * This is something like post-increment .
   * @return The current item referred to by the iterator.
   * @throws NoSuchElementException if the iterator has no next item.
   */
  public T next () throws NoSuchElementException {
    if(list.head == null)
      throw new NoSuchElementException();

    T item = list.head.payload;
    list = list.head.rest;
    return item;
  }

  /** Predicate function for a valid iterator.
   *
   * @return true if the next call to <code>next</code> will not throw an
   * exception.
   */
  public boolean hasNext() {
    return list.head != null;
  }
}
