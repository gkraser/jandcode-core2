package jandcode.commons.test;

import java.lang.management.*;

/**
 * Информация о памяти
 */
public class MemoryInfo {

    private long init;
    private long used;
    private long committed;
    private long max;

    /**
     * Создать для текущего состояния памяти
     */
    public static MemoryInfo create() {
        MemoryInfo res = new MemoryInfo();
        MemoryMXBean m = ManagementFactory.getMemoryMXBean();
        MemoryUsage mu = m.getHeapMemoryUsage();
        res.committed = mu.getCommitted();
        res.init = mu.getInit();
        res.max = mu.getMax();
        res.used = mu.getUsed();
        return res;
    }


    /**
     * Создать как разницу 2-х состояний
     */
    public static MemoryInfo diff(MemoryInfo oldInfo, MemoryInfo newInfo) {
        MemoryInfo res = new MemoryInfo();
        res.init = newInfo.init - oldInfo.init;
        res.used = newInfo.used - oldInfo.used;
        res.committed = newInfo.committed - oldInfo.committed;
        res.max = newInfo.max - oldInfo.max;
        return res;
    }

    //////

    public long getInit() {
        return init;
    }

    public long getUsed() {
        return used;
    }

    public long getCommitted() {
        return committed;
    }

    public long getMax() {
        return max;
    }

}
