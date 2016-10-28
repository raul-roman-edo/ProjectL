package com.insurancetelematics.team.projectl.core;

import android.util.Log;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadExecutor {
    private static final String TAG = ThreadExecutor.class.getName();
    private static final int MAX_SECONDS = 10;
    private static final int CAPACITY = 5;
    private static final String RUN_TASK = "runTask";
    public static ThreadExecutor singleton = null;
    private ThreadPoolExecutor threadPool = null;

    private ThreadExecutor() {
        int poolSize = Runtime.getRuntime().availableProcessors();
        int maxPoolSize = Runtime.getRuntime().availableProcessors();
        long keepAliveTime = MAX_SECONDS;
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(CAPACITY);
        threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
    }

    public synchronized static ThreadExecutor getInstance() {
        if (singleton == null) {
            singleton = new ThreadExecutor();
        }

        return singleton;
    }

    public void runTask(Runnable task) {
        try {
            threadPool.execute(task);
        } catch (RejectedExecutionException e) {
            Log.e(TAG, RUN_TASK, e);
        }
    }

    public void shutDown() {
        threadPool.shutdown();
        singleton = null;
    }
}