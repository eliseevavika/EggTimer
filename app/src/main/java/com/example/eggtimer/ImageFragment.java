package com.example.eggtimer;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private static final String POSITON = "position";
    private static final String SCALE = "scale";
    private static final String DRAWABLE_RESOURE = "resource";
    private int id;
    private int num;

    private int screenWidth;
    private int screenHeight;


    public static final ImageFragment[] imageArray = {
            new ImageFragment(R.drawable.a1min, 1),
            new ImageFragment(R.drawable.a3min, 3),
            new ImageFragment(R.drawable.a5min, 5),
            new ImageFragment(R.drawable.a7min, 7),
            new ImageFragment(R.drawable.a9min, 9),
            new ImageFragment(R.drawable.a11min, 11),
            new ImageFragment(R.drawable.a13min, 13),
            new ImageFragment(R.drawable.a15min, 15)
    };

    @SuppressLint("ValidFragment")
    private ImageFragment(int id, int num) {
        this.id = id;
        this.num = num;
    }

    public ImageFragment() {

    }

    public int getImageId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public static Fragment newInstance(MainActivity context, int pos, float scale) {
        Bundle b = new Bundle();
        b.putInt(POSITON, pos);
        b.putFloat(SCALE, scale);

        return Fragment.instantiate(context, ImageFragment.class.getName(), b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        final int postion = this.getArguments().getInt(POSITON);
        float scale = this.getArguments().getFloat(SCALE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth / 2, screenHeight / 2);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_image, container, false);

        TextView textView = linearLayout.findViewById(R.id.text);
        CarouselLinearLayout root = linearLayout.findViewById(R.id.root_container);
        final ImageView imageView = linearLayout.findViewById(R.id.imageView);

        textView.setText("Carousel item: " + postion);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(imageArray[postion].getImageId());

        imageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
                intent.putExtra(DRAWABLE_RESOURE, imageArray[postion].getImageId());
                startActivity(intent);
            }
        });


        root.setScaleBoth(scale);

        return linearLayout;
    }

    /**
     * Get device screen width and height
     */
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }
}

