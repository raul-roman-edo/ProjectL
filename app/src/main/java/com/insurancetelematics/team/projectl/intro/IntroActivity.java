package com.insurancetelematics.team.projectl.intro;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.insurancetelematics.team.projectl.R;
import com.insurancetelematics.team.projectl.android.core.LoadRawResourceCommand;
import com.insurancetelematics.team.projectl.core.ThreadExecutor;
import com.insurancetelematics.team.projectl.main.MainPage;

public class IntroActivity extends AndroidApplication implements IntroView {
    private IntroPresenter presenter;

    @Override
    public void close() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPresenter();
        presenter.prepare();
    }

    private void createPresenter() {
        LoadRawResourceCommand loadActorsCommand = new LoadRawResourceCommand(getApplicationContext(), R.raw.actors);
        LoadRawResourceCommand loadActionsCommand = new LoadRawResourceCommand(getApplicationContext(), R.raw.actions);
        IntroDataLoader introDataLoader =
                new IntroDataLoader(ThreadExecutor.getInstance(), loadActorsCommand, loadActionsCommand);
        MainPage mainPage = new MainPage(this);
        presenter = new IntroPresenter(this, ThreadExecutor.getInstance(), introDataLoader, mainPage);
    }
}
