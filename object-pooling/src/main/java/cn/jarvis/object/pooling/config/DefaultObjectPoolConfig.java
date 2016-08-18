package cn.jarvis.object.pooling.config;

/**
 * @author zjnktion
 */
public class DefaultObjectPoolConfig
{

    // --- 默认常量 -----------------------------------------------------------------------------------------------------
    public static final int DEFAULT_MAX_TOTAL = 8;
    public static final boolean DEFAULT_BLOCK_WHEN_RESOURCE_SHORTAGE = true;
    public static final long DEFAULT_WAIT_MILLIS = 1000L;
    public static final boolean DEFAULT_FAIR = false;

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private int maxTotal = DEFAULT_MAX_TOTAL;
    private boolean blockWhenResourceShortage = DEFAULT_BLOCK_WHEN_RESOURCE_SHORTAGE;
    private long waitMillis = DEFAULT_WAIT_MILLIS;
    private boolean fair = DEFAULT_FAIR;

    // --- getter setter -----------------------------------------------------------------------------------------------
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

    public long getWaitMillis()
    {
        return waitMillis;
    }

    public void setWaitMillis(long waitMillis)
    {
        this.waitMillis = waitMillis;
    }

    public boolean isFair()
    {
        return fair;
    }

    public void setFair(boolean fair)
    {
        this.fair = fair;
    }
}
