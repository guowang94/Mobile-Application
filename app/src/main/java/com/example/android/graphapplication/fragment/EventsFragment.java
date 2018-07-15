package com.example.android.graphapplication.fragment;

import android.content.Context;
import android.mtp.MtpEvent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.graphapplication.Constants;
import com.example.android.graphapplication.R;
import com.example.android.graphapplication.adapter.EventsAdapter;
import com.example.android.graphapplication.model.Events;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventsFragment extends Fragment implements Constants {

    private static final String TAG = "EventsFragment";
    private RecyclerView mEventsRecyclerView;
    private Toolbar mToolBar;
    private ConstraintLayout mLayout;
    private TextView mToolbarTitle;

    private String fileContent;
    private HashMap<String, String> content;
    private EventsAdapter mEventsAdapter;
    private List<Events> eventsList = new ArrayList<>();
    private boolean isViewShown = false;
    private boolean isViewLoaded = false;
    private boolean isDataLoaded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreate: out");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: in");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        mEventsRecyclerView = view.findViewById(R.id.event_recycler_view);
        mToolBar = view.findViewById(R.id.event_toolbar);
        mLayout = view.findViewById(R.id.layout);
        mToolbarTitle = view.findViewById(R.id.toolbar_title);

        isViewLoaded = true;

        if (isViewShown) {
            initData();
            isDataLoaded = true;
        }

        Log.d(TAG, "onCreateView: out");
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d(TAG, "setUserVisibleHint: in, isVisibleToUser: " + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isViewShown = true;
            if (isViewLoaded) {
                if (!isDataLoaded) {
                    initData();
                    isDataLoaded = true;
                }
            }
        } else {
            isViewShown = false;
        }
        Log.d(TAG, "setUserVisibleHint: out");
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        Log.d(TAG, "initData: in");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        // Get a support ActionBar corresponding to this mToolBar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mToolbarTitle.setText(Constants.TOOLBAR_TITLE_EVENTS);
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));

        // Enable the top left button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeActionContentDescription("Edit");

        //Get the content from internal storage file
        Context context = getActivity().getApplicationContext();
        fileContent = readFile(context, FILE_USER_INFO);
        content = splitFileContent(fileContent);
        Log.i(TAG, "onCreateView: " + fileContent);
        Log.i(TAG, "onCreateView: " + content);

        //Initialise Recycle view data for Events
//        eventsList.add(new Events(content.get(CONTENT_EVENT_TITLE)));
        eventsList.add(new Events("Testing"));

        //Recycler View Setup for Events
        mEventsAdapter = new EventsAdapter(eventsList);
        RecyclerView.LayoutManager mSummaryLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mEventsRecyclerView.setLayoutManager(mSummaryLayoutManager);
        mEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mEventsRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        mEventsRecyclerView.setAdapter(mEventsAdapter);
        Log.d(TAG, "initData: out");
    }

    @Override
    public void onPause() {
        super.onPause();
        eventsList.clear();
        isViewLoaded = false;
        isDataLoaded = false;
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

    /**
     * This method will create the more option in the action bar
     *
     * @param menu store all the menu items
     * @return boolean
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.events_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
            case R.id.add_event:
                Snackbar.make(mLayout, "Add Event", Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Do nothing
                    }
                }).show();
                break;

            default:
                Log.i(TAG, "onOptionsItemSelected: In default");
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
