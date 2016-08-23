import cn.jarvis.object.pooling.DefaultObjectPool;
import cn.jarvis.object.pooling.ObjectPool;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhengjn on 2016/8/23.
 */
public class MainTest
{
    public static final ObjectPool<TestObject> pool = new DefaultObjectPool<TestObject>(new TestFactory());

    final TestObject[] vs = new TestObject[32];
    int index;
final ReentrantLock lock = new ReentrantLock();
    volatile  int sign;

    final ConcurrentLinkedQueue<TestObject> q = new ConcurrentLinkedQueue<TestObject>();
    void offer(TestObject v)
    {
        if(v == null)
            throw new NullPointerException();
        lock.lock();
        this.vs[index++] = v;
        lock.unlock();
//        q.offer(v);
    }

    TestObject poll(){
        lock.lock();
        TestObject v = this.vs[--index];
        this.vs[index] = null;
        lock.unlock();
        return v;
//        return q.poll();
    }

    static long time;
    public static void main(String[] args)
    {
        TestThread tt = new TestThread();

        Thread[] ts = new Thread[8];
        for(int i = 0; i < 8; MT.offer(new TestObject()), ++i);
        for (int i = 0; i < 8; i++)
        {
            ts[i] = new Thread(tt);
        }
        time = System.currentTimeMillis();
        for (Thread t : ts)
        {
            t.start();
        }


    }
static final MainTest MT = new MainTest();
    static class TestThread implements Runnable
    {

        public void run()
        {
            for (int i = 0; i < 1000000; i++)
            {
                try
                {
//                    TestObject o = pool.checkOut();
//                    pool.checkIn(o);
                    MT. offer(MT.poll());
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
