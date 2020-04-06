import java.util.Random;

public class Customers implements Runnable {
    Random rand = new Random(); // use Random class to decide a number 0-2
    private int num; // holds the num id of the customer
    private boolean useLine = false; // will check if customer had to wait in line vs went straight to the PW
    public static int workerChosen;

    enum Task {
        BUY, LETTER, PACKAGE, ERROR;
    }

    Customers(int num) {
        this.num = num;
    }

    @Override
    public void run() {
        // set customer event
        int randNum = rand.nextInt(3);
        Task task;
        //called ask because the PW would 'ask' what task they would like and this would be the answer
        String ask = "ERROR"; //if this isn't changed, then there is an error 
        String askEnd = "ERROR"; //same idea as 'ask', but for past tense
        if (randNum == 0)
        {
            task = Task.BUY;
            ask = "buy stamps";
            askEnd = "buying stamps";
        }
        else if (randNum == 1)
        {
            task = Task.LETTER;
            ask = "mail a letter";
            askEnd = "mailing a letter";
        }
        else if (randNum == 2)
        {
            task = Task.PACKAGE;
            ask = "mail a package";
            askEnd = "mailing a package";
        }
        else
        {
            try 
            {
                throw new Exception("Customer was not able to find a task.");
            } catch (Exception e) 
            {
                e.printStackTrace();
                System.out.println(e);
            }
            task = Task.ERROR; //initializes task even though this would be ended for error reasons
        }
         while(true)
         {
             try
             {
                 //wait to enter the building and display when they do
                PostOffice.lineMutex.acquire();
                PostOffice.line.acquire();
                System.out.println("Customer " + num + " enters post office");
                PostOffice.lineMutex.release();


                //mutual exclusion and acquire postal worker
                PostOffice.lineToPWMutex.acquire();
                PostOffice.PW.acquire(); //retrieves one postal worker or waits for one
                PostOffice.lineToPWMutex.release();

                //choose postal worker from list of available ones
                //we have already determined one was available, so there should be one in the list
                PostOffice.pwMutex.acquire();
                workerChosen = (int)PostOffice.PWQueue.removeFront(); //use this for the next worker

                System.out.println("Customer " + num + " asks postal worker " + workerChosen + " to " + ask); //display activity

                //have the postworker start performing their job and send them the detail: 
                //give them the customer thread number, postwal worker thread number, and the task that the customer wants performed 
                PostWorkers.performTask(num, workerChosen, task); 
                 
                //two actions are needed to leave the postalworker, so we use mutual exclusion to make sure another customer doesn't show up too quickly
                PostOffice.PW.release(); 
                System.out.println("Customer " + num + " finished " + askEnd); //display they are leaving
                PostOffice.pwMutex.release(); //customer has completely left and the next customer may approach the same worker now

                System.out.println("Customer " + num + " leaves post office");
                PostOffice.line.release(); //customer has officially left the building
                break;
             } 
             catch (InterruptedException e) 
             {
                e.printStackTrace();
                System.out.println(e);
             }
         }
    }
}