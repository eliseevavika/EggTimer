package com.project.eggtimer;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class TimerTab {
    @StringRes
    final int tabName;
    @DrawableRes
    final int image;
    final long timeMillis;

    public TimerTab(@StringRes int tabName, @DrawableRes int image, long timeMillis) {
        this.tabName = tabName;
        this.image = image;
        this.timeMillis = timeMillis;
    }
}