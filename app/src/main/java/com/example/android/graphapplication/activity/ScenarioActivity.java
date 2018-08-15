package com.example.android.graphapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.adapter.ScenarioSectionAdapter;
import com.example.android.graphapplication.constants.SQLConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.model.ScenarioModel;
import com.example.android.graphapplication.model.ScenarioSectionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScenarioActivity extends AppCompatActivity {

    private static final String TAG = "ScenarioActivity";
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private TextView mToolbarTitle;

    private List<ScenarioSectionModel> scenarioSectionModelList = new ArrayList<>();
    private List<ScenarioModel> eventsModelList = new ArrayList<>();

    private DBHelper mydb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);

        mToolbar = findViewById(R.id.scenario_toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mRecyclerView = findViewById(R.id.recycler_view);

        mydb = new DBHelper(getApplicationContext());

        initData();
        Log.d(TAG, "onCreate: out");
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        Log.d(TAG, "initData: in");

        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_SCENARIO);
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final List<HashMap<String, String>> eventsList = mydb.getAllEvent();
        for (HashMap<String, String> event : eventsList) {
            eventsModelList.add(new ScenarioModel(event.get(SQLConstants.EVENT_TABLE_EVENT_NAME)));
        }

        if (eventsModelList.size() > 0) {
            scenarioSectionModelList.add(new ScenarioSectionModel(ScreenConstants.TOOLBAR_TITLE_EVENTS, eventsModelList));
        }

        if (scenarioSectionModelList.size() > 0) {
            //Setup Recycler View
            ScenarioSectionAdapter scenarioSectionAdapter = new ScenarioSectionAdapter(
                    getApplicationContext(), scenarioSectionModelList);
            RecyclerView.LayoutManager mSummaryLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mSummaryLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new DividerItemDecoration(
                    getApplicationContext(), LinearLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(scenarioSectionAdapter);
        }
        Log.d(TAG, "initData: out");
    }
}
