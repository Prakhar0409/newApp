package com.prakhar_squared_mayank.grs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class ComplaintDetailActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    JSONArray timelineData, commentData;
    TimelineFragment timeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);

        try {
            timelineData = new JSONArray("[{\"status_id\"=\"2\",\"status_name\"=\"Received supplies\"},{\"status_id\"=\"34\",\"status_name\"=\"Work almost done\"},{\"status_id\"=\"34\",\"status_name\"=\"Work almost done2\"}]");
            commentData = new JSONArray("[{\"comment\":\"Great Job!!\",\"comment_id\":\"31\",\"posted_by\":\"U29\",\"posted_on\":\"23:30 30-10-2016\"},{\"comment\":\"Appreciate it. This is a long bitchy comment. Fuck Yea. This is some m'fuckin test. And we bloody pass it. Oh yeah!!\",\"comment_id\":\"32\",\"posted_by\":\"U291\",\"posted_on\":\"23:30 01-11-2016\"}]");
        }
        catch(JSONException e) {

        }
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

//        getData();

    }

    void getData() {
        try {
            timelineData = new JSONArray("[{\"status_id\"=\"2\",\"status_name\"=\"Received supplies\"},{\"status_id\"=\"34\",\"status_name\"=\"Work almost done\"},{\"status_id\"=\"34\",\"status_name\"=\"Work almost done2\"}]");
        }
        catch(JSONException e) {

        }
        setTimeline();
    }

    void setTimeline() {
        if(timeline != null) {
            timeline.updateTimeline(timelineData);
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    timeline = TimelineFragment.newInstance(timelineData);
                    return timeline;
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return WallFragment.newInstance(commentData);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) {
                return "Timeline";
            }
            else if(position == 1) {
                return "Wall";
            }
            return "Page " + position;
        }

    }
}
