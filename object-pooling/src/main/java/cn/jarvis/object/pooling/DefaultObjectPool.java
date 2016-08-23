package cn.jarvis.object.pooling;

import cn.jarvis.object.pooling.config.DefaultObjectPoolConfig;

import java.util.NoSuchElementException;
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
    private final long maxBlockMillis;
    private final boolean retryWhileCheckOutValidateFail;
    private final long maxIdleValidateMillis;
    private final boolean fair;

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private final PooledObjectFactory<T> objectFactory;

    private final ConcurrentHashMap<T, PooledObject<T>> managedObjects = new ConcurrentHashMap<T, PooledObject<T>>();
    private final AtomicLong managedCount = new AtomicLong(0L);

    private final ConcurrentLinkedQueue<PooledObject<T>> idleObjects = new ConcurrentLinkedQueue<PooledObject<T>>();

    private final ReentrantLock lock;
    private final Condition resourceShorage;

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

        // 设置配置属性
        this.maxTotal = config.getMaxTotal();
        this.blockWhenResourceShortage = config.isBlockWhenResourceShortage();
        this.maxBlockMillis = config.getMaxBlockMillis();
        this.retryWhileCheckOutValidateFail = config.isRetryWhileCheckOutValidateFail();
        this.maxIdleValidateMillis = config.getMaxIdleValidateMillis();
        this.fair = config.isFair();

        // 设置基本字段
        this.objectFactory = objectFactory;

        // 设置资源紧缺锁
        lock = new ReentrantLock(fair);
        resourceShorage = lock.newCondition();
    }

    // --- 接口方法 -----------------------------------------------------------------------------------------------------
    public T checkOut() throws Exception
    {
        PooledObject<T> item = null;

        while (item == null) // 对于取得了对象，但是没有通过校验的，给予再重试的机会
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
                        // 若设置了资源紧缺则等待直到取到或者超时(非精确超时)
                        lock.lockInterruptibly();
                        try
                        {
                            if (maxBlockMillis < 0)
                            {
                                while ((item = idleObjects.poll()) == null)
                                {
                                    resourceShorage.await();
                                }
                            }
                            else
                            {
                                long localWaitMillis = 0;
                                while ((item = idleObjects.poll()) == null)
                                {
                                    long loopStartMillis = System.currentTimeMillis();
                                    if (localWaitMillis < this.maxBlockMillis)
                                    {
                                        resourceShorage.await(this.maxBlockMillis, TimeUnit.MILLISECONDS);
                                        localWaitMillis += System.currentTimeMillis() - loopStartMillis;
                                    }
                                    else
                                    {
                                        throw new NoSuchElementException("Resource shortage.");
                                    }
                                }
                            }
                        }
                        finally
                        {
                            lock.unlock();
                        }
                    }
                    else
                    {
                        // 若没有设置资源紧缺等待，则给予最后一次尝试机会
                        item = idleObjects.poll();
                        if (item == null)
                        {
                            throw new NoSuchElementException("Resource shortage.");
                        }
                    }
                }
            }

            if (needValidateBeforeCheckOut(item))
            {
                // 校验对象
                boolean validated = false;
                Throwable validateException = null;
                try
                {
                    validated = objectFactory.validate(item);
                }
                catch (Throwable e)
                {
                    // 避免未知异常导致线程问题
                    validateException = e;
                }
                if (!validated)
                {
                    try
                    {
                        destroyInternal(item);
                    }
                    catch (Exception e)
                    {
                        // do nothing
                    }
                    item = null;
                    if (!retryWhileCheckOutValidateFail)
                    {
                        NoSuchElementException nsee = new NoSuchElementException("Validate fail while checking out.");
                        nsee.initCause(validateException);
                        throw nsee;
                    }
                }
            }

            // 设置对象借出使用信息
            if (item != null)
            {
                if(!item.inuse()) {
                    // 正常来说，不会出现这种情况，但是为了避免不可控自己菜的原因或许会导致的并发bug而加上的一段
                    item = null;
                }
            }
        }

        return item.originalObject();
    }

    public void checkIn(T obj) throws Exception
    {
        PooledObject<T> item = managedObjects.get(obj);

        if (item == null)
        {
            throw new IllegalArgumentException("The object tried to check in not a part of this object pool.");
        }

        // 设置对象偿还释放信息
        if (!item.idle())
        {
            throw new IllegalStateException("The object tried to check in not in a correct status.");
        }

        idleObjects.offer(item);

        tryToSignal();
    }

    public void create() throws Exception
    {
        PooledObject<T> item = createInternal();

        if (item == null)
        {
            throw new NoSuchElementException("This object pool has managed objects reach to max total.");
        }

        idleObjects.offer(item);

        tryToSignal();
    }

    public void destroy(T obj) throws Exception
    {
        PooledObject<T> item = managedObjects.get(obj);

        if (item == null)
        {
            throw new IllegalArgumentException("The object tried to destroy not a part of this object pool.");
        }

        destroyInternal(item);
    }

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
            managedObjects.put(item.originalObject(), item);
        }
        catch (Exception e)
        {
            managedCount.decrementAndGet();
            return null;
        }
        return item;
    }

    private void destroyInternal(PooledObject<T> item) throws Exception
    {
        item.invalidate();
        idleObjects.remove(item);
        managedObjects.remove(item.originalObject());
        try
        {
            objectFactory.destory(item);
        }
        finally
        {
            managedCount.decrementAndGet();
        }
    }

    private boolean needValidateBeforeCheckOut(PooledObject<T> item)
    {
        return this.maxIdleValidateMillis >= 0 && System.currentTimeMillis() - item.getLastCheckInMillis() >= this.maxIdleValidateMillis;
    }

    private void tryToSignal() {
        lock.lock();
        try
        {
            if (lock.hasWaiters(resourceShorage))
            {
                // 当资源紧缺锁的资源紧缺条件上有阻塞的线程的时候再唤醒
                resourceShorage.signal();
            }
        }
        finally
        {
            lock.unlock();
        }
    }

}
