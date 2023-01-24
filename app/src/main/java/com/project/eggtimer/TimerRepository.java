package com.project.eggtimer;

public class TimerRepository {
    private final TimerLocalDataSource local;

    TimerRepository(TimerLocalDataSource local) {
        this.local = local;
    }

    public Boolean isVibrationOn() {
        return local.isVibrationOn();
    }

    public Boolean toggleVibration() {
        return local.toggleVibration();
    }
}