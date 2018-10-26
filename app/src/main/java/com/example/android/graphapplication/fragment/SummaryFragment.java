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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.adapter.SummaryAdapter;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.model.SummaryModel;
import com.example.android.graphapplication.model.UserModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SummaryFragment extends Fragment {

    private static final String TAG = "SummaryFragment";
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private TextView mToolbarTitle;

    private List<SummaryModel> summaryModelList;
    private boolean isViewShown;
    private boolean isViewLoaded;
    private boolean isDataLoaded;

    private DBHelper mydb;

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
        mRecyclerView = view.findViewById(R.id.recycerView);
        mToolbarTitle = view.findViewById(R.id.toolbar_title);

        if (getActivity() != null) {
            mRecyclerView.addItemDecoration(new DividerItemDecoration(
                    getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        summaryModelList = new ArrayList<>();

        isViewLoaded = true;
        mydb = new DBHelper(getActivity().getApplicationContext());

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
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        }
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_SUMMARY);

        Context context = getActivity().getApplicationContext();

        UserModel userModel = mydb.getAllUser().get(0);

        String shortfall = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(userModel.getShortfall()));
        String balance = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(userModel.getBalance()));
        String expensesExceededIncomeAge = String.valueOf(userModel.getExpensesExceededIncomeAge());
        String initialAssets = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(userModel.getInitialAssets()));
        String name = userModel.getName();
        String age = String.valueOf(userModel.getAge());
        String shortfallAge = String.valueOf(userModel.getShortfallAge());
        String retirementAge = String.valueOf(userModel.getExpectedRetirementAge());
        String jobStatus = userModel.getJobStatus();
        String citizenship = userModel.getCitizenship();
        String monthlyIncome = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(userModel.getMonthlyIncome()));
        String specialAccount = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(userModel.getSpecialAccount()));
        String medisaveAccount = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(userModel.getMedisaveAccount()));


        shortfallAge = shortfallAge.equals("-1") ? "N/A" : shortfallAge;
        expensesExceededIncomeAge = expensesExceededIncomeAge.equals("-1") ? "N/A" : expensesExceededIncomeAge;

        summaryModelList.add(new SummaryModel(getString(R.string.financial_summary), SummaryModel.SECTION_HEADER));
        summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_total_shortfall, getString(R.string.total_shortfall), shortfall, SummaryModel.CONTENT));
        summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_balance, getString(R.string.balance_left_at_retirement), balance, SummaryModel.CONTENT));
        summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_expenses_exceeded_income, getString(R.string.expenses_exceeded_income_age), expensesExceededIncomeAge, SummaryModel.CONTENT));
        summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_initial_assets, getString(R.string.inital_assets), initialAssets, SummaryModel.CONTENT));

        summaryModelList.add(new SummaryModel(getString(R.string.details_summary), SummaryModel.SECTION_HEADER));
        summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_name, getString(R.string.name), name, SummaryModel.CONTENT));
        summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_age, getString(R.string.age), age, SummaryModel.CONTENT));
        summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_shortfallage, getString(R.string.shortfall_age), shortfallAge, SummaryModel.CONTENT));
        summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_retirementage, getString(R.string.retirement_age), retirementAge, SummaryModel.CONTENT));
        summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_jobstatus, getString(R.string.job_status), jobStatus, SummaryModel.CONTENT));
        summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_citizenship, getString(R.string.citizenship_status), citizenship, SummaryModel.CONTENT));
        summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_income, getString(R.string.gross_monthly_income), monthlyIncome, SummaryModel.CONTENT));

        if (!ScreenConstants.SEGMENTED_BUTTON_VALUE_SELF_EMPLOYED.equals(userModel.getJobStatus()) &&
                !ScreenConstants.SEGMENTED_BUTTON_VALUE_FOREIGNER_OR_PR.equals(userModel.getCitizenship())) {
            summaryModelList.add(new SummaryModel(getString(R.string.cpf_contribution), SummaryModel.SECTION_HEADER));
            summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_cpf, getString(R.string.cpf_special_account), specialAccount, SummaryModel.CONTENT));
            summaryModelList.add(new SummaryModel(R.mipmap.ic_summary_cpf, getString(R.string.cpf_medisave_account), medisaveAccount, SummaryModel.CONTENT));
        }

        SummaryAdapter summaryAdapter = new SummaryAdapter(summaryModelList);
        RecyclerView.LayoutManager mSummaryLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mSummaryLayoutManager);
        mRecyclerView.setAdapter(summaryAdapter);

        Log.d(TAG, "initData: out");
    }

    /**
     * This method will clear the current list value when Fragment is onPause()
     */
    @Override
    public void onPause() {
        super.onPause();
        summaryModelList.clear();
        isViewLoaded = false;
        isDataLoaded = false;
    }
}


