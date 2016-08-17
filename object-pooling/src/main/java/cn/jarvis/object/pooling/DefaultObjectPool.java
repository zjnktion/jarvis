package cn.jarvis.object.pooling;

import cn.jarvis.object.pooling.config.ObjectPoolConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zjnktion
 */
public class DefaultObjectPool<T> implements ObjectPool<T>
{

    public T checkOut()
    {
        PooledObject<T> item = null;

        while (item == null)
        {
            if (blockWhenNoIdle)
            {
                item = idleObjects.poll();
                if (item == null)
                {

                }
            }
        }
        return null;
    }

    public void checkIn(T item)
    {

    }

    // --- 配置属性 -----------------------------------------------------------------------------------------------------
    private volatile int maxTotal = ObjectPoolConfig.DEFAULT_MAX_TOTAL;
    private volatile boolean blockWhenNoIdle = ObjectPoolConfig.DEFAULT_BLOCK_WHEN_NO_IDLE;

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private final ConcurrentHashMap<T, PooledObject<T>> managedObjects = new ConcurrentHashMap<T, PooledObject<T>>();
    private final AtomicInteger managedCount = new AtomicInteger(0);

    private final ConcurrentLinkedQueue<PooledObject<T>> idleObjects = new ConcurrentLinkedQueue<PooledObject<T>>();
    private final AtomicInteger idleCount = new AtomicInteger(0);
}
