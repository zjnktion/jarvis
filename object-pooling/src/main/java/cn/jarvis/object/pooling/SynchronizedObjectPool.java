package cn.jarvis.object.pooling;

import cn.jarvis.object.pooling.config.SynchronizedObjectPoolConfig;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 目前为止，性能最好的实现
 *
 * @author zjnktion
 */
public class SynchronizedObjectPool<T> implements ObjectPool<T>
{

    // --- 配置属性 -----------------------------------------------------------------------------------------------------
    private final int maxTotal;
    private final boolean blockWhenResourceShortage;
    private final long maxBlockMillis;
    private final boolean retryWhileCheckOutValidateFail;
    private final long maxIdleValidateMillis;

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private final PooledObjectFactory<T> objectFactory;

    private final ConcurrentHashMap<T, PooledObject<T>> managedObjects = new ConcurrentHashMap<T, PooledObject<T>>();
    private int managedCount = 0;

    private final PooledObject<T>[] idleObjects;
    private int index = -1;

    // --- 构造方法 -----------------------------------------------------------------------------------------------------
    public SynchronizedObjectPool(PooledObjectFactory<T> objectFactory)
    {
        this(objectFactory, new SynchronizedObjectPoolConfig());
    }

    public SynchronizedObjectPool(PooledObjectFactory<T> objectFactory, SynchronizedObjectPoolConfig config)
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

        // 设置基本字段
        this.objectFactory = objectFactory;
        this.idleObjects = new PooledObject[this.maxTotal];
    }

    // --- 实现接口 -----------------------------------------------------------------------------------------------------
    public synchronized T checkOut() throws Exception
    {
        PooledObject<T> item = null;

        while (item == null)
        {
            if (this.index < 0)
            {
                item = createInternal();
                if (item == null)
                {
                    // 资源紧缺
                    if (this.blockWhenResourceShortage)
                    {
                        // 若设置了资源紧缺则等待直到取到或者超时(非精确超时)
                        if (this.maxBlockMillis <= 0)
                        {
                            while (this.index < 0)
                            {
                                this.wait();
                            }
                        }
                        else
                        {
                            long localWaitMillis = 0L;
                            while (this.index < 0)
                            {
                                long loopStartMillis = System.currentTimeMillis();
                                if (localWaitMillis < this.maxBlockMillis)
                                {
                                    this.wait(this.maxBlockMillis - localWaitMillis);
                                    localWaitMillis += System.currentTimeMillis() - loopStartMillis;
                                }
                                else
                                {
                                    throw new NoSuchElementException("Resource shortage after wait.");
                                }
                            }
                        }
                        item = idleObjects[this.index];
                        idleObjects[this.index--] = null;
                    }
                    else
                    {
                        throw new NoSuchElementException("Resource shortage without wait.");
                    }
                }
            }
            else
            {
                item = idleObjects[this.index];
                idleObjects[this.index--] = null;
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
                if (!item.inuse())
                {
                    // 正常来说，不会出现这种情况，但是为了避免不可控自己菜的原因或许会导致的并发bug而加上的一段
                    item = null;
                }
            }
        }

        return item.originalObject();
    }

    public synchronized void checkIn(T obj) throws Exception
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

        this.idleObjects[++this.index] = item;

        this.notifyAll();
    }

    public synchronized void create() throws Exception
    {
        PooledObject<T> item = createInternal();

        if (item == null)
        {
            throw new IllegalStateException("The object tried to check in not in a correct status.");
        }

        this.idleObjects[++this.index] = item;

        this.notify();
    }

    public synchronized void destroy(T obj) throws Exception
    {
        PooledObject<T> item = managedObjects.get(obj);

        if (item == null)
        {
            throw new IllegalArgumentException("The object tried to destroy not a part of this object pool.");
        }

        destroyInternal(item);
    }

    // --- 私有方法 -----------------------------------------------------------------------------------------------------
    private PooledObject<T> createInternal()
    {
        if (this.managedCount == Integer.MAX_VALUE || this.managedCount == maxTotal)
        {
            return null;
        }

        PooledObject<T> item;
        try
        {
            item = objectFactory.create();
            managedObjects.put(item.originalObject(), item);
            this.managedCount++;
            return item;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private void destroyInternal(PooledObject<T> item) throws Exception
    {
        item.invalidate();

        if (this.index >= 0)
        {
            for (int i = 0; i <= this.index; i++)
            {
                if (this.idleObjects[i] == item)
                {
                    int moveNum = this.index - i;
                    if (moveNum > 0)
                    {
                        System.arraycopy(this.idleObjects, i + 1, this.idleObjects, i, moveNum);
                    }
                    idleObjects[this.index--] = null;
                    break;
                }
            }
        }

        managedObjects.remove(item.originalObject());
        try
        {
            objectFactory.destroy(item);
        }
        finally
        {
            this.managedCount--;
        }
    }

    private boolean needValidateBeforeCheckOut(PooledObject<T> item)
    {
        return this.maxIdleValidateMillis >= 0 && System.currentTimeMillis() - item.getLastCheckInMillis() >= this.maxIdleValidateMillis;
    }
}
