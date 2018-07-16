package com.example.android.graphapplication.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.android.graphapplication.Constants;
import com.example.android.graphapplication.R;
import com.example.android.graphapplication.adapter.ViewPagerAdapter;
import com.example.android.graphapplication.fragment.EventsFragment;
import com.example.android.graphapplication.fragment.GraphFragment;
import com.example.android.graphapplication.fragment.MileStonesFragment;
import com.example.android.graphapplication.fragment.PlansFragment;
import com.example.android.graphapplication.fragment.SummaryFragment;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        try {
            if (getIntent() != null) {
                mViewPager.setCurrentItem(getIntent()
                        .getIntExtra(Constants.INTENT_KEY_FRAGMENT_POSITION, 0));
            }
        } catch (Exception e) {
            Log.d(TAG, "onCreate: getIntent() is null");
        }

        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        Log.d(TAG, "setupTabIcons: width, " + width);

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, mTabLayout, false);
        tabOne.setText(Constants.NAV_GRAPH);
        tabOne.setWidth(width / 5);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_nav_bar_chart, 0, 0);
        tabOne.setCompoundDrawablePadding(8);
        mTabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(Constants.NAV_EVENTS);
        tabTwo.setWidth(width / 5);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_nav_event, 0, 0);
        tabTwo.setCompoundDrawablePadding(8);
        mTabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText(Constants.NAV_MILESTONES);
        tabThree.setWidth(width / 5);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_nav_milestone, 0, 0);
        tabThree.setCompoundDrawablePadding(8);
        mTabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText(Constants.NAV_PLANS);
        tabFour.setWidth(width / 5);
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_nav_plans, 0, 0);
        tabFour.setCompoundDrawablePadding(8);
        mTabLayout.getTabAt(3).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFive.setText(Constants.NAV_SUMMARY);
        tabFive.setWidth(width / 5);
        tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_nav_summary, 0, 0);
        tabFive.setCompoundDrawablePadding(8);
        mTabLayout.getTabAt(4).setCustomView(tabFive);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GraphFragment(), Constants.TOOLBAR_TITLE_GRAPH);
        adapter.addFragment(new EventsFragment(), Constants.TOOLBAR_TITLE_EVENTS);
        adapter.addFragment(new MileStonesFragment(), Constants.TOOLBAR_TITLE_MILESTONES);
        adapter.addFragment(new PlansFragment(), Constants.TOOLBAR_TITLE_PLANS);
        adapter.addFragment(new SummaryFragment(), Constants.TOOLBAR_TITLE_SUMMARY);
        viewPager.setAdapter(adapter);
    }
}