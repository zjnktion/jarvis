package cn.jarvis.connection.pooling;

import cn.jarvis.object.pooling.ObjectPool;
import cn.jarvis.object.pooling.PooledObjectFactory;
import cn.jarvis.object.pooling.config.SynchronizedObjectPoolConfig;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

/**
 * 池化数据源
 * @author zjnktion
 */
public class PoolingDataSource implements DataSource
{
    // --- 基本属性 -----------------------------------------------------------------------------------------------------
    private int maxTotal = SynchronizedObjectPoolConfig.DEFAULT_MAX_TOTAL;
    private boolean blockWhenResourceShortage = SynchronizedObjectPoolConfig.DEFAULT_BLOCK_WHEN_RESOURCE_SHORTAGE;
    private long maxBlockMillis = SynchronizedObjectPoolConfig.DEFAULT_MAX_BLOCK_MILLIS; // 当blockWhenResourceShortage为true是，该属性才生效
    private boolean retryWhileCheckOutValidateFail = SynchronizedObjectPoolConfig.DEFAULT_RETRY_WHILE_CHECK_OUT_VALIDATE_FAIL;
    private long maxIdleValidateMillis = SynchronizedObjectPoolConfig.DEFAULT_MAX_IDLE_VALIDATE_MILLIS;

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String validateQuery = "SELECT 1";
    private int validateTimeout = -1;

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private PrintWriter printWriter = null;
    private volatile ObjectPool<PooledConnection> connectionPool = null;
    private volatile PooledConnectionFactory connectionFactory = null;

    // --- 实现接口 -----------------------------------------------------------------------------------------------------
    public Connection getConnection() throws SQLException
    {
//        try
//        {
//            PooledConnection pooledConnection = connectionPool.checkOut();
//            if (pooledConnection == null)
//            {
//                return null;
//            }
//            return  pooledConnection.getOriginalConnection();
//        }
//        catch (SQLException e)
//        {
//            throw e;
//        }
//        catch (NoSuchElementException e)
//        {
//            throw new SQLException("Can not get a connection from pool.", e);
//        }
//        catch (Exception e)
//        {
//            throw new SQLException("Can not get a connection because unknown exception.", e);
//        }
    }

    public Connection getConnection(String username, String password) throws SQLException
    {
        throw new UnsupportedOperationException("PoolingDataSource is base on connection pooling, does not support this operation.");
    }

    public PrintWriter getLogWriter() throws SQLException
    {
        return this.printWriter;
    }

    public void setLogWriter(PrintWriter out) throws SQLException
    {
        this.printWriter = out;
    }

    public void setLoginTimeout(int seconds) throws SQLException
    {
        throw new UnsupportedOperationException("PoolingDataSource is not support login timeout.");
    }

    public int getLoginTimeout() throws SQLException
    {
        throw new UnsupportedOperationException("PoolingDataSource is not support login timeout.");
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        throw new SQLFeatureNotSupportedException();
    }

    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        throw new UnsupportedOperationException("PoolingDataSource is not a wrapper.");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return false;
    }

    // --- 私有方法 -----------------------------------------------------------------------------------------------------
    private ObjectPool<PooledConnection> acquirePool() throws SQLException
    {
        if (connectionPool != null)
        {
            return connectionPool;
        }

        synchronized (this)
        {
            if (connectionPool != null)
            {
                return connectionPool;
            }

            if (connectionFactory == null)
            {
                connectionFactory = createConnectionFactory();
            }
        }
    }

    private PooledObjectFactory<PooledConnection> createConnectionFactory() throws SQLException
    {
        try
        {
            return new PooledConnectionFactory(driverClassName, )
        }
        catch (Exception e)
        {
            throw new SQLException("Create ConnectionFactory")
        }
    }

    // --- 公开方法 -----------------------------------------------------------------------------------------------------
    public int getMaxTotal()
    {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal)
    {
        this.maxTotal = maxTotal;
    }

    public boolean isBlockWhenResourceShortage()
    {
        return blockWhenResourceShortage;
    }

    public void setBlockWhenResourceShortage(boolean blockWhenResourceShortage)
    {
        this.blockWhenResourceShortage = blockWhenResourceShortage;
    }

    public long getMaxBlockMillis()
    {
        return maxBlockMillis;
    }

    public void setMaxBlockMillis(long maxBlockMillis)
    {
        this.maxBlockMillis = maxBlockMillis;
    }

    public boolean isRetryWhileCheckOutValidateFail()
    {
        return retryWhileCheckOutValidateFail;
    }

    public void setRetryWhileCheckOutValidateFail(boolean retryWhileCheckOutValidateFail)
    {
        this.retryWhileCheckOutValidateFail = retryWhileCheckOutValidateFail;
    }

    public long getMaxIdleValidateMillis()
    {
        return maxIdleValidateMillis;
    }

    public void setMaxIdleValidateMillis(long maxIdleValidateMillis)
    {
        this.maxIdleValidateMillis = maxIdleValidateMillis;
    }

    public String getDriverClassName()
    {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName)
    {
        this.driverClassName = driverClassName;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getValidateQuery()
    {
        return validateQuery;
    }

    public void setValidateQuery(String validateQuery)
    {
        this.validateQuery = validateQuery;
    }

    public int getValidateTimeout()
    {
        return validateTimeout;
    }

    public void setValidateTimeout(int validateTimeout)
    {
        this.validateTimeout = validateTimeout;
    }
}
