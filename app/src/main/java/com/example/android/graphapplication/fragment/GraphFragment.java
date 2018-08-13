package com.example.android.graphapplication.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.example.android.graphapplication.constants.SQLConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.validations.MyAxisValueFormatter;
import com.example.android.graphapplication.validations.MyValueFormatter;
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
import java.util.Locale;

public class GraphFragment extends Fragment implements OnChartValueSelectedListener {

    private static final String TAG = "GraphFragment";
    private ConstraintLayout mLayout;
    private CombinedChart mChart;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    private boolean isViewShown = false;
    private boolean isViewLoaded = false;
    private boolean isDataLoaded = false;

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
        // Get a support ActionBar corresponding to this mToolbar
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_GRAPH);
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));

        mChart.setOnChartValueSelectedListener(this);

        graphViewSetup();
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
            String type = null;

            if (entry.getYVals() != null) {
                Log.i(TAG, "y values: " + entry.getY());
                for (int i = 0; i < entry.getYVals().length; i++) {
                    Log.i(TAG, "y values at " + i + " index: " + entry.getYVals()[i]);
                }
                Log.i(TAG, "Selected stack index: " + h.getStackIndex());
                Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);

                switch (h.getStackIndex()) {
                    case 0:
                        type = ", Expenses: ";
                        break;

                    case 1:
                        type = ", Income: ";
                        break;

                    default:
                        Log.i(TAG, "Index Value: " + h.getStackIndex() + ", type not available");
                }

                Snackbar.make(mLayout, "Age: " + NumberFormat.getIntegerInstance().format(entry.getX()) +
                                type + DecimalFormat.getCurrencyInstance(Locale.US).format(entry.getYVals()[h.getStackIndex()]),
                        Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Do nothing
                    }
                }).show();
            } else {
                Log.i("VAL SELECTED", "Value: " + entry.getY());
            }
        } else {
            Snackbar.make(mLayout, "Age: " + NumberFormat.getIntegerInstance().format(e.getX()) +
                            ", Value: " + DecimalFormat.getCurrencyInstance(Locale.US).format(e.getY()),
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
        // TODO Auto-generated method stub
    }

    @Override
    public void onPause() {
        super.onPause();
        isDataLoaded = false;
        isViewLoaded = false;
    }

    /**
     * This method will show the graph
     */
    private void graphViewSetup() {

        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, /*CombinedChart.DrawOrder.BAR, */CombinedChart.DrawOrder.LINE
        });

        Cursor rs = mydb.getData(SQLConstants.USER_TABLE, 1);
        rs.moveToFirst();

        float assets = rs.getFloat(rs.getColumnIndex(SQLConstants.USER_TABLE_INITIAL_ASSETS));
        float monthlyIncome = rs.getFloat(rs.getColumnIndex(SQLConstants.USER_TABLE_INCOME));
        float fixedExpenses = rs.getFloat(rs.getColumnIndex(SQLConstants.USER_TABLE_FIXED_EXPENSES));
        float variableExpenses = rs.getFloat(rs.getColumnIndex(SQLConstants.USER_TABLE_VARIABLE_EXPENSES));
        int age = rs.getInt(rs.getColumnIndex(SQLConstants.USER_TABLE_AGE));
        int retirementAge = rs.getInt(rs.getColumnIndex(SQLConstants.USER_TABLE_EXPECTED_RETIREMENT_AGE));
        int expectancy = rs.getInt(rs.getColumnIndex(SQLConstants.USER_TABLE_EXPECTANCY));
        int increment = rs.getInt(rs.getColumnIndex(SQLConstants.USER_TABLE_INCREMENT));
        int inflation = rs.getInt(rs.getColumnIndex(SQLConstants.USER_TABLE_INFLATION));

        if (!rs.isClosed()) {
            rs.close();
        }

        CombinedData data = getGraphData(assets, monthlyIncome, fixedExpenses,
                variableExpenses, age, retirementAge, expectancy, increment, inflation);

        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(true);

        mChart.animateY(3000);
        mChart.setDrawGridBackground(false);
        //When false the value will be inside the bar graph but when set to true it will be outside of the graph
        mChart.setDrawValueAboveBar(false);
        //This method need to set as false for the onValueSelected() to work
        mChart.setHighlightFullBarEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(age - 1);
        xAxis.setAxisMaximum(expectancy + 1);

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
    }

    /**
     * This method will calculate and return the data for the graph
     *
     * @param assets           The current amount that the user has
     * @param grossIncome      The monthly income of the user
     * @param fixedExpenses    e.g. Bills, Loans, etc
     * @param variableExpenses e.g. Food, Entertainment, Transport, etc
     * @param age              The current age of the user
     * @param retirementAge    The age when the user stop working
     * @param expectancy       The age when the user is tired of living on earth
     * @param increment
     * @param inflation
     * @return ArrayList
     */
    private CombinedData getGraphData(float assets, float grossIncome, float fixedExpenses,
                                      float variableExpenses, int age, int retirementAge,
                                      int expectancy, int increment, int inflation) {
        ArrayList<BarEntry> incomeBarEntries = new ArrayList<>();
        ArrayList<BarEntry> expensesBarEntries = new ArrayList<>();
        ArrayList<Entry> lineEntries = new ArrayList<>();

        float firstYearIncome = grossIncome * (12 - (Calendar.getInstance().get(Calendar.MONTH))) * 0.8f;
        float firstYearExpenses = (fixedExpenses + variableExpenses) * (12 - (Calendar.getInstance().get(Calendar.MONTH)));
        float subsequentAnnualIncome = grossIncome * 12 * 0.8f;
        float subsequentAnnualExpenses = (fixedExpenses + variableExpenses) * 12;
        float annualIncome = 0f;
        float annualExpenses = 0f;
        float cpfOrdinaryAccount = 0f;
        float cpfSpecialAccount = 0f;
        float cpfMedisaveAccount = 0f;
        float balance = 0f;
        int shortfallAge = -1;
        int expensesExceededIncomeAge = -1;

        for (int i = age; i < expectancy + 1; i++) {
            if (i < retirementAge + 1) {

                if (i == age) {
//                    Log.i(TAG, "getGraphData: first year");
                    annualIncome = firstYearIncome * 0.8f;
                    annualExpenses = firstYearExpenses;
                    Log.d(TAG, "getGraphData: income: " + annualIncome + " at " + i);
                    Log.d(TAG, "getGraphData: expenses: " + annualExpenses + " at " + i);
                } else if (i == age + 1) {
//                    Log.i(TAG, "getGraphData: subsequent");
                    annualIncome = subsequentAnnualIncome * (100 + increment) / 100;
                    annualExpenses = subsequentAnnualExpenses * (100 + inflation) / 100;
                    Log.d(TAG, "getGraphData: income: " + annualIncome + " at " + i);
                    Log.d(TAG, "getGraphData: expenses: " + annualExpenses + " at " + i);
                } else {
                    annualIncome = annualIncome * (100 + increment) / 100;
                    annualExpenses = annualExpenses * (100 + inflation) / 100;
                    Log.d(TAG, "getGraphData: income: " + annualIncome + " at " + i);
                    Log.d(TAG, "getGraphData: expenses: " + annualExpenses + " at " + i);
                }

                float cpfContribution;

                //----------------Calculation for CPF Contribution %------------------

                if (i <= 55) {
                    cpfContribution = annualIncome * 0.37f;
                    if (cpfContribution > 2200 * 12) {
                        cpfContribution = 2200 * 12;
                    }
                } else if (i <= 60) {
                    cpfContribution = annualIncome * 0.26f;
                    if (cpfContribution > 1560 * 12) {
                        cpfContribution = 1560 * 12;
                    }
                } else if (i <= 65) {
                    cpfContribution = annualIncome * 0.165f;
                    if (cpfContribution > 990 * 12) {
                        cpfContribution = 990 * 12;
                    }
                } else {
                    cpfContribution = annualIncome * 0.125f;
                    if (cpfContribution > 750 * 12) {
                        cpfContribution = 750 * 12;
                    }
                }

                //----------------Calculation for CPF distribution-------------------

                if (i < 35) {
//                    Log.i(TAG, "getGraphData: under 35, " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.6217;
                    cpfSpecialAccount += cpfContribution * 0.2162;
                    cpfMedisaveAccount += cpfContribution * 0.1621;

                } else if (i <= 45) {
//                    Log.i(TAG, "getGraphData: 35 to 45, " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.5677;
                    cpfSpecialAccount += cpfContribution * 0.1891;
                    cpfMedisaveAccount += cpfContribution * 0.2432;

                } else if (i <= 50) {
//                    Log.i(TAG, "getGraphData: 46 to 50, " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.5136;
                    cpfSpecialAccount += cpfContribution * 0.2162;
                    cpfMedisaveAccount += cpfContribution * 0.2702;

                } else if (i <= 55) {
//                    Log.i(TAG, "getGraphData: 51 to 55, " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.4055;
                    cpfSpecialAccount += cpfContribution * 0.3108;
                    cpfMedisaveAccount += cpfContribution * 0.2837;

                } else if (i <= 60) {
//                    Log.i(TAG, "getGraphData: 56 to 60, " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.4616;
                    cpfSpecialAccount += cpfContribution * 0.1346;
                    cpfMedisaveAccount += cpfContribution * 0.4038;

                } else if (i <= 65) {
//                    Log.i(TAG, "getGraphData: 61 to 65, " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.2122;
                    cpfSpecialAccount += cpfContribution * 0.1515;
                    cpfMedisaveAccount += cpfContribution * 0.6363;

                } else {
//                    Log.i(TAG, "getGraphData: above 65, " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.08;
                    cpfSpecialAccount += cpfContribution * 0.08;
                    cpfMedisaveAccount += cpfContribution * 0.84;

                }

                //-------------Calculation for the graph---------------

                float remainder = annualIncome - annualExpenses;

                if (remainder < 0) {
                    assets += remainder;
//                    remainder = 0;
                }

                expensesBarEntries.add(new BarEntry(i, new float[]{annualExpenses, remainder}));
//                incomeBarEntries.add(new BarEntry(i, new float[]{annualIncome}));
//                expensesBarEntries.add(new BarEntry(i, new float[]{annualExpenses}));
                lineEntries.add(new Entry(i, assets));

                Log.d(TAG, "getGraphData: assets: " + assets + " at " + i);

                assets += remainder;

                if (i == retirementAge) {
                    balance = assets;
                }

            } else {
                annualExpenses = annualExpenses * (100 + inflation) / 100;
                assets -= annualExpenses;
                expensesBarEntries.add(new BarEntry(i, new float[]{annualExpenses, 0}));
//                incomeBarEntries.add(new BarEntry(i, new float[]{0}));
//                expensesBarEntries.add(new BarEntry(i, new float[]{annualExpenses}));
                lineEntries.add(new Entry(i, assets));

                Log.d(TAG, "getGraphData: assets: " + assets + " at " + i);
            }

            if (assets < 0f) {
                if (shortfallAge == -1) {
                    shortfallAge = i;
                }
            }

            if (annualExpenses > annualIncome) {
                if (expensesExceededIncomeAge == -1) {
                    expensesExceededIncomeAge = i;
                }
            }

            if (i == expectancy) {
                mydb.updateUser(cpfOrdinaryAccount, cpfSpecialAccount, cpfMedisaveAccount,
                        balance, assets, shortfallAge, expensesExceededIncomeAge);
            }
        }

        //----------- Bar Graph ------------
        //BarDataSet is similar to series
        BarDataSet barDataSet = new BarDataSet(expensesBarEntries, null);
        barDataSet.setColors(getResources().getColor(R.color.expensesGraph), getResources().getColor(R.color.incomeGraph));
        barDataSet.setStackLabels(new String[]{ScreenConstants.GRAPH_LEGEND_EXPENSES, ScreenConstants.GRAPH_LEGEND_INCOME});

        //values will appear on the graph
        barDataSet.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);

        BarData barData = new BarData(dataSets);
        barData.setValueFormatter(new MyValueFormatter());
        barData.setValueTextColor(Color.BLACK);
        //======================================end of working code==============================================


        //TRYING TO DISPLAY 2 OVERLAP GRAPH (NOT WORKING AS IT IS ONLY DISPLAYING 1)
//        //BarDataSet is similar to series
//        BarDataSet incomeBarDataSet = new BarDataSet(incomeBarEntries, ScreenConstants.GRAPH_LEGEND_INCOME);
//        incomeBarDataSet.setColors(getResources().getColor(R.color.incomeGraph));
////        incomeBarDataSet.setLabel(ScreenConstants.GRAPH_LEGEND_INCOME);
//
//        //values will appear on the graph
//        incomeBarDataSet.setDrawValues(false);
//
//        BarData incomeBarData = new BarData(incomeBarDataSet);
//        incomeBarData.setValueFormatter(new MyValueFormatter());
//        incomeBarData.setValueTextColor(Color.BLACK);
//
//        //BarDataSet is similar to series
//        BarDataSet expensesBarDataSet = new BarDataSet(expensesBarEntries, ScreenConstants.GRAPH_LEGEND_EXPENSES);
//        expensesBarDataSet.setColors(getResources().getColor(R.color.expensesGraph));
////        incomeBarDataSet.setLabel(ScreenConstants.GRAPH_LEGEND_EXPENSES);
//
//        //values will appear on the graph
//        expensesBarDataSet.setDrawValues(false);
//
//        BarData expensesBarData = new BarData(expensesBarDataSet);
//        expensesBarData.setValueFormatter(new MyValueFormatter());
//        expensesBarData.setValueTextColor(Color.BLACK);


        //THIS CODE TO DISPLAY 2 BAR GRAPH SIDE BY SIDE
//        float groupSpace = 0.06f;
//        float barSpace = 0.0f; // x2 dataset
//        float barWidth = 0.45f; // x2 dataset
//
//        BarData barData = new BarData(incomeBarDataSet, expensesBarDataSet);
//        barData.setValueFormatter(new MyValueFormatter());
//        barData.setValueTextColor(Color.BLACK);
//        barData.setBarWidth(barWidth);
//        barData.groupBars(age, groupSpace, barSpace);



        //------------- Line Graph ------------
        LineDataSet lineDataSet = new LineDataSet(lineEntries, null);
        lineDataSet.setLabel(ScreenConstants.GRAPH_LEGEND_ASSETS);
        lineDataSet.setColor(getResources().getColor(R.color.purple));
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCircleColor(getResources().getColor(R.color.purple));
        lineDataSet.setCircleRadius(1f);
        lineDataSet.setFillColor(getResources().getColor(R.color.purple));
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawValues(false);

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
//        combinedData.setData(expensesBarData);
        combinedData.setData(lineData);

        return combinedData;
    }

    /**
     * This method will create the more option in the action bar
     *
     * @param menu     store all the menu items
     * @param inflater it takes an XML file as input and builds the View objects from it
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.graph_fragment_menu, menu);
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
            case R.id.action_apply_scenarios:
                Snackbar.make(mLayout, "Apply Scenarios", Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Do nothing
                    }
                }).show();
                break;

            case R.id.action_export:
                Snackbar.make(mLayout, "Export", Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Do nothing
                    }
                }).show();
                break;

            default:
                Log.i(TAG, "onOptionsItemSelected: In default");
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
