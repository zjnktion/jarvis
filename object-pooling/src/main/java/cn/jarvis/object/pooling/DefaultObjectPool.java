package cn.jarvis.object.pooling;

import cn.jarvis.object.pooling.config.DefaultObjectPoolConfig;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zjnktion
 */
public class DefaultObjectPool<T> implements ObjectPool<T>
{

    // --- 配置属性 -----------------------------------------------------------------------------------------------------
    private int maxTotal;
    private int maxIdle;
    private boolean blockWhenResourceShortage;
    private long maxBlockMillis;
    private boolean fair;

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private final PooledObjectFactory<T> objectFactory;

    private final ConcurrentHashMap<T, PooledObject<T>> managedObjects = new ConcurrentHashMap<T, PooledObject<T>>();
    private final AtomicLong managedCount = new AtomicLong(0L);

    private final PooledObject<T>[] idleObjects;
    private final AtomicLong idleCount = new AtomicLong(0L);

    private final ReentrantLock lock;
    private final Condition resourceShorage;

    // --- 构造方法 -----------------------------------------------------------------------------------------------------
    public DefaultObjectPool(PooledObjectFactory<T> objectFactory)
    {
        this(objectFactory, new DefaultObjectPoolConfig());
    }

    @SuppressWarnings("unchecked")
    public DefaultObjectPool(PooledObjectFactory<T> objectFactory, DefaultObjectPoolConfig config)
    {
        if (objectFactory == null)
        {
            throw new IllegalArgumentException("object factory can not be null.");
        }

        // 设置配置属性
        this.maxTotal = config.getMaxTotal();
        this.maxIdle = config.getMaxIdle();
        this.blockWhenResourceShortage = config.isBlockWhenResourceShortage();
        this.maxBlockMillis = config.getMaxBlockMillis();
        this.fair = config.isFair();

        // 设置基本字段
        this.objectFactory = objectFactory;
        this.idleObjects = (PooledObject<T>[]) new PooledObject[maxIdle];

        // 设置资源紧缺锁
        lock = new ReentrantLock(fair);
        resourceShorage = lock.newCondition();
    }

    // --- 接口方法 -----------------------------------------------------------------------------------------------------
    public T checkOut() throws Exception
    {
        PooledObject<T> item = null;

        while (item == null)
        {

        }
        return null;
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
