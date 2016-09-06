package cn.jarvis.connection.pooling;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author zjnktion
 */
public class PooledConnection implements Connection
{

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private final Connection conn;
    private PreparedStatement validateStatement;

    // --- 构造方法 -----------------------------------------------------------------------------------------------------
    public PooledConnection(Connection conn)
    {
        if (conn == null)
        {
            throw new IllegalArgumentException("Can not set a null Connection to PooledConnection.");
        }

        this.conn = conn;
    }

    // --- 实现接口 -----------------------------------------------------------------------------------------------------
    public Statement createStatement() throws SQLException
    {
        return this.conn.createStatement();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException
    {
        return this.conn.prepareStatement(sql);
    }

    public CallableStatement prepareCall(String sql) throws SQLException
    {
        return this.conn.prepareCall(sql);
    }

    public String nativeSQL(String sql) throws SQLException
    {
        return this.conn.nativeSQL(sql);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException
    {
        this.conn.setAutoCommit(autoCommit);
    }

    public boolean getAutoCommit() throws SQLException
    {
        return this.conn.getAutoCommit();
    }

    public void commit() throws SQLException
    {
        this.conn.commit();
    }

    public void rollback() throws SQLException
    {
        this.conn.rollback();
    }

    public void close() throws SQLException
    {
        try
        {
            if (this.validateStatement != null)
            {
                this.validateStatement.close();
            }
        }
        finally
        {
            this.conn.close();
        }
    }

    public boolean isClosed() throws SQLException
    {
        return this.conn.isClosed();
    }

    public DatabaseMetaData getMetaData() throws SQLException
    {
        return this.conn.getMetaData();
    }

    public void setReadOnly(boolean readOnly) throws SQLException
    {
        this.conn.setReadOnly(readOnly);
    }

    public boolean isReadOnly() throws SQLException
    {
        return this.conn.isReadOnly();
    }

    public void setCatalog(String catalog) throws SQLException
    {
        this.conn.setCatalog(catalog);
    }

    public String getCatalog() throws SQLException
    {
        return this.conn.getCatalog();
    }

    public void setTransactionIsolation(int level) throws SQLException
    {
        this.conn.setTransactionIsolation(level);
    }

    public int getTransactionIsolation() throws SQLException
    {
        return this.conn.getTransactionIsolation();
    }

    public SQLWarning getWarnings() throws SQLException
    {
        return this.conn.getWarnings();
    }

    public void clearWarnings() throws SQLException
    {
        this.conn.clearWarnings();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
    {
        return this.conn.createStatement(resultSetType, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {
        return this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {
        return this.conn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException
    {
        return this.conn.getTypeMap();
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException
    {
        this.conn.setTypeMap(map);
    }

    public void setHoldability(int holdability) throws SQLException
    {
        this.conn.setHoldability(holdability);
    }

    public int getHoldability() throws SQLException
    {
        return this.conn.getHoldability();
    }

    public Savepoint setSavepoint() throws SQLException
    {
        return this.conn.setSavepoint();
    }

    public Savepoint setSavepoint(String name) throws SQLException
    {
        return this.conn.setSavepoint(name);
    }

    public void rollback(Savepoint savepoint) throws SQLException
    {
        this.conn.rollback(savepoint);
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException
    {
        this.conn.releaseSavepoint(savepoint);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        return this.conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        return this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        return this.conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
    {
        return this.conn.prepareStatement(sql, autoGeneratedKeys);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException
    {
        return this.conn.prepareStatement(sql, columnIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException
    {
        return this.conn.prepareStatement(sql, columnNames);
    }

    public Clob createClob() throws SQLException
    {
        return this.conn.createClob();
    }

    public Blob createBlob() throws SQLException
    {
        return this.conn.createBlob();
    }

    public NClob createNClob() throws SQLException
    {
        return this.conn.createNClob();
    }

    public SQLXML createSQLXML() throws SQLException
    {
        return this.conn.createSQLXML();
    }

    public boolean isValid(int timeout) throws SQLException
    {
        return this.conn.isValid(timeout);
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException
    {
        this.conn.setClientInfo(name, value);
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException
    {
        this.conn.setClientInfo(properties);
    }

    public String getClientInfo(String name) throws SQLException
    {
        return this.conn.getClientInfo(name);
    }

    public Properties getClientInfo() throws SQLException
    {
        return this.conn.getClientInfo();
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException
    {
        return this.conn.createArrayOf(typeName, elements);
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException
    {
        return this.conn.createStruct(typeName, attributes);
    }

    public void setSchema(String schema) throws SQLException
    {
        this.conn.setSchema(schema);
    }

    public String getSchema() throws SQLException
    {
        return this.conn.getSchema();
    }

    public void abort(Executor executor) throws SQLException
    {
        this.conn.abort(executor);
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException
    {
        this.conn.setNetworkTimeout(executor, milliseconds);
    }

    public int getNetworkTimeout() throws SQLException
    {
        return this.conn.getNetworkTimeout();
    }

    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        return this.conn.unwrap(iface);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return this.conn.isWrapperFor(iface);
    }

    // --- 公开方法 -----------------------------------------------------------------------------------------------------
    public Connection getOriginalConnection()
    {
        return this.conn;
    }

    public void validate(String sql, int timeout) throws SQLException
    {
        if (sql == null || sql.length() == 0)
        {
            if (timeout < 0)
            {
                timeout = 0;
            }
            if (!isValid(timeout))
            {
                throw new SQLException("isValid() returned false.");
            }
            return;
        }

        if (validateStatement == null)
        {
            validateStatement = prepareStatement(sql);
        }

        if (timeout > 0)
        {
            validateStatement.setQueryTimeout(timeout);
        }

        try (ResultSet rs = validateStatement.executeQuery())
        {
            if (!rs.next())
            {
                throw new SQLException("Validate sql return no data.");
            }
        }
        catch (SQLException e)
        {
            throw e;
        }
    }
}
