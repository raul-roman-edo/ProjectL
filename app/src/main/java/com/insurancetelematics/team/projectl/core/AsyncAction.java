package com.insurancetelematics.team.projectl.core;

import com.insurancetelematics.team.projectl.android.core.StoppableRunnable;

public abstract class AsyncAction<Param, Return> {
    private final ThreadExecutor executor;

    protected AsyncAction(ThreadExecutor executor) {
        this.executor = executor;
    }

    public void run(Dispatcher<Return> dispatcher) {
        run(null, dispatcher);
    }

    public void run(Param param, Dispatcher<Return> dispatcher) {
        Runnable action = createAction(param, dispatcher);

        executor.runTask(action);
    }

    protected abstract Return parallelExecution(Param param);

    protected void postExecution(Return result, Dispatcher<Return> dispatcher) {
        dispatcher.dispatch(result);
    }

    private Runnable createAction(final Param param, final Dispatcher<Return> dispatcher) {
        StoppableRunnable runnable = new StoppableRunnable() {
            private Return result;

            @Override
            protected void doExecute() {
                result = parallelExecution(param);
            }

            @Override
            protected void doPostExecute() {
                postExecution(result, dispatcher);
            }
        };

        runnable.setPostExecuteOnUiThread(true);

        return runnable;
    }
}
