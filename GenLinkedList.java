/*
Colton Henderson
cjh160030
CS 3345.002
Project 1: General Linked List
*/
import java.util.NoSuchElementException;

public class GenLinkedList<any>
{
    private Node<any> head; //element of type node <with any type> to represent the front of the list
    private Node<any> tail; //element of type node <with any type> to represent the end of the list
    static int size = 0; //counter for how big our list is

    public void addFront(any d)
    {
        //checks if there is currently a head. If the statement registers as true, then there is not a head yet
       if (head == null)
       {
           head = new Node<any>(d, null);
           tail = head;
       }
       //else means there is already a head, so we add a new node as the head
       else
       {
           head = new Node<any>(d, head);
       }
       size++; //always keep track of how many nodes there are
    }

    public void addEnd(any d)
    {
        //same idea as addFront; checks if there is a head. If there isn't a head, then there isn't a tail
        //so the new node is the head and the tail
       if (head == null)
       {
           head = new Node<any>(d, null);
           tail = head;  
       }
       //whatever the current tail is, add a node next to it and make it the new tail
       else
       {
           tail.next = new Node<any>(d, null);
           tail = tail.next;
       }
       size++; //always keep track of how many nodes there are
    }

    public any removeFront()
    {
       any olddata;

       //there is no head to remove
       if (head == null)
           throw new NoSuchElementException();
        //if there is only one node total, then head and tail are empty
       else if (head == tail)
       {
           olddata = head.data;          
           head = null;
           tail = null;
       }
       //there is more than one node, so set the new head as the one next to the old head and ignore the old head
       else
       {
           olddata = head.data;
           head = head.next;           
       }
       size--; //always keep track of how many nodes there are
       return olddata;
    }

    public any removeEnd()
    {
       any olddata;

       //checks if there is any nodes at all
       if (head == null)
           throw new NoSuchElementException();
        //if there is only one node, set head and tail as null
       else if (head == tail)
       {
           olddata = head.data;          
           head = null;
           tail = null;
       }
       //there is more than one node
       //since this is a single linked list, we cannot go backwards
       //constantly check if the next node is the tail, if it is, 
       //then update tail to be the current position (which would be right before tail)
       else
       {
           Node<any> p = head;
           while (p.next != tail)
              p = p.next;
           olddata = tail.data;
           p.next = null;
           tail = p;
       }
       size--; //always keep track of how many nodes there are
       return olddata;
    }

    public any get(int i)
    {
		//if the number, i, is out of range: print an error
        if (i < 0 || i > size - 1)
           throw new ArrayIndexOutOfBoundsException("Index " + i + "; size " + size);
              
        //else set 'item' at the front of the list and traverse the list until the item wanted is found
        Node<any> item = head;
        for (int k=0; k<i; k++)
           item = item.next;

        return item.data;
	}
	
	public void set(int i, any item)
	{
		//if the number, i, is out of range: print an error
        if (i < 0 || i > size - 1)
		   throw new ArrayIndexOutOfBoundsException("Index " + i + "; size " + size);
		   
        Node<any> elem = head; //elem is the place of the node

        //traverse the linked list until elem is equal to the node position we want
        for(int k = 0; k < i; k++) 
        {
            elem = elem.next;
        }
        elem.data = item;

		//return elem.data;
    }
    
    public void swap(int index1, int index2)
    {
        //if either index is out of bounds, throw an exception
        if (index1 < 0 || index1 > size - 1)
		   throw new ArrayIndexOutOfBoundsException("Index " + index1 + "; size " + size);
		if (index2 < 0 || index2 > size - 1)
		   throw new ArrayIndexOutOfBoundsException("Index " + index2 + "; size " + size);
           
        //make 3 variables. One for each index and then a temp place holder
        Node<any> p1 = head;
        Node<any> p2 = head;
        any p3; //will hold value of p1

        //find which node the user requested for the first swap
        for(int k = 0; k < index1; k++) 
        {
            p1 = p1.next; //find the first index's node
        }
        //find which node the user requested for the second swap
        for(int k = 0; k < index2; k++) 
        {
            p2 = p2.next; //find the second index's node
        }

        //use p3 as a temporary place holder and switch the nodes around
        p3 = p1.data; //set p3 equal to what is in p1
        p1.data = p2.data; //set p1 equal to p2
        p2.data = p3; //set p2's data equal to what used to be in p1       
    }

    public void shift(int move)
    {
        //if we are told to move more than the total size of the array
        if (Math.sqrt(move*move) > size) //checks absolute value of 'move'
		   throw new ArrayIndexOutOfBoundsException("Move amount " + move + "; size " + size);
        
        Node<any> firstNode = head;
        Node<any> lastNode;
        Node<any> temp;


        //if there is nothing or only one node, then there is no operation to perform
        if(firstNode == null || firstNode.next == null)
            return;

        //checks if 'move' has been finished
        while(move != 0)
        {
            //if move is positive, then it will move forward
            if(move > 0)
            {
                temp = firstNode;
                firstNode = firstNode.next;
                size--;
                lastNode = temp;
                move--; //move closer to zero
                head = firstNode; 
                addEnd(lastNode.data); //add the original head back as the tail
            }
            //this means 'move' is negative 
            else
            {
                lastNode = tail; //initialize lastNode
                temp = lastNode; //hold the value of lastNode since we arew changing it
                removeEnd(); //remove the last Node
                lastNode = temp; //remember what the value was
                addFront(lastNode.data); //add it back to the front
                move++; //move closer to zero
            }
        }

    }

    public void removeMatching(any item)
    {
        if (head == null)
            throw new NoSuchElementException();
        
        Node<any> p = head;
        for(int i = 0; i < size; i++)//goes throughout the whole list
        {
            if(p.data == item) //if the current postion is equal to the item
            {
                shift(i); //set the item as the head of the list
                removeFront(); //remove the head
                shift(-i); //return the list back to normal
                i--; //the size is now smaller, so we make i smaller
            }
            p = p.next; //always update p to the next position
        }
    }

    public void insertList(GenLinkedList testList, int index)
    {
        if (index < 0 || index > size - 1)
        throw new ArrayIndexOutOfBoundsException("Index " + index + "; size " + size);

        Node<any> findAmount = testList.head; //will help us determine how many elements are in the new list
        Node<any> newHead = testList.head; //will help set the new lists elements at the end of the old list
        int counter = 0;
        int finishLine = size - index; //variable that will determine when to stop moving elements
        
        //find how many elements are in the new list
        for(int i = 0; i == counter; i++)
        {
            if(findAmount != null) //checks if the current one is null
            {
                if(findAmount.next != null) //makes sure if we update the variable, it will be valid
                   findAmount = findAmount.next;  //progress findAmount
                else
                  i++; //change i so the for loop will end
                
                counter++;
            }
        }

        //add the new elements to the end of the old list
        for(int j = 0; j < counter; j++)
        {
            addEnd(newHead.data);
            newHead = newHead.next;
        }

        while(finishLine > 0)
        {
            //find the next element to be moved down
            Node<any> p = head;
            for(int k = 0; k < index; k++)
            {
                p = p.next;
            }

            addEnd(p.data); //place element at the end
            shift(index); //shift the list so the original element is at the front
            removeFront(); //remove the original element
            shift(-index); //return the list back to normal
            finishLine--; 
        }
    }

    public void erase(int index, int num)
    {
        //check if indez is valid
        if (index < 0 || index > size - 1)
        throw new ArrayIndexOutOfBoundsException("Index " + index + "; size " + size);
        //check if the total amount of elements being looked at is valid
        if (index+num > size - 1)
        throw new ArrayIndexOutOfBoundsException("Index combined with the amount to remove " + (index+num) + "; size " + size);

        shift(index); //set head to be the position that we start erasing
        for(int i = 0; i < index; i++) //remove the first node until we satisfy the amount to delete
            removeFront();
        shift(-index); //return the list back to the original order
    }

    //print the list
    @Override
    public String toString()
    {
       StringBuilder sb = new StringBuilder("[ ");

       Node<any> p = head;
       while (p != null)
       {
          sb.append(p.data + " ");
          p = p.next;
       }

       sb.append("]");

       return new String(sb);
    }


	//This class creates the Node to be used throughout the program
    private static class Node<any> 
    {
        public any data;
        public Node<any> next;

        public Node(any data)
        {
            this(data, null);
        }
       public Node(any d, Node<any> n) //can call to set data or next
        {
            this.data = d;
            this.next = n;
        }
    }


}    /*public static void main(String args[])
    {
       GenLinkedList list = new GenLinkedList();
       GenLinkedList testList = new GenLinkedList();


       //testing addFront()
       System.out.println("We will had the numbers 0-4 to the list using addFront(). \n" +
        "We are adding to the front of the list, so the list will count backwards");
       for (int i=0; i<5; i++)
       {
           list.addFront(i);
       }

       System.out.println(list + "\n");

       //testing get() method
       System.out.println("We will use the get() method to see the current list:");
       for (int i=0; i < size; i++)
       {
          System.out.println("Item " + i + " is " + list.get(i) + "\n");  
       }  

       //testing addEnd() method
       System.out.println("Let's add 'C' to the end of the list now using addEnd()");
       list.addEnd('C');

       System.out.println("The current list is: ");
       System.out.println(list + "\n");

       //testing removeEnd() method
       System.out.println("We will now remove 'C' using removeEnd()");
       list.removeEnd();

       System.out.println("The current list is: ");
       System.out.println(list + "\n");

       //testing removeFront() method
       System.out.println("We will now remove '4' using removeFront()");
       list.removeFront();

       System.out.println("The current list is: ");
       System.out.println(list + "\n");

       //testing shift() method
       System.out.println("\n\nWe will now shift the list by one node using shift()");
       list.shift(1);

       System.out.println("The current list is: ");
       System.out.println(list + "\n");

       System.out.println("We will now shift the list by minus one node using shift(). This will bring us back to the orignal order.");
       list.shift(-1);

       System.out.println("The current list is: ");
       System.out.println(list + "\n");

       System.out.println("We will now shift the list by three nodes using shift()");
       list.shift(3);

       System.out.println("The current list is: ");
       System.out.println(list + "\n");

       System.out.println("We will now shift the list by minus three nodes using shift(). This will bring us back to the orignal order.");
       list.shift(-3);

       System.out.println("The current list is: ");
       System.out.println(list + "\n\n\n");

       //testing swap() method
       System.out.println("We will now swap the first postion (index 0) and the third position (index 2) using the swap() method.");
       list.swap(0, 2);

       System.out.println("The current list is: ");
       System.out.println(list + "\n");

       //testing set() and removeMatching()
       System.out.println("The next method is removeMatching(), so let's add a couple of the same characters using the " + 
        "addFront(), addEnd(), and the set() method: ");

        list.addEnd(2);
        list.addFront(2);
        list.set(3, 2);    

       System.out.println("We added a 2 at the front, a 2 at the end, and after adding those: we set position 3 (the fourth digit) equal to 2. The current list is: ");
       System.out.println(list + "\n");

       System.out.println("We will now remove all instances of '2' using removeMatching()");
       list.removeMatching(2);

       System.out.println("The current list is: ");
       System.out.println(list + "\n");


       //testing insertList method
       System.out.println("We will now add another list into the current list starting at index 2");
       //create the list. Since we are creating the list here, we must remove the additional elements added to size
       testList.addFront('w');
       size--;
       testList.addFront(3);
       size--;
       testList.addEnd('#');
       size--;
       
       System.out.println("The new list we are adding is: ");
       System.out.println(testList + "\n");

       System.out.println("We are adding this list starting at index 1. Now the combined list is: ");
       list.insertList(testList, 1);
       System.out.println(list + "\n");


       //testing the erase method
       System.out.println("We will now erase 2 elements starting at index 2");
       list.erase(2, 2);;

       System.out.println("The current list is: ");
       System.out.println(list + "\n");
    }
}*/