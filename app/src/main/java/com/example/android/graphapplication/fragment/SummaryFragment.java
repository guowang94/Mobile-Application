package com.example.android.graphapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.graphapplication.CirclePagerIndicatorDecoration;
import com.example.android.graphapplication.Constants;
import com.example.android.graphapplication.R;
import com.example.android.graphapplication.ReadFileData;
import com.example.android.graphapplication.adapter.CPFContributionAdapter;
import com.example.android.graphapplication.adapter.SummaryBalanceAdapter;
import com.example.android.graphapplication.adapter.UserInfoAdapter;
import com.example.android.graphapplication.model.CPFContribution;
import com.example.android.graphapplication.model.SummaryBalance;
import com.example.android.graphapplication.model.UserInfo;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SummaryFragment extends Fragment implements Constants {

    private static final String TAG = "SummaryFragment";
    private Toolbar mToolbar;
    private RecyclerView mSummaryRecyclerView;
    private RecyclerView mBalanceRecyclerView;
    private RecyclerView mCPFContributionRecyclerView;
    private LinearLayout mLinearLayout;
    private TextView mToolbarTitle;

    private UserInfoAdapter mUserInfoAdapter;
    private List<UserInfo> userInfoList = new ArrayList<>();
    private SummaryBalanceAdapter mSummaryBalanceAdapter;
    private List<SummaryBalance> summaryBalanceList = new ArrayList<>();
    private CPFContributionAdapter mCPFContributionAdapter;
    private List<CPFContribution> cpfContributionList = new ArrayList<>();
    private boolean isViewShown = false;
    private boolean isViewLoaded = false;
    private boolean isDataLoaded = false;

    private String fileContent;
    private HashMap<String, String> content;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: out");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: in");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        mToolbar = view.findViewById(R.id.summary_toolbar);
        mSummaryRecyclerView = view.findViewById(R.id.summary_recycler_view);
        mBalanceRecyclerView = view.findViewById(R.id.horizontal_recycler_view);
        mCPFContributionRecyclerView = view.findViewById(R.id.cpf_contribution_recycler_view);
        mLinearLayout = view.findViewById(R.id.layout);
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
     * This method will initialise the data for the activity
     */
    private void initData() {
        Log.d(TAG, "initData: in");
        String shortfallAge;

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this mToolbar
//        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mToolbarTitle.setText(Constants.TOOLBAR_TITLE_SUMMARY);
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));

        //Get the content from internal storage file
        Context context = getActivity().getApplicationContext();
        fileContent = new ReadFileData().readFile(context, FILE_USER_INFO);
        content = new ReadFileData().splitFileContent(fileContent);
        Log.i(TAG, "initData: " + content);

        if (content.get(CONTENT_SHORTFALL_AGE) == null) {
            shortfallAge = "N/A";
        } else {
            shortfallAge = content.get(CONTENT_SHORTFALL_AGE);
        }

        //Initialise Recycle view data for UserInfo
        userInfoList.add(new UserInfo(CONTENT_NAME, content.get(CONTENT_NAME), R.mipmap.ic_summary_screen_name));
        userInfoList.add(new UserInfo(CONTENT_AGE, content.get(CONTENT_AGE), R.mipmap.ic_summary_screen_age));
        userInfoList.add(new UserInfo(CONTENT_SHORTFALL_AGE, shortfallAge, R.mipmap.ic_summary_screen_age_shortfall));
        userInfoList.add(new UserInfo(CONTENT_RETIREMENT_AGE, content.get(CONTENT_RETIREMENT_AGE), R.mipmap.ic_summary_screen_retirement));
        userInfoList.add(new UserInfo(CONTENT_JOB_STATUS, content.get(CONTENT_JOB_STATUS), R.mipmap.ic_summary_screen_job_status));
        userInfoList.add(new UserInfo(CONTENT_CITIZENSHIP_STATUS, content.get(CONTENT_CITIZENSHIP_STATUS), R.mipmap.ic_summary_screen_citizen));
        userInfoList.add(new UserInfo(CONTENT_GROSS_MONTHLY_INCOME, String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(Float.valueOf(content.get(CONTENT_GROSS_MONTHLY_INCOME)))), R.mipmap.ic_summary_screen_income));

        //Recycler View Setup for UserInfo
        mUserInfoAdapter = new UserInfoAdapter(userInfoList);
        RecyclerView.LayoutManager mSummaryLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mSummaryRecyclerView.setLayoutManager(mSummaryLayoutManager);
        mSummaryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSummaryRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        mSummaryRecyclerView.setAdapter(mUserInfoAdapter);

        //Initialise Recycle view data for Summary Balance
        summaryBalanceList.add(new SummaryBalance(SUMMARY_BALANCE, String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(Float.valueOf(content.get(CONTENT_BALANCE)))), R.mipmap.ic_summary_screen_balance));
        summaryBalanceList.add(new SummaryBalance(SUMMARY_SHORTFALL, String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(Float.valueOf(content.get(CONTENT_SHORTFALL)))), R.mipmap.ic_summary_screen_shortfall));

        //Recycler View Setup for Summary Balance
        mSummaryBalanceAdapter = new SummaryBalanceAdapter(summaryBalanceList);
        RecyclerView.LayoutManager mBalanceLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mBalanceRecyclerView.setLayoutManager(mBalanceLayoutManager);
        mBalanceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBalanceRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL));
        mBalanceRecyclerView.setAdapter(mSummaryBalanceAdapter);
        mBalanceRecyclerView.addItemDecoration(new CirclePagerIndicatorDecoration());
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mBalanceRecyclerView);

        //Initialise Recycle view data
        cpfContributionList.add(new CPFContribution(CONTENT_ORDINARY_ACCOUNT,
                NumberFormat.getCurrencyInstance(Locale.US)
                        .format(Float.valueOf(content.get(CONTENT_ORDINARY_ACCOUNT)))));
        cpfContributionList.add(new CPFContribution(CONTENT_SPECIAL_ACCOUNT,
                NumberFormat.getCurrencyInstance(Locale.US)
                        .format(Float.valueOf(content.get(CONTENT_SPECIAL_ACCOUNT)))));
        cpfContributionList.add(new CPFContribution(CONTENT_MEDISAVE_ACCOUNT,
                NumberFormat.getCurrencyInstance(Locale.US)
                        .format(Float.valueOf(content.get(CONTENT_MEDISAVE_ACCOUNT)))));

        //Recycler View Setup for CPF Contribution
        mCPFContributionAdapter = new CPFContributionAdapter(cpfContributionList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mCPFContributionRecyclerView.setLayoutManager(mLayoutManager);
        mCPFContributionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCPFContributionRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        mCPFContributionRecyclerView.setAdapter(mCPFContributionAdapter);
        Log.d(TAG, "initData: out");
    }

    /**
     * This method will clear the current list value when Fragment is onPause()
     */
    @Override
    public void onPause() {
        super.onPause();
        userInfoList.clear();
        summaryBalanceList.clear();
        cpfContributionList.clear();
        isViewLoaded = false;
        isDataLoaded = false;
    }
}


