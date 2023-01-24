package com.project.eggtimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerViewModel extends ViewModel {
    private final TimerRepository timerRepository;

    private final MutableLiveData<Boolean> _isVibrationOn = new MutableLiveData<>();
    public final LiveData<Boolean> isVibrationOn = _isVibrationOn;

    public TimerViewModel(TimerRepository repository) {
        this.timerRepository = repository;
        _isVibrationOn.setValue(repository.isVibrationOn());
    }

    public void onVibrationButtonClicked() {
        Boolean value = timerRepository.toggleVibration();
        _isVibrationOn.setValue(value);
    }
}