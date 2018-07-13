package com.example.android.graphapplication.fragment;

import android.content.Context;
import android.os.Bundle;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        Log.i(TAG, "onCreate: " + fileContent);
        Log.i(TAG, "onCreate: " + content);

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
