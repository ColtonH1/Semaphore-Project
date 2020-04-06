public class PostWorkers implements Runnable
{
    PostWorkers() 
    {
        
    }

    @Override
    public void run() {


    }
    


    public static void performTask(int custNum, int pwNum, Customers.Task task)
    {
        PostOffice.pwMutex.release(); //release this now that we don't need to keep the same number anymore from customer
        try 
        {
            System.out.println("Postal worker " + pwNum + " serving customer " + custNum);

            //performing task
            if(task == Customers.Task.BUY)
            {
                Thread.sleep(1000);
            }
            else if(task == Customers.Task.LETTER)
            {
                Thread.sleep(1500);
            }
            else if(task == Customers.Task.PACKAGE)
            {
                //make sure this worker is the only one using the scale
                PostOffice.scaleMutex.acquire();
                PostOffice.scale.acquire();
                PostOffice.scaleMutex.release();
                System.out.println("Scales in use by postal worker " + pwNum);
                Thread.sleep(2000);
                PostOffice.scale.release(); //release immediately so someone else can use it
                System.out.println("Scales released by postal worker " + pwNum);
            }
            else if(task == Customers.Task.ERROR)
            {
                System.out.println("Error in PostWorkers --> PerformTask --> performing task if/else statement"); //error has occured if we get here
                System.exit(0);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            System.out.println(e);
        }

        System.out.println("Postal worker " + pwNum + " finished serving customer " + custNum);

        //acquire the mutex so another thread won't cause a race condition
        try 
        {
            PostOffice.pwMutex.acquire();
            PostOffice.PWQueue.addEnd(pwNum); //add the worker back to the list of availability 
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            System.out.println(e);
        }
    }




}