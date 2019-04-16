package com.example.android.graphapplication.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.adapter.ViewPagerAdapter;
import com.example.android.graphapplication.constants.KeyConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.fragment.EventsFragment;
import com.example.android.graphapplication.fragment.GraphFragment;
import com.example.android.graphapplication.fragment.MilestoneFragment;
import com.example.android.graphapplication.fragment.PlansFragment;
import com.example.android.graphapplication.fragment.SummaryFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager mViewPager = findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        try {
            if (getIntent() != null) {
                mViewPager.setCurrentItem(getIntent()
                        .getIntExtra(KeyConstants.INTENT_KEY_FRAGMENT_POSITION, 0));
            }
        } catch (Exception e) {
            Log.d(TAG, "onCreate: getIntent() is null");
        }

        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
//                if (tab.getIcon() != null) {
//                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.MULTIPLY);
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
//                if (tab.getIcon() != null) {
//                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
//                }
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
//                if (tab.getIcon() != null) {
//                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.MULTIPLY);
//                }
//            }
//        });
        setupTabIcons();

        Log.d(TAG, "onCreate: out");
    }

    /**
     * This method will setup the TabLayout
     */
    private void setupTabIcons() {
        Objects.requireNonNull(mTabLayout.getTabAt(0)).setCustomView(getCustomTab(0));
        Objects.requireNonNull(mTabLayout.getTabAt(1)).setCustomView(getCustomTab(1));
        Objects.requireNonNull(mTabLayout.getTabAt(2)).setCustomView(getCustomTab(2));
        Objects.requireNonNull(mTabLayout.getTabAt(3)).setCustomView(getCustomTab(3));
        Objects.requireNonNull(mTabLayout.getTabAt(4)).setCustomView(getCustomTab(4));
    }

    /**
     * This method will return custom tab
     *
     * @param position of tab
     * @return Custom Tab
     */
    private TextView getCustomTab(int position) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        TextView tab;

        switch (position) {
            case 0:
                tab = (TextView) LayoutInflater.from(this).inflate(
                        R.layout.custom_tab, mTabLayout, false);
                tab.setText(ScreenConstants.NAV_GRAPH);
                tab.setWidth(width / 5);
                tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_graph_selector, 0, 0);
                tab.setCompoundDrawablePadding(8);
                return tab;
            case 1:
                tab = (TextView) LayoutInflater.from(this).inflate(
                        R.layout.custom_tab, mTabLayout, false);
                tab.setText(ScreenConstants.NAV_EVENTS);
                tab.setWidth(width / 5);
                tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_event_selector, 0, 0);
                tab.setCompoundDrawablePadding(8);
                return tab;
            case 2:
                tab = (TextView) LayoutInflater.from(this).inflate(
                        R.layout.custom_tab, mTabLayout, false);
                tab.setText(ScreenConstants.NAV_MILESTONES);
                tab.setWidth(width / 5);
                tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_milestone_selector, 0, 0);
                tab.setCompoundDrawablePadding(8);
                return tab;
            case 3:
                tab = (TextView) LayoutInflater.from(this).inflate(
                        R.layout.custom_tab, mTabLayout, false);
                tab.setText(ScreenConstants.NAV_PLANS);
                tab.setWidth(width / 5);
                tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_plans_selector, 0, 0);
                tab.setCompoundDrawablePadding(8);
                return tab;
            case 4:
                tab = (TextView) LayoutInflater.from(this).inflate(
                        R.layout.custom_tab, mTabLayout, false);
                tab.setText(ScreenConstants.NAV_SUMMARY);
                tab.setWidth(width / 5);
                tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_summary_selector, 0, 0);
                tab.setCompoundDrawablePadding(8);
                return tab;
            default:
                tab = (TextView) LayoutInflater.from(this).inflate(
                        R.layout.custom_tab, mTabLayout, false);
                tab.setText(ScreenConstants.NAV_GRAPH);
                tab.setWidth(width / 5);
                tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_graph_selector, 0, 0);
                tab.setCompoundDrawablePadding(8);
                return tab;
        }
    }

    /**
     * This method wil setup ViewPager
     *
     * @param mViewPager is the parent view for the fragment
     */
    private void setupViewPager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GraphFragment(), ScreenConstants.TOOLBAR_TITLE_GRAPH);
        adapter.addFragment(new EventsFragment(), ScreenConstants.TOOLBAR_TITLE_EVENTS);
        adapter.addFragment(new MilestoneFragment(), ScreenConstants.TOOLBAR_TITLE_MILESTONES);
        adapter.addFragment(new PlansFragment(), ScreenConstants.TOOLBAR_TITLE_PLANS);
        adapter.addFragment(new SummaryFragment(), ScreenConstants.TOOLBAR_TITLE_SUMMARY);
        mViewPager.setAdapter(adapter);
    }
}