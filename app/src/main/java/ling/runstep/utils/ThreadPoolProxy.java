package ling.runstep.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jalyn on 2016/1/19.
 */
public class ThreadPoolProxy {
    private ThreadPoolExecutor executor;
    private int corePoolSize;
    private int maximumPoolSize;
    private long keepAliveTime;
    private List<Runnable> runnables = new ArrayList<>();

    public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
    }

    public void initThreadPoolProxy() {
        if (executor == null) {
            synchronized (ThreadPoolProxy.class) {
                if (executor == null) {
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(Integer.MAX_VALUE);
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
                    executor = new ThreadPoolExecutor(
                            corePoolSize,
                            maximumPoolSize,
                            keepAliveTime,
                            TimeUnit.SECONDS,
                            workQueue,
                            threadFactory,
                            handler);
                }
            }
        }
    }

    public void execute(Runnable task) {
        initThreadPoolProxy();
        runnables.add(task);
        executor.execute(task);
    }

    // 提交任务（Future+execute(Runnable task)）
    public Future<?> summit(Runnable task) {
        initThreadPoolProxy();
        runnables.add(task);
        return executor.submit(task);
    }

    public void remove(Runnable task) {
        initThreadPoolProxy();
        executor.remove(task);
    }

    public void removeAll() {
        initThreadPoolProxy();
        if (runnables.size() > 0) {
            for (int i = 0; i < runnables.size(); i++) {
                executor.remove(runnables.get(i));
            }
        }
    }
}
