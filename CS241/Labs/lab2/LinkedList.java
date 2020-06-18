public class LinkedList<T>
    {
      class ListNode<T>
        {
          public T data;
          public ListNode next;

          public ListNode(T data)
            {
              this.data = data;
              this.next = null;
            }
          public ListNode(T data, ListNode<T> next)
            {
              this.data = data;
              this.next = next;
            }
        }
      ListNode<T> front = null;

      public void add(T data)
        {
          if (front == null)
            {
              front = new ListNode(data);
            }
          else
            {
              ListNode<T> current = front;
              while(current.next != null)
                {
                  current = current.next;
                }
              current.next = new ListNode(data); // new Node
            }
          }

        public int size()
          {
            int counter = 0;
            ListNode<T> current = front;
            while(current != null)
              {
                current = current.next;
                counter++;
              }
              return counter;
          }

        public T get(int index) throws IndexOutOfBoundsException
          {
                ListNode<T> current = front;
                for (int i = 0; i < index; i++)
                  {
                    current = current.next;
                  }
                return current.data;
            }
      }
