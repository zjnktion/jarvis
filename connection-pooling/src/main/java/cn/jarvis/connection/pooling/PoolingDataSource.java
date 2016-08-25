package cn.jarvis.connection.pooling;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 池化数据源
 * @author zjnktion
 */
public class PoolingDataSource<C extends Connection> implements DataSource
{

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private PrintWriter printWriter = null;

    // --- 实现接口 -----------------------------------------------------------------------------------------------------
    public Connection getConnection() throws SQLException
    {
        return null;
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
}
