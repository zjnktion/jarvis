package cn.jarvis.object.pooling;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author zjnktion
 */
public class DefaultObjectPool<T> implements ObjectPool<T>
{

    private final ConcurrentHashMap<T, PooledObject<T>> managedObjects = new ConcurrentHashMap<T, PooledObject<T>>();
    private final ConcurrentLinkedQueue<PooledObject<T>> idleObjects = new ConcurrentLinkedQueue<PooledObject<T>>();

    public T checkOut()
    {
        return null;
    }

    public void checkIn(T item)
    {

    }
}
