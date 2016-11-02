package com.insurancetelematics.team.projectl.android.core.cards;

import com.insurancetelematics.team.projectl.android.core.StoppableRunnable;
import com.insurancetelematics.team.projectl.core.ExecutionListener;
import com.insurancetelematics.team.projectl.core.Result;
import com.insurancetelematics.team.projectl.core.ThreadExecutor;
import java.util.List;

public abstract class CardsCreationStrategy {
    private final ThreadExecutor executor;

    protected CardsCreationStrategy(ThreadExecutor executor) {
        this.executor = executor;
    }

    public void create(ExecutionListener<Result<List<Card>>> listener) {
        Runnable runnable = obtainRunnable(listener);
        executor.runTask(runnable);
    }

    protected abstract Result<List<Card>> createCards();

    private Runnable obtainRunnable(final ExecutionListener<Result<List<Card>>> listener) {
        StoppableRunnable runnable = new StoppableRunnable() {
            private Result<List<Card>> result;

            @Override
            protected void doExecute() {
                result = createCards();
            }

            @Override
            protected void doPostExecute() {
                if (listener == null) return;

                listener.onFinished(result);
            }
        };
        runnable.setPostExecuteOnUiThread(true);

        return runnable;
    }
}
