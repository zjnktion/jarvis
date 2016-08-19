package cn.jarvis.object.pooling.config;

/**
 * @author zjnktion
 */
public class DefaultObjectPoolConfig
{

    // --- 默认常量 -----------------------------------------------------------------------------------------------------
    public static final int DEFAULT_MAX_TOTAL = 8;
    public static final int DEFAULT_MAX_IDLE = 8;
    public static final boolean DEFAULT_BLOCK_WHEN_RESOURCE_SHORTAGE = true;
    public static final long DEFAULT_MAX_BLOCK_MILLIS = -1;
    public static final boolean DEFAULT_FAIR = false;

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private int maxTotal = DEFAULT_MAX_TOTAL;
    private int maxIdle = DEFAULT_MAX_IDLE;
    private boolean blockWhenResourceShortage = DEFAULT_BLOCK_WHEN_RESOURCE_SHORTAGE;
    private long maxBlockMillis = DEFAULT_MAX_BLOCK_MILLIS; // 当blockWhenResourceShortage为true是，该属性才生效
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

    public int getMaxIdle()
    {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle)
    {
        this.maxIdle = maxIdle;
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

    public boolean isFair()
    {
        return fair;
    }

    public void setFair(boolean fair)
    {
        this.fair = fair;
    }
}
