package com.example.android.graphapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.graphapplication.Const;
import com.example.android.graphapplication.R;
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

public class GraphActivity extends AppCompatActivity implements Const,
        OnChartValueSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "GraphActivity";
    private Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mFrameLayout;
    private NavigationView mNavigationView;
    private BarChart mChart;

    private String fileContent;
    private HashMap<String, String> content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        mChart = findViewById(R.id.stack_bar_graph);
        mToolBar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mFrameLayout = findViewById(R.id.content_frame);
        mNavigationView = findViewById(R.id.nav_view);

        initData();

        Log.d(TAG, "onCreate: out");
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        //Get the content from internal storage file
        Context context = getApplicationContext();
        fileContent = readFile(context, FILE_USER_INFO);
        content = splitFileContent(fileContent);
        Log.i(TAG, "onCreate: " + fileContent);

        setSupportActionBar(mToolBar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();

        // Enable the top left button
        actionBar.setDisplayHomeAsUpEnabled(true);

        //This will open and close the drawer instead of navigate to the parent screen
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //this will perform action based on the item selected
        mNavigationView.setNavigationItemSelectedListener(this);
        mChart.setOnChartValueSelectedListener(this);

        drawerMenuSetup();
        graphViewSetup();
    }


    /**
     * This method will open and close the drawer instead of back button
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * This method will select the item in the drawer
     *
     * @param menuItem store all the menu items
     * @return boolean
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // set item as selected to persist highlight
        menuItem.setChecked(true);
        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();

        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        switch (menuItem.getItemId()) {
            case R.id.nav_summary_details:
                //Update the file data
                try {
                    FileOutputStream fileOutputStream = openFileOutput(FILE_USER_INFO, MODE_PRIVATE);
                    fileOutputStream.write(fileContent.getBytes());
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(GraphActivity.this, SummaryActivity.class));
                break;

            case R.id.nav_events:
                break;

            case R.id.nav_milestones:
                break;

            case R.id.nav_plans:
                break;

            default:
                Log.i(TAG, "onNavigationItemSelected: in default");
        }
        return true;
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

            Snackbar.make(mFrameLayout, "Age: " + NumberFormat.getIntegerInstance().format(entry.getX()) +
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
                    Log.i(TAG, "getGraphData: first year");
                    annualIncome = firstYearIncome;
                } else {
                    Log.i(TAG, "getGraphData: subsequent");
                    annualIncome = grossIncome * 12;
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
                    Log.i(TAG, "getGraphData: under 35 " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.6217;
                    cpfSpecialAccount += cpfContribution * 0.2162;
                    cpfMedisaveAccount += cpfContribution * 0.1621;

                } else if (i <= 45) {
                    Log.i(TAG, "getGraphData: 35 to 45 " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.5677;
                    cpfSpecialAccount += cpfContribution * 0.1891;
                    cpfMedisaveAccount += cpfContribution * 0.2432;

                } else if (i <= 50) {
                    Log.i(TAG, "getGraphData: 46 to 50 " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.5136;
                    cpfSpecialAccount += cpfContribution * 0.2162;
                    cpfMedisaveAccount += cpfContribution * 0.2702;

                } else if (i <= 55) {
                    Log.i(TAG, "getGraphData: 51 to 55 " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.4055;
                    cpfSpecialAccount += cpfContribution * 0.3108;
                    cpfMedisaveAccount += cpfContribution * 0.2837;

                } else if (i <= 60) {
                    Log.i(TAG, "getGraphData: 56 to 60 " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.4616;
                    cpfSpecialAccount += cpfContribution * 0.1346;
                    cpfMedisaveAccount += cpfContribution * 0.4038;

                } else if (i <= 65) {
                    Log.i(TAG, "getGraphData: 61 to 65 " + i);
                    cpfOrdinaryAccount += cpfContribution * 0.2122;
                    cpfSpecialAccount += cpfContribution * 0.1515;
                    cpfMedisaveAccount += cpfContribution * 0.6363;

                } else {
                    Log.i(TAG, "getGraphData: above 65 " + i);
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
                    fileContent = fileContent.concat("//" + CONTENT_AGE_OF_SHORTFALL + ":" + String.valueOf(shortfallAge));
                }
            }

            if (i == expectancy) {
                fileContent += "//" + CONTENT_SHORTFALL + ":" + String.valueOf(assets) +
                        "//" + CONTENT_CPF_ORDINARY_ACCOUNT + ":" + String.valueOf(cpfOrdinaryAccount) +
                        "//" + CONTENT_CPF_SPECIAL_ACCOUNT + ":" + String.valueOf(cpfSpecialAccount) +
                        "//" + CONTENT_CPF_MEDISAVE_ACCOUNT + ":" + String.valueOf(cpfMedisaveAccount);
            }
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
                Integer.valueOf(content.get(CONTENT_EXPECTANCY).trim()));

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
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setFormSize(20f);
        legend.setFormToTextSpace(5f);
        legend.setXEntrySpace(6f);
        legend.setTextSize(10f);

        mChart.setData(data);
        mChart.invalidate();
    }

    /**
     * This method will draw the menu programmatically
     */
    private void drawerMenuSetup() {
        Menu menu = mNavigationView.getMenu();
        //To allow the icon to display it's original color
        mNavigationView.setItemIconTintList(null);
        menu.add(1, R.id.nav_home, 0, NAV_HOME).setIcon(R.mipmap.ic_nav_home);
        menu.add(2, R.id.nav_summary_details, 0, NAV_SUMMARY_DETAILS).setIcon(R.mipmap.ic_nav_summary);
        menu.add(3, R.id.nav_events, 0, NAV_EVENTS).setIcon(R.mipmap.ic_nav_event);
        menu.add(4, R.id.nav_milestones, 0, NAV_MILESTONES).setIcon(R.mipmap.ic_nav_milestone);
        menu.add(5, R.id.nav_plans, 0, NAV_PLANS).setIcon(R.mipmap.ic_nav_plans);
        //TODO need to ask annie for the icon for the graph
        menu.add(6, R.id.nav_graph, 0, NAV_GRAPH);
        //To create a line after the last item
        menu.add(7, 0, 0, "");
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
        System.out.println(content);
        return content;
    }

    /**
     * This method will create the more option in the action bar
     *
     * @param menu store all the menu items
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu_list, menu);
        return super.onCreateOptionsMenu(menu);
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
            //TODO need to get the icon for apply scenario and export
            case R.id.action_apply_scenarios:
                Snackbar.make(mFrameLayout, "Apply Scenarios", Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Do nothing
                    }
                }).show();
                break;

            case R.id.action_export:
                Snackbar.make(mFrameLayout, "Export", Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
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
