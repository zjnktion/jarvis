import cn.jarvis.object.pooling.DefaultPooledObject;
import cn.jarvis.object.pooling.PooledObject;
import cn.jarvis.object.pooling.PooledObjectFactory;

/**
 * Created by zhengjn on 2016/8/23.
 */
public class TestFactory implements PooledObjectFactory<TestObject>
{
    public PooledObject<TestObject> create() throws Exception
    {
        return new DefaultPooledObject<TestObject>(new TestObject());
    }

    public boolean validate(PooledObject<TestObject> item) throws Exception
    {
        return true;
    }

    public void destory(PooledObject<TestObject> item) throws Exception
    {

    }
}
