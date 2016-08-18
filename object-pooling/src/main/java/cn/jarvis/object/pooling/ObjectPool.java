package cn.jarvis.object.pooling;

/**
 * 对象池接口
 * @author zhengjn
 */
public interface ObjectPool<T>
{

    T checkOut();

    void checkIn(T item);

    T create();
}
