package ling.runstep.utils;

/**
 * Created by Jalyn on 2016/1/19.
 */
public class ThreadPoolFactory {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;
    private static ThreadPoolProxy commonThreadPool;
    private static ThreadPoolProxy downThreadPool;

    public static ThreadPoolProxy getCommonThreadPool() {
        if (commonThreadPool == null) {
            synchronized (ThreadPoolFactory.class) {
                if (commonThreadPool == null) {
                    commonThreadPool = new ThreadPoolProxy(
                            CORE_POOL_SIZE,
                            MAXIMUM_POOL_SIZE,
                            KEEP_ALIVE);
                }
            }
        }
        return commonThreadPool;
    }

    public static ThreadPoolProxy getDownThreadPool() {
        if (downThreadPool == null) {
            synchronized (ThreadPoolFactory.class) {
                if (downThreadPool == null) {
                    downThreadPool = new ThreadPoolProxy(
                            CORE_POOL_SIZE,
                            MAXIMUM_POOL_SIZE,
                            KEEP_ALIVE);
                }
            }
        }
        return downThreadPool;
    }

}
