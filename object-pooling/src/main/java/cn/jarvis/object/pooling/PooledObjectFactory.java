package cn.jarvis.object.pooling;

/**
 * Created by zhengjn on 2016/8/18.
 */
public interface PooledObjectFactory<T>
{

    PooledObject<T> create() throws Exception;
}