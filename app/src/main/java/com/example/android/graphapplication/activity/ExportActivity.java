package com.example.android.graphapplication.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.adapter.ExportAdapter;
import com.example.android.graphapplication.constants.ExportConstant;
import com.example.android.graphapplication.constants.KeyConstants;
import com.example.android.graphapplication.constants.SQLConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.model.ExportModel;
import com.example.android.graphapplication.model.UserModel;
import com.example.android.graphapplication.utils.PermissionUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ExportActivity extends AppCompatActivity {

    private static final String TAG = "ExportActivity";
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private RecyclerView mRecyclerView;

    private List<ExportModel> exportModelList = new ArrayList<>();

    private DBHelper mydb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        mToolbar = findViewById(R.id.export_toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mRecyclerView = findViewById(R.id.recycler_view);

        mydb = new DBHelper(getApplicationContext());

        initData();
        Log.d(TAG, "onCreate: out");
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_EXPORT_PREVIEW);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setupReportRecyclerView();
    }

    /**
     * This method will create the more option in the action bar
     *
     * @param menu store all the menu items
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.export_menu, menu);
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
            case R.id.action_export:
                PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        new PermissionUtil.PermissionAskListener() {

                            @Override
                            public void onPermissionAsk() {
                                ActivityCompat.requestPermissions(
                                        ExportActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        KeyConstants.REQUEST_EXTERNAL_STORAGE
                                );
                            }

                            @Override
                            public void onPermissionPreviouslyDenied() {
                                //show a dialog explaining permission and then request permission
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExportActivity.this);

                                // Setting Dialog Title
                                alertDialog.setTitle("Unable to Export");

                                // Setting Dialog Message
                                alertDialog.setMessage("Permission is needed to export into PDF file.");

                                // Setting Positive "Yes" Button
                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to invoke YES event
                                        ActivityCompat.requestPermissions(
                                                ExportActivity.this,
                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                KeyConstants.REQUEST_EXTERNAL_STORAGE
                                        );
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();
                            }

                            @Override
                            public void onPermissionDisabled() {
                                Toast.makeText(getApplicationContext(), "Permission Disabled.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionGranted() {
                                exportPDFToEmailApp();
                            }
                        });

                break;
            default:
                Log.i(TAG, "onOptionsItemSelected: In default");
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * This method will take a screenshot of the RecyclerView and save as PDF and attached the pdf in the email
     */
    private void exportPDFToEmailApp() {
        mRecyclerView.getLayoutManager().scrollToPosition(0);
        mRecyclerView.measure(
                View.MeasureSpec.makeMeasureSpec(mRecyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        Bitmap bm = Bitmap.createBitmap(mRecyclerView.getWidth(), mRecyclerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mRecyclerView.draw(new Canvas(bm));

        ImageView im = new ImageView(this);
        im.setImageBitmap(bm);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
        String directoryPath = null;
        String fileName = "Financial_Report-" + simpleDateFormat.format(new Date()) + ".pdf";
        try {
            Document document = new Document();

            // Create 'GraphApplication' folder in Downloads if it does not exist
            directoryPath = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/GraphApplication/";
            File f = new File(directoryPath);
            if (!f.exists()) {
                if (!f.mkdir()) {
                    Log.d(TAG, "onOptionsItemSelected: Unable to create folder");
                } else {
                    Log.d(TAG, "onOptionsItemSelected: Folder has been created");
                }
            } else {
                Log.d(TAG, "onOptionsItemSelected: Folder already exists");
            }

            PdfWriter.getInstance(document, new FileOutputStream(directoryPath + fileName));

            document.open();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());

            mRecyclerView.draw(new Canvas(bm));

            float scaler = ((document.getPageSize().getHeight() - document.topMargin()
                    - document.bottomMargin() - 0) / image.getHeight()) * 100; // 0 means you have no indentation. If you have any, change it.
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

            document.add(image);
            document.close();

        } catch (Exception e) {
            //do nothing
        }

        /* Create the Intent */
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        Log.d(TAG, "onOptionsItemSelected: dir: " + directoryPath + fileName);

        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(root, "GraphApplication/" + fileName);
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

        /* Fill it with Data */
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"nickloh94@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
        emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);

        /* Send it off to the Activity-Chooser */
        this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    /**
     * This method will check if permission is granted and perform relevant action
     *
     * @param requestCode Created in KeyConstant
     * @param permissions Permissions
     * @param grantResults Grant result array
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case KeyConstants.REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    exportPDFToEmailApp();
                }
                /*else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }*/
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    /**
     * This method will setup Report RecyclerView
     */
    private void setupReportRecyclerView() {
        UserModel userModel = mydb.getAllUser().get(0);
        List<HashMap<String, String>> milestoneList = mydb.getAllSelectedMilestone();
        List<HashMap<String, String>> existingPlanList = mydb.getAllExistingPlan();
        List<HashMap<String, String>> nonExistingPlanList = mydb.getAllNonExistingPlan();
        mydb.close();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        String currentDate = simpleDateFormat.format(new Date());
        Float totalMilestoneCost = 0f;
        Float totalExistingCoverage = 0f;
        Float totalNonExistingCoverage = 0f;

        String shortfallAge = userModel.getShortfallAge() == -1 ?
                "N/A" : String.valueOf(userModel.getShortfallAge());

        for (HashMap<String, String> milestone : milestoneList) {
            totalMilestoneCost = totalMilestoneCost +
                    (Float.valueOf(milestone.get(SQLConstants.MILESTONE_TABLE_AMOUNT)) *
                            Float.valueOf(milestone.get(SQLConstants.MILESTONE_TABLE_DURATION)));
        }

        for (HashMap<String, String> existingPlan : existingPlanList) {
            totalExistingCoverage = totalExistingCoverage +
                    (Float.valueOf(existingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT)) *
                            Float.valueOf(existingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_DURATION)));
        }

        for (HashMap<String, String> nonExistingPlan : nonExistingPlanList) {
            totalNonExistingCoverage = totalNonExistingCoverage +
                    (Float.valueOf(nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT)) *
                            Float.valueOf(nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_DURATION)));
        }


        //Setting data for Recycler View
        exportModelList.add(new ExportModel(currentDate, ExportModel.REPORT_TITLE));

        //Client Details
        exportModelList.add(new ExportModel(ExportConstant.CLIENT_DETAILS, ExportModel.REPORT_HEADER));
        exportModelList.add(new ExportModel(ExportConstant.TOTAL_SHORTFALL,
                String.valueOf(userModel.getTotalAssets()), ExportModel.REPORT_TOTAL_SECTION_WITH_COLOR));
        exportModelList.add(new ExportModel(ExportConstant.CLIENT_NAME,
                userModel.getName(), ExportModel.CLIENT_DETAIL));
        exportModelList.add(new ExportModel(ExportConstant.GROSS_MONTHLY_INCOME,
                String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                        .format(userModel.getMonthlyIncome())), ExportModel.CLIENT_DETAIL));
        exportModelList.add(new ExportModel(ExportConstant.CURRENT_AVAILABLE_ASSETS,
                String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                        .format(userModel.getInitialAssets())), ExportModel.CLIENT_DETAIL));
        exportModelList.add(new ExportModel(ExportConstant.JOB_STATUS,
                userModel.getJobStatus(), ExportModel.CLIENT_DETAIL));
        exportModelList.add(new ExportModel(ExportConstant.CITIZENSHIP_STATUS,
                userModel.getCitizenship(), ExportModel.CLIENT_DETAIL));
        exportModelList.add(new ExportModel(ExportConstant.CURRENT_AGE,
                String.valueOf(userModel.getAge()), ExportModel.CLIENT_DETAIL));
        exportModelList.add(new ExportModel(ExportConstant.RETIREMENT_AGE,
                String.valueOf(userModel.getExpectedRetirementAge()), ExportModel.CLIENT_DETAIL));
        exportModelList.add(new ExportModel(ExportConstant.EXPECTANCY_AGE,
                String.valueOf(userModel.getExpectancy()), ExportModel.CLIENT_DETAIL));
        exportModelList.add(new ExportModel(ExportConstant.CASHFLOW_AVAILABLE_AT_RETIREMENT,
                String.valueOf(userModel.getBalance()), ExportModel.ROW_VALUE_WITH_COLOR));
        exportModelList.add(new ExportModel(ExportConstant.AGE_WHERE_SHORTFALL_OCCURS,
                shortfallAge, ExportModel.CLIENT_DETAIL));

        //Milestone Details
        if (milestoneList.size() > 0) {
            exportModelList.add(new ExportModel(ExportConstant.MILESTONE_DETAILS, ExportModel.REPORT_HEADER));
            exportModelList.add(new ExportModel(ExportConstant.TOTAL_MILESTONE_COST,
                    String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                            .format(totalMilestoneCost)), ExportModel.REPORT_TOTAL_SECTION));

            for (HashMap<String, String> milestone : milestoneList) {
                String ageRange;
                if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(milestone.get(SQLConstants.MILESTONE_TABLE_MILESTONE_STATUS))) {
                    ageRange = milestone.get(SQLConstants.MILESTONE_TABLE_MILESTONE_AGE);
                } else {
                    ageRange = milestone.get(SQLConstants.MILESTONE_TABLE_MILESTONE_AGE) + " - " +
                            (Integer.valueOf(milestone.get(SQLConstants.MILESTONE_TABLE_MILESTONE_AGE)) +
                                    Integer.valueOf(milestone.get(SQLConstants.MILESTONE_TABLE_DURATION)) - 1);
                }

                exportModelList.add(new ExportModel(milestone.get(SQLConstants.MILESTONE_TABLE_MILESTONE_NAME),
                        milestone.get(SQLConstants.MILESTONE_TABLE_MILESTONE_TYPE),
                        ageRange, milestone.get(SQLConstants.MILESTONE_TABLE_MILESTONE_STATUS),
                        String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                                .format(Float.valueOf(milestone.get(SQLConstants.MILESTONE_TABLE_AMOUNT)) *
                                        Float.valueOf(milestone.get(SQLConstants.MILESTONE_TABLE_DURATION)))),
                        ExportModel.MILESTONE_DETAIL));
            }
        }

        //Existing Plan Details
        if (existingPlanList.size() > 0) {
            exportModelList.add(new ExportModel(ExportConstant.PLANS_EXISTING
                    .replace("$1", String.valueOf(existingPlanList.size())), ExportModel.REPORT_HEADER));
            exportModelList.add(new ExportModel(ExportConstant.TOTAL_COVERAGE,
                    String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                            .format(totalExistingCoverage)), ExportModel.REPORT_TOTAL_SECTION));


            for (HashMap<String, String> existingPlan : existingPlanList) {
                String premiumAgeRange;
                String payoutAgeRange;

                String payoutStatus = existingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_DURATION).equals("1") ?
                        ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME :
                        ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING;

                if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(existingPlan.get(SQLConstants.PLAN_TABLE_PAYMENT_TYPE))) {
                    premiumAgeRange = existingPlan.get(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE);
                } else {
                    premiumAgeRange = existingPlan.get(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE) + " - " +
                            (Integer.valueOf(existingPlan.get(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE)) +
                                    Integer.valueOf(existingPlan.get(SQLConstants.PLAN_TABLE_PLAN_DURATION)) - 1);
                }

                if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(payoutStatus)) {
                    payoutAgeRange = existingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_AGE);
                } else {
                    payoutAgeRange = existingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_AGE) + " - " +
                            (Integer.valueOf(existingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_AGE)) +
                                    Integer.valueOf(existingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_DURATION)) - 1);
                }

                List<ExportModel> planTypeModelList = new ArrayList<>();
                String[] planType = existingPlan.get(SQLConstants.PLAN_TABLE_PLAN_TYPE).split(", ");
                String[] allPlanType = getResources().getStringArray(R.array.plan_type_array);
                Arrays.sort(planType);
                Arrays.sort(allPlanType);

                for (String type : allPlanType) {
                    String planTypeAmount = "0";

                    for (String selectedType : planType) {
                        if (selectedType.equals(type)) {
                            planTypeAmount = existingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT);
                            break;
                        }
                    }
                    planTypeAmount = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US).format(Float.valueOf(planTypeAmount)));
                    planTypeModelList.add(new ExportModel(type, planTypeAmount, ExportModel.PLAN_TYPE_DETAIL));
                }

                exportModelList.add(new ExportModel(existingPlan.get(SQLConstants.PLAN_TABLE_PLAN_NAME),
                        premiumAgeRange, existingPlan.get(SQLConstants.PLAN_TABLE_PAYMENT_TYPE),
                        String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                                .format(Float.valueOf(existingPlan.get(SQLConstants.PLAN_TABLE_PAYMENT_AMOUNT)) *
                                        Float.valueOf(existingPlan.get(SQLConstants.PLAN_TABLE_PLAN_DURATION)))),
                        payoutAgeRange, payoutStatus, String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                        .format(Float.valueOf(existingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT)) *
                                Float.valueOf(existingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_DURATION)))),
                        planTypeModelList, ExportModel.PLAN_DETAIL));
            }
        }

        //Non-Existing Plan Details
        if (nonExistingPlanList.size() > 0) {
            exportModelList.add(new ExportModel(ExportConstant.PLANS_NON_EXISTING
                    .replace("$1", String.valueOf(nonExistingPlanList.size())), ExportModel.REPORT_HEADER));
            exportModelList.add(new ExportModel(ExportConstant.TOTAL_COVERAGE,
                    String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                            .format(totalNonExistingCoverage)), ExportModel.REPORT_TOTAL_SECTION));


            for (HashMap<String, String> nonExistingPlan : nonExistingPlanList) {
                List<ExportModel> planTypeModelList = new ArrayList<>();
                String premiumAgeRange;
                String payoutAgeRange;
                String[] planType = nonExistingPlan.get(SQLConstants.PLAN_TABLE_PLAN_TYPE).split(", ");
                String[] allPlanType = getResources().getStringArray(R.array.plan_type_array);
                Arrays.sort(planType);
                Arrays.sort(allPlanType);

                String payoutStatus = nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_DURATION).equals("1") ?
                        ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME :
                        ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING;

                if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYMENT_TYPE))) {
                    premiumAgeRange = nonExistingPlan.get(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE);
                } else {
                    premiumAgeRange = nonExistingPlan.get(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE) + " - " +
                            (Integer.valueOf(nonExistingPlan.get(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE)) +
                                    Integer.valueOf(nonExistingPlan.get(SQLConstants.PLAN_TABLE_PLAN_DURATION)) - 1);
                }

                if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(payoutStatus)) {
                    payoutAgeRange = nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_AGE);
                } else {
                    payoutAgeRange = nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_AGE) + " - " +
                            (Integer.valueOf(nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_AGE)) +
                                    Integer.valueOf(nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_DURATION)) - 1);
                }

                for (String type : allPlanType) {
                    String planTypeAmount = "0";

                    for (String selectedType : planType) {
                        if (selectedType.equals(type)) {
                            planTypeAmount = nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT);
                            break;
                        }
                    }
                    planTypeAmount = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US).format(Float.valueOf(planTypeAmount)));
                    planTypeModelList.add(new ExportModel(type, planTypeAmount, ExportModel.PLAN_TYPE_DETAIL));
                }

                exportModelList.add(new ExportModel(nonExistingPlan.get(SQLConstants.PLAN_TABLE_PLAN_NAME),
                        premiumAgeRange, nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYMENT_TYPE),
                        String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                                .format(Float.valueOf(nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYMENT_AMOUNT)) *
                                        Float.valueOf(nonExistingPlan.get(SQLConstants.PLAN_TABLE_PLAN_DURATION)))),
                        payoutAgeRange, payoutStatus, String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                        .format(Float.valueOf(nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT)) *
                                Float.valueOf(nonExistingPlan.get(SQLConstants.PLAN_TABLE_PAYOUT_DURATION)))),
                        planTypeModelList, ExportModel.PLAN_DETAIL));
            }
        }

        ExportAdapter exportAdapter = new ExportAdapter(exportModelList, this);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(exportAdapter);
    }
}