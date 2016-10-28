package com.insurancetelematics.team.projectl.intro;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.insurancetelematics.team.projectl.core.Store;

public class FirstTimeStore implements Store<Boolean> {
    private static final String KEY = "isFirstTime";
    private SharedPreferences prefs;

    public FirstTimeStore(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public Boolean load() {
        return prefs.getBoolean(KEY, true);
    }

    @Override
    public void save(Boolean data) {
        prefs.edit().putBoolean(KEY, data).commit();
    }
}
