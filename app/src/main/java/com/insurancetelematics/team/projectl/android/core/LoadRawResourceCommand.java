package com.insurancetelematics.team.projectl.android.core;

import android.content.Context;

public class LoadRawResourceCommand {
    private final Context context;
    private final int resId;

    public LoadRawResourceCommand(Context context, int resId) {
        this.context = context;
        this.resId = resId;
    }

    public String obtain() {
        String actors = ResourceUtils.extractStringFromRawResource(context, resId);

        return actors;
    }
}
