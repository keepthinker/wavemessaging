package com.keepthinker.wavemessaging.core;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by keepthinker on 2017/4/28.
 */
public class ConcurrentTool {

    public static ConcurrentToolBuilder newBuilder(){
        return new ConcurrentToolBuilder();
    }

    public static class ConcurrentToolBuilder {

        private final ConcurrentTool concurrentTool;

        public ConcurrentToolBuilder(){
            concurrentTool = new ConcurrentTool();
        }

        public ConcurrentToolBuilder setCorePoolSize(int corePoolSize){
            concurrentTool.corePoolSize = corePoolSize;
            return this;
        }
        public ConcurrentToolBuilder setMaxPoolSize(int maxPoolSize) {
            concurrentTool.maxPoolSize = maxPoolSize;
            return this;
        }

        public ConcurrentToolBuilder setMaxWorkQueueSize(int maxWorkQueueSize) {
            concurrentTool.maxWorkQueueSize = maxWorkQueueSize;
            return this;
        }

        public ConcurrentToolBuilder setThreadAbortListener(ThreadRejectedListener threadAbortListener){
            concurrentTool.threadRejectedListener = threadAbortListener;
            return this;
        }
        public ConcurrentTool build(){
            concurrentTool.blockingQueue = new LinkedBlockingQueue<Runnable>(concurrentTool.maxWorkQueueSize);
            concurrentTool.executor = new ThreadPoolExecutor(
                    concurrentTool.corePoolSize, concurrentTool.maxPoolSize,
                    10L, TimeUnit.MINUTES,
                    concurrentTool.blockingQueue,
                    new ExceptionLogThreadFactory(),
                    new DiscardAndLogPolicy(concurrentTool.threadRejectedListener));
            return concurrentTool;
        }

    }

    private final static Logger LOGGER = LogManager.getLogger(ConcurrentTool.class);

    private int corePoolSize = 10;
    private int maxPoolSize = 15;
    private int maxWorkQueueSize = 1000000;

    private BlockingQueue<Runnable> blockingQueue;
    private ExecutorService executor;

    private ThreadRejectedListener threadRejectedListener;


    public void execute(Runnable runnable){
        executor.execute(runnable);
    }

    public void submit(Callable callable){
        executor.submit(callable);
    }

    public int workPoolSize(){
        return blockingQueue.size();
    }

    /**
     * don't depend on this method to decide to add task to the thread pool without aborting the task.
     * @return
     */
    public boolean isAvailable(){
        return blockingQueue.size() < maxWorkQueueSize;
    }

    private static class DiscardAndLogPolicy implements RejectedExecutionHandler{

        private ThreadRejectedListener threadRejectedListener;

        public DiscardAndLogPolicy(ThreadRejectedListener threadAbortListeners){
            this.threadRejectedListener = threadAbortListeners;
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            LOGGER.error("Thread pool executor is full and can't accept more task");
            if(threadRejectedListener != null) {
                threadRejectedListener.abortEvent(r);
            }
        }
    }

    private static class ExceptionLogThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public ExceptionLogThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            t.setUncaughtExceptionHandler(new UncaughtExceptionLogHandler());
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }

        private static class UncaughtExceptionLogHandler implements Thread.UncaughtExceptionHandler{

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                LOGGER.error(String.format("Thread: %s", t), e);
            }
        }

    }


}

