package com.mpapps.hueapplication.Activities;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.HueProtocol;
import com.mpapps.hueapplication.Volley.VolleyHelper;
import com.mpapps.hueapplication.Volley.VolleyListener;
import com.mpapps.hueapplication.Volley.VolleyService;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalTime;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends FragmentActivity
{

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private TabLayout tabLayout;
    private PagerAdapter mPagerAdapter;
    private Bridge thisBridge;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        JodaTimeAndroid.init(this);

        thisBridge = getIntent().getParcelableExtra("HUE_BRIDGE_OBJECT");

        mPager = findViewById(R.id.viewpager);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item ->
        {
            switch (item.getItemId()) {
                case R.id.action_lights:
                    mPager.setCurrentItem(0);
                    break;
                case R.id.action_groups:
                    mPager.setCurrentItem(1);
                    break;
                case R.id.action_schedules:
                    mPager.setCurrentItem(2);
                    break;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.action_lights);

        SharedPreferences prefs = getSharedPreferences("ViewPagerActivity", MODE_PRIVATE);
        int page = prefs.getInt("PAGENUMBER", 0);
        mPager.setCurrentItem(page);

        LightManager.getInstance().getLights().clear();
        LightManager.getInstance().getGroups().clear();
        LightManager.getInstance().getSchedules().clear();

        }


    private class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private List<Fragment> fragments;
        private List<String> listTitles;

        public ViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
            fragments = new ArrayList<>();
            fragments.add(RecyclerviewFragment.newInstance(thisBridge));
            fragments.add(GroupFragment.newInstance(thisBridge));
            fragments.add(SchedulesFragment.newInstance(thisBridge));
            listTitles = new ArrayList<>();
            listTitles.add("Lights");
            listTitles.add("Groups");
            listTitles.add("Schedules");
        }

        @Override
        public Fragment getItem(int position)
        {
            return fragments.get(position);
        }

        @Override
        public int getCount()
        {
            return NUM_PAGES;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position)
        {
            return listTitles.get(position);
        }



    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("ViewPagerActivity", MODE_PRIVATE);
        int page = prefs.getInt("PAGENUMBER", 0);
        mPager.setCurrentItem(page);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("ViewPagerActivity", MODE_PRIVATE).edit();
        editor.putInt("PAGENUMBER", mPager.getCurrentItem());
        editor.apply();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("ViewPagerActivity", MODE_PRIVATE).edit();
        editor.putInt("PAGENUMBER", mPager.getCurrentItem());
        editor.apply();
    }
}
