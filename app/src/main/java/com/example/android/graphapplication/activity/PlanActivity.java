package com.example.android.graphapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.constants.KeyConstants;
import com.example.android.graphapplication.constants.SQLConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class PlanActivity extends AppCompatActivity {

    private static final String TAG = "PlanActivity";

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private TextInputLayout mPlanNameInputLayout;
    private TextInputLayout mPlanTypeInputLayout;
    private TextInputLayout mPremiumStartAgeInputLayout;
    private TextInputLayout mPaymentAmountInputLayout;
    private TextInputLayout mPlanDurationInputLayout;
    private TextInputLayout mPayoutAgeInputLayout;
    private TextInputLayout mPayoutAmountInputLayout;
    private TextInputLayout mPayoutDurationInputLayout;
    private SegmentedButtonGroup mPaymentTypeSegmentedButton;
    private SegmentedButtonGroup mPlanStatusSegmentedButton;
    private Button mShowDialogButton;
    private ConstraintLayout mLayout;
    private DBHelper mydb;

    private String planAction;
    private int currentPlanID;
    private boolean[] isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        //TODO need to add validation for all the fields (Low Priority)

        mToolbar = findViewById(R.id.create_plan_toolbar);
        mPlanNameInputLayout = findViewById(R.id.name_input_layout);
        mPlanTypeInputLayout = findViewById(R.id.plan_type_input_layout);
        mPremiumStartAgeInputLayout = findViewById(R.id.premium_start_age_input_layout);
        mPaymentAmountInputLayout = findViewById(R.id.payment_amount_input_layout);
        mPlanDurationInputLayout = findViewById(R.id.plan_duration_input_layout);
        mPayoutAgeInputLayout = findViewById(R.id.payout_age_input_layout);
        mPayoutAmountInputLayout = findViewById(R.id.payout_amount_input_layout);
        mPayoutDurationInputLayout = findViewById(R.id.payout_duration_input_layout);
        mPaymentTypeSegmentedButton = findViewById(R.id.payment_type_segmented_button);
        mPlanStatusSegmentedButton = findViewById(R.id.plan_status_segmented_button);
        mShowDialogButton = findViewById(R.id.showDialogButton);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mLayout = findViewById(R.id.layout);

        mydb = new DBHelper(getApplicationContext());

        if (getIntent() != null) {
            planAction = getIntent().getStringExtra(KeyConstants.INTENT_KEY_ACTION);
            currentPlanID = getIntent().getIntExtra(
                    KeyConstants.INTENT_KEY_RECYCLER_VIEW_POSITION, -1);
            Log.d(TAG, "onCreate: displayData, " + currentPlanID);
        }

        initData();
        Log.d(TAG, "onCreate: out");
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        Log.d(TAG, "initData: in");
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this mToolbar
        ActionBar actionBar = getSupportActionBar();
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_ADD_PLAN);

        // Enable the top left button
        actionBar.setDisplayHomeAsUpEnabled(true);

        isChecked = new boolean[getResources().getTextArray(R.array.plan_type_array).length];

        mShowDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intialize  readable sequence of char values
                final CharSequence[] dialogList = getResources().getTextArray(R.array.plan_type_array);
                final AlertDialog.Builder builderDialog = new AlertDialog.Builder(PlanActivity.this);
                builderDialog.setTitle("Select Plan Type");
                int count = dialogList.length;

                // Creating multiple selection by using setMutliChoiceItem method
                builderDialog.setMultiChoiceItems(dialogList, isChecked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton, boolean isChecked) {
                            }
                        });

                builderDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView list = ((AlertDialog) dialog).getListView();
                        // make selected item in the comma seprated string
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < list.getCount(); i++) {
                            boolean checked = list.isItemChecked(i);
                            isChecked[i] = list.isItemChecked(i);


                            if (checked) {
                                if (stringBuilder.length() > 0) {
                                    stringBuilder.append(", ");
                                }
                                stringBuilder.append(list.getItemAtPosition(i));
                            }
                        }

                        mPlanTypeInputLayout.getEditText().setText(stringBuilder);

                    }
                });
                builderDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                });
                AlertDialog alert = builderDialog.create();
                alert.show();
            }
        });

        // Default selection for Plan Status is One-Time therefore Plan Duration is set to disabled
        mPlanDurationInputLayout.setEnabled(false);
        //fixme the hint color does not change. will do this later
//        mPlanDurationInputLayout.setHintTextAppearance(R.style.inactive_input_layout_hint_color);
//        mPlanDurationInputLayout.getEditText().setTextColor(getResources().getColor(R.color.light_gray));
//        mPlanDurationInputLayout.getEditText().setHintTextColor(getResources().getColor(R.color.light_gray));
//        mPlanDurationInputLayout.getEditText().setHighlightColor(getResources().getColor(R.color.light_gray));

        mPaymentTypeSegmentedButton.setOnPositionChangedListener(
                new SegmentedButtonGroup.OnPositionChangedListener() {
                    @Override
                    public void onPositionChanged(int position) {
                        Log.d(TAG, "onPositionChanged: " + position);
                        if (position == 0) {
                            mPlanDurationInputLayout.setEnabled(false);
//                            mPlanDurationInputLayout.getEditText().setTextColor(getResources().getColor(R.color.light_gray));
//                            mPlanDurationInputLayout.getEditText().setHintTextColor(getResources().getColor(R.color.light_gray));
//                            mPlanDurationInputLayout.getEditText().setHighlightColor(getResources().getColor(R.color.light_gray));
                        } else {
                            mPlanDurationInputLayout.setEnabled(true);
//                            mPlanDurationInputLayout.getEditText().setTextColor(getResources().getColor(R.color.formColor));
//                            mPlanDurationInputLayout.getEditText().setHintTextColor(getResources().getColor(R.color.formColor));
//                            mPlanDurationInputLayout.getEditText().setHighlightColor(getResources().getColor(R.color.formColor));
//                            mPlanDurationInputLayout.getEditText().setHintTextColor(getResources().getColor(R.color.light_gray));
//                            mPlanDurationInputLayout.getEditText().setHighlightColor(getResources().getColor(R.color.light_gray));
//                            mPlanDurationInputLayout.setHintTextAppearance(R.style.inactive_input_layout_hint_color);

                        }
                    }
                });

        if (KeyConstants.INTENT_KEY_VALUE_EDIT.equalsIgnoreCase(planAction) && currentPlanID != -1) {
            displayData();
        }
        Log.d(TAG, "initData: out");
    }

    /**
     * This method will display previous values of the event
     */
    private void displayData() {
        Log.d(TAG, "displayData: in");

        Cursor rs = mydb.getData(SQLConstants.PLAN_TABLE, currentPlanID);
        rs.moveToFirst();

        String planName = rs.getString(rs.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_NAME));
        String planType = rs.getString(rs.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_TYPE));
        String paymentType = rs.getString(rs.getColumnIndex(SQLConstants.PLAN_TABLE_PAYMENT_TYPE));
        String premiumStartAge = String.valueOf(rs.getInt(rs.getColumnIndex(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE)));
        String paymentAmount = String.valueOf(rs.getFloat(rs.getColumnIndex(SQLConstants.PLAN_TABLE_PAYMENT_AMOUNT)));
        String planduration = String.valueOf(rs.getInt(rs.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_DURATION)));
        String payoutAge = String.valueOf(rs.getInt(rs.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_AGE)));
        String payoutAmount = String.valueOf(rs.getFloat(rs.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT)));
        String payoutDuration = String.valueOf(rs.getInt(rs.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_DURATION)));
        String planStatus = rs.getString(rs.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_STATUS));


        if (!rs.isClosed()) {
            rs.close();
        }

        Log.d(TAG, "displayData: " + planName);

        mPlanNameInputLayout.getEditText().setText(planName);
        mPlanTypeInputLayout.getEditText().setText(planType);
        mPremiumStartAgeInputLayout.getEditText().setText(premiumStartAge);
        mPaymentAmountInputLayout.getEditText().setText(paymentAmount);
        mPayoutAgeInputLayout.getEditText().setText(payoutAge);
        mPayoutAmountInputLayout.getEditText().setText(payoutAmount);
        mPayoutDurationInputLayout.getEditText().setText(payoutDuration);

        if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equalsIgnoreCase(paymentType)) {
            mPaymentTypeSegmentedButton.setPosition(0);
        } else {
            mPaymentTypeSegmentedButton.setPosition(1);
            if (!planduration.equals("0")) {
                mPlanDurationInputLayout.getEditText().setText(planduration);
            }
        }

        if (ScreenConstants.SEGMENTED_BUTTON_VALUE_EXISTING_PLAN.equals(planStatus)) {
            mPlanStatusSegmentedButton.setPosition(0);
        } else {
            mPlanStatusSegmentedButton.setPosition(1);
        }

        CharSequence[] allPlanTypes = getResources().getTextArray(R.array.plan_type_array);
        String[] planTypes = planType.split(", ");
        for (int i = 0; i < allPlanTypes.length; i++) {
            for (String plan : planTypes) {
                if (allPlanTypes[i].toString().equals(plan)) {
                    isChecked[i] = true;
                }
            }
        }

        Log.d(TAG, "displayData: out");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra(
                KeyConstants.INTENT_KEY_FRAGMENT_POSITION, 3));
    }

    /**
     * This method will create the more option in the action bar
     *
     * @param menu store all the menu items
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
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
            case R.id.save:
                String planName = mPlanNameInputLayout.getEditText().getText().toString();
                String planType = mPlanTypeInputLayout.getEditText().getText().toString();
                String paymentType = mPaymentTypeSegmentedButton.getPosition() == 0 ?
                        ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME :
                        ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING;
                int premiumStartAge = Integer.valueOf(mPremiumStartAgeInputLayout
                        .getEditText().getText().toString());
                float paymentAmount = Float.valueOf(mPaymentAmountInputLayout
                        .getEditText().getText().toString());
                int payoutAge = Integer.valueOf(mPayoutAgeInputLayout
                        .getEditText().getText().toString());
                float payoutAmount = Float.valueOf(mPayoutAmountInputLayout
                        .getEditText().getText().toString());
                int payoutDuration = Integer.valueOf(mPayoutDurationInputLayout
                        .getEditText().getText().toString());
                String planStatus = mPlanStatusSegmentedButton.getPosition() == 0 ?
                        ScreenConstants.SEGMENTED_BUTTON_VALUE_EXISTING_PLAN :
                        ScreenConstants.SEGMENTED_BUTTON_VALUE_NON_EXISTING_PLAN;
                int planDuration = 0;


                if (ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING.equals(paymentType)) {
                    planDuration = Integer.valueOf(mPlanDurationInputLayout
                            .getEditText().getText().toString());
                }

                if (KeyConstants.INTENT_KEY_VALUE_CREATE.equalsIgnoreCase(planAction)) {

                    mydb.insertPlan(planName, planType, paymentType, premiumStartAge, paymentAmount,
                            planDuration, payoutAge, payoutAmount, payoutDuration, planStatus);

                } else if (KeyConstants.INTENT_KEY_VALUE_EDIT.equalsIgnoreCase(planAction)) {

                    mydb.updatePlan(currentPlanID, planName, planType, paymentType, premiumStartAge,
                            paymentAmount, planDuration, payoutAge, payoutAmount, payoutDuration, planStatus);
                }

                startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra(
                        KeyConstants.INTENT_KEY_FRAGMENT_POSITION, 3));
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                Log.i(TAG, "onOptionsItemSelected: In default");
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
