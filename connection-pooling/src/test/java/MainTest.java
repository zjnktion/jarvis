import cn.jarvis.connection.pooling.PoolingDataSource;

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
    static final int TNUM = 1;
    static final int LNUM = 100;
    static long time;

    static
    {
        PDS = new PoolingDataSource();
        PDS.setDriverClassName("com.mysql.cj.jdbc.Driver");
        PDS.setUrl("jdbc:mysql://localhost:3306/jarvis?serverTimezone=UTC&characterEncoding=utf-8");
        PDS.setUsername("root");
        PDS.setPassword("aizai051023");
    }

    public static void main(String[] args)
    {
        QueryThread qt = new QueryThread();

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
                    Statement stm = conn.createStatement();
                    ResultSet rs = stm.executeQuery(SQL);
                    System.err.println(Thread.currentThread().getName() + ":::" + conn + ":::" + "begin::::::::::::::::::::::::::::::::");
                    while (rs.next())
                    {
                        System.err.println("id:" + rs.getString(1) + "===name:" + rs.getString(2));
                    }
                    System.err.println(Thread.currentThread().getName() + ":::" + conn + ":::" + "end::::::::::::::::::::::::::::::::" + i);

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
