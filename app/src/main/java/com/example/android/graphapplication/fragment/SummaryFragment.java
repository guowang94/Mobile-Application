package com.example.android.graphapplication.fragment;

import android.content.Context;
import android.database.Cursor;
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
import com.example.android.graphapplication.adapter.SectionRecyclerAdapter;
import com.example.android.graphapplication.constants.SQLConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.model.SectionModel;
import com.example.android.graphapplication.model.SummaryModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SummaryFragment extends Fragment {

    private static final String TAG = "SummaryFragment";
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private TextView mToolbarTitle;

    private List<SectionModel> sectionModels = new ArrayList<>();
    private List<SummaryModel> financialSummary = new ArrayList<>();
    private List<SummaryModel> detailsSummary = new ArrayList<>();
    private List<SummaryModel> cpfContributions = new ArrayList<>();
    private boolean isViewShown = false;
    private boolean isViewLoaded = false;
    private boolean isDataLoaded = false;

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

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_SUMMARY);
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));

        //Get the content from internal storage file
        Context context = getActivity().getApplicationContext();

        Cursor rs = mydb.getData(SQLConstants.USER_TABLE, 1);
        rs.moveToFirst();

        String shortfall = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(rs.getFloat(rs.getColumnIndex(SQLConstants.USER_TABLE_SHORTFALL))));
        String balance = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(rs.getFloat(rs.getColumnIndex(SQLConstants.USER_TABLE_BALANCE))));
        String expensesExceededIncomeAge = String.valueOf(rs.getInt(
                rs.getColumnIndex(SQLConstants.USER_TABLE_EXPENSES_EXCEEDED_INCOME_AGE)));
        String initialAssets = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(rs.getFloat(rs.getColumnIndex(SQLConstants.USER_TABLE_INITIAL_ASSETS))));
        String name = rs.getString(rs.getColumnIndex(SQLConstants.USER_TABLE_NAME));
        String age = String.valueOf(rs.getInt(rs.getColumnIndex(SQLConstants.USER_TABLE_AGE)));
        String shortfallAge = String.valueOf(rs.getInt(rs.getColumnIndex(SQLConstants.USER_TABLE_SHORTFALL_AGE)));
        String retirementAge = String.valueOf(rs.getInt(rs.getColumnIndex(SQLConstants.USER_TABLE_EXPECTED_RETIREMENT_AGE)));
        String jobStatus = rs.getString(rs.getColumnIndex(SQLConstants.USER_TABLE_JOB_STATUS));
        String citizenship = rs.getString(rs.getColumnIndex(SQLConstants.USER_TABLE_CITIZENSHIP));
        String monthlyIncome = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(rs.getFloat(rs.getColumnIndex(SQLConstants.USER_TABLE_INCOME))));
        String specialAccount = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(rs.getFloat(rs.getColumnIndex(SQLConstants.USER_TABLE_SPECIAL_ACCOUNT))));
        String medisaveAccount = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(rs.getFloat(rs.getColumnIndex(SQLConstants.USER_TABLE_MEDISAVE_ACCOUNT))));

        if (!rs.isClosed()) {
            rs.close();
        }

        shortfallAge = shortfallAge.equals("-1") ? "N/A" : shortfallAge;

        financialSummary.add(new SummaryModel(R.mipmap.ic_summary_total_shortfall, getString(R.string.total_shortfall), shortfall));
        financialSummary.add(new SummaryModel(R.mipmap.ic_summary_balance, getString(R.string.balance_left_at_retirement), balance));
        financialSummary.add(new SummaryModel(R.mipmap.ic_summary_expenses_exceeded_income, getString(R.string.expenses_exceeded_income_age), expensesExceededIncomeAge));
        financialSummary.add(new SummaryModel(R.mipmap.ic_summary_initial_assets, getString(R.string.inital_assets), initialAssets));
        sectionModels.add(new SectionModel(getString(R.string.financial_summary), financialSummary));

        detailsSummary.add(new SummaryModel(R.mipmap.ic_summary_name, getString(R.string.name), name));
        detailsSummary.add(new SummaryModel(R.mipmap.ic_summary_age, getString(R.string.age), age));
        detailsSummary.add(new SummaryModel(R.mipmap.ic_summary_shortfallage, getString(R.string.shortfall_age), shortfallAge));
        detailsSummary.add(new SummaryModel(R.mipmap.ic_summary_retirementage, getString(R.string.retirement_age), retirementAge));
        detailsSummary.add(new SummaryModel(R.mipmap.ic_summary_jobstatus, getString(R.string.job_status), jobStatus));
        detailsSummary.add(new SummaryModel(R.mipmap.ic_summary_citizenship, getString(R.string.citizenship_status), citizenship));
        detailsSummary.add(new SummaryModel(R.mipmap.ic_summary_income, getString(R.string.gross_monthly_income), monthlyIncome));
        sectionModels.add(new SectionModel(getString(R.string.details_summary), detailsSummary));

        cpfContributions.add(new SummaryModel(R.mipmap.ic_summary_cpf, getString(R.string.cpf_special_account), specialAccount));
        cpfContributions.add(new SummaryModel(R.mipmap.ic_summary_cpf, getString(R.string.cpf_medisave_account), medisaveAccount));
        sectionModels.add(new SectionModel(getString(R.string.cpf_contribution), cpfContributions));

        //Recycler View Setup for UserInfo
        SectionRecyclerAdapter sectionRecyclerAdapter = new SectionRecyclerAdapter(context, sectionModels);
        RecyclerView.LayoutManager mSummaryLayoutManager = new LinearLayoutManager(
                getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mSummaryLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                context, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(sectionRecyclerAdapter);
        Log.d(TAG, "initData: out");
    }

    /**
     * This method will clear the current list value when Fragment is onPause()
     */
    @Override
    public void onPause() {
        super.onPause();
//        userInfoList.clear();
//        summaryBalanceList.clear();
//        cpfContributionList.clear();
        sectionModels.clear();
        financialSummary.clear();
        detailsSummary.clear();
        cpfContributions.clear();
        isViewLoaded = false;
        isDataLoaded = false;
    }
}


