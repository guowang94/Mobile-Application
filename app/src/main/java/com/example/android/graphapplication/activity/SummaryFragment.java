package com.example.android.graphapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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

import com.example.android.graphapplication.Adapter.CirclePagerIndicatorDecoration;
import com.example.android.graphapplication.Constants;
import com.example.android.graphapplication.R;
import com.example.android.graphapplication.Model.CPFContribution;
import com.example.android.graphapplication.Adapter.CPFContributionAdapter;
import com.example.android.graphapplication.Model.SummaryBalance;
import com.example.android.graphapplication.Adapter.SummaryBalanceAdapter;
import com.example.android.graphapplication.Model.UserInfo;
import com.example.android.graphapplication.Adapter.UserInfoAdapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SummaryFragment extends Fragment implements Constants {

    private static final String TAG = "SummaryFragment";
    private Toolbar mToolBar;
    private RecyclerView mSummaryRecyclerView;
    private RecyclerView mBalanceRecyclerView;
    private RecyclerView mCPFContributionRecyclerView;
    private LinearLayout mLinearLayout;

    private UserInfoAdapter mUserInfoAdapter;
    private List<UserInfo> userInfoList = new ArrayList<>();
    private SummaryBalanceAdapter mSummaryBalanceAdapter;
    private List<SummaryBalance> summaryBalanceList = new ArrayList<>();
    private CPFContributionAdapter mCPFContributionAdapter;
    private List<CPFContribution> cpfContributionList = new ArrayList<>();

    private String fileContent;
    private HashMap<String, String> content;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: out");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: in");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        mToolBar = view.findViewById(R.id.toolbar);
        mSummaryRecyclerView = view.findViewById(R.id.summary_recycler_view);
        mBalanceRecyclerView = view.findViewById(R.id.horizontal_recycler_view);
        mCPFContributionRecyclerView = view.findViewById(R.id.cpf_contribution_recycler_view);
        mLinearLayout = view.findViewById(R.id.layout);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolBar);
        // Get a support ActionBar corresponding to this mToolBar
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(Constants.TOOLBAR_TITLE_GRAPH);

        // Enable the top left button
        actionBar.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        initData();

        Log.d(TAG, "onCreateView: out");
        return view;
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        String shortfallAge = null;

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolBar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        // Enable the top left button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(TOOLBAR_TITLE_SUMMARY);

        //Get the content from internal storage file
        Context context = getActivity().getApplicationContext();
        fileContent = readFile(context, FILE_USER_INFO);
        content = splitFileContent(fileContent);
        Log.i(TAG, "onCreate: " + fileContent);
        Log.i(TAG, "onCreate: " + content);

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


