package com.example.android.graphapplication.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.RecyclerViewTouchListener;
import com.example.android.graphapplication.activity.PlanActivity;
import com.example.android.graphapplication.adapter.CommonTitleAdapter;
import com.example.android.graphapplication.constants.KeyConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.model.PlanModel;
import com.example.android.graphapplication.viewHolder.CommonTitleViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PlansFragment extends Fragment {

    private static final String TAG = "PlansFragment";
    private RecyclerView mPlansRecyclerView;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private TextView mEmptyRecyclerTextView;

    private CommonTitleAdapter mPlansAdapter;
    private List<CommonTitleViewHolder> plansModelList;
    private boolean isViewShown;
    private boolean isViewLoaded;
    private boolean isDataLoaded;
    private DBHelper mydb;

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
        View view = inflater.inflate(R.layout.fragment_plans, container, false);

        mPlansRecyclerView = view.findViewById(R.id.plans_recycler_view);
        mToolbar = view.findViewById(R.id.plans_toolbar);
        mToolbarTitle = view.findViewById(R.id.toolbar_title);
        mEmptyRecyclerTextView = view.findViewById(R.id.empty_recycler_text_view);

        if (getActivity() != null) {
            mPlansRecyclerView.addItemDecoration(new DividerItemDecoration(
                    getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        }
        mPlansRecyclerView.setItemAnimator(new DefaultItemAnimator());

        isViewLoaded = true;
        mydb = new DBHelper(getActivity().getApplicationContext());

        if (isViewShown) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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
        plansModelList = new ArrayList<>();

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        }
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_PLANS);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context context = getActivity().getApplicationContext();

        final List<PlanModel> plansList = mydb.getAllPlan();
        if (plansModelList.size() == 0) {
            for (PlanModel plan : plansList) {
                this.plansModelList.add(new CommonTitleViewHolder(plan.getPlanName()));
            }
        }

        if (plansModelList.size() == 0) {
            mEmptyRecyclerTextView.setVisibility(View.VISIBLE);
        } else {
            mEmptyRecyclerTextView.setVisibility(View.INVISIBLE);
        }

        //Recycler View Setup for CommonTitleViewHolder
        mPlansAdapter = new CommonTitleAdapter(this.plansModelList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getActivity().getApplicationContext());
        mPlansRecyclerView.setLayoutManager(layoutManager);
        mPlansRecyclerView.setAdapter(mPlansAdapter);

        //Create on item touch listener
        mPlansRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(
                context, mPlansRecyclerView, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d(TAG, "onClick: position, " + position);
                Log.d(TAG, "onClick: plan TABLE_ID, " + plansList.get(position).getId());
                startActivity(new Intent(context, PlanActivity.class)
                        .putExtra(KeyConstants.INTENT_KEY_ACTION, KeyConstants.INTENT_KEY_VALUE_EDIT)
                        .putExtra(KeyConstants.INTENT_KEY_RECYCLER_VIEW_POSITION,
                                Integer.valueOf(plansList.get(position).getId())));
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
                        mydb.deletePlan(plansList.get(position).getId());
                        mPlansAdapter.removeItem(position);
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
        if (plansModelList != null)
            plansModelList.clear();
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
        inflater.inflate(R.menu.plus_menu, menu);
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
                if (getActivity() != null) {
                    startActivity(new Intent(getActivity().getApplicationContext(),
                            PlanActivity.class).putExtra(
                            KeyConstants.INTENT_KEY_ACTION, KeyConstants.INTENT_KEY_VALUE_CREATE));
                }
                break;
            default:
                Log.i(TAG, "onOptionsItemSelected: In default");
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
