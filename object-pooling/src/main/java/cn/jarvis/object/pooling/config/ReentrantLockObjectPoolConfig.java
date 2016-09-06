package cn.jarvis.object.pooling.config;

/**
 * @author zjnktion
 */
public class ReentrantLockObjectPoolConfig
{

    // --- 默认常量 -----------------------------------------------------------------------------------------------------
    public static final int DEFAULT_MAX_TOTAL = 8;
    public static final boolean DEFAULT_BLOCK_WHEN_RESOURCE_SHORTAGE = true;
    public static final long DEFAULT_MAX_BLOCK_MILLIS = -1;
    public static final boolean DEFAULT_RETRY_WHILE_CHECK_OUT_VALIDATE_FAIL = true;
    public static final long DEFAULT_MAX_IDLE_VALIDATE_MILLIS = 1000L * 60L * 30L;
    public static final boolean DEFAULT_FAIR = false;

    // --- 基本字段 -----------------------------------------------------------------------------------------------------
    private int maxTotal = DEFAULT_MAX_TOTAL;
    private boolean blockWhenResourceShortage = DEFAULT_BLOCK_WHEN_RESOURCE_SHORTAGE;
    private long maxBlockMillis = DEFAULT_MAX_BLOCK_MILLIS; // 当blockWhenResourceShortage为true是，该属性才生效
    private boolean retryWhileCheckOutValidateFail = DEFAULT_RETRY_WHILE_CHECK_OUT_VALIDATE_FAIL;
    private long maxIdleValidateMillis = DEFAULT_MAX_IDLE_VALIDATE_MILLIS;
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

    public boolean isFair()
    {
        return fair;
    }

    public void setFair(boolean fair)
    {
        this.fair = fair;
    }
}
