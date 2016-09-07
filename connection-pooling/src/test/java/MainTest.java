import cn.jarvis.connection.pooling.PooledConnection;
import cn.jarvis.connection.pooling.PoolingDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author zjnktion
 */
public class MainTest
{

    static final PoolingDataSource PDS;
    static final String SQL = "select * from t_user";
    static final int TNUM = 1000;
    static final int LNUM = 100;
    static long time;

    static final BasicDataSource BDS;
    static final ComboPooledDataSource CPDS;

    static
    {
        PDS = new PoolingDataSource();
//        PDS.setMaxBlockMillis(3000L);
        PDS.setDriverClassName("com.mysql.cj.jdbc.Driver");
        PDS.setUrl("jdbc:mysql://10.31.90.118:3306/jarvis?serverTimezone=UTC&characterEncoding=utf-8");
        PDS.setUsername("root");
        PDS.setPassword("!2D#34S3aA$");

        BDS = new BasicDataSource();
        BDS.setDriverClassName("com.mysql.cj.jdbc.Driver");
        BDS.setUrl("jdbc:mysql://10.31.90.118:3306/jarvis?serverTimezone=UTC&characterEncoding=utf-8");
        BDS.setUsername("root");
        BDS.setPassword("!2D#34S3aA$6");

        CPDS = new ComboPooledDataSource();
        try
        {
            CPDS.setDriverClass("com.mysql.cj.jdbc.Driver");
        }
        catch (PropertyVetoException e)
        {
            e.printStackTrace();
        }
        CPDS.setMaxPoolSize(8);
        CPDS.setJdbcUrl("jdbc:mysql://10.31.90.118:3306/jarvis?serverTimezone=UTC&characterEncoding=utf-8");
        CPDS.setUser("root");
        CPDS.setPassword("!2D#34S3aA$");
    }

    public static void main(String[] args)
    {
//        QueryThread qt = new QueryThread();
//        QueryThread1 qt = new QueryThread1();
        QueryThread2 qt = new QueryThread2();

        Thread[] ts = new Thread[TNUM];
        for (int i = 0; i < TNUM; i++)
        {
            ts[i] = new Thread(qt);
        }
        time = System.currentTimeMillis();
        for (Thread t : ts)
        {
            t.start();
        }
    }

    static class QueryThread implements Runnable
    {
        public void run()
        {
            for (int i = 0; i < LNUM; i++)
            {
                Connection conn = null;
                try
                {
                    conn = PDS.getConnection();
//                    Statement stm = conn.createStatement();
//                    ResultSet rs = stm.executeQuery(SQL);
//                    System.err.println(Thread.currentThread().getName() + ":::" + conn + ":::" + "begin::::::::::::::::::::::::::::::::" + i);
//                    while (rs.next())
//                    {
//                        System.err.println("id:" + rs.getString(1) + "===name:" + rs.getString(2));
//                    }
//                    System.err.println(Thread.currentThread().getName() + ":::" + conn + ":::" + "end::::::::::::::::::::::::::::::::" + i);
                    Thread.yield();
                    PDS.releaseConnection((PooledConnection) conn);
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            long t = System.currentTimeMillis() - time;
            System.err.println(t);
        }
    }

    static class QueryThread1 implements Runnable
    {
        public void run()
        {
            for (int i = 0; i < LNUM; i++)
            {
                Connection conn = null;
                try
                {
                    conn = BDS.getConnection();
//                    Statement stm = conn.createStatement();
//                    ResultSet rs = stm.executeQuery(SQL);
//                    System.err.println(Thread.currentThread().getName() + ":::" + conn + ":::" + "begin::::::::::::::::::::::::::::::::" + i);
//                    while (rs.next())
//                    {
//                        System.err.println("id:" + rs.getString(1) + "===name:" + rs.getString(2));
//                    }
//                    System.err.println(Thread.currentThread().getName() + ":::" + conn + ":::" + "end::::::::::::::::::::::::::::::::" + i);
                    Thread.yield();
                    conn.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            long t = System.currentTimeMillis() - time;
            System.err.println(t);
        }
    }

    static class QueryThread2 implements Runnable
    {
        public void run()
        {
            for (int i = 0; i < LNUM; i++)
            {
                Connection conn = null;
                try
                {
                    conn = CPDS.getConnection();
//                    Statement stm = conn.createStatement();
//                    ResultSet rs = stm.executeQuery(SQL);
//                    System.err.println(Thread.currentThread().getName() + ":::" + conn + ":::" + "begin::::::::::::::::::::::::::::::::" + i);
//                    while (rs.next())
//                    {
//                        System.err.println("id:" + rs.getString(1) + "===name:" + rs.getString(2));
//                    }
//                    System.err.println(Thread.currentThread().getName() + ":::" + conn + ":::" + "end::::::::::::::::::::::::::::::::" + i);
                    Thread.yield();
                    conn.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            long t = System.currentTimeMillis() - time;
            System.err.println(t);
        }
    }
}
