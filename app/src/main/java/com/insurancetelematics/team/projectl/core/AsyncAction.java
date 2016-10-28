package com.insurancetelematics.team.projectl.core;

import com.insurancetelematics.team.projectl.android.core.StoppableRunnable;

public abstract class AsyncAction<Param, Return> {
    private final ThreadExecutor executor;

    protected AsyncAction(ThreadExecutor executor) {
        this.executor = executor;
    }

    public void run(Param param) {
        Runnable action = createAction(param);

        executor.runTask(action);
    }

    protected abstract Return parallelExecution(Param param);

    protected abstract void postExecution(Return result);

    private Runnable createAction(final Param param) {
        StoppableRunnable runnable = new StoppableRunnable() {
            private Return result;

            @Override
            protected void doExecute() {
                result = parallelExecution(param);
            }

            @Override
            protected void doPostExecute() {
                postExecution(result);
            }
        };

        runnable.setPostExecuteOnUiThread(true);

        return runnable;
    }
}
