package cn.jarvis.object.pooling;

/**
 * Created by zhengjn on 2016/8/22.
 */
public class DefaultPooledObject<T> implements PooledObject<T>
{

    private T obj;

    private final long createTimeMillis = System.currentTimeMillis();
    private long lastCheckOutMillis = createTimeMillis;
    private long lastCheckInMillis = createTimeMillis;
    private PooledObjectStatus currentStatus = PooledObjectStatus.IDLE;

    public DefaultPooledObject(T obj)
    {
        this.obj = obj;
    }

    public T originalObject()
    {
        return this.obj;
    }

    public boolean inuse()
    {
        if (this.currentStatus == PooledObjectStatus.INUSED || this.currentStatus == PooledObjectStatus.INVALID)
        {
            return false;
        }

        lastCheckOutMillis = System.currentTimeMillis();
        this.currentStatus = PooledObjectStatus.INUSED;
        return true;
    }

    public boolean idle()
    {
        if (this.currentStatus == PooledObjectStatus.IDLE || this.currentStatus == PooledObjectStatus.INVALID)
        {
            return false;
        }

        lastCheckInMillis = System.currentTimeMillis();
        this.currentStatus = PooledObjectStatus.IDLE;
        return true;
    }

    public void invalidate()
    {
        this.currentStatus = PooledObjectStatus.INVALID;
    }

    public PooledObjectStatus getCurrentStatus()
    {
        return this.currentStatus;
    }

    public long getCreateTimeMillis()
    {
        return this.createTimeMillis;
    }

    public long getLastCheckOutMillis()
    {
        return this.lastCheckOutMillis;
    }

    public long getLastCheckInMillis()
    {
        return this.lastCheckInMillis;
    }

    public int compareTo(PooledObject<T> o)
    {
        long lastInuseDiff = this.getLastCheckInMillis() - o.getLastCheckInMillis();
        if (lastInuseDiff == 0)
        {
            return System.identityHashCode(this) - System.identityHashCode(o);
        }
        // 处理long转int溢出
        return (int) Math.min(Math.max(lastInuseDiff, Integer.MIN_VALUE), Integer.MAX_VALUE);
    }
}
