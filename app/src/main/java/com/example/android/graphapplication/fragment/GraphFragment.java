package com.example.android.graphapplication.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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

import com.example.android.graphapplication.Constants;
import com.example.android.graphapplication.R;
import com.example.android.graphapplication.ReadFileData;
import com.example.android.graphapplication.validations.MyAxisValueFormatter;
import com.example.android.graphapplication.validations.MyValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class GraphFragment extends Fragment implements Constants, OnChartValueSelectedListener {

    private static final String TAG = "GraphFragment";
    private ConstraintLayout mLayout;
    private BarChart mChart;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    private String fileContent;
    private HashMap<String, String> content;
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
        Log.d(TAG, "onCreateView: in");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        mChart = view.findViewById(R.id.stack_bar_graph);
        mLayout = view.findViewById(R.id.layout);
        mToolbar = view.findViewById(R.id.graph_toolbar);
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
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this mToolbar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mToolbarTitle.setText(Constants.TOOLBAR_TITLE_GRAPH);
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));

        //Get the content from internal storage file
        Context context = getActivity().getApplicationContext();
        fileContent = new ReadFileData().readFile(context, FILE_USER_INFO);
        content = new ReadFileData().splitFileContent(fileContent);
        Log.i(TAG, "initData: " + content);

        //if event count does not exist, make event count = 1
        if (content.get(CONTENT_EVENT_COUNT) == null) {
            fileContent += "//" + CONTENT_EVENT_COUNT + ":" + "0";
        }

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
                    type = ", Income: ";
                    break;

                case 1:
                    type = ", Assets: ";
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
        } else
            Log.i("VAL SELECTED", "Value: " + entry.getY());
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
     * This method will calculate and return the data for the graph
     *
     * @param assets           The current amount that the user has
     * @param grossIncome      The monthly income of the user
     * @param fixedExpenses    e.g. Bills, Loans, etc
     * @param variableExpenses e.g. Food, Entertainment, Transport, etc
     * @param age              The current age of the user
     * @param retirementAge    The age when the user stop working
     * @param expectancy       The age when the user is tired of living on earth
     * @return ArrayList
     */
    private ArrayList<BarEntry> getGraphData(float assets, float grossIncome, float fixedExpenses,
                                             float variableExpenses, int age, int retirementAge, int expectancy) {
        ArrayList<BarEntry> yVals = new ArrayList<>();
        float firstYearIncome = grossIncome * (12 - (Calendar.getInstance().get(Calendar.MONTH)));
        float firstYearExpenses = (fixedExpenses + variableExpenses) * (12 - (Calendar.getInstance().get(Calendar.MONTH)));
        float annualIncome;
        float annualExpenses = (fixedExpenses + variableExpenses) * 12;
        float cpfOrdinaryAccount = 0f;
        float cpfSpecialAccount = 0f;
        float cpfMedisaveAccount = 0f;
        int shortfallAge = -1;

        for (int i = age; i < expectancy + 1; i++) {
            if (i < retirementAge + 1) {

                if (i == age) {
//                    Log.i(TAG, "getGraphData: first year");
                    annualIncome = firstYearIncome * 0.8f;
                } else {
//                    Log.i(TAG, "getGraphData: subsequent");
                    annualIncome = (grossIncome * 12) * 0.8f;
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

                if (i == age) {
                    annualIncome -= firstYearExpenses;
                } else {
                    annualIncome -= annualExpenses;
                }

                if (annualIncome < 0) {
                    assets += annualIncome;
                    annualIncome = 0;
                }

                yVals.add(new BarEntry(i, new float[]{annualIncome, assets}));

                assets += annualIncome;

                if (i == retirementAge) {
                    fileContent = fileContent.concat("//" + CONTENT_BALANCE + ":" + String.valueOf(assets));
                }

            } else {
                assets -= annualExpenses;
                yVals.add(new BarEntry(i, new float[]{0, assets}));
            }

            if (assets < 0f) {
                if (shortfallAge == -1) {
                    shortfallAge = i;
                    fileContent = fileContent.concat("//" + CONTENT_SHORTFALL_AGE + ":" + String.valueOf(shortfallAge));
                }
            }

            if (i == expectancy) {
                fileContent += "//" + CONTENT_SHORTFALL + ":" + String.valueOf(assets) +
                        "//" + CONTENT_ORDINARY_ACCOUNT + ":" + String.valueOf(cpfOrdinaryAccount) +
                        "//" + CONTENT_SPECIAL_ACCOUNT + ":" + String.valueOf(cpfSpecialAccount) +
                        "//" + CONTENT_MEDISAVE_ACCOUNT + ":" + String.valueOf(cpfMedisaveAccount);
            }
        }

        //Update the file data
        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput(FILE_USER_INFO, MODE_PRIVATE);
            fileOutputStream.write(fileContent.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return yVals;
    }

    /**
     * This method will show the graph
     */
    private void graphViewSetup() {
        ArrayList<BarEntry> yVals1 = getGraphData(Float.valueOf(content.get(CONTENT_CURRENT_ASSETS)),
                Float.valueOf(content.get(CONTENT_GROSS_MONTHLY_INCOME)),
                Float.valueOf(content.get(CONTENT_FIXED_EXPENSES)),
                Float.valueOf(content.get(CONTENT_VARIABLE_EXPENSES)),
                Integer.valueOf(content.get(CONTENT_AGE)),
                Integer.valueOf(content.get(CONTENT_RETIREMENT_AGE)),
                Integer.valueOf(content.get(CONTENT_EXPECTANCY)));

        //BarDataSet is similar to series
        BarDataSet set1 = new BarDataSet(yVals1, null);
        set1.setColors(getResources().getColor(R.color.incomeGraph), getResources().getColor(R.color.assetsGraph));
        set1.setStackLabels(new String[]{GRAPH_LEGEND_INCOME, GRAPH_LEGEND_ASSETS});

        //values will appear on the graph
        set1.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextColor(Color.BLACK);

        mChart.setFitBars(true);
        mChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be drawn
        mChart.setMaxVisibleValueCount(10);
        mChart.setPinchZoom(true);

        mChart.animateY(3000);
        mChart.setDrawGridBackground(false);
        //When false the value will be inside the bar graph but when set to true it will be outside of the graph
        mChart.setDrawValueAboveBar(false);
        //This method need to set as false for the onValueSelected() to work
        mChart.setHighlightFullBarEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setValueFormatter(new MyAxisValueFormatter());
        yAxis.setCenterAxisLabels(true);
        mChart.getAxisRight().setEnabled(false);

        Legend legend = mChart.getLegend();
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
     * This method will create the more option in the action bar
     *
     * @param menu store all the menu items
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
