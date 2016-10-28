package com.insurancetelematics.team.projectl.main;

import android.content.Context;

public class MainPage {
    private final Context context;

    public MainPage(Context context) {
        this.context = context;
    }

    public void navigate() {
        MainActivity.launch(context);
    }
}
