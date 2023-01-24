package com.project.eggtimer;

import android.content.SharedPreferences;

public class TimerLocalDataSource {
   private final SharedPreferences sharedPrefs;
    private final String vibration_key = "vibration";

    TimerLocalDataSource(SharedPreferences sharedPrefs) {
        this.sharedPrefs = sharedPrefs;
    }

    public Boolean isVibrationOn() {
        return sharedPrefs.getBoolean(vibration_key, false);
    }

    public Boolean toggleVibration() {
        boolean currentValue = sharedPrefs.getBoolean(vibration_key, false);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(vibration_key, !currentValue);
        editor.apply();
        return sharedPrefs.getBoolean(vibration_key, false);
    }
}