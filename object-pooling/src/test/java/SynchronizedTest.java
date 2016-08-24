/**
 * Created by zhengjn on 2016/8/24.
 */
public class SynchronizedTest
{

    public static final SynchronizedTest ST = new SynchronizedTest();
    public static int i = 0;

    public static void main(String[] args)
    {
        new WaitThread().start();

        try
        {
            Thread.sleep(1000L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        new LoopThread().start();
    }

    public synchronized void waitMe()
    {
        try
        {
            this.wait(2000L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println("i was notified...");
    }

    public synchronized void loopAndNotify()
    {
        for (;;)
        {
            i++;
            if (i == Integer.MAX_VALUE)
            {
                System.out.println("try to notify one thread...");
                this.notify();
            }
        }
    }

    static class WaitThread extends Thread
    {
        @Override
        public void run()
        {
            ST.waitMe();
        }
    }

    static class LoopThread extends Thread
    {
        @Override
        public void run()
        {
            ST.loopAndNotify();
        }
    }
}
