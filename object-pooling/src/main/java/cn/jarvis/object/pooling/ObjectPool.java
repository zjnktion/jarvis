package cn.jarvis.object.pooling;

/**
 * 对象池接口
 * @author zhengjn
 */
public interface ObjectPool<T>
{

    T checkOut() throws Exception;

    void checkIn(T obj) throws Exception;

    void create() throws Exception;

    void destroy(T obj) throws Exception;
}
