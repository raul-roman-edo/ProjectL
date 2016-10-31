package com.insurancetelematics.team.projectl.intro;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.insurancetelematics.team.projectl.R;
import com.insurancetelematics.team.projectl.android.core.LoadRawResourceCommand;
import com.insurancetelematics.team.projectl.core.ThreadExecutor;
import com.insurancetelematics.team.projectl.main.MainPage;

public class IntroActivity extends AndroidApplication implements IntroView {
    private IntroPresenter presenter;
    private FrameLayout frame;

    @Override
    public void addLayers() {
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        frame = new FrameLayout(this);
        frame.setKeepScreenOn(true);
        addContentView(frame, params);
    }

    @Override
    public void allowSkip() {
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSkipPressed();
            }
        });
    }

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
        FirstTimeStore firstTimeStore = new FirstTimeStore(getApplicationContext());
        LoadRawResourceCommand loadActorsCommand = new LoadRawResourceCommand(getApplicationContext(), R.raw.actors);
        LoadRawResourceCommand loadActionsCommand = new LoadRawResourceCommand(getApplicationContext(), R.raw.actions);
        IntroDataLoader introDataLoader =
                new IntroDataLoader(ThreadExecutor.getInstance(), loadActorsCommand, loadActionsCommand);
        MainPage mainPage = new MainPage(this);
        presenter = new IntroPresenter(this, ThreadExecutor.getInstance(), firstTimeStore, introDataLoader, mainPage);
    }
}
