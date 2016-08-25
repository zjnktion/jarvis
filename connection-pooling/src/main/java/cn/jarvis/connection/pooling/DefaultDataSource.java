package cn.jarvis.connection.pooling;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 基于PoolingDataSource封装的一个给spring用的data source
 * @author zjnktion
 */
public class DefaultDataSource implements DataSource
{

    // --- 实现接口 -----------------------------------------------------------------------------------------------------
    public Connection getConnection() throws SQLException
    {
        return null;
    }

    public Connection getConnection(String username, String password) throws SQLException
    {
        throw new UnsupportedOperationException("DefaultDataSource is base on connection pooling, does not support this operation.");
    }

    public PrintWriter getLogWriter() throws SQLException
    {
        return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException
    {

    }

    public void setLoginTimeout(int seconds) throws SQLException
    {

    }

    public int getLoginTimeout() throws SQLException
    {
        return 0;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        throw new SQLFeatureNotSupportedException();
    }

    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        throw new UnsupportedOperationException("DefaultDataSource is not a wrapper.");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return false;
    }
}
