package com.example.android.graphapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.adapter.ScenarioSectionAdapter;
import com.example.android.graphapplication.constants.KeyConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.model.CommonModel;
import com.example.android.graphapplication.model.PlanModel;
import com.example.android.graphapplication.model.ScenarioModel;
import com.example.android.graphapplication.model.ScenarioSectionModel;

import java.util.ArrayList;
import java.util.List;

public class ScenarioActivity extends AppCompatActivity {

    private static final String TAG = "ScenarioActivity";
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private TextView mToolbarTitle;
    private ConstraintLayout mLayout;

    private ScenarioSectionAdapter scenarioSectionAdapter;
    private List<CommonModel> eventsList;
    private List<CommonModel> milestonesList;
    private List<PlanModel> plansList;

    private DBHelper mydb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);

        mToolbar = findViewById(R.id.scenario_toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayout = findViewById(R.id.layout);

        initData();
        Log.d(TAG, "onCreate: out");
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        Log.d(TAG, "initData: in");
        mydb = new DBHelper(getApplicationContext());
        List<ScenarioSectionModel> scenarioSectionModelList = new ArrayList<>();
        List<ScenarioModel> eventsModelList = new ArrayList<>();
        List<ScenarioModel> milestonesModelList = new ArrayList<>();
        List<ScenarioModel> plansModelList = new ArrayList<>();

        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_SCENARIO);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        eventsList = mydb.getAllEvent();
        for (CommonModel event : eventsList) {
            eventsModelList.add(new ScenarioModel(event.getName(), event.getIsSelected() == 1));
        }

        milestonesList = mydb.getAllMilestone();
        for (CommonModel milestone : milestonesList) {
            milestonesModelList.add(new ScenarioModel(milestone.getName(), milestone.getIsSelected() == 1));
        }

        plansList = mydb.getAllPlan();
        for (PlanModel plan : plansList) {
            plansModelList.add(new ScenarioModel(plan.getPlanName(), plan.getIsSelected() == 1));
        }

        if (eventsModelList.size() > 0) {
            scenarioSectionModelList.add(new ScenarioSectionModel(
                    ScreenConstants.TOOLBAR_TITLE_EVENTS, eventsModelList));
        }

        if (milestonesModelList.size() > 0) {
            scenarioSectionModelList.add(new ScenarioSectionModel(
                    ScreenConstants.TOOLBAR_TITLE_MILESTONES, milestonesModelList));
        }

        if (plansModelList.size() > 0) {
            scenarioSectionModelList.add(new ScenarioSectionModel(
                    ScreenConstants.TOOLBAR_TITLE_PLANS, plansModelList));
        }

        if (scenarioSectionModelList.size() > 0) {
            //Setup Recycler View
            scenarioSectionAdapter = new ScenarioSectionAdapter(
                    getApplicationContext(), scenarioSectionModelList);
            RecyclerView.LayoutManager mSummaryLayoutManager = new LinearLayoutManager(
                    getApplicationContext());
            mRecyclerView.setLayoutManager(mSummaryLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(scenarioSectionAdapter);
        }

        Log.d(TAG, "initData: out");
    }

    /**
     * This method will create the more option in the action bar
     *
     * @param menu store all the menu items
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method will tap on Option Item
     *
     * @param menuItem store all the menu items
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.save:
                Log.d(TAG, "onOptionsItemSelected: save");
                if (scenarioSectionAdapter != null) {
                    for (ScenarioSectionModel scenarioSectionModel : scenarioSectionAdapter.getAllScenario()) {
                        Log.d(TAG, "onOptionsItemSelected: " + scenarioSectionModel.getTitle());

                        if (ScreenConstants.TOOLBAR_TITLE_EVENTS.equals(scenarioSectionModel.getTitle())) {
                            for (int i = 0; i < scenarioSectionModel.getScenarioModelList().size(); i++) {
                                //Commented code is to print the value of the adapter. used for debugging
//                            Log.d(TAG, "onOptionsItemSelected: title: " + scenarioSectionModel.getScenarioModelList().get(i).getTitle() +
//                            ", is selected: " + scenarioSectionModel.getScenarioModelList().get(i).isSelected() +
//                            ", event id: " + eventsList.get(i).get(SQLConstants.TABLE_ID));

                                mydb.updateEventIsSelectedStatus(eventsList.get(i).getId(),
                                        scenarioSectionModel.getScenarioModelList().get(i).isSelected()
                                                ? 1 : 0);
                            }
                        }

                        if (ScreenConstants.TOOLBAR_TITLE_MILESTONES.equals(scenarioSectionModel.getTitle())) {
                            for (int i = 0; i < scenarioSectionModel.getScenarioModelList().size(); i++) {

                                mydb.updateMilestoneIsSelectedStatus(milestonesList.get(i).getId(),
                                        scenarioSectionModel.getScenarioModelList().get(i).isSelected()
                                                ? 1 : 0);
                            }
                        }

                        if (ScreenConstants.TOOLBAR_TITLE_PLANS.equals(scenarioSectionModel.getTitle())) {
                            for (int i = 0; i < scenarioSectionModel.getScenarioModelList().size(); i++) {

                                mydb.updatePlanIsSelectedStatus(plansList.get(i).getId(),
                                        scenarioSectionModel.getScenarioModelList().get(i).isSelected()
                                                ? 1 : 0);
                            }
                        }
                    }

                    startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra(
                            KeyConstants.INTENT_KEY_FRAGMENT_POSITION, 0).putExtra(
                            KeyConstants.INTENT_KEY_ACTION,
                            KeyConstants.INTENT_KEY_VALUE_APPLIED_SCENARIO));
                } else {
                    Snackbar.make(mLayout, "There is no scenario to be applied", Snackbar.LENGTH_LONG).setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Do nothing
                        }
                    }).show();
                    break;
                }
                break;
            default:
                Log.i(TAG, "onOptionsItemSelected: In default");
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
