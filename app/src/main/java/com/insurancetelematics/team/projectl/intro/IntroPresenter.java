package com.insurancetelematics.team.projectl.intro;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.insurancetelematics.team.projectl.core.AsyncActionListener;
import com.insurancetelematics.team.projectl.core.ThreadExecutor;
import com.insurancetelematics.team.projectl.main.MainPage;
import es.tid.pdg.gdx.main.MainGDX;
import es.tid.pdg.gdx.main.OnFinishedActions;

public class IntroPresenter {
    private final IntroView view;
    private final ThreadExecutor executor;
    private final IntroDataLoader introDataLoader;
    private final MainPage mainPage;
    private MainGDX app;

    private AsyncActionListener<Intro> onDataLoaded = new AsyncActionListener<Intro>() {
        @Override
        public void onFinished(final Intro result) {
            executor.runTask(new Runnable() {
                @Override
                public void run() {
                    app.configure(result.getRawActorsJson(), result.getRawActionsJson(), actionsListener);
                }
            });
        }
    };

    private OnFinishedActions actionsListener = new OnFinishedActions() {
        @Override
        public void onFinished() {
            goToNextScreen();
        }
    };

    public IntroPresenter(IntroView view, ThreadExecutor executor, IntroDataLoader introDataLoader, MainPage mainPage) {
        this.view = view;
        this.executor = executor;
        this.introDataLoader = introDataLoader;
        this.mainPage = mainPage;
    }

    public void prepare() {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        app = new MainGDX();
        view.initialize(app, config);
        introDataLoader.setOnLoadFinishedListener(onDataLoaded);
        introDataLoader.run(null);
    }

    private void goToNextScreen() {
        mainPage.navigate();
        view.close();
    }
}
