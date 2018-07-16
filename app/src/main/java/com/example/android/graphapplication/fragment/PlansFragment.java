package com.example.android.graphapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.graphapplication.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class PlansFragment extends Fragment {

    private static final String TAG = "PlansFragment";

    private String fileContent;
    private HashMap<String, String> content;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreate: out");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: in");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        /*initData();*/

        Log.d(TAG, "onCreateView: out");
        return view;
    }

    /**
     * This method will initialise the data for the activity
     */
    /*private void initData() {
        //Get the content from internal storage file
        Context context = getActivity().getApplicationContext();
        fileContent = readFile(context, FILE_USER_INFO);
        content = splitFileContent(fileContent);
        Log.i(TAG, "initData: " + content);

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
    }*/
    @Override
    public void onPause() {
        super.onPause();
    }
}
