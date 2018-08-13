package com.example.android.graphapplication.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import com.example.android.graphapplication.DAOFile;
import com.example.android.graphapplication.R;
import com.example.android.graphapplication.RecyclerViewTouchListener;
import com.example.android.graphapplication.activity.EventActivity;
import com.example.android.graphapplication.adapter.EventsAdapter;
import com.example.android.graphapplication.constants.KeyConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.model.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventsFragment extends Fragment {

    private static final String TAG = "EventsFragment";
    private RecyclerView mEventsRecyclerView;
    private Toolbar mToolbar;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //FIXME need to change the way i store value. From Internal Storage to SQLite Database
        //TODO need to be able to delete event (swipe, long press or delete selected from toolbar)
        //TODO need to do validation
        Log.d(TAG, "onCreateView: in");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        mEventsRecyclerView = view.findViewById(R.id.event_recycler_view);
        mToolbar = view.findViewById(R.id.event_toolbar);
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
        if (getUserVisibleHint()) {
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
     * This method will initialise the data for the fragment
     */
    private void initData() {
        Log.d(TAG, "initData: in");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_EVENTS);
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));

        //Get the content from internal storage file
        final Context context = getActivity().getApplicationContext();
        fileContent = new DAOFile().readFile(context, KeyConstants.FILE_USER_INFO);
        content = new DAOFile().splitFileContent(fileContent);
        Log.i(TAG, "onCreateView: " + content);

        //Initialise Recycle view data for Events
//        final int eventCount = Integer.valueOf(content.get(KeyConstants.CONTENT_EVENT_COUNT));
//        for (int i = 0; i < eventCount; i++) {
//            int currentEvent = i + 1;
//            eventsList.add(new Events(content.get(KeyConstants.CONTENT_EVENT_NAME + currentEvent)));
//        }

        //Recycler View Setup for Events
        mEventsAdapter = new EventsAdapter(eventsList);
        RecyclerView.LayoutManager mSummaryLayoutManager = new LinearLayoutManager(
                getActivity().getApplicationContext());
        mEventsRecyclerView.setLayoutManager(mSummaryLayoutManager);
        mEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mEventsRecyclerView.addItemDecoration(new DividerItemDecoration(
                context, LinearLayoutManager.VERTICAL));
        mEventsRecyclerView.setAdapter(mEventsAdapter);

        //Create on item touch listener
        mEventsRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(
                context, mEventsRecyclerView, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d(TAG, "onClick: position, " + position);
                startActivity(new Intent(getActivity().getApplicationContext(), EventActivity.class)
                        .putExtra(KeyConstants.INTENT_KEY_EVENT_ACTION, "Edit")
                        .putExtra(KeyConstants.INTENT_KEY_RECYCLER_VIEW_POSITION, position));
            }

            @Override
            public void onLongClick(View view, final int position) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this?");

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke YES event
                        mEventsAdapter.removeItem(position);

//                        //todo need to remove the data from the file content
//                        String count = String.valueOf(eventCount - 1);
//                        fileContent = fileContent.replace("EventCount:" + eventCount, "EventCount:" + count)
//                                .replace("EventName" + position, "EventName-1")
//                                .replace("EventType" + position, "EventType-1")
//                                .replace("YearOccurred" + position, "YearOccurred-1")
//                                .replace("EventDescription" + position, "EventDescription-1");
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        }));

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
     * This method will create the more option in the action bar
     *
     * @param menu     store all the menu items
     * @param inflater it takes an XML file as input and builds the View objects from it
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
                startActivity(new Intent(getActivity().getApplicationContext(),
                        EventActivity.class).putExtra(
                        KeyConstants.INTENT_KEY_EVENT_ACTION, "Create"));
                break;
            case R.id.home:
                //todo this should be edit/delete action

                break;
            default:
                Log.i(TAG, "onOptionsItemSelected: In default");
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
