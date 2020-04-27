package com.example.eggtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

enum ButtonState {Start, Stop}

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    TextView timeView;
    MediaPlayer ring;
    long mTimeLeftInMillis = 0;
    Button startStopButton;
    CountDownTimer countDownTimer;

    private ButtonState startStopButtonState = ButtonState.Start;

    public static final int[] imageArray = {R.drawable.ic_layer1, R.drawable.ic_layer2, R.drawable.ic_layer3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeView = findViewById(R.id.time_view);
        tabLayout = findViewById(R.id.tab_layout);
        startStopButton = findViewById(R.id.btn);
        timeView.setText(String.format("%02d:%02d", 4, 40));

        setUpViewPager();

        String[] strings = new String[]{"SOFT", "MEDIUM", "HARD"};

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(strings[position])).attach();
        setUpPagerAdapter();
        startStopButton.setOnClickListener(v -> {
            if (startStopButtonState == ButtonState.Start) {
                onStartClick();
            } else if (startStopButtonState == ButtonState.Stop) {
                onStopClick();
            } else throw new IllegalStateException("Invalid State");
        });
    }

    private void setUpViewPager() {
        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setAdapter(createCardAdapter());
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
                        timeView.setText(String.format("%02d:%02d", 4, 40));
                        break;
                    case 1:
                        timeView.setText(String.format("%02d:%02d", 5, 40));
                        break;
                    case 2:
                        timeView.setText(String.format("%02d:%02d", 9, 40));
                        break;
                }
            }
        });
    }

    private void onStopClick() {
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
        timeView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void onStartClick() {
        startStopButtonState = ButtonState.Stop;
        startStopButton.setText("stop");
        mTimeLeftInMillis = getTimerValue();
        viewPager2.setUserInputEnabled(false);

        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
                int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
                timeView.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                ring = MediaPlayer.create(MainActivity.this, R.raw.ring);
                ring.start();
                ring.setLooping(true);
            }
        }.start();
    }

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        return adapter;
    }

    private void setUpPagerAdapter() {
        PagerAdapter pagerAdapter = new PagerAdapter(fetchDummyData());
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }

    private List<PagerM> fetchDummyData() {
        List<PagerM> pagerMList = new ArrayList<>();

        for (int image : imageArray) {
            PagerM pagerM = new PagerM();
            pagerM.setImageId(image);
            pagerMList.add(pagerM);
        }
        return pagerMList;
    }

    private long getTimerValue() {
        int position = tabLayout.getSelectedTabPosition();
        switch (position) {
            case 0:
                return TimeUnit.SECONDS.toMillis(4);
            case 1:
                return TimeUnit.MINUTES.toMillis(5) + TimeUnit.SECONDS.toMillis(40);
            case 2:
                return TimeUnit.MINUTES.toMillis(9) + TimeUnit.SECONDS.toMillis(40);
        }
        throw new IllegalStateException("Is there an extra Tab");
    }
}




