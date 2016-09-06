package cn.jarvis.object.pooling;

/**
 * @author zjnktion
 */
public interface PooledObject<T> extends Comparable<PooledObject<T>>
{

    T originalObject();

    boolean inuse();

    boolean idle();

    void invalidate();

    PooledObjectStatus getCurrentStatus();

    long getCreateTimeMillis();

    long getLastCheckOutMillis();

    long getLastCheckInMillis();
}
