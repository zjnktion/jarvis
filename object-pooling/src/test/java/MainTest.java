import cn.jarvis.object.pooling.DefaultObjectPool;
import cn.jarvis.object.pooling.LockObjectPool;
import cn.jarvis.object.pooling.ObjectPool;
import cn.jarvis.object.pooling.SynchronizedObjectPool;

/**
 * Created by zhengjn on 2016/8/23.
 */
public class MainTest
{
    public static final ObjectPool<TestObject> pool = new DefaultObjectPool<TestObject>(new TestFactory());
    public static final ObjectPool<TestObject> pool1 = new SynchronizedObjectPool<TestObject>(new TestFactory());
    public static final ObjectPool<TestObject> pool2 = new LockObjectPool<TestObject>(new TestFactory());

    static long time;

    public static void main(String[] args)
    {
        TestThread tt = new TestThread();

        int num = 500;
        Thread[] ts = new Thread[num];
        for (int i = 0; i < num; i++)
        {
            ts[i] = new Thread(tt);
        }
        time = System.currentTimeMillis();
        for (Thread t : ts)
        {
            t.start();
        }

    }

    static class TestThread implements Runnable
    {

        public void run()
        {
            for (int i = 0; i < 1000000; i++)
            {
                try
                {
//                    pool.checkIn(pool.checkOut());

                    pool1.checkIn(pool1.checkOut());

//                    pool2.checkIn(pool2.checkOut());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            long t = System.currentTimeMillis() - time;
            System.err.println(t);
        }
    }
}
