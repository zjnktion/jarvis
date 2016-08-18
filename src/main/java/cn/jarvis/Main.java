package cn.jarvis;

import java.util.concurrent.locks.LockSupport;

/**
 * @author zhengjn
 */
public class Main
{

    public static void main(String[] args)
    {

//        LockSupport.unpark(Thread.currentThread());
//        LockSupport.unpark(Thread.currentThread());
//        LockSupport.unpark(Thread.currentThread());
//
//        LockSupport.park();
//        LockSupport.park();
//
//        System.out.println("1");

        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                while (!Thread.interrupted())
                {
                    System.out.println(1);
                }
            }
        });
        t.start();

        try
        {
            Thread.sleep(2000L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        t.interrupt();
    }
}
