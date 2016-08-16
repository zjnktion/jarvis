package cn.jarvis.object.pooling;

/**
 * @author zjnktion
 */
public interface PooledObject<T> extends Comparable<T>
{

    T getPlainObject();

    PooledObjectStatus getCurrentStatus();

    long getCreateTimeMillis();
}
