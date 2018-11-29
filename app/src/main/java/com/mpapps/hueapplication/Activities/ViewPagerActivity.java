package com.mpapps.hueapplication.Activities;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.android.volley.Request;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.HueProtocol;
import com.mpapps.hueapplication.Volley.VolleyListener;
import com.mpapps.hueapplication.Volley.VolleyService;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends FragmentActivity
{

    private static final int NUM_PAGES = 2;
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

        thisBridge = getIntent().getParcelableExtra("HUE_BRIDGE_OBJECT");

        mPager = findViewById(R.id.viewpager);
//        tabLayout = findViewById(R.id.tabLayout);
//        tabLayout.addTab(tabLayout.newTab().setText("Lights"));
//        tabLayout.addTab(tabLayout.newTab().setText("Schedules"));

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                //item.setIconTintList(getResources().getColorStateList(R.color.navigationbar_text));
                switch (item.getItemId()) {
                    case R.id.action_lights:
                        mPager.setCurrentItem(0);
                        break;
                    case R.id.action_schedules:
                        mPager.setCurrentItem(1);
                        break;
                }
                return false;
            }
        });

//        tabLayout.setupWithViewPager(mPager);
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
            fragments.add(RecyclerviewFragment.newInstance(thisBridge));
            listTitles = new ArrayList<>();
            listTitles.add("Lights");
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
}
