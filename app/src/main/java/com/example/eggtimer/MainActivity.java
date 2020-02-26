package com.example.eggtimer;

import android.app.ActivityOptions;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    public final static int LOOPS = 1000;
    public CarouselPagerAdapter adapter;
    public ViewPager pager;
//    private FragmentPagerAdapter fragmentPagerAdapter;
    public static int count = 8; //ViewPager items size
    public static int FIRST_PAGE = 8;

//    private List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActivityCompat.postponeEnterTransition(this);

        setContentView(R.layout.activity_main);

//        this.fragments.add(1, new ImageFragment());
//        this.fragments.add(2, new ImageDetailsActivity());


        pager = findViewById(R.id.pager);

        //set page margin between pages for viewpager
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int pageMargin = ((metrics.widthPixels / 4) * 2);
        pager.setPageMargin(-pageMargin);

        adapter = new CarouselPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        pager.addOnPageChangeListener(adapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        pager.setCurrentItem(FIRST_PAGE);
        pager.setOffscreenPageLimit(3);

    }




    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

    }
}


