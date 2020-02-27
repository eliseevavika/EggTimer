package com.example.eggtimer;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

//import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2 = findViewById(R.id.viewPager2);
        setUpPagerAdapter();
    }

    /**
     * set up adapter same like you do for RecyclerView or other components
     */
    private void setUpPagerAdapter() {
        PagerAdapter pagerAdapter = new PagerAdapter(fetchDummyData());
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }

    /**
     *
     * @return this method will return dummy data in form of list
     */
    private List<PagerM> fetchDummyData() {
        List<PagerM> pagerMList = new ArrayList<>();
        String[] dummyArr = getResources().getStringArray(R.array.array_str_values);
        for (String str : dummyArr) {
            PagerM pagerM = new PagerM();
            pagerM.setPagerDescription(str);
            pagerMList.add(pagerM);
        }
        return pagerMList;
    }
}




