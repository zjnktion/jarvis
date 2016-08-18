package cn.jarvis.object.pooling;

import cn.jarvis.object.pooling.config.DefaultObjectPoolConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zjnktion
 */
public class DefaultObjectPool<T> implements ObjectPool<T>
{

    // --- 配置属性 -----------------------------------------------------------------------------------------------------
    private final int maxTotal;
    private final boolean blockWhenResourceShortage;
    private final long waitMillis;
    private final boolean fair;

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private final ConcurrentHashMap<T, PooledObject<T>> managedObjects = new ConcurrentHashMap<T, PooledObject<T>>();
    private final AtomicLong managedCount = new AtomicLong(0L);

    private final ConcurrentLinkedQueue<PooledObject<T>> idleObjects = new ConcurrentLinkedQueue<PooledObject<T>>();
    private final AtomicLong idleCount = new AtomicLong(0L);

    private final PooledObjectFactory<T> objectFactory;

    private final ReentrantLock checkLock;
    private final Condition resourceShortage;

    private volatile boolean disposing = false;

    // --- 构造方法 -----------------------------------------------------------------------------------------------------
    public DefaultObjectPool(PooledObjectFactory<T> objectFactory)
    {
        this(objectFactory, new DefaultObjectPoolConfig());
    }

    public DefaultObjectPool(PooledObjectFactory<T> objectFactory, DefaultObjectPoolConfig config)
    {
        if (objectFactory == null)
        {
            throw new IllegalArgumentException("object factory can not be null.");
        }

        // 初始化属性配置
        this.maxTotal = config.getMaxTotal();
        this.blockWhenResourceShortage = config.isBlockWhenResourceShortage();
        this.waitMillis = config.getWaitMillis();
        this.fair = config.isFair();

        // 初始化对象工厂
        this.objectFactory = objectFactory;

        // 初始化资源紧缺锁
        checkLock = new ReentrantLock(fair);
        resourceShortage = checkLock.newCondition();
    }

    // --- 接口方法 -----------------------------------------------------------------------------------------------------
    public T checkOut() throws Exception
    {
        PooledObject<T> item = null;

        while (item == null && !disposing)
        {
            item = idleObjects.poll();
            if (item == null)
            {
                item = createInternal();
                if (item == null)
                {
                    // 资源紧缺
                    if (blockWhenResourceShortage)
                    {
                        // 资源紧缺时加锁等待标志为真
                        checkLock.lockInterruptibly();
                        try
                        {
                            if (waitMillis < 0)
                            {
                                resourceShortage.await();
                            }
                            else
                            {
                                resourceShortage.await(waitMillis, TimeUnit.MILLISECONDS);
                            }
                            item = idleObjects.poll();
                        }
                        finally
                        {
                            checkLock.unlock();
                        }
                    }
                }
            }

            if (item != null)
            {
                // 检查对象
            }
        }

        return item == null ? null : item.getPlainObject();
    }

    public void checkIn(T item)
    {

    }

    public T create()
    {
        return null;
    }

    // --- public 方法 --------------------------------------------------------------------------------------------------

    // --- protected 方法 -----------------------------------------------------------------------------------------------

    // --- private 方法 -------------------------------------------------------------------------------------------------
    private PooledObject<T> createInternal()
    {
        long afterCreateCount = managedCount.incrementAndGet();
        if (afterCreateCount > maxTotal || afterCreateCount > Integer.MAX_VALUE)
        {
            managedCount.decrementAndGet();
            return null;
        }

        PooledObject<T> item;
        try
        {
            item = objectFactory.create();
        }
        catch (Exception e)
        {
            managedCount.decrementAndGet();
            return null;
        }
        return item;
    }
}
