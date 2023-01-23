package com.project.eggtimer;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

enum ButtonState {Start, Stop}

public class MainActivity extends AppCompatActivity {
    public static final String MY_PREFERENCES = "MyPrefs";
    private final List<TimerTab> timerTabs = new ArrayList<TimerTab>() {
        {
            add(new TimerTab(R.string.tab_title_soft, R.drawable.ic_new_layer1, TimeUnit.MINUTES.toMillis(4) + TimeUnit.SECONDS.toMillis(0)));
            add(new TimerTab(R.string.tab_title_medium, R.drawable.ic_new_layer2, TimeUnit.MINUTES.toMillis(6) + TimeUnit.SECONDS.toMillis(0)));
            add(new TimerTab(R.string.tab_title_hard, R.drawable.ic_new_layer3, TimeUnit.MINUTES.toMillis(9) + TimeUnit.SECONDS.toMillis(0)));
        }
    };
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    TextView timeView;
    MediaPlayer ring;
    long mTimeLeftInMillis = 0;
    Button startStopButton;
    ImageButton vibrationButton;
    CountDownTimer countDownTimer;
    Boolean vibrationOn = false;
    Vibrator vibration;
    private SharedPreferences sharedPreferences;
    private ButtonState startStopButtonState = ButtonState.Start;
    private TimerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrationButton = findViewById(R.id.btn_vibration);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);

        TimerLocalDataSource local = new TimerLocalDataSource(sharedPreferences);
        TimerRepository repository = new TimerRepository(local);

        viewModel = new TimerViewModel(repository);
        viewModel.isVibrationOn.observe(this, value -> {
            vibrationOn = value;
            vibrationButton.setImageResource(value ? R.drawable.ic_vibration_black_24dp : R.drawable.ic_notifications_none_black_24dp);
        });

        timeView = findViewById(R.id.time_view);
        tabLayout = findViewById(R.id.tab_layout);
        startStopButton = findViewById(R.id.btn_start_stop);

        vibration = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        setUpViewPager();

        new TabLayoutMediator(
                tabLayout,
                viewPager2,
                (tab, position) -> tab.setText(timerTabs.get(position).tabName)
        ).attach();

        startStopButton.setOnClickListener(v -> {
            if (startStopButtonState == ButtonState.Start) {
                onStartClick();
            } else if (startStopButtonState == ButtonState.Stop) {
                onStopClick();
            } else throw new IllegalStateException("Invalid State");
        });

        vibrationButton.setOnClickListener(view -> {
            viewModel.onVibrationButtonClicked();
            Boolean value = viewModel.isVibrationOn.getValue();
            vibrationOn = value;
        });
    }

    private void setUpViewPager() {
        viewPager2 = findViewById(R.id.viewPager2);
        registerOnPageChangeCallback();
        PagerAdapter pagerAdapter = new PagerAdapter(timerTabs);
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setCurrentItem(1, false);
    }

    private void registerOnPageChangeCallback() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTimeLeftInMillis = 0;
                startStopButton.setText(R.string.action_start);
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position) {
                    case 0:
                        timeView.setText(String.format(Locale.US, "%02d:%02d", 4, 0));
                        break;
                    case 1:
                        timeView.setText(String.format(Locale.US, "%02d:%02d", 6, 0));
                        break;
                    case 2:
                        timeView.setText(String.format(Locale.US, "%02d:%02d", 9, 0));
                        break;
                }
            }
        });
    }

    private void onStopClick() {
        vibration.cancel();
        vibrationButton.setEnabled(true);
        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        tabStrip.setEnabled(false);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(true);
        }
        viewPager2.setUserInputEnabled(true);
        countDownTimer.cancel();
        startStopButtonState = ButtonState.Start;
        startStopButton.setText(R.string.action_start);
        if (ring != null) {
            ring.stop();
            ring = null;
        }
        mTimeLeftInMillis = getTimerValue();
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        timeView.setText(String.format(Locale.US, "%02d:%02d", minutes, seconds));
    }

    private void onStartClick() {
        startStopButtonState = ButtonState.Stop;
        startStopButton.setText(R.string.action_stop);
        mTimeLeftInMillis = getTimerValue();

        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        tabStrip.setEnabled(false);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(false);
        }

        viewPager2.setUserInputEnabled(false);

        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
                int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
                timeView.setText(String.format(Locale.US, "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                long[] mVibratePattern = new long[]{0, 400, 200, 400};
                if (!vibrationOn) {
                    vibrationButton.setEnabled(false);
                    ring = MediaPlayer.create(MainActivity.this, R.raw.ring);
                    ring.start();
                    ring.setLooping(true);
                } else {
                    vibrationButton.setEnabled(false);
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibration.vibrate(VibrationEffect.createWaveform(mVibratePattern, 0));
                    } else {
                        vibration.vibrate(200);
                    }
                }
            }
        }.start();
    }

    private long getTimerValue() {
        int position = tabLayout.getSelectedTabPosition();
        switch (position) {
            case 0:
                return timerTabs.get(0).timeMillis;
            case 1:
                return timerTabs.get(1).timeMillis;
            case 2:
                return timerTabs.get(2).timeMillis;
        }
        throw new IllegalStateException("Is there an extra Tab");
    }
}




