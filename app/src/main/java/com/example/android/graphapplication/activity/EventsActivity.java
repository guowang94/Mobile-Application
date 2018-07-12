package com.example.android.graphapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.android.graphapplication.Constants;
import com.example.android.graphapplication.R;
import com.example.android.graphapplication.recyclerList.EventsAdapter;
import com.example.android.graphapplication.recyclerList.Model.CPFContribution;
import com.example.android.graphapplication.recyclerList.CPFContributionAdapter;
import com.example.android.graphapplication.recyclerList.CirclePagerIndicatorDecoration;
import com.example.android.graphapplication.recyclerList.Model.Events;
import com.example.android.graphapplication.recyclerList.Model.SummaryBalance;
import com.example.android.graphapplication.recyclerList.SummaryBalanceAdapter;
import com.example.android.graphapplication.recyclerList.Model.UserInfo;
import com.example.android.graphapplication.recyclerList.UserInfoAdapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EventsActivity extends AppCompatActivity implements Constants {

    private static final String TAG = "EventsActivity";
    private RecyclerView mEventsRecyclerView;
    private Toolbar mToolBar;

    private String fileContent;
    private HashMap<String, String> content;
    private EventsAdapter mEventsAdapter;
    private List<Events> eventsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        mEventsRecyclerView = findViewById(R.id.event_recycler_view);
        mToolBar = findViewById(R.id.toolbar);

        initData();
        Log.d(TAG, "onCreate: out");
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        String shortfallAge = null;

        setSupportActionBar(mToolBar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the top left button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(TOOLBAR_TITLE_EVENTS);

        //Get the content from internal storage file
        Context context = getApplicationContext();
        fileContent = readFile(context, FILE_USER_INFO);
        content = splitFileContent(fileContent);
        Log.i(TAG, "onCreate: " + fileContent);
        Log.i(TAG, "onCreate: " + content);

        //Initialise Recycle view data for Events
//        eventsList.add(new Events(content.get(CONTENT_EVENT_TITLE)));
        eventsList.add(new Events("Testing"));

        //Recycler View Setup for Events
        mEventsAdapter = new EventsAdapter(eventsList);
        RecyclerView.LayoutManager mSummaryLayoutManager = new LinearLayoutManager(getApplicationContext());
        mEventsRecyclerView.setLayoutManager(mSummaryLayoutManager);
        mEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mEventsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mEventsRecyclerView.setAdapter(mEventsAdapter);
    }

    /**
     * This method will return the file content
     *
     * @param context
     * @param filename
     * @return String
     */
    private String readFile(Context context, String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method will split the File Content into key and value
     *
     * @param fileContent
     * @return HashMap
     */
    private HashMap<String, String> splitFileContent(String fileContent) {
        String[] value = fileContent.split("//");
        HashMap<String, String> content = new HashMap<>();

        for (String val : value) {
            content.put(val.split(":")[0], val.split(":")[1].trim());
        }

        return content;
    }
}
