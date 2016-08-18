package cn.jarvis.object.pooling;

import cn.jarvis.object.pooling.config.DefaultObjectPoolConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zjnktion
 */
public class DefaultObjectPool<T> implements ObjectPool<T>
{

    // --- 配置属性 -----------------------------------------------------------------------------------------------------
    private int maxTotal = DefaultObjectPoolConfig.DEFAULT_MAX_TOTAL;
    private boolean blockWhenResourceShortage = DefaultObjectPoolConfig.DEFAULT_BLOCK_WHEN_RESOURCE_SHORTAGE;
    private boolean fair = DefaultObjectPoolConfig.DEFAULT_FAIR;

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private final ConcurrentHashMap<T, PooledObject<T>> managedObjects = new ConcurrentHashMap<T, PooledObject<T>>();
    private final AtomicLong managedCount = new AtomicLong(0L);

    private final ConcurrentLinkedQueue<PooledObject<T>> idleObjects = new ConcurrentLinkedQueue<PooledObject<T>>();
    private final AtomicLong idleCount = new AtomicLong(0L);

    private final PooledObjectFactory<T> objectFactory;

    private final ReentrantLock checkLock;

    // --- 构造方法 -----------------------------------------------------------------------------------------------------
    public DefaultObjectPool(PooledObjectFactory<T> objectFactory)
    {
        this(objectFactory, null);
    }

    public DefaultObjectPool(PooledObjectFactory<T> objectFactory, DefaultObjectPoolConfig config)
    {
        if (objectFactory == null)
        {
            throw new IllegalArgumentException("object factory can not be null.");
        }

        this.objectFactory = objectFactory;

        if (config != null)
        {
            loadConfig(config);
        }
    }

    // --- 接口方法 -----------------------------------------------------------------------------------------------------
    public T checkOut()
    {
        PooledObject<T> item = null;

        while (item == null)
        {
            if (blockWhenResourceShortage)
            {
                item = idleObjects.poll();
                if (item == null)
                {
                    item = createInternal();
                    if (item == null)
                    {
                        
                    }
                }
            }
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

    public void loadConfig(DefaultObjectPoolConfig config)
    {
        this.maxTotal = config.getMaxTotal();
        this.blockWhenResourceShortage = config.isBlockWhenResourceShortage();
        this.fair = config.isFair();
    }
}
