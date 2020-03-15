package com.example.eggtimer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    TextView timeView;
    long starttime = 0;
    long mTimeLeftInMillis = starttime;
    Button btn;

    public static final int[] imageArray = {R.drawable.image, R.drawable.image, R.drawable.image};

    Handler handler = new Handler();

    Runnable run = new Runnable() {
        @Override
        public void run() {

            int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
            int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
            timeView.setText(String.format("%02d:%02d", minutes, seconds));

            handler.postDelayed(this, 1000);
        }
    };

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeView = findViewById(R.id.time_view);
        viewPager2 = findViewById(R.id.viewPager2);
        tabLayout = findViewById(R.id.tab_layout);
        btn = findViewById(R.id.btn);
        viewPager2.setAdapter(createCardAdapter());

        timeView.setText(String.format("%02d:%02d", 4, 40));

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                timer.cancel();
                handler.removeCallbacks(run);
                mTimeLeftInMillis = 0;
                starttime = 0;
                btn.setText("start");
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


        String[] strings = new String[]{"SOFT", "MEDIUM", "HARD"};

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(strings[position])).attach();
        setUpPagerAdapter();

        btn.setText("start");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = tabLayout.getSelectedTabPosition();

                switch (position) {
                    case 0:
                        mTimeLeftInMillis = 280000;
                        startStopAction(btn);
                        break;
                    case 1:
                        mTimeLeftInMillis = 340000;
                        startStopAction(btn);
                        break;
                    case 2:
                        mTimeLeftInMillis = 580000;
                        startStopAction(btn);
                        break;
                }


            }
        });
    }

    private void startStopAction(Button btn) {
        if (btn.getText().equals("stop")) {
            int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
            int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
            timeView.setText(String.format("%02d:%02d", minutes, seconds));

//            timeView.setText(String.format("%02d:%02d", 0, 0));
            timer.cancel();
            handler.removeCallbacks(run);
            mTimeLeftInMillis = 0;
            starttime = 0;
            btn.setText("start");
        } else {
            starttime = System.currentTimeMillis();
            timer = new Timer();
            new CountDownTimer(mTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftInMillis = millisUntilFinished;
                }  

                @Override
                public void onFinish() {
//                    timeView.setText("Stop");
//                            ring = MediaPlayer.create(ImageDetailsActivity.this, R.raw.ring);
//                            ring.start();
//                            ring.setLooping(true);
                }
            }.start();

            handler.postDelayed(run, 0);
            btn.setText("stop");
        }
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
}




