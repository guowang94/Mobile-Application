package com.example.android.graphapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.model.PlanModel;
import com.example.android.graphapplication.model.UserModel;
import com.example.android.graphapplication.validations.Validation;

import java.text.DecimalFormat;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class PlanActivity extends AppCompatActivity {

    private static final String TAG = "PlanActivity";

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private TextInputLayout mPlanNameInputLayout;
    private TextInputLayout mPlanTypeInputLayout;
    private TextInputLayout mPremiumStartAgeInputLayout;
    private TextInputLayout mPremiumAmountInputLayout;
    private TextInputLayout mPremiumDurationInputLayout;
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

    public static final String KEY_PLANE_NAME = "PLAN_NAME";
    public static final String KEY_PLAN_TYPE = "PLAN_TYPE";
    public static final String KEY_PREMIUM_START_AGE = "PREMIUM_START_AGE";
    public static final String KEY_PREMIUM_AMOUNT = "PREMIUM_AMOUNT";
    public static final String KEY_PREMIUM_DURATION = "PREMIUM_DURATION";
    public static final String KEY_PAYOUT_AGE = "PAYOUT_AGE";
    public static final String KEY_PAYOUT_AMOUNT = "PAYOUT_AMOUNT";
    public static final String KEY_PAYOUT_DURATION = "PAYOUT_DURATION";
    public static final String KEY_IS_CHECKED = "IS_CHECKED";
    public static final String KEY_PAYMENT_STATUS = "PAYMENT_STATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        mToolbar = findViewById(R.id.create_plan_toolbar);
        mPlanNameInputLayout = findViewById(R.id.plan_name_input_layout);
        mPlanTypeInputLayout = findViewById(R.id.plan_type_input_layout);
        mPremiumStartAgeInputLayout = findViewById(R.id.premium_start_age_input_layout);
        mPremiumAmountInputLayout = findViewById(R.id.premium_amount_input_layout);
        mPremiumDurationInputLayout = findViewById(R.id.premium_duration_input_layout);
        mPayoutAgeInputLayout = findViewById(R.id.payout_age_input_layout);
        mPayoutAmountInputLayout = findViewById(R.id.payout_amount_input_layout);
        mPayoutDurationInputLayout = findViewById(R.id.payout_duration_input_layout);
        mPaymentTypeSegmentedButton = findViewById(R.id.payment_type_segmented_button);
        mPlanStatusSegmentedButton = findViewById(R.id.plan_status_segmented_button);
        mShowDialogButton = findViewById(R.id.showDialogButton);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mLayout = findViewById(R.id.layout);

        if (getIntent() != null) {
            planAction = getIntent().getStringExtra(KeyConstants.INTENT_KEY_ACTION);
            currentPlanID = getIntent().getIntExtra(
                    KeyConstants.INTENT_KEY_RECYCLER_VIEW_POSITION, -1);
            Log.d(TAG, "onCreate: displayData, " + currentPlanID);
        }

        if (savedInstanceState != null) {
            mPlanNameInputLayout.getEditText().setText(savedInstanceState.getString(KEY_PLANE_NAME));
            mPlanTypeInputLayout.getEditText().setText(savedInstanceState.getString(KEY_PLAN_TYPE));
            mPaymentTypeSegmentedButton.setPosition(savedInstanceState.getInt(KEY_PAYMENT_STATUS));
            isChecked = savedInstanceState.getBooleanArray(KEY_IS_CHECKED);
            mPremiumStartAgeInputLayout.getEditText().setText(savedInstanceState.getString(KEY_PREMIUM_START_AGE));
            mPremiumAmountInputLayout.getEditText().setText(savedInstanceState.getString(KEY_PREMIUM_AMOUNT));
            mPayoutAgeInputLayout.getEditText().setText(savedInstanceState.getString(KEY_PAYOUT_AGE));
            mPayoutAmountInputLayout.getEditText().setText(savedInstanceState.getString(KEY_PAYOUT_AMOUNT));
            mPayoutDurationInputLayout.getEditText().setText(savedInstanceState.getString(KEY_PAYOUT_DURATION));
            if (mPaymentTypeSegmentedButton.getPosition() == 1) {
                mPremiumDurationInputLayout.getEditText().setText(savedInstanceState.getString(KEY_PREMIUM_DURATION));
            }

            Log.d(TAG, "onCreate: premium duration " + savedInstanceState.getString(KEY_PREMIUM_DURATION) + ", position " + mPaymentTypeSegmentedButton.getPosition());
        }

        initData();
        Log.d(TAG, "onCreate: out");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_PLANE_NAME, mPlanNameInputLayout.getEditText().getText().toString());
        outState.putString(KEY_PLAN_TYPE, mPlanTypeInputLayout.getEditText().getText().toString());
        outState.putBooleanArray(KEY_IS_CHECKED, isChecked);
        outState.putString(KEY_PREMIUM_START_AGE, mPremiumStartAgeInputLayout.getEditText().getText().toString());
        outState.putString(KEY_PREMIUM_AMOUNT, mPremiumAmountInputLayout.getEditText().getText().toString());
        outState.putString(KEY_PAYOUT_AGE, mPayoutAgeInputLayout.getEditText().getText().toString());
        outState.putString(KEY_PAYOUT_AMOUNT, mPayoutAmountInputLayout.getEditText().getText().toString());
        outState.putString(KEY_PAYOUT_DURATION, mPayoutDurationInputLayout.getEditText().getText().toString());
        outState.putInt(KEY_PAYMENT_STATUS, mPaymentTypeSegmentedButton.getPosition());
        if (mPaymentTypeSegmentedButton.getPosition() == 1) {
            outState.putString(KEY_PREMIUM_DURATION, mPremiumDurationInputLayout.getEditText().getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        Log.d(TAG, "initData: in");
        mydb = new DBHelper(getApplicationContext());
        validation = new Validation();

        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this mToolbar
        ActionBar actionBar = getSupportActionBar();
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_ADD_PLAN);

        // Enable the top left button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        isChecked = isChecked != null ? isChecked :
                new boolean[getResources().getTextArray(R.array.plan_type_array).length];

        mShowDialogButton.setOnClickListener(onClickListenerForShowDialog());

        // Default selection for Plan Status is One-Time therefore Plan Duration is set to disabled
        if (mPaymentTypeSegmentedButton.getPosition() == 0) {
            mPremiumDurationInputLayout.setEnabled(false);
            mPremiumDurationInputLayout.setAlpha(.5f);
        }

        mPaymentTypeSegmentedButton.setOnPositionChangedListener(
                new SegmentedButtonGroup.OnPositionChangedListener() {
                    @Override
                    public void onPositionChanged(int position) {
                        Log.d(TAG, "onPositionChanged: " + position);
                        if (position == 0) {
                            mPremiumDurationInputLayout.setEnabled(false);
                            mPremiumDurationInputLayout.setAlpha(.5f);
                        } else {
                            mPremiumDurationInputLayout.setEnabled(true);
                            mPremiumDurationInputLayout.setAlpha(1f);
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
        if (mPremiumAmountInputLayout.getEditText() != null)
            mPremiumAmountInputLayout.getEditText().setOnFocusChangeListener(
                    validation.onFocusChangeListenerForNegativeValueValidation(mPremiumAmountInputLayout));
        if (mPayoutAmountInputLayout.getEditText() != null)
            mPayoutAmountInputLayout.getEditText().setOnFocusChangeListener(
                    validation.onFocusChangeListenerForNegativeValueValidation(mPayoutAmountInputLayout));

        if (mPremiumDurationInputLayout.getEditText() != null)
            mPremiumDurationInputLayout.getEditText().setOnFocusChangeListener(
                    onFocusChangeListenerForDurationValidation());
        if (mPayoutDurationInputLayout.getEditText() != null)
            mPayoutDurationInputLayout.getEditText().setOnFocusChangeListener(
                    onFocusChangeListenerForDurationValidation());
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

        PlanModel planModel = mydb.getPlanDetails(currentPlanID);

        String planName = planModel.getPlanName();
        String planType = planModel.getPlanType();
        String paymentType = planModel.getPaymentType();
        String premiumStartAge = String.valueOf(planModel.getPremiumStartAge());
        String planduration = String.valueOf(planModel.getPlanDuration());
        String payoutAge = String.valueOf(planModel.getPayoutAge());
        String payoutDuration = String.valueOf(planModel.getPayoutDuration());
        String planStatus = planModel.getPlanStatus();

        Log.d(TAG, "displayData: Plan name: " + planName);

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        String paymentAmount = df.format(planModel.getPaymentAmount());
        String payoutAmount = df.format(planModel.getPayoutAmount());


        if (mPlanNameInputLayout.getEditText() != null)
            mPlanNameInputLayout.getEditText().setText(planName);
        if (mPlanTypeInputLayout.getEditText() != null)
            mPlanTypeInputLayout.getEditText().setText(planType);
        if (mPremiumStartAgeInputLayout.getEditText() != null)
            mPremiumStartAgeInputLayout.getEditText().setText(premiumStartAge);
        if (mPremiumAmountInputLayout.getEditText() != null)
            mPremiumAmountInputLayout.getEditText().setText(paymentAmount);
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
                if (mPremiumDurationInputLayout.getEditText() != null)
                    mPremiumDurationInputLayout.getEditText().setText(planduration);
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

                if (!validation.blankFieldValidation(mPremiumAmountInputLayout)) {
                    validation.negativeValueValidation(mPremiumAmountInputLayout);
                }

                //If payment type is recurring then validate duration TextInputLayout
                if (mPaymentTypeSegmentedButton.getPosition() == 1) {
                    if (!validation.blankFieldValidation(mPremiumDurationInputLayout)) {
                        durationValidation();
                    }
                }

                if (!validation.blankFieldValidation(mPayoutAgeInputLayout)) {
                    ageValidation();
                }

                if (!validation.blankFieldValidation(mPayoutAmountInputLayout)) {
                    validation.negativeValueValidation(mPayoutAmountInputLayout);
                }

                if (!validation.blankFieldValidation(mPayoutDurationInputLayout)) {
                    durationValidation();
                }

                if (mPlanNameInputLayout.isErrorEnabled() || mPlanTypeInputLayout.isErrorEnabled() ||
                        mPremiumStartAgeInputLayout.isErrorEnabled() || mPremiumAmountInputLayout.isErrorEnabled() ||
                        mPremiumDurationInputLayout.isErrorEnabled() || mPayoutAgeInputLayout.isErrorEnabled() ||
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
                    if (mPremiumAmountInputLayout.getEditText() != null)
                        paymentAmount = Float.valueOf(mPremiumAmountInputLayout
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
                        planDuration = Integer.valueOf(mPremiumDurationInputLayout
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
                        // make selected item in the comma separated string
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
        UserModel userModel = mydb.getAllUser().get(0);
        int expectancy = userModel.getExpectancy();

        //validate premium start age
        try {
            if (mPremiumStartAgeInputLayout.getEditText() != null) {
                if (mPremiumStartAgeInputLayout.getEditText().getText().toString().isEmpty()) {
                    mPremiumStartAgeInputLayout.setErrorEnabled(false);
                } else if (Integer.valueOf(mPremiumStartAgeInputLayout.getEditText().getText().toString()) > expectancy) {
                    mPremiumStartAgeInputLayout.getEditText().setText(String.valueOf(expectancy));
                    Snackbar.make(mLayout, ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_MORE_THAN_EXPECTANCY,
                            Snackbar.LENGTH_LONG).show();
                    mPayoutAgeInputLayout.setErrorEnabled(false);
                } else if (Integer.valueOf(mPremiumStartAgeInputLayout.getEditText().getText().toString()) < 0) {
                    mPremiumStartAgeInputLayout.setError(ErrorMsgConstants.ERR_MSG_PREMIUM_AGE_CANNOT_BE_LESS_THAN_0);
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
                } else if (Integer.valueOf(mPayoutAgeInputLayout.getEditText().getText().toString()) < 1) {
                    mPayoutAgeInputLayout.setError(ErrorMsgConstants.ERR_MSG_PAYOUT_AGE_CANNOT_BE_LESS_THAN_1);
                } else if (Integer.valueOf(mPayoutAgeInputLayout.getEditText().getText().toString()) > expectancy) {
                    mPayoutAgeInputLayout.getEditText().setText(String.valueOf(expectancy));
                    Snackbar.make(mLayout, ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_MORE_THAN_EXPECTANCY,
                            Snackbar.LENGTH_LONG).show();
                    mPayoutAgeInputLayout.setErrorEnabled(false);
                } else if (Integer.valueOf(mPayoutAgeInputLayout.getEditText().getText().toString()) <
                        Integer.valueOf(mPremiumStartAgeInputLayout.getEditText().getText().toString())) {
                    mPayoutAgeInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_PAYOUT_AGE);
                } else {
                    mPayoutAgeInputLayout.setErrorEnabled(false);
                }
            }
        } catch (NumberFormatException e) {
            //Do nothing
        }
    }

    /**
     * This method will validate duration
     */
    private void durationValidation() {
        UserModel userModel = mydb.getAllUser().get(0);
        int expectancy = userModel.getExpectancy();
        int premiumAge = 0;
        int payoutAge = 0;

        if (mPremiumStartAgeInputLayout.getEditText() != null) {
            if (!mPremiumStartAgeInputLayout.getEditText().getText().toString().isEmpty())
                premiumAge = Integer.valueOf(mPremiumStartAgeInputLayout.getEditText().getText().toString());
        }

        if (mPayoutAgeInputLayout.getEditText() != null) {
            if (!mPayoutAgeInputLayout.getEditText().getText().toString().isEmpty())
                payoutAge = Integer.valueOf(mPayoutAgeInputLayout.getEditText().getText().toString());
        }

        //validate premium duration age
        try {
            if (mPremiumDurationInputLayout.getEditText() != null) {
                if (mPremiumDurationInputLayout.getEditText().getText().toString().isEmpty()) {
                    mPremiumDurationInputLayout.setErrorEnabled(false);
                } else if (Integer.valueOf(mPremiumDurationInputLayout.getEditText().getText().toString()) < 1) {
                    mPremiumDurationInputLayout.setError(ErrorMsgConstants.ERR_MSG_DURATION_CANNOT_BE_LESS_THAN_1);
                } else if (premiumAge + Integer.valueOf(mPremiumDurationInputLayout.getEditText().getText().toString()) > expectancy) {
                    Snackbar.make(mLayout, ErrorMsgConstants.ERR_MSG_DURATION_CANNOT_EXCEED_EXPECTANCY,
                            Snackbar.LENGTH_LONG).show();
                    int duration = expectancy - premiumAge;
                    duration = duration == 0 ? 1 : duration;
                    mPremiumDurationInputLayout.getEditText().setText(String.valueOf(duration));
                    mPremiumDurationInputLayout.setErrorEnabled(false);
                } else {
                    mPremiumDurationInputLayout.setErrorEnabled(false);
                }
            }
        } catch (NumberFormatException e) {
            //Do nothing
        }

        //validate payout duration age
        try {
            if (mPayoutDurationInputLayout.getEditText() != null) {
                if (mPayoutDurationInputLayout.getEditText().getText().toString().isEmpty()) {
                    mPayoutDurationInputLayout.setErrorEnabled(false);
                } else if (Integer.valueOf(mPayoutDurationInputLayout.getEditText().getText().toString()) < 1) {
                    mPayoutDurationInputLayout.setError(ErrorMsgConstants.ERR_MSG_DURATION_CANNOT_BE_LESS_THAN_1);
                } else if (payoutAge + Integer.valueOf(mPayoutDurationInputLayout.getEditText().getText().toString()) > expectancy) {
                    Snackbar.make(mLayout, ErrorMsgConstants.ERR_MSG_DURATION_CANNOT_EXCEED_EXPECTANCY,
                            Snackbar.LENGTH_LONG).show();
                    int duration = expectancy - payoutAge;
                    duration = duration == 0 ? 1 : duration;
                    mPayoutDurationInputLayout.getEditText().setText(String.valueOf(duration));
                    mPayoutDurationInputLayout.setErrorEnabled(false);
                } else {
                    mPayoutDurationInputLayout.setErrorEnabled(false);
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

    /**
     * This method will return onFocusChangeListener for duration validation
     *
     * @return onFocusChangeListener
     */
    private View.OnFocusChangeListener onFocusChangeListenerForDurationValidation() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    durationValidation();
                }
            }
        };
    }
}
