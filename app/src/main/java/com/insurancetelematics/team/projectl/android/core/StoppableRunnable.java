package com.insurancetelematics.team.projectl.android.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public abstract class StoppableRunnable implements Runnable {
    private static final String TAG = StoppableRunnable.class.getName();
    private boolean stop = false;
    private Thread myThread;
    private boolean postExecuteOnUiThread = false;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            doPostExecute();
        }
    };

    public void setPostExecuteOnUiThread(boolean enable) {
        this.postExecuteOnUiThread = enable;
    }

    public final synchronized void stop() {
        Log.d(TAG, "setting stop flag on " + this);
        stop = true;
        if (myThread != null) {
            Log.d(TAG, "interrupting current thread: " + myThread + " for task: " + this);
            myThread.interrupt();
        }
    }

    @Override
    public final void run() {
        if (stop) {
            Log.d(TAG, "stopping task: " + this);
            return;
        }
        synchronized (this) {
            myThread = Thread.currentThread();
        }
        doExecute();
        synchronized (this) {
            myThread = null;
        }

        if (postExecuteOnUiThread) {
            mHandler.sendEmptyMessage(0);
        } else {
            doPostExecute();
        }
    }

    protected abstract void doExecute();

    protected void doPostExecute() {
    }
}
