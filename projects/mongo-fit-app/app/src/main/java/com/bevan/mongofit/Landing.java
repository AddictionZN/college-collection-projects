package com.bevan.mongofit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bevan.mongofit.MenuActivities.Camera;
import com.bevan.mongofit.MenuActivities.Help;
import com.bevan.mongofit.MenuActivities.Profile;
import com.bevan.mongofit.MenuActivities.Quit;
import com.bevan.mongofit.MenuActivities.Settings;
import com.bevan.mongofit.MenuActivities.historyWeight;
import com.bevan.mongofit.Tabs.tabCalories;
import com.bevan.mongofit.Tabs.tabHome;
import com.bevan.mongofit.Tabs.tabStepCounter;

public class Landing extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent i = getIntent();
        username = i.getStringExtra("username");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        int limit = (mSectionsPagerAdapter.getCount() > 1 ? mSectionsPagerAdapter.getCount() - 1 : 1);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOffscreenPageLimit(limit);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.history:
                Intent hist = new Intent(Landing.this, historyWeight.class);
                hist.putExtra("username", username.trim());
                startActivity(hist);
                Landing.this.finish();
                return true;
            case R.id.profile:
                Intent prof = new Intent(Landing.this, Profile.class);
                prof.putExtra("username", username.trim());
                startActivity(prof);
                Landing.this.finish();
                return true;
            case R.id.setting:
                Intent set = new Intent(Landing.this, Settings.class);
                set.putExtra("username", username.trim());
                startActivity(set);
                Landing.this.finish();
                return true;
            //case R.id.help:
            //    Intent help = new Intent(Landing.this, Help.class);
            //    help.putExtra("username", username.trim());
            //    startActivity(help);
            //    Landing.this.finish();
            //    return true;
            case R.id.camera:
                Intent cam = new Intent(Landing.this, Camera.class);
                cam.putExtra("username", username.trim());
                startActivity(cam);
                Landing.this.finish();
                return true;
            case R.id.quit:
                Intent exit = new Intent(Landing.this, Quit.class);
                startActivity(exit);
                Landing.this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // Returning the current tabs
            switch (position) {
                case 0:
                    tabHome tab1 = new tabHome();
                    return tab1;
                case 1:
                    tabCalories tab2 = new tabCalories();
                    return tab2;
                case 2:
                    tabStepCounter tab3 = new tabStepCounter();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Calories";
                case 2:
                    return "Step Counter";
            }
            return null;
        }
    }
}
