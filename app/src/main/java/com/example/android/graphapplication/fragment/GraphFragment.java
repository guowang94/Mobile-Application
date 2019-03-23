package com.example.android.graphapplication.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.activity.ExportActivity;
import com.example.android.graphapplication.activity.ScenarioActivity;
import com.example.android.graphapplication.adapter.SelectedScenarioAdapter;
import com.example.android.graphapplication.constants.KeyConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.model.CommonModel;
import com.example.android.graphapplication.model.PlanModel;
import com.example.android.graphapplication.model.UserModel;
import com.example.android.graphapplication.validations.MyAxisValueFormatter;
import com.example.android.graphapplication.validations.MyValueFormatter;
import com.example.android.graphapplication.viewHolder.SelectedScenarioViewHolder;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GraphFragment extends Fragment implements OnChartValueSelectedListener {

    private static final String TAG = "GraphFragment";
    private static final String EXPENSES = "EXPENSES";
    private static final String COVERED_EXPENSES = "COVERED_EXPENSES";
    private static final String UNCOVERED_EXPENSES = "UNCOVERED_EXPENSES";
    private static final String ASSETS = "ASSETS";

    private ConstraintLayout mLayout;
    private BarChart mChart;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private TextView mEmptyRecyclerTextView;
    private RecyclerView mRecyclerView;
    private Switch mToggleGraph;
    private TextView mAgeTextView;
    private TextView mExpensesTextView;
    private TextView mUncoveredExpensesTextView;
    private TextView mCoveredExpensesTextView;
    private TextView mAssetsTextView;

    private boolean isViewShown;
    private boolean isViewLoaded;
    private boolean isDataLoaded;

    private List<SelectedScenarioViewHolder> mSelectedScenarioViewHolderList;
    private List<CommonModel> eventsList;
    private List<CommonModel> milestonesList;
    private List<PlanModel> plansList;
    private List<CommonModel> noIncomeEventsList;

    private SparseArray<HashMap<String, Float>> graphData;

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
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        mChart = view.findViewById(R.id.stack_bar_graph);
        mLayout = view.findViewById(R.id.layout);
        mToolbar = view.findViewById(R.id.graph_toolbar);
        mToolbarTitle = view.findViewById(R.id.toolbar_title);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mEmptyRecyclerTextView = view.findViewById(R.id.empty_recycler_text_view);
        mToggleGraph = view.findViewById(R.id.graph_switch);
        mAgeTextView = view.findViewById(R.id.age_text_view);
        mExpensesTextView = view.findViewById(R.id.expenses_text_view);
        mUncoveredExpensesTextView = view.findViewById(R.id.uncovered_expenses_text_view);
        mCoveredExpensesTextView = view.findViewById(R.id.covered_expenses_text_view);
        mAssetsTextView = view.findViewById(R.id.assets_text_view);

        if (getActivity() != null) {
            mRecyclerView.addItemDecoration(new DividerItemDecoration(
                    getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        isViewLoaded = true;
        mydb = new DBHelper(getActivity().getApplicationContext());

        if (isViewShown) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
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
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    initData();
                    isDataLoaded = true;
                }
            }
        } else {
            isViewShown = false;
        }
        Log.d(TAG, "setUserVisibleHint: out, isViewShown: " + isViewShown +
                ", isViewLoaded: " + isViewLoaded + ", isDataLoaded: " + isDataLoaded);
    }

    /**
     * This method will initialise the data for the fragment
     */
    private void initData() {
        Log.d(TAG, "initData: in");
        mSelectedScenarioViewHolderList = new ArrayList<>();
        eventsList = new ArrayList<>();
        milestonesList = new ArrayList<>();
        plansList = new ArrayList<>();
        noIncomeEventsList = new ArrayList<>();

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        }
        // Get a support ActionBar corresponding to this mToolbar
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_GRAPH);

        mChart.setOnChartValueSelectedListener(this);

        mToggleGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mToggleGraph.isChecked()) {
                    mToggleGraph.setText(getResources().getString(R.string.assets_graph));
                } else {
                    mToggleGraph.setText(getResources().getString(R.string.expenses_graph));
                }
                mChart.clear();
                graphViewSetup();
            }
        });

        graphViewSetup();
        recyclerViewSetup();
        Log.d(TAG, "initData: out");
    }

    /**
     * This method will display the value of the bar that is tapped
     *
     * @param e the variable that stored all the value in bar
     * @param h get the index of the bar
     */
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        Log.d(TAG, "onValueSelected: entry/age, " + e);
        Log.d(TAG, "onValueSelected: highlight/value, " + h);

        HashMap<String, Float> currentData = graphData.get((int) e.getX());

        mAgeTextView.setText(NumberFormat.getIntegerInstance().format(e.getX()));
        mExpensesTextView.setText(DecimalFormat.getCurrencyInstance(Locale.US).format(currentData.get(EXPENSES)));
        mCoveredExpensesTextView.setText(DecimalFormat.getCurrencyInstance(Locale.US).format(currentData.get(COVERED_EXPENSES)));
        mUncoveredExpensesTextView.setText(DecimalFormat.getCurrencyInstance(Locale.US).format(currentData.get(UNCOVERED_EXPENSES)));
        mAssetsTextView.setText(DecimalFormat.getCurrencyInstance(Locale.US).format(currentData.get(ASSETS)));

//        if (h.getStackIndex() != -1) {
//            BarEntry entry = (BarEntry) e;
//            String type;
//
//            if (entry.getYVals() != null) {
//                Log.i(TAG, "y values: " + entry.getY());
//                for (int i = 0; i < entry.getYVals().length; i++) {
//                    Log.i(TAG, "y values at " + i + " index: " + entry.getYVals()[i]);
//                }
//                Log.i(TAG, "Selected stack index: " + h.getStackIndex());
//                Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
//
//
//
//                switch (h.getStackIndex()) {
//                    //Green - expenses (baseline) yellow - addiitonal red - uncovered
//                    case 0:
//                        type = ", Expenses Value: ";
//                        if (entry.getYVals()[h.getStackIndex()] != 0f) {
//                            Snackbar.make(mLayout, "Age: " + NumberFormat.getIntegerInstance()
//                                            .format(entry.getX()) + type +
//                                            DecimalFormat.getCurrencyInstance(Locale.US)
//                                                    .format(entry.getYVals()[h.getStackIndex()]),
//                                    Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    // Do nothing
//                                }
//                            }).show();
//                        }
//                        break;
//
//                    case 1:
//                        type = ", Additional Value: ";
//                        if (entry.getYVals()[h.getStackIndex()] != 0f) {
//                            Snackbar.make(mLayout, "Age: " + NumberFormat.getIntegerInstance()
//                                            .format(entry.getX()) + type +
//                                            DecimalFormat.getCurrencyInstance(Locale.US)
//                                                    .format(entry.getYVals()[h.getStackIndex()]),
//                                    Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    // Do nothing
//                                }
//                            }).show();
//                        }
//                        break;
//                    case 2:
//                        type = ", Uncovered: ";
//                        if (entry.getYVals()[h.getStackIndex()] != 0f) {
//                            Snackbar.make(mLayout, "Age: " + NumberFormat.getIntegerInstance()
//                                            .format(entry.getX()) + type +
//                                            DecimalFormat.getCurrencyInstance(Locale.US)
//                                                    .format(entry.getYVals()[h.getStackIndex()]),
//                                    Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    // Do nothing
//                                }
//                            }).show();
//                        }
//                        break;
//                    default:
//                        Log.d(TAG, "onValueSelected: None of the ids have matched. " +
//                                "Current Graph Index: " + h.getStackIndex());
//                }
//            } else {
//                Log.i("VAL SELECTED", "Value: " + entry.getY());
//            }
//        } else {
//            mAgeTextView.setText(NumberFormat.getIntegerInstance().format(e.getX()));
//            mAssetsTextView.setText(DecimalFormat.getCurrencyInstance(Locale.US).format(e.getY()));
//
//            Snackbar.make(mLayout, "Age: " + NumberFormat.getIntegerInstance().format(e.getX()) +
//                            ", Assets: " + DecimalFormat.getCurrencyInstance(Locale.US).format(e.getY()),
//                    Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Do nothing
//                }
//            }).show();
//        }
    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: in");
        super.onPause();
        isDataLoaded = false;
        isViewLoaded = false;
        if (eventsList != null)
            eventsList.clear();
        if (milestonesList != null)
            milestonesList.clear();
        if (plansList != null)
            plansList.clear();
        if (mSelectedScenarioViewHolderList != null)
            mSelectedScenarioViewHolderList.clear();
        if (noIncomeEventsList != null)
            noIncomeEventsList.clear();
        Log.d(TAG, "onPause: out");
    }

    /**
     * This method will show the graph
     */
    private void graphViewSetup() {
        Log.d(TAG, "graphViewSetup: in");

        graphData = new SparseArray<>();

        UserModel userModel = mydb.getAllUser().get(0);

        BarData data;
        eventsList = mydb.getAllSelectedEvent();
        milestonesList = mydb.getAllSelectedMilestone();
        plansList = mydb.getAllSelectedPlan();
        noIncomeEventsList = mydb.getAllSelectedAndNoIncomeEvent();

        data = getGraphData(userModel);

        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(true);

        mChart.setDrawGridBackground(false);
        //When false the value will be inside the bar graph but when set to true it will be outside of the graph
        mChart.setDrawValueAboveBar(false);
        //This method need to set as false for the onValueSelected() to work
        mChart.setHighlightFullBarEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(userModel.getAge() - 1);
        xAxis.setAxisMaximum(userModel.getExpectancy() + 1);
        xAxis.setLabelCount(8, false);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setValueFormatter(new MyAxisValueFormatter());
        yAxis.setCenterAxisLabels(true);
        yAxis.setLabelCount(7, true);
        if (!mToggleGraph.isChecked()) {
            Log.d(TAG, "graphViewSetup: Y Axis is set at 0.");
            yAxis.setAxisMinimum(0f);
        } else {
            yAxis.resetAxisMinimum();
        }
        mChart.getAxisRight().setEnabled(false);

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);
//        legend.setWordWrapEnabled(true);
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        legend.setDrawInside(false);
//        legend.setFormSize(15f);
//        legend.setFormToTextSpace(5f);
//        legend.setXEntrySpace(6f);
//        legend.setTextSize(10f);

        mChart.setData(data);
        mChart.invalidate();
        Log.d(TAG, "graphViewSetup: out");
    }

    /**
     * This method will calculate and return the data for the graph
     *
     * @param userModel Contains details of the user
     * @return BarData for the graph
     */
    private BarData getGraphData(UserModel userModel) {
        Log.d(TAG, "getGraphData: in");
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        float assets = userModel.getInitialAssets();
        float firstYearIncome = userModel.getMonthlyIncome() * (12 - (Calendar.getInstance().get(Calendar.MONTH)));
        float firstYearExpenses = userModel.getExpenses() * (12 - (Calendar.getInstance().get(Calendar.MONTH)));
        float subsequentAnnualIncome = userModel.getMonthlyIncome() * 12;
        float subsequentAnnualExpenses = userModel.getExpenses() * 12;
        float annualIncome = 0f;
        float annualExpenses = 0f;
        float annualIncomeAfterDeductCPF = 0f;
        float balance = 0f;
        float cpfOrdinaryAccount = 0f;
        float cpfSpecialAccount = 0f;
        float cpfMedisaveAccount = 0f;
        int shortfallAge = -1;
        int expensesExceededIncomeAge = -1;
        int age = userModel.getAge();
        int retirementAge = userModel.getExpectedRetirementAge();
        int expectancy = userModel.getExpectancy();
        int increment = userModel.getIncrement();
        int inflation = userModel.getInflation();
        List<Float> selectedScenarioValuesList = getCombineSelectedList(expectancy);
        List<Integer> noIncomeAgeList = getNoIncomeEventSelectedList(expectancy);

        for (int currentAge = age; currentAge <= expectancy; currentAge++) {
            if (currentAge <= retirementAge) {
                if (noIncomeAgeList.contains(currentAge)) {
                    annualIncomeAfterDeductCPF = 0f;
                } else {
                    // Income Increment Calculation
                    if (currentAge == age) {
//                    Log.i(TAG, "getGraphData: first year");
                        annualIncome = firstYearIncome;
                        annualExpenses = firstYearExpenses;
                        Log.d(TAG, "getGraphData: income: " + annualIncome + " at " + currentAge);
                        Log.d(TAG, "getGraphData: expenses: " + annualExpenses + " at " + currentAge);
                    } else if (currentAge == age + 1) {
//                    Log.i(TAG, "getGraphData: subsequent");
                        annualIncome = subsequentAnnualIncome * (100 + increment) / 100;
                        annualExpenses = subsequentAnnualExpenses * (100 + inflation) / 100;
                        Log.d(TAG, "getGraphData: income: " + annualIncome + " at " + currentAge);
                        Log.d(TAG, "getGraphData: expenses: " + annualExpenses + " at " + currentAge);
                    } else {
                        annualIncome = annualIncome * (100 + increment) / 100;
                        annualExpenses = annualExpenses * (100 + inflation) / 100;
                        Log.d(TAG, "getGraphData: income: " + annualIncome + " at " + currentAge);
                        Log.d(TAG, "getGraphData: expenses: " + annualExpenses + " at " + currentAge);
                    }

                    //CPF Calculation
                    if (ScreenConstants.SEGMENTED_BUTTON_VALUE_EMPLOYED.equals(userModel.getJobStatus()) &&
                            ScreenConstants.SEGMENTED_BUTTON_VALUE_SINGPOREAN.equals(userModel.getCitizenship())) {
                        List<Float> calculatedValues = getCPFContribution(annualIncome, currentAge);
                        List<Float> cpfDistribution = getCPFDistribution(calculatedValues.get(1), currentAge);

                        cpfOrdinaryAccount += cpfDistribution.get(0);
                        cpfSpecialAccount += cpfDistribution.get(1);
                        cpfMedisaveAccount += cpfDistribution.get(2);
                        annualIncomeAfterDeductCPF = calculatedValues.get(0);
                        Log.d(TAG, "getGraphData: income after deduct cpf: " + annualIncome + " at " + currentAge);
                    } else {
                        //Update the value of annualIncomeAfterDeductCPF as it will be used for graph calculation
                        annualIncomeAfterDeductCPF = annualIncome;
                    }
                }

                //-------------Calculation for the graph---------------

                //Green - expenses (baseline) yellow - addiitonal red - uncovered

                float editedValue = selectedScenarioValuesList.get(currentAge);

                //For graph value only
                float totalExpenses = annualExpenses + Math.abs(editedValue);
                float expensesValue = 0f;
                float coveredValue = 0f;
                float uncoveredValue = 0f;

                if (totalExpenses < annualIncomeAfterDeductCPF) {
                    expensesValue = totalExpenses;
                } else if (totalExpenses > annualIncomeAfterDeductCPF) {
                    expensesValue = annualIncomeAfterDeductCPF;

                    float exceededExpensesAmount = totalExpenses - annualIncomeAfterDeductCPF;

                    if (exceededExpensesAmount < assets) {
                        coveredValue = exceededExpensesAmount;
                    } else if (exceededExpensesAmount > assets) {
                        if (assets > 0f) {
                            coveredValue = assets;
                            uncoveredValue = exceededExpensesAmount - assets;
                        } else {
                            uncoveredValue = exceededExpensesAmount;
                        }
                    }
                }
                Log.d(TAG, "getGraphData: ============================");
                Log.d(TAG, "getGraphData: Age: " + currentAge);
                Log.d(TAG, "getGraphData: Expenses: " + expensesValue);
                Log.d(TAG, "getGraphData: Covered: " + coveredValue);
                Log.d(TAG, "getGraphData: Uncovered: " + uncoveredValue);
                Log.d(TAG, "getGraphData: ============================");

                assets += annualIncomeAfterDeductCPF - annualExpenses;
                assets += editedValue;

                if (mToggleGraph.isChecked()) {
                    barEntries.add(new BarEntry(currentAge, new float[]{assets}));
                } else {
                    barEntries.add(new BarEntry(currentAge, new float[]{expensesValue, coveredValue, uncoveredValue}));
                }

                Log.d(TAG, "getGraphData: assets: " + assets + " at " + currentAge);


                if (currentAge == retirementAge) {
                    balance = assets;
                }

                if (currentAge == age) {
                    mAgeTextView.setText(String.valueOf(age));
                    mExpensesTextView.setText(NumberFormat.getCurrencyInstance(Locale.US).format(expensesValue));
                    mCoveredExpensesTextView.setText(NumberFormat.getCurrencyInstance(Locale.US).format(coveredValue));
                    mUncoveredExpensesTextView.setText(NumberFormat.getCurrencyInstance(Locale.US).format(uncoveredValue));
                    mAssetsTextView.setText(NumberFormat.getCurrencyInstance(Locale.US).format(assets));
                }

                //Store data so that it can display the data when the graph is tapped on
                HashMap<String, Float> currentData = new HashMap<>();
                currentData.put(EXPENSES, expensesValue);
                currentData.put(COVERED_EXPENSES, coveredValue);
                currentData.put(UNCOVERED_EXPENSES, uncoveredValue);
                currentData.put(ASSETS, assets);

                graphData.put(currentAge, currentData);

            } else {
                annualExpenses = annualExpenses * (100 + inflation) / 100;
                float editedValue = selectedScenarioValuesList.get(currentAge);

                //For graph value only
                float totalExpenses = annualExpenses + Math.abs(editedValue);
                float expensesValue = 0f;
                float coveredValue = 0f;
                float uncoveredValue = 0f;

                if (totalExpenses < assets) {
                    coveredValue = totalExpenses;
                } else if (totalExpenses > assets) {
                    if (assets > 0f) {
                        coveredValue = assets;
                        uncoveredValue = totalExpenses - assets;
                    } else {
                        uncoveredValue = totalExpenses;
                    }
                }
                Log.d(TAG, "getGraphData: ============================");
                Log.d(TAG, "getGraphData: Age: " + currentAge);
                Log.d(TAG, "getGraphData: Expenses: " + expensesValue);
                Log.d(TAG, "getGraphData: Covered: " + coveredValue);
                Log.d(TAG, "getGraphData: Uncovered: " + uncoveredValue);
                Log.d(TAG, "getGraphData: ============================");

                assets -= annualExpenses;
                assets += editedValue;

                if (mToggleGraph.isChecked()) {
                    barEntries.add(new BarEntry(currentAge, new float[]{assets}));
                } else {
                    barEntries.add(new BarEntry(currentAge, new float[]{expensesValue, coveredValue, uncoveredValue}));
                }

                //Store data so that it can display the data when the graph is tapped on
                HashMap<String, Float> currentData = new HashMap<>();
                currentData.put(EXPENSES, expensesValue);
                currentData.put(COVERED_EXPENSES, coveredValue);
                currentData.put(UNCOVERED_EXPENSES, uncoveredValue);
                currentData.put(ASSETS, assets);

                graphData.put(currentAge, currentData);

                Log.d(TAG, "getGraphData: assets: " + assets + " at " + currentAge);
            }

            if (assets < 0f) {
                if (shortfallAge == -1) {
                    shortfallAge = currentAge;
                }
            } else {
                if (shortfallAge != -1) {
                    shortfallAge = -1;
                }
            }

            if (annualExpenses > annualIncomeAfterDeductCPF) {
                if (expensesExceededIncomeAge == -1) {
                    expensesExceededIncomeAge = currentAge;
                }
            }

            if (currentAge == expectancy) {
                mydb.updateUser(cpfOrdinaryAccount, cpfSpecialAccount, cpfMedisaveAccount,
                        balance, assets, shortfallAge, expensesExceededIncomeAge);
            }
        }

        //----------- Bar Graph ------------
        BarDataSet barDataSet = new BarDataSet(barEntries, null);
        if (mToggleGraph.isChecked()) {
            barDataSet.setColors(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.expensesGraph));
        } else {
            barDataSet.setColors(Color.GREEN, Color.YELLOW, Color.RED);
        }
//        barDataSet.setStackLabels(new String[]{"Negative Value", ScreenConstants.GRAPH_LEGEND_EXPENSES,
//                ScreenConstants.GRAPH_LEGEND_INCOME, "Positive Value"});

        //values will appear on the graph
        barDataSet.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);

        BarData barData = new BarData(dataSets);
        barData.setValueFormatter(new MyValueFormatter());
        barData.setValueTextColor(Color.BLACK);

        //------------- Line Graph ------------
//        LineDataSet lineDataSet = new LineDataSet(lineEntries, ScreenConstants.GRAPH_LEGEND_ASSETS);
//        lineDataSet.setColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.lineGraph));
//        lineDataSet.setLineWidth(2.5f);
//        lineDataSet.setCircleColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.lineGraph));
//        lineDataSet.setCircleRadius(1f);
//        lineDataSet.setFillColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.lineGraph));
//        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        lineDataSet.setDrawValues(false);
//
//        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
//        LineData lineData = new LineData();
//        lineData.addDataSet(lineDataSet);
//
//        CombinedData combinedData = new CombinedData();
//        combinedData.setData(barData);
//        combinedData.setData(lineData);

        Log.d(TAG, "getGraphData: out");
        return barData;
    }

    /**
     * this method will get Annual Income(after deduction of cpf) and CPF Contribution
     *
     * @param annualIncome Annual Income
     * @param currentAge   Current age
     * @return CalculatedValues
     * <ul>
     * <li>0 - Annual Income after deducted for CPF</li>
     * <li>1 - CPF Contribution</li>
     * </ul>
     */
    public List<Float> getCPFContribution(float annualIncome, int currentAge) {
        List<Float> calculatedValues = new ArrayList<>();
        float cpfContribution;
        if (currentAge <= 55) {
            //CPF Contribution from employer
            cpfContribution = annualIncome * 0.37f;
            //Annual income after deducted for CPR
            annualIncome = annualIncome * 0.8f;
            if (cpfContribution > 2200 * 12) {
                cpfContribution = 2200 * 12;
            }
        } else if (currentAge <= 60) {
            cpfContribution = annualIncome * 0.26f;
            annualIncome = annualIncome * 0.87f;
            if (cpfContribution > 1560 * 12) {
                cpfContribution = 1560 * 12;
            }
        } else if (currentAge <= 65) {
            cpfContribution = annualIncome * 0.165f;
            annualIncome = annualIncome * 0.925f;
            if (cpfContribution > 990 * 12) {
                cpfContribution = 990 * 12;
            }
        } else {
            cpfContribution = annualIncome * 0.125f;
            annualIncome = annualIncome * 0.95f;
            if (cpfContribution > 750 * 12) {
                cpfContribution = 750 * 12;
            }
        }
        calculatedValues.add(annualIncome);
        calculatedValues.add(cpfContribution);
        return calculatedValues;
    }

    /**
     * This method to get CPF Distribution
     *
     * @param cpfContribution Amount of money for CPF
     * @param currentAge      Current age of user
     * @return List of float values
     */
    public List<Float> getCPFDistribution(float cpfContribution, int currentAge) {
        float cpfOrdinaryAccount = 0f;
        float cpfSpecialAccount = 0f;
        float cpfMedisaveAccount = 0f;

        List<Float> cpfDistribution = new ArrayList<>();
        if (currentAge < 35) {
            cpfOrdinaryAccount += cpfContribution * 0.6217;
            cpfSpecialAccount += cpfContribution * 0.2162;
            cpfMedisaveAccount += cpfContribution * 0.1621;

        } else if (currentAge <= 45) {
            cpfOrdinaryAccount += cpfContribution * 0.5677;
            cpfSpecialAccount += cpfContribution * 0.1891;
            cpfMedisaveAccount += cpfContribution * 0.2432;

        } else if (currentAge <= 50) {
            cpfOrdinaryAccount += cpfContribution * 0.5136;
            cpfSpecialAccount += cpfContribution * 0.2162;
            cpfMedisaveAccount += cpfContribution * 0.2702;

        } else if (currentAge <= 55) {
            cpfOrdinaryAccount += cpfContribution * 0.4055;
            cpfSpecialAccount += cpfContribution * 0.3108;
            cpfMedisaveAccount += cpfContribution * 0.2837;

        } else if (currentAge <= 60) {
            cpfOrdinaryAccount += cpfContribution * 0.4616;
            cpfSpecialAccount += cpfContribution * 0.1346;
            cpfMedisaveAccount += cpfContribution * 0.4038;

        } else if (currentAge <= 65) {
            cpfOrdinaryAccount += cpfContribution * 0.2122;
            cpfSpecialAccount += cpfContribution * 0.1515;
            cpfMedisaveAccount += cpfContribution * 0.6363;

        } else {
            cpfOrdinaryAccount += cpfContribution * 0.08;
            cpfSpecialAccount += cpfContribution * 0.08;
            cpfMedisaveAccount += cpfContribution * 0.84;

        }

        cpfDistribution.add(cpfOrdinaryAccount);
        cpfDistribution.add(cpfSpecialAccount);
        cpfDistribution.add(cpfMedisaveAccount);
        return cpfDistribution;
    }

    /**
     * This method will create the more option in the action bar
     *
     * @param menu     store all the menu items
     * @param inflater it takes an XML file as input and builds the View objects from it
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: in");
        inflater.inflate(R.menu.graph_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(TAG, "onCreateOptionsMenu: out");
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
            case R.id.action_apply_scenarios:
                startActivity(new Intent(getContext(), ScenarioActivity.class));
                break;
            case R.id.action_export:
                startActivity(new Intent(getContext(), ExportActivity.class));
                break;

            default:
                Log.i(TAG, "onOptionsItemSelected: In default");
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * This method to get the combined data of Event, Milestone and Plan selected scenario
     *
     * @param expectancy Expected to live age
     * @return list of combined data
     */
    public List<Float> getCombineSelectedList(int expectancy) {
        List<Float> combineSelectedList = new ArrayList<>();
        List<HashMap<String, String>> hashMapList = new ArrayList<>();

        //add event till age value and recurring/amount value in hash map
        for (int i = 0; i < eventsList.size(); i++) {
            HashMap<String, String> eventHashmap = new HashMap<>();

            //if event status is Recurring, add current age and duration else current age
            //Note: all duration -1 because it starts from current year
            if (ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING.equals(eventsList.get(i).getStatus())) {

                eventHashmap.put(KeyConstants.KEY_EVENT_START_AGE,
                        String.valueOf(eventsList.get(i).getAge()));
                eventHashmap.put(KeyConstants.KEY_EVENT_TILL_AGE, String.valueOf(
                        eventsList.get(i).getAge() + eventsList.get(i).getDuration() - 1));
            } else {
                eventHashmap.put(KeyConstants.KEY_EVENT_START_AGE,
                        String.valueOf(eventsList.get(i).getAge()));
                eventHashmap.put(KeyConstants.KEY_EVENT_TILL_AGE,
                        String.valueOf(eventsList.get(i).getAge()));
            }
            eventHashmap.put(KeyConstants.KEY_EVENT_RECURRING,
                    String.valueOf(0 - eventsList.get(i).getAmount()));

            hashMapList.add(eventHashmap);
        }

        for (int i = 0; i < milestonesList.size(); i++) {
            HashMap<String, String> milestoneHashmap = new HashMap<>();

            //if milestone status is Recurring, add current age and duration else current age
            if (ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING.equals(milestonesList.get(i).getStatus())) {

                milestoneHashmap.put(KeyConstants.KEY_MILESTONE_START_AGE,
                        String.valueOf(milestonesList.get(i).getAge()));
                milestoneHashmap.put(KeyConstants.KEY_MILESTONE_TILL_AGE, String.valueOf(
                        milestonesList.get(i).getAge() + milestonesList.get(i).getDuration() - 1));
            } else {
                milestoneHashmap.put(KeyConstants.KEY_MILESTONE_START_AGE,
                        String.valueOf(milestonesList.get(i).getAge()));
                milestoneHashmap.put(KeyConstants.KEY_MILESTONE_TILL_AGE,
                        String.valueOf(milestonesList.get(i).getAge()));
            }
            milestoneHashmap.put(KeyConstants.KEY_MILESTONE_RECURRING,
                    String.valueOf(0 - milestonesList.get(i).getAmount()));

            hashMapList.add(milestoneHashmap);
        }

        for (int i = 0; i < plansList.size(); i++) {
            HashMap<String, String> planHashmap = new HashMap<>();

            //if plan status is Recurring, add current age and duration else current age
            if (ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING.equals(plansList.get(i).getPaymentType())) {

                planHashmap.put(KeyConstants.KEY_PLAN_START_AGE,
                        String.valueOf(plansList.get(i).getPremiumStartAge()));
                planHashmap.put(KeyConstants.KEY_PLAN_TILL_AGE, String.valueOf(
                        plansList.get(i).getPremiumStartAge() +
                                plansList.get(i).getPlanDuration() - 1));
            } else {
                planHashmap.put(KeyConstants.KEY_PLAN_START_AGE,
                        String.valueOf(plansList.get(i).getPremiumStartAge()));
                planHashmap.put(KeyConstants.KEY_PLAN_TILL_AGE,
                        String.valueOf(plansList.get(i).getPremiumStartAge()));
            }
            planHashmap.put(KeyConstants.KEY_PLAN_RECURRING,
                    String.valueOf(0 - plansList.get(i).getPaymentAmount()));

            planHashmap.put(KeyConstants.KEY_PAYOUT_START_AGE,
                    String.valueOf(plansList.get(i).getPayoutAge()));

            planHashmap.put(KeyConstants.KEY_PAYOUT_TILL_AGE, String.valueOf(
                    plansList.get(i).getPayoutAge() + plansList.get(i).getPayoutDuration() - 1));

            planHashmap.put(KeyConstants.KEY_PAYOUT_RECURRING, String.valueOf(
                    plansList.get(i).getPayoutAmount() / plansList.get(i).getPayoutDuration()));

            hashMapList.add(planHashmap);
        }

        Log.d(TAG, "getCombineSelectedList: hashmap list: " + hashMapList.size());

        //when add value to total when till age is more then or equal to current age
        for (int currentAge = 0; currentAge <= expectancy; currentAge++) {
            float totalValue = 0f;

            for (HashMap<String, String> hashMap : hashMapList) {
                if (hashMap.get(KeyConstants.KEY_EVENT_TILL_AGE) != null &&
                        hashMap.get(KeyConstants.KEY_EVENT_START_AGE) != null &&
                        currentAge >= Integer.valueOf(hashMap.get(KeyConstants.KEY_EVENT_START_AGE)) &&
                        currentAge <= Integer.valueOf(hashMap.get(KeyConstants.KEY_EVENT_TILL_AGE))) {
                    totalValue += Float.valueOf(hashMap.get(KeyConstants.KEY_EVENT_RECURRING));
                }

                if (hashMap.get(KeyConstants.KEY_MILESTONE_TILL_AGE) != null &&
                        hashMap.get(KeyConstants.KEY_MILESTONE_START_AGE) != null &&
                        currentAge >= Integer.valueOf(hashMap.get(KeyConstants.KEY_MILESTONE_START_AGE)) &&
                        currentAge <= Integer.valueOf(hashMap.get(KeyConstants.KEY_MILESTONE_TILL_AGE))) {
                    totalValue += Float.valueOf(hashMap.get(KeyConstants.KEY_MILESTONE_RECURRING));
                }

                if (hashMap.get(KeyConstants.KEY_PLAN_TILL_AGE) != null &&
                        hashMap.get(KeyConstants.KEY_PLAN_START_AGE) != null &&
                        currentAge >= Integer.valueOf(hashMap.get(KeyConstants.KEY_PLAN_START_AGE)) &&
                        currentAge <= Integer.valueOf(hashMap.get(KeyConstants.KEY_PLAN_TILL_AGE))) {
                    totalValue += Float.valueOf(hashMap.get(KeyConstants.KEY_PLAN_RECURRING));
                }

                if (hashMap.get(KeyConstants.KEY_PAYOUT_TILL_AGE) != null &&
                        hashMap.get(KeyConstants.KEY_PAYOUT_START_AGE) != null &&
                        currentAge >= Integer.valueOf(hashMap.get(KeyConstants.KEY_PAYOUT_START_AGE)) &&
                        currentAge <= Integer.valueOf(hashMap.get(KeyConstants.KEY_PAYOUT_TILL_AGE))) {
                    totalValue += Float.valueOf(hashMap.get(KeyConstants.KEY_PAYOUT_RECURRING));
                }
            }
            if (totalValue != 0f) {
                Log.d(TAG, "getCombineSelectedList: total: " + totalValue + ", current age: " + currentAge);
            }

            combineSelectedList.add(totalValue);
        }

        return combineSelectedList;
    }

    /**
     * This method to get event that user does not have income
     *
     * @param expectancy Expected to live age
     * @return list of age where income is zero
     */
    public List<Integer> getNoIncomeEventSelectedList(int expectancy) {
        List<Integer> noIncomeAgeList;
        List<HashMap<String, String>> hashMapList = new ArrayList<>();
        Set<Integer> uniqueNoIncomeAgeSet = new HashSet<>();

        //add event till age value and recurring/amount value in hash map
        for (int i = 0; i < noIncomeEventsList.size(); i++) {
            HashMap<String, String> eventHashmap = new HashMap<>();

            //if event status is Recurring, add current age and duration else current age
            //Note: all duration -1 because it starts from current year
            if (ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING.equals(eventsList.get(i).getStatus())) {

                eventHashmap.put(KeyConstants.KEY_EVENT_START_AGE,
                        String.valueOf(eventsList.get(i).getAge()));
                eventHashmap.put(KeyConstants.KEY_EVENT_TILL_AGE, String.valueOf(
                        eventsList.get(i).getAge() + eventsList.get(i).getDuration() - 1));
            } else {
                eventHashmap.put(KeyConstants.KEY_EVENT_START_AGE,
                        String.valueOf(eventsList.get(i).getAge()));
                eventHashmap.put(KeyConstants.KEY_EVENT_TILL_AGE,
                        String.valueOf(eventsList.get(i).getAge()));
            }
            eventHashmap.put(KeyConstants.KEY_EVENT_RECURRING,
                    String.valueOf(0 - eventsList.get(i).getAmount()));

            hashMapList.add(eventHashmap);
        }

        Log.d(TAG, "getCombineSelectedList: hashmap list: " + hashMapList.size());

        //when add value to total when till age is more then or equal to current age
        for (int currentAge = 0; currentAge <= expectancy; currentAge++) {
            for (HashMap<String, String> hashMap : hashMapList) {
                if (hashMap.get(KeyConstants.KEY_EVENT_TILL_AGE) != null &&
                        hashMap.get(KeyConstants.KEY_EVENT_START_AGE) != null &&
                        currentAge >= Integer.valueOf(hashMap.get(KeyConstants.KEY_EVENT_START_AGE)) &&
                        currentAge <= Integer.valueOf(hashMap.get(KeyConstants.KEY_EVENT_TILL_AGE))) {
                    uniqueNoIncomeAgeSet.add(currentAge);
                }
            }
//            Log.d(TAG, "getCombineSelectedList: current age: " + currentAge);

        }
        noIncomeAgeList = new ArrayList<>(uniqueNoIncomeAgeSet);
        Collections.sort(noIncomeAgeList);
        Log.d(TAG, "getNoIncomeEventSelectedList: All age that does not have income: " + noIncomeAgeList);

        return noIncomeAgeList;
    }

    /**
     * This method will setup the recycler view if there is any selected scenario
     */
    public void recyclerViewSetup() {
        if (eventsList.size() > 0) {
            mSelectedScenarioViewHolderList.add(new SelectedScenarioViewHolder(
                    ScreenConstants.TOOLBAR_TITLE_EVENTS, SelectedScenarioViewHolder.SECTION_HEADER));

            for (CommonModel event : eventsList) {
                mSelectedScenarioViewHolderList.add(new SelectedScenarioViewHolder(event.getName(),
                        event.getType(), String.valueOf(event.getAge()),
                        String.valueOf(NumberFormat.getCurrencyInstance(Locale.US).format(event.getAmount())),
                        String.valueOf(event.getDuration()), SelectedScenarioViewHolder.OTHER_SCENARIO));
            }
        }

        if (milestonesList.size() > 0) {
            mSelectedScenarioViewHolderList.add(new SelectedScenarioViewHolder(
                    ScreenConstants.TOOLBAR_TITLE_MILESTONES, SelectedScenarioViewHolder.SECTION_HEADER));

            for (CommonModel milestone : milestonesList) {
                mSelectedScenarioViewHolderList.add(new SelectedScenarioViewHolder(milestone.getName(),
                        milestone.getType(), String.valueOf(milestone.getAge()),
                        String.valueOf(NumberFormat.getCurrencyInstance(Locale.US).format(milestone.getAmount())),
                        String.valueOf(milestone.getDuration()), SelectedScenarioViewHolder.OTHER_SCENARIO));
            }

        }

        if (plansList.size() > 0) {
            mSelectedScenarioViewHolderList.add(new SelectedScenarioViewHolder(
                    ScreenConstants.TOOLBAR_TITLE_PLANS, SelectedScenarioViewHolder.SECTION_HEADER));

            for (PlanModel plan : plansList) {
                Log.d(TAG, "recyclerViewSetup: plan status: " + plan.getPlanStatus());
                mSelectedScenarioViewHolderList.add(new SelectedScenarioViewHolder(plan.getPlanName(),
                        plan.getPlanType(), String.valueOf(plan.getPremiumStartAge()),
                        String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                                .format(plan.getPaymentAmount())),
                        String.valueOf(plan.getPlanDuration()), String.valueOf(plan.getPayoutAge()),
                        String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                                .format(plan.getPayoutAmount())),
                        String.valueOf(plan.getPayoutDuration()), plan.getPlanStatus(),
                        SelectedScenarioViewHolder.PLAN_SCENARIO));
            }
        }

        if (mSelectedScenarioViewHolderList.size() > 0) {
            //Setup Recycler View
            SelectedScenarioAdapter selectedScenarioAdapter = new SelectedScenarioAdapter(mSelectedScenarioViewHolderList);
            RecyclerView.LayoutManager mLayoutManager = null;
            if (getActivity() != null) {
                mLayoutManager = new LinearLayoutManager(
                        getActivity().getApplicationContext());
            }
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(selectedScenarioAdapter);

            mEmptyRecyclerTextView.setVisibility(View.INVISIBLE);
        } else {
            mEmptyRecyclerTextView.setVisibility(View.VISIBLE);
        }
    }
}
