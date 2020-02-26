package com.example.eggtimer;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageDetailsActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    MediaPlayer ring;
    private final long startTime = 30000;
    private final long interval = 1000;

    private ImageView imageView;
    private Button button;
    TextView timeView;
    public int counter;
    int min = 0;
    private static final String DRAWABLE_RESOURE = "resource";

    final Handler handler = new Handler();


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timeView.setText(String.format("%d:%02d", minutes, seconds));

            timeView.setText(String.format("%d:%02d", minutes, seconds));

            handler.postDelayed(this, 500);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_image);

        imageView = findViewById(R.id.imageViewDetails);
        button = findViewById(R.id.btnClose);
        timeView = findViewById(R.id.time_view);
        int drawbleResource = getIntent().getIntExtra(DRAWABLE_RESOURE, 0);
        imageView.setImageResource(drawbleResource);

        for (int i = 0; i < ImageFragment.imageArray.length; i++) {
            if (ImageFragment.imageArray[i].getImageId() == drawbleResource) {
                min = ImageFragment.imageArray[i].getNum();
            }
        }


        new CountDownTimer(min * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                        0,
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

//                String.format(Locale.getDefault(), "%d:%02d:%02d", 0, min, seconds)
                timeView.setText(hms);//set text
            }

            @Override
            public void onFinish() {
                timeView.setText("Finished");
                ring = MediaPlayer.create(ImageDetailsActivity.this, R.raw.ring);
                ring.start();
                ring.setLooping(true);
            }
        }.start();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
                if (ring != null) {
                    ring.stop();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }


//    @Override
//    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);

//        RelativeLayout linearLayout = (RelativeLayout) inflater.inflate(R.layout.activity_full_image, container, false);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setSharedElementReturnTransition(TransitionInflater.from(
//                    getActivity()).inflateTransition(R.transition.change_image_trans));
//            setExitTransition(TransitionInflater.from(
//                    getActivity()).inflateTransition(android.R.transition.fade));
//
//            endFragment.setSharedElementEnterTransition(TransitionInflater.from(
//                    getActivity()).inflateTransition(R.transition.change_image_trans));
//            endFragment.setEnterTransition(TransitionInflater.from(
//                    getActivity()).inflateTransition(android.R.transition.fade));
//        }

//        progressBar= findViewById(R.id.progressBar);

//        imageViewDetails = linearLayout.findViewById(R.id.imageViewDetails);
//        button = linearLayout.findViewById(R.id.btnClose);
//        timeView = linearLayout.findViewById(R.id.time_view);

//        int drawbleResource = this.getArguments().getInt(DRAWABLE_RESOURE);



}
