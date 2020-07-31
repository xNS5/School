/*
* Michael Kennedy
* Lab 4
* Linked List Exercise
*
*
*/


import java.util.*;                                                             // Importing the util library

public class LinkedList<T>
    {
      class ListNode<T>                                                         // Creating a private class node
        {
          public T data;                                                        // type T variable data
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

      public void add(T data)                                                   // Adding data
        {
          if (front == null)
            {
              front = new ListNode(data);                                       // Creating new node at Front
            }
          else
            {
              ListNode<T> current = front;                                      //Assigning current to Front
              while(current.next != null)
                {
                  current = current.next;                                       // Current is now the next one
                }
              current.next = new ListNode(data); // new Node
            }
          }

        public int size()                                                       // Determines the size of the list
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

        public void remove(T item)
          {
            ListNode<T> current = front;
            while(current.next != null && current.next != item)
              {
                current = current.next;
              }
            current.next = current.next.next;
          }

      }
