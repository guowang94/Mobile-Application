package com.example.android.graphapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
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
import com.example.android.graphapplication.constants.ErrorMsgConstants;
import com.example.android.graphapplication.constants.KeyConstants;
import com.example.android.graphapplication.constants.SQLConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.validations.Validation;

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

    private Validation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        mToolbar = findViewById(R.id.create_plan_toolbar);
        mPlanNameInputLayout = findViewById(R.id.plan_name_input_layout);
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
        validation = new Validation();

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
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        isChecked = new boolean[getResources().getTextArray(R.array.plan_type_array).length];

        mShowDialogButton.setOnClickListener(onClickListenerForShowDialog());

        // Default selection for Plan Status is One-Time therefore Plan Duration is set to disabled
        mPlanDurationInputLayout.setEnabled(false);
        mPlanDurationInputLayout.setAlpha(.5f);

        mPaymentTypeSegmentedButton.setOnPositionChangedListener(
                new SegmentedButtonGroup.OnPositionChangedListener() {
                    @Override
                    public void onPositionChanged(int position) {
                        Log.d(TAG, "onPositionChanged: " + position);
                        if (position == 0) {
                            mPlanDurationInputLayout.setEnabled(false);
                            mPlanDurationInputLayout.setAlpha(.5f);
                        } else {
                            mPlanDurationInputLayout.setEnabled(true);
                            mPlanDurationInputLayout.setAlpha(1f);
                        }
                    }
                });

        //Validation
        if (mPlanNameInputLayout.getEditText() != null)
            mPlanNameInputLayout.getEditText().setOnFocusChangeListener(
                    validation.onFocusChangeListenerForNameValidation(mPlanNameInputLayout));
        if (mPlanTypeInputLayout.getEditText() != null)
            mPlanTypeInputLayout.getEditText().setOnFocusChangeListener(
                    validation.onFocusChangeListenerForBlankFieldValidation(mPlanTypeInputLayout));
        if (mPaymentAmountInputLayout.getEditText() != null)
            mPaymentAmountInputLayout.getEditText().setOnFocusChangeListener(
                    validation.onFocusChangeListenerForNegativeValueValidation(mPaymentAmountInputLayout));
        if (mPlanDurationInputLayout.getEditText() != null)
            mPlanDurationInputLayout.getEditText().setOnFocusChangeListener(
                    validation.onFocusChangeListenerForNegativeValueValidation(mPlanDurationInputLayout));
        if (mPayoutAmountInputLayout.getEditText() != null)
            mPayoutAmountInputLayout.getEditText().setOnFocusChangeListener(
                    validation.onFocusChangeListenerForNegativeValueValidation(mPayoutAmountInputLayout));
        if (mPayoutDurationInputLayout.getEditText() != null)
            mPayoutDurationInputLayout.getEditText().setOnFocusChangeListener(
                    validation.onFocusChangeListenerForNegativeValueValidation(mPayoutDurationInputLayout));
        if (mPremiumStartAgeInputLayout.getEditText() != null)
            mPremiumStartAgeInputLayout.getEditText().setOnFocusChangeListener(onFocusChangeListenerForAgeValidation());
        if (mPayoutAgeInputLayout.getEditText() != null)
            mPayoutAgeInputLayout.getEditText().setOnFocusChangeListener(onFocusChangeListenerForAgeValidation());

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

        if (mPlanNameInputLayout.getEditText() != null)
            mPlanNameInputLayout.getEditText().setText(planName);
        if (mPlanTypeInputLayout.getEditText() != null)
            mPlanTypeInputLayout.getEditText().setText(planType);
        if (mPremiumStartAgeInputLayout.getEditText() != null)
            mPremiumStartAgeInputLayout.getEditText().setText(premiumStartAge);
        if (mPaymentAmountInputLayout.getEditText() != null)
            mPaymentAmountInputLayout.getEditText().setText(paymentAmount);
        if (mPayoutAgeInputLayout.getEditText() != null)
            mPayoutAgeInputLayout.getEditText().setText(payoutAge);
        if (mPayoutAmountInputLayout.getEditText() != null)
            mPayoutAmountInputLayout.getEditText().setText(payoutAmount);
        if (mPayoutDurationInputLayout.getEditText() != null)
            mPayoutDurationInputLayout.getEditText().setText(payoutDuration);

        if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equalsIgnoreCase(paymentType)) {
            mPaymentTypeSegmentedButton.setPosition(0);
        } else {
            mPaymentTypeSegmentedButton.setPosition(1);
            if (!planduration.equals("0")) {
                if (mPlanDurationInputLayout.getEditText() != null)
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
                //Validation
                validation.blankFieldValidation(mPlanNameInputLayout);
                validation.blankFieldValidation(mPlanTypeInputLayout);

                if (!validation.blankFieldValidation(mPremiumStartAgeInputLayout)) {
                    ageValidation();
                }

                if (!validation.blankFieldValidation(mPaymentAmountInputLayout)) {
                    validation.negativeValueValidation(mPaymentAmountInputLayout);
                }

                //If payment type is recurring then validate duration TextInputLayout
                if (mPaymentTypeSegmentedButton.getPosition() == 1) {
                    if (!validation.blankFieldValidation(mPlanDurationInputLayout)) {
                        validation.negativeValueValidation(mPlanDurationInputLayout);
                    }
                }

                if (!validation.blankFieldValidation(mPayoutAgeInputLayout)) {
                    validation.negativeValueValidation(mPayoutAgeInputLayout);
                }

                if (!validation.blankFieldValidation(mPayoutAmountInputLayout)) {
                    validation.negativeValueValidation(mPayoutAmountInputLayout);
                }

                if (!validation.blankFieldValidation(mPayoutDurationInputLayout)) {
                    validation.negativeValueValidation(mPayoutDurationInputLayout);
                }

                if (mPlanNameInputLayout.isErrorEnabled() || mPlanTypeInputLayout.isErrorEnabled() ||
                        mPremiumStartAgeInputLayout.isErrorEnabled() || mPaymentAmountInputLayout.isErrorEnabled() ||
                        mPlanDurationInputLayout.isErrorEnabled() || mPayoutAgeInputLayout.isErrorEnabled() ||
                        mPayoutAmountInputLayout.isErrorEnabled() || mPayoutDurationInputLayout.isErrorEnabled()) {
                    Snackbar.make(mLayout, ErrorMsgConstants.ERR_MSG_ENTER_VALID_INPUT,
                            Snackbar.LENGTH_LONG).show();
                } else {
                    String planName = null;
                    String planType = null;
                    int premiumStartAge = -1;
                    float paymentAmount = -1f;
                    int payoutAge = -1;
                    float payoutAmount = -1f;
                    int payoutDuration = -1;
                    String paymentType = mPaymentTypeSegmentedButton.getPosition() == 0 ?
                            ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME :
                            ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING;
                    String planStatus = mPlanStatusSegmentedButton.getPosition() == 0 ?
                            ScreenConstants.SEGMENTED_BUTTON_VALUE_EXISTING_PLAN :
                            ScreenConstants.SEGMENTED_BUTTON_VALUE_NON_EXISTING_PLAN;
                    int planDuration = 1;

                    if (mPlanNameInputLayout.getEditText() != null)
                        planName = mPlanNameInputLayout.getEditText().getText().toString();
                    if (mPlanTypeInputLayout.getEditText() != null)
                        planType = mPlanTypeInputLayout.getEditText().getText().toString();
                    if (mPremiumStartAgeInputLayout.getEditText() != null)
                        premiumStartAge = Integer.valueOf(mPremiumStartAgeInputLayout
                                .getEditText().getText().toString());
                    if (mPaymentAmountInputLayout.getEditText() != null)
                        paymentAmount = Float.valueOf(mPaymentAmountInputLayout
                                .getEditText().getText().toString());
                    if (mPayoutAgeInputLayout.getEditText() != null)
                        payoutAge = Integer.valueOf(mPayoutAgeInputLayout
                                .getEditText().getText().toString());
                    if (mPayoutAmountInputLayout.getEditText() != null)
                        payoutAmount = Float.valueOf(mPayoutAmountInputLayout
                                .getEditText().getText().toString());
                    if (mPayoutDurationInputLayout.getEditText() != null)
                        payoutDuration = Integer.valueOf(mPayoutDurationInputLayout
                                .getEditText().getText().toString());


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
                }
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                Log.i(TAG, "onOptionsItemSelected: In default");
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * This method will return a onClickListener to show dialog
     *
     * @return onClickListenerForShowDialog
     */
    private View.OnClickListener onClickListenerForShowDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize  readable sequence of char values
                final CharSequence[] dialogList = getResources().getTextArray(R.array.plan_type_array);
                final AlertDialog.Builder builderDialog = new AlertDialog.Builder(PlanActivity.this);
                builderDialog.setTitle("Select Plan Type");
//                int count = dialogList.length;

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

                        if (mPlanTypeInputLayout.getEditText() != null) {
                            mPlanTypeInputLayout.getEditText().setText(stringBuilder);
                        }
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
        };
    }

    /**
     * This method will validate the age input
     */
    private void ageValidation() {
        //validate premium start age
        try {
            if (mPremiumStartAgeInputLayout.getEditText() != null) {
                if (mPremiumStartAgeInputLayout.getEditText().getText().toString().isEmpty()) {
                    mPremiumStartAgeInputLayout.setErrorEnabled(false);
                } else if (Integer.valueOf(mPremiumStartAgeInputLayout.getEditText().getText().toString()) > 999) {
                    mPremiumStartAgeInputLayout.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_MORE_THAN_999);
                } else if (Integer.valueOf(mPremiumStartAgeInputLayout.getEditText().getText().toString()) < 18) {
                    mPremiumStartAgeInputLayout.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_LESS_THAN_18);
                } else {
                    mPremiumStartAgeInputLayout.setErrorEnabled(false);
                }
            }
        } catch (NumberFormatException e) {
            //Do nothing
        }

        //validate payout age
        try {
            if (mPayoutAgeInputLayout.getEditText() != null) {
                if (mPayoutAgeInputLayout.getEditText().getText().toString().isEmpty()) {
                    mPayoutAgeInputLayout.setErrorEnabled(false);
                } else if (Integer.valueOf(mPayoutAgeInputLayout.getEditText().getText().toString()) > 999) {
                    mPayoutAgeInputLayout.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_MORE_THAN_999);
                } else if (Integer.valueOf(mPayoutAgeInputLayout.getEditText().getText().toString()) < 18) {
                    mPayoutAgeInputLayout.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_LESS_THAN_18);
                }
//                else if (Integer.valueOf(mPayoutAgeInputLayout.getEditText().getText().toString()) < 999 &&
//                        Integer.valueOf(mPayoutAgeInputLayout.getEditText().getText().toString()) > 18) {
//                    mPayoutAgeInputLayout.setErrorEnabled(false);
//                }
                else {
                    mPayoutAgeInputLayout.setErrorEnabled(false);
                }

                if (!mPayoutAgeInputLayout.isErrorEnabled()) {
                    if (Integer.valueOf(mPayoutAgeInputLayout.getEditText().getText().toString()) <
                            Integer.valueOf(mPremiumStartAgeInputLayout.getEditText().getText().toString())) {
                        mPayoutAgeInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_PAYOUT_AGE);
                    } else {
                        mPayoutAgeInputLayout.setErrorEnabled(false);
                    }
                }
            }
        } catch (NumberFormatException e) {
            //Do nothing
        }
    }

    /**
     * This method will return onFocusChangeListener for age validation
     *
     * @return onFocusChangeListener
     */
    private View.OnFocusChangeListener onFocusChangeListenerForAgeValidation() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ageValidation();
                }
            }
        };
    }
}
