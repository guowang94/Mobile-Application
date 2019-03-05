package com.example.android.graphapplication.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GraphFragment extends Fragment implements OnChartValueSelectedListener {

    private static final String TAG = "GraphFragment";
    private ConstraintLayout mLayout;
    private CombinedChart mChart;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private TextView mEmptyRecyclerTextView;
    private RecyclerView mRecyclerView;

    private boolean isViewShown;
    private boolean isViewLoaded;
    private boolean isDataLoaded;

    private List<SelectedScenarioViewHolder> mSelectedScenarioViewHolderList;
    private List<CommonModel> eventsList;
    private List<CommonModel> milestonesList;
    private List<PlanModel> plansList;

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

        if (getActivity() != null) {
            mRecyclerView.addItemDecoration(new DividerItemDecoration(
                    getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

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

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        }
        // Get a support ActionBar corresponding to this mToolbar
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_GRAPH);

        mChart.setOnChartValueSelectedListener(this);

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

        Log.d(TAG, "onValueSelected: entry, " + e);
        Log.d(TAG, "onValueSelected: highlight, " + h);
        if (h.getStackIndex() != -1) {
            BarEntry entry = (BarEntry) e;
            String type;

            if (entry.getYVals() != null) {
                Log.i(TAG, "y values: " + entry.getY());
                for (int i = 0; i < entry.getYVals().length; i++) {
                    Log.i(TAG, "y values at " + i + " index: " + entry.getYVals()[i]);
                }
                Log.i(TAG, "Selected stack index: " + h.getStackIndex());
                Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);

                switch (h.getStackIndex()) {
                    case 0:
                        type = ", Negative Value: ";
                        if (entry.getYVals()[h.getStackIndex()] != 0f) {
                            Snackbar.make(mLayout, "Age: " + NumberFormat.getIntegerInstance()
                                            .format(entry.getX()) + type +
                                            DecimalFormat.getCurrencyInstance(Locale.US)
                                                    .format(entry.getYVals()[h.getStackIndex()]),
                                    Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Do nothing
                                }
                            }).show();
                        }
                        break;

                    case 1:
                        type = ", Expenses: ";
                        if (entry.getYVals()[h.getStackIndex()] != 0f) {
                            Snackbar.make(mLayout, "Age: " + NumberFormat.getIntegerInstance()
                                            .format(entry.getX()) + type +
                                            DecimalFormat.getCurrencyInstance(Locale.US)
                                                    .format(entry.getYVals()[h.getStackIndex()]),
                                    Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Do nothing
                                }
                            }).show();
                        }
                        break;
                    case 2:
                        type = ", Income: ";
                        if (entry.getYVals()[h.getStackIndex()] != 0f) {
                            Snackbar.make(mLayout, "Age: " + NumberFormat.getIntegerInstance()
                                            .format(entry.getX()) + type +
                                            DecimalFormat.getCurrencyInstance(Locale.US)
                                                    .format(entry.getYVals()[h.getStackIndex()] +
                                                            entry.getYVals()[1]),
                                    Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Do nothing
                                }
                            }).show();
                        }
                        break;

                    case 3:
                        type = ", Positive Value: ";
                        if (entry.getYVals()[h.getStackIndex()] != 0f) {
                            Snackbar.make(mLayout, "Age: " + NumberFormat.getIntegerInstance()
                                            .format(entry.getX()) + type +
                                            DecimalFormat.getCurrencyInstance(Locale.US)
                                                    .format(entry.getYVals()[h.getStackIndex()]),
                                    Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Do nothing
                                }
                            }).show();
                        }
                        break;

                    default:
                        Log.d(TAG, "onValueSelected: None of the ids have matched. " +
                                "Current Graph Index: " + h.getStackIndex());
                }
            } else {
                Log.i("VAL SELECTED", "Value: " + entry.getY());
            }
        } else {
            Snackbar.make(mLayout, "Age: " + NumberFormat.getIntegerInstance().format(e.getX()) +
                            ", Assets: " + DecimalFormat.getCurrencyInstance(Locale.US).format(e.getY()),
                    Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Do nothing
                }
            }).show();
        }
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
        Log.d(TAG, "onPause: out");
    }

    /**
     * This method will show the graph
     */
    private void graphViewSetup() {
        Log.d(TAG, "graphViewSetup: in");
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });

        UserModel userModel = mydb.getAllUser().get(0);

        CombinedData data;
        eventsList = mydb.getAllSelectedEvent();
        milestonesList = mydb.getAllSelectedMilestone();
        plansList = mydb.getAllSelectedPlan();

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

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setValueFormatter(new MyAxisValueFormatter());
        yAxis.setCenterAxisLabels(true);
        mChart.getAxisRight().setEnabled(false);

        Legend legend = mChart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setFormSize(15f);
        legend.setFormToTextSpace(5f);
        legend.setXEntrySpace(6f);
        legend.setTextSize(10f);

        mChart.setData(data);
        mChart.invalidate();
        Log.d(TAG, "graphViewSetup: out");
    }

    /**
     * This method will calculate and return the data for the graph
     *
     * @param userModel Contains details of the user
     * @return CombinedData for the graph
     */
    private CombinedData getGraphData(UserModel userModel) {
        Log.d(TAG, "getGraphData: in");
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<Entry> lineEntries = new ArrayList<>();

        float assets = userModel.getInitialAssets();
        float firstYearIncome = userModel.getMonthlyIncome() * (12 - (Calendar.getInstance().get(Calendar.MONTH)));
        float firstYearExpenses = (userModel.getFixedExpenses() + userModel.getVariableExpenses()) * (12 - (Calendar.getInstance().get(Calendar.MONTH)));
        float subsequentAnnualIncome = userModel.getMonthlyIncome() * 12;
        float subsequentAnnualExpenses = (userModel.getFixedExpenses() + userModel.getVariableExpenses()) * 12;
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

        for (int currentAge = age; currentAge <= expectancy; currentAge++) {
            if (currentAge <= retirementAge) {

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
                if (!ScreenConstants.SEGMENTED_BUTTON_VALUE_SELF_EMPLOYED.equals(userModel.getJobStatus()) &&
                        !ScreenConstants.SEGMENTED_BUTTON_VALUE_FOREIGNER_OR_PR.equals(userModel.getCitizenship())) {
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

                //-------------Calculation for the graph---------------

                float remainder = annualIncomeAfterDeductCPF - annualExpenses;

                if (remainder < 0) {
                    assets += remainder;
                    remainder = 0;
                }

                float editedValue = selectedScenarioValuesList.get(currentAge);
                assets += editedValue;

                if (editedValue > 0) {
                    barEntries.add(new BarEntry(currentAge, new float[]{0, annualExpenses, remainder, editedValue}));
                } else {
                    barEntries.add(new BarEntry(currentAge, new float[]{editedValue, annualExpenses, remainder, 0}));
                }
                lineEntries.add(new Entry(currentAge, assets));

                Log.d(TAG, "getGraphData: assets: " + assets + " at " + currentAge);

                assets += remainder;

                if (currentAge == retirementAge) {
                    balance = assets;
                }

            } else {
                annualExpenses = annualExpenses * (100 + inflation) / 100;
                float editedValue = selectedScenarioValuesList.get(currentAge);
                assets -= annualExpenses;
                assets += editedValue;

                if (editedValue > 0) {
                    barEntries.add(new BarEntry(currentAge, new float[]{0, annualExpenses, 0, editedValue}));
                } else {
                    barEntries.add(new BarEntry(currentAge, new float[]{editedValue, annualExpenses, 0, 0}));
                }
                lineEntries.add(new Entry(currentAge, assets));

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
        //BarDataSet is similar to series
        BarDataSet barDataSet = new BarDataSet(barEntries, null);
        barDataSet.setColors(Color.RED, ContextCompat.getColor(getActivity().getApplicationContext(), R.color.expensesGraph),
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.incomeGraph), Color.GREEN);
        barDataSet.setStackLabels(new String[]{"Negative Value", ScreenConstants.GRAPH_LEGEND_EXPENSES,
                ScreenConstants.GRAPH_LEGEND_INCOME, "Positive Value"});

        //values will appear on the graph
        barDataSet.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);

        BarData barData = new BarData(dataSets);
        barData.setValueFormatter(new MyValueFormatter());
        barData.setValueTextColor(Color.BLACK);

        //------------- Line Graph ------------
        LineDataSet lineDataSet = new LineDataSet(lineEntries, ScreenConstants.GRAPH_LEGEND_ASSETS);
        lineDataSet.setColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.lineGraph));
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCircleColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.lineGraph));
        lineDataSet.setCircleRadius(1f);
        lineDataSet.setFillColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.lineGraph));
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawValues(false);

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        combinedData.setData(lineData);

        Log.d(TAG, "getGraphData: out");
        return combinedData;
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
            Log.d(TAG, "getCombineSelectedList: total: " + totalValue + ", current age: " + currentAge);

            combineSelectedList.add(totalValue);
        }

        return combineSelectedList;
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
