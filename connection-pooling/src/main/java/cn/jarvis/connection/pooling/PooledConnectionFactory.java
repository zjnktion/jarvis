package cn.jarvis.connection.pooling;

import cn.jarvis.object.pooling.DefaultPooledObject;
import cn.jarvis.object.pooling.PooledObject;
import cn.jarvis.object.pooling.PooledObjectFactory;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 池化连接工厂
 * @author zjnktion
 */
public class PooledConnectionFactory implements PooledObjectFactory<PooledConnection>
{

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private final ConnectionFactory connectionFactory;
    private final String validateQuery;
    private final int validateTimeout;

    // --- 构造方法 -----------------------------------------------------------------------------------------------------
    public PooledConnectionFactory(String driverClassName, String url, String username, String password, String validateQuery, int validateTimeout)
    {
        Class<?> driverClass = null;

        if (driverClassName != null)
        {
            try
            {
                try
                {
                    driverClass = Class.forName(driverClassName);
                }
                catch (ClassNotFoundException e)
                {
                    driverClass = Thread.currentThread().getContextClassLoader().loadClass(driverClassName);
                }
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException("Cannot load JDBC driver class '" + driverClassName + "'", e);
            }
        }

        Driver driver = null;
        try
        {
            if (driverClass == null)
            {
                driver = DriverManager.getDriver(url);
            }
            else
            {
                driver = (Driver) driverClass.newInstance();
                if (!driver.acceptsURL(url))
                {
                    throw new IllegalArgumentException("Not suitable url '" + url + "' for driver '" + driverClassName + "'");
                }
            }
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Cannot create JDBC driver of class '" + (driverClassName == null ? "" : driverClassName) + "' using url '" + url + "'", e);
        }

        Properties connProps = new Properties();
        if (username != null)
        {
            connProps.put("user", username);
        }
        if (password != null)
        {
            connProps.put("password", password);
        }

        this.connectionFactory = new ConnectionFactory(driver, url, connProps);
        this.validateQuery = validateQuery;
        this.validateTimeout = validateTimeout;
    }

    // --- 实现接口 -----------------------------------------------------------------------------------------------------
    public PooledObject<PooledConnection> create() throws Exception
    {
        Connection conn = this.connectionFactory.createConnection();
        if (conn == null)
        {
            throw new IllegalArgumentException("ConnectionFactory return null from createConnection.");
        }

        PooledConnection pooledConnection = new PooledConnection(conn);

        return new DefaultPooledObject<PooledConnection>(pooledConnection);
    }

    public boolean validate(PooledObject<PooledConnection> item)
    {
        try
        {
            PooledConnection pooledConnection = item.originalObject();
            pooledConnection.validate(validateQuery, validateTimeout);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void destroy(PooledObject<PooledConnection> item) throws Exception
    {
        PooledConnection pooledConnection = item.originalObject();
        pooledConnection.close();
    }

    // --- 内部类 -------------------------------------------------------------------------------------------------------
    static class ConnectionFactory
    {

        // --- 基本字段 -------------------------------------------------------------------------------------------------
        private final Driver driver;
        private final String connectUrl;
        private final Properties props;

        // --- 构造方法 -------------------------------------------------------------------------------------------------
        public ConnectionFactory(Driver driver, String connectUrl, Properties props)
        {
            this.driver = driver;
            this.connectUrl = connectUrl;
            this.props = props;
        }

        public Connection createConnection() throws SQLException
        {
            return this.driver.connect(connectUrl, props);
        }
    }
}
