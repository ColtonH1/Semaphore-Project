import java.util.concurrent.Semaphore;

public class PostOffice
{
    public static int totalWorker = 3; //there are 3 workers
    //global semaphores
    public static Semaphore PW = new Semaphore(3, true ); //3 PostWorkers are available at any given time
    public static Semaphore line = new Semaphore(10, true );
    public static Semaphore lineMutex = new Semaphore( 1, true );   //mutex to wait in line
    public static Semaphore pwMutex = new Semaphore( 1, true );     //mutex to choose pw
    public static Semaphore scaleMutex = new Semaphore( 1, true );  //mutex to claim the scale
    public static Semaphore lineToPWMutex = new Semaphore( 1, true );  //mutex to move from line to PW
    public static GenLinkedList PWQueue = new GenLinkedList(); //Note: this is a file from a project in CS 3345 with Greg Ozbirn as the professor 
    public static GenLinkedList custTaskQ = new GenLinkedList();
    //semaphore for all threads to check if scale is available
    public static Semaphore scale = new Semaphore(1); //binary semaphore 
    public static Thread PWThread[] = new Thread[totalWorker];


    //create the post office with its customers and employees
    public static void main(String args[])
    {
        //set the limit of total customer and total workers
        int totalCustomer = 50; //50 customers come in during the day
        

        //create semaphore to keep track of the number of customers inside
        Semaphore inside = new Semaphore(10); //up to 10
        //semaphore for mutual exclusion
        Semaphore mutex = new Semaphore(1); //mutual exclusion

        PostWorkers PWArray[] = new PostWorkers[totalWorker];
        
        //create worker threads
        for(int i = 0; i < totalWorker; i++)
        {
            PWArray[i] = new PostWorkers();
            PWThread[i] = new Thread(PWArray[i]);
            PWQueue.addEnd(i); //list of available workers
            System.out.println("Postal Worker "+ i +" created ");
            PWThread[i].start();
        }

        //create customer threads
        Customers custArray[] = new Customers[totalCustomer];
        Thread custThread[] = new Thread[totalCustomer];
        for(int i = 0; i < totalCustomer; i++)
        {
            custArray[i] = new Customers(i);
            custThread[i] = new Thread(custArray[i]);
            System.out.println("Customer " + i + " created ");
            custThread[i].start();
        }

        //have customers join
        for(int i = 0; i < totalCustomer; i++)
        {
            try
                {
                    custThread[i].join();
                    System.out.println("Joined customer "+ i);
                }
                catch(InterruptedException e)
                {

                }
        }
    }



}