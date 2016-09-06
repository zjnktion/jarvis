package cn.jarvis.object.pooling;

/**
 * @author zjnktion
 */
public interface PooledObjectFactory<T>
{

    PooledObject<T> create() throws Exception;

    boolean validate(PooledObject<T> item);

    void destroy(PooledObject<T> item) throws Exception;
}
