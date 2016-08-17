package cn.jarvis;

import java.util.concurrent.locks.LockSupport;

/**
 * @author zhengjn
 */
public class Main
{

    public static void main(String[] args)
    {

        LockSupport.unpark(Thread.currentThread());
        LockSupport.unpark(Thread.currentThread());
        LockSupport.unpark(Thread.currentThread());

        LockSupport.park();
        //LockSupport.park();

        System.out.println("1");
    }
}
