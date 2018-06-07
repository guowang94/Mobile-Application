package com.example.android.graphapplication.Activity;

import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.graphapplication.Const;
import com.example.android.graphapplication.R;
import com.example.android.graphapplication.RecycleList.RecyclerTouchListener;
import com.example.android.graphapplication.RecycleList.UserInfo;
import com.example.android.graphapplication.RecycleList.UserInfoAdapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SummaryActivity extends AppCompatActivity implements Const {

    private static final String TAG = "SummaryActivity";
    private Toolbar mToolBar;
    private TextView mLeftoversBalance;
    private TextView mShortfall;
    private RecyclerView mRecyclerView;
    private LinearLayout mLinearLayout;

    private UserInfoAdapter mAdapter;
    private String fileContent;
    private HashMap<String, String> content;
    private List<UserInfo> userInfoList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        mToolBar = findViewById(R.id.toolbar);
        mLeftoversBalance = findViewById(R.id.leftovers_balance);
        mShortfall = findViewById(R.id.short_fall);
        mRecyclerView = findViewById(R.id.recycler_view);
        mLinearLayout = findViewById(R.id.layout);

        initData();

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        UserInfo userInfo = userInfoList.get(position);

                        if (CONTENT_CPF_DETAILS.equals(userInfo.getTitle())) {
                            startActivity(new Intent(SummaryActivity.this, CPFContributionActivity.class));
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        Log.i(TAG, "onCreate: out");
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        String shortfallAge = null;

        setSupportActionBar(mToolBar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the top left button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(TOOLBAR_TITLE_SUMMARY);

        //Get the content from internal storage file
        Context context = getApplicationContext();
        fileContent = readFile(context, FILE_USER_INFO);
        content = splitFileContent(fileContent);
        Log.i(TAG, "onCreate: " + fileContent);
        Log.i(TAG, "onCreate: " + content);

        mAdapter = new UserInfoAdapter(userInfoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        if (Float.valueOf(content.get(CONTENT_SHORTFALL)) < 0f) {
            mLeftoversBalance.setText("$0.00");
        } else {
            mLeftoversBalance.setText(String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                    .format(Float.valueOf(content.get(CONTENT_BALANCE)))));
        }

        if (Float.valueOf(content.get(CONTENT_SHORTFALL)) > 0f) {
            mShortfall.setText("$0.00");
        } else {
            mShortfall.setText(String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                    .format(Float.valueOf(content.get(CONTENT_SHORTFALL)))));
        }

        if (content.get(CONTENT_AGE_OF_SHORTFALL) == null) {
            shortfallAge = "N/A";
        } else {
            shortfallAge = content.get(CONTENT_AGE_OF_SHORTFALL);
        }

        //Initialise Recycle view data
        userInfoList.add(new UserInfo(CONTENT_NAME, content.get(CONTENT_NAME), R.mipmap.ic_summary_screen_name));
        userInfoList.add(new UserInfo(CONTENT_AGE, content.get(CONTENT_AGE), R.mipmap.ic_summary_screen_age));
        userInfoList.add(new UserInfo(CONTENT_MONTHLY_INCOME, String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                .format(Float.valueOf(content.get(CONTENT_GROSS_MONTHLY_INCOME)))), R.mipmap.ic_summary_screen_income));
        userInfoList.add(new UserInfo(CONTENT_RETIREMENT_AGE, content.get(CONTENT_RETIREMENT_AGE), R.mipmap.ic_summary_screen_retirement));
        userInfoList.add(new UserInfo(CONTENT_AGE_OF_SHORTFALL, shortfallAge, R.mipmap.ic_summary_screen_age_shortfall));
        userInfoList.add(new UserInfo(CONTENT_CPF_DETAILS, ">", R.mipmap.ic_summary_screen_cpf));
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


