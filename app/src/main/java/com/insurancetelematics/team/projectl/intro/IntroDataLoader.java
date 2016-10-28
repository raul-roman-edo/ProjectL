package com.insurancetelematics.team.projectl.intro;

import com.insurancetelematics.team.projectl.android.core.LoadRawResourceCommand;
import com.insurancetelematics.team.projectl.core.AsyncAction;
import com.insurancetelematics.team.projectl.core.AsyncActionListener;
import com.insurancetelematics.team.projectl.core.ThreadExecutor;

public class IntroDataLoader extends AsyncAction<Void, Intro> {
    private static final String EMPTY_ARRAY = "[]";
    private static final String EMPTY_OBJECT = "{}";
    private final LoadRawResourceCommand loadActorsCommand;
    private final LoadRawResourceCommand loadActionsCommand;
    private AsyncActionListener<Intro> onLoadFinishedListener;

    public void setOnLoadFinishedListener(AsyncActionListener<Intro> onLoadFinishedListener) {
        this.onLoadFinishedListener = onLoadFinishedListener;
    }

    protected IntroDataLoader(ThreadExecutor executor, LoadRawResourceCommand loadActorsCommand,
            LoadRawResourceCommand loadActionsCommand) {
        super(executor);
        this.loadActorsCommand = loadActorsCommand;
        this.loadActionsCommand = loadActionsCommand;
    }

    @Override
    protected Intro parallelExecution(Void aVoid) {
        Intro intro = new Intro();
        fillActors(intro);
        fillActions(intro);

        return intro;
    }

    private void fillActions(Intro intro) {
        String actions = loadActionsCommand.obtain();
        if (actions.isEmpty()) {
            actions = EMPTY_ARRAY;
        }
        intro.setRawActionsJson(actions);
    }

    private void fillActors(Intro intro) {
        String actors = loadActorsCommand.obtain();
        if (actors.isEmpty()) {
            actors = EMPTY_OBJECT;
        }
        intro.setRawActorsJson(actors);
    }

    @Override
    protected void postExecution(Intro result) {
        if (onLoadFinishedListener != null) {
            onLoadFinishedListener.onFinished(result);
        }
    }
}
