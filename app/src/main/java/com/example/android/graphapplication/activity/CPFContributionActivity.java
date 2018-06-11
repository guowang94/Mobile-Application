package com.example.android.graphapplication.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.android.graphapplication.Const;
import com.example.android.graphapplication.R;
import com.example.android.graphapplication.recycleList.CPFContribution;
import com.example.android.graphapplication.recycleList.CPFContributionAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CPFContributionActivity extends AppCompatActivity implements Const {

    private static final String TAG = "CPFContributionActivity";
    private RecyclerView mRecyclerView;
    private PieChart mChart;
    private Toolbar mToolBar;

    private CPFContributionAdapter mAdapter;
    private String fileContent;
    private HashMap<String, String> content;
    private List<CPFContribution> cpfContributionList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpf_contribution);

        mChart = findViewById(R.id.pie_chart);
        mToolBar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.vertical_recycler_view);

        initData();
        pieChartSetup();
    }

    /**
     * This method will show the chart
     */
    private void pieChartSetup() {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 5, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);


        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(false);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        setData();

        mChart.animateY(1400);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTextSize(11f);
    }

    /**
     * This method will set the data
     */
    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(Float.valueOf(content.get(CONTENT_CPF_ORDINARY_ACCOUNT)),
                GRAPH_LEGEND_CPF_OA));
        entries.add(new PieEntry(Float.valueOf(content.get(CONTENT_CPF_SPECIAL_ACCOUNT)),
                GRAPH_LEGEND_CPF_SA));
        entries.add(new PieEntry(Float.valueOf(content.get(CONTENT_CPF_MEDISAVE_ACCOUNT)),
                GRAPH_LEGEND_CPF_MA));

        PieDataSet dataSet = new PieDataSet(entries, null);

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(getResources().getColor(R.color.cpfOAChart),
                getResources().getColor(R.color.cpfSAChart),
                getResources().getColor(R.color.cpfMAChart));

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        mChart.invalidate();
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        setSupportActionBar(mToolBar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the top left button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(TOOLBAR_TITLE_ACCUMULATIVE_CPF);

        //Get the content from internal storage file
        Context context = getApplicationContext();
        fileContent = readFile(context, FILE_USER_INFO);
        content = splitFileContent(fileContent);
        Log.i(TAG, "onCreate: " + fileContent);
        Log.i(TAG, "onCreate: " + content);

        mAdapter = new CPFContributionAdapter(cpfContributionList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        //Initialise Recycle view data
        cpfContributionList.add(new CPFContribution(CONTENT_CPF_ORDINARY_ACCOUNT,
                NumberFormat.getCurrencyInstance(Locale.US)
                        .format(Float.valueOf(content.get(CONTENT_CPF_ORDINARY_ACCOUNT)))));
        cpfContributionList.add(new CPFContribution(CONTENT_CPF_SPECIAL_ACCOUNT,
                NumberFormat.getCurrencyInstance(Locale.US)
                        .format(Float.valueOf(content.get(CONTENT_CPF_SPECIAL_ACCOUNT)))));
        cpfContributionList.add(new CPFContribution(CONTENT_CPF_MEDISAVE_ACCOUNT,
                NumberFormat.getCurrencyInstance(Locale.US)
                        .format(Float.valueOf(content.get(CONTENT_CPF_MEDISAVE_ACCOUNT)))));
        Log.i(TAG, "initData: " + cpfContributionList);
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
