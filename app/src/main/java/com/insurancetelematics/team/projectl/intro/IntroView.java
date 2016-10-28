package com.insurancetelematics.team.projectl.intro;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public interface IntroView {
    void initialize(ApplicationListener app, AndroidApplicationConfiguration config);

    void close();
}
