package com.example.android.graphapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.constants.ErrorMsgConstants;
import com.example.android.graphapplication.constants.SQLConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.validations.Validation;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class FormActivity extends AppCompatActivity {

    private static final String TAG = "FormActivity";
    private TextInputLayout mNameInput;
    private TextInputLayout mAgeInput;
    private TextInputLayout mAssets;
    private TextInputLayout mIncomeInput;
    private TextInputLayout mExpensesInput;
    private TextInputLayout mRetirementAgeInput;
    private TextInputLayout mExpectancyInput;
    private TextInputLayout mIncrementInput;
    private TextInputLayout mInflationInput;
    private SegmentedButtonGroup mEmploymentStatusSegmentedButton;
    private SegmentedButtonGroup mCitizenshipSegmentedButton;
    private Button mComputeButton;
    private Toolbar mToolbar;
    private ConstraintLayout mLayout;
    private TextView mToolbarTitle;

    private Validation validation;

    public static final String KEY_NAME = "NAME";
    public static final String KEY_AGE = "AGE";
    public static final String KEY_RETIREMENT_AGE = "RETIREMENT_AGE";
    public static final String KEY_EXPECTANCY = "EXPECTANCY";
    public static final String KEY_INCOME = "INCOME";
    public static final String KEY_INCREMENT = "INCREMENT";
    public static final String KEY_EXPENSES = "EXPENSES";
    public static final String KEY_INFLATION = "INFLATION";
    public static final String KEY_ASSETS = "ASSETS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mNameInput = findViewById(R.id.user_name_input_layout);
        mAgeInput = findViewById(R.id.age_input_layout);
        mAssets = findViewById(R.id.assets_input_layout);
        mIncomeInput = findViewById(R.id.income_input_layout);
        mExpensesInput = findViewById(R.id.expenses_input_layout);
        mRetirementAgeInput = findViewById(R.id.retirement_age_input_layout);
        mExpectancyInput = findViewById(R.id.expectancy_input_layout);
        mIncrementInput = findViewById(R.id.increment_input_layout);
        mInflationInput = findViewById(R.id.inflation_input_layout);
        mEmploymentStatusSegmentedButton = findViewById(R.id.employment_status_segmented_button);
        mCitizenshipSegmentedButton = findViewById(R.id.citizenship_segmented_button);
        mComputeButton = findViewById(R.id.compute_button);
        mToolbar = findViewById(R.id.create_event_toolbar);
        mLayout = findViewById(R.id.layout);
        mToolbarTitle = findViewById(R.id.toolbar_title);

        if (savedInstanceState != null) {
            mNameInput.getEditText().setText(savedInstanceState.getString(KEY_NAME));
            mAgeInput.getEditText().setText(savedInstanceState.getString(KEY_AGE));
            mRetirementAgeInput.getEditText().setText(savedInstanceState.getString(KEY_RETIREMENT_AGE));
            mExpectancyInput.getEditText().setText(savedInstanceState.getString(KEY_EXPECTANCY));
            mIncomeInput.getEditText().setText(savedInstanceState.getString(KEY_INCOME));
            mIncrementInput.getEditText().setText(savedInstanceState.getString(KEY_INCREMENT));
            mExpensesInput.getEditText().setText(savedInstanceState.getString(KEY_EXPENSES));
            mInflationInput.getEditText().setText(savedInstanceState.getString(KEY_INFLATION));
            mAssets.getEditText().setText(savedInstanceState.getString(KEY_ASSETS));
        }

        //todo static text to be commented
//        mNameInput.getEditText().setText("wang");
//        mAgeInput.getEditText().setText("40");
//        mRetirementAgeInput.getEditText().setText("60");
//        mExpectancyInput.getEditText().setText("80");
//        mIncomeInput.getEditText().setText("2000");
//        mIncrementInput.getEditText().setText("10");
//        mExpensesInput.getEditText().setText("1230");
//        mInflationInput.getEditText().setText("2");
//        mAssets.getEditText().setText("12345");

        initData();
        Log.d(TAG, "onCreate: out");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_NAME, mNameInput.getEditText().getText().toString());
        outState.putString(KEY_AGE, mAgeInput.getEditText().getText().toString());
        outState.putString(KEY_RETIREMENT_AGE, mRetirementAgeInput.getEditText().getText().toString());
        outState.putString(KEY_EXPECTANCY, mExpectancyInput.getEditText().getText().toString());
        outState.putString(KEY_INCOME, mIncomeInput.getEditText().getText().toString());
        outState.putString(KEY_INCREMENT, mIncrementInput.getEditText().getText().toString());
        outState.putString(KEY_EXPENSES, mExpensesInput.getEditText().getText().toString());
        outState.putString(KEY_INFLATION, mInflationInput.getEditText().getText().toString());
        outState.putString(KEY_ASSETS, mAssets.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    /**
     * This method will setup toolbar and set onClickListener
     */
    private void initData() {
        validation = new Validation();

        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this toolbar
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_ENTER_USER_DETAILS);

        // Set onFocusChangeListener
        if (mNameInput.getEditText() != null)
            mNameInput.getEditText().setOnFocusChangeListener(validation.onFocusChangeListenerForNameValidation(mNameInput));
        if (mAgeInput.getEditText() != null)
            mAgeInput.getEditText().setOnFocusChangeListener(onFocusChangeListenerForAgeValidation());
        if (mAssets.getEditText() != null)
            mAssets.getEditText().setOnFocusChangeListener(validation.onFocusChangeListenerForNegativeValueValidation(mAssets));
        if (mIncomeInput.getEditText() != null)
            mIncomeInput.getEditText().setOnFocusChangeListener(validation.onFocusChangeListenerForNegativeValueValidation(mIncomeInput));
        if (mExpensesInput.getEditText() != null)
            mExpensesInput.getEditText().setOnFocusChangeListener(validation.onFocusChangeListenerForNegativeValueValidation(mExpensesInput));
        if (mRetirementAgeInput.getEditText() != null)
            mRetirementAgeInput.getEditText().setOnFocusChangeListener(onFocusChangeListenerForAgeValidation());
        if (mExpectancyInput.getEditText() != null)
            mExpectancyInput.getEditText().setOnFocusChangeListener(onFocusChangeListenerForAgeValidation());
        if (mIncrementInput.getEditText() != null)
            mIncrementInput.getEditText().setOnFocusChangeListener(validation.onFocusChangeListenerForNegativeValueValidation(mIncrementInput));
        if (mInflationInput.getEditText() != null)
            mInflationInput.getEditText().setOnFocusChangeListener(validation.onFocusChangeListenerForNegativeValueValidation(mInflationInput));

        mComputeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validation.blankFieldValidation(mNameInput)) {
                    //Double check Name Input
                    validation.nameValidation(mNameInput);
                }

                if (!validation.blankFieldValidation(mAgeInput)) {
                    //Double check Age Input
                    ageValidation();
                }

                if (!validation.blankFieldValidation(mRetirementAgeInput)) {
                    //Double check Retirement Age Input
                    ageValidation();
                }

                if (!validation.blankFieldValidation(mExpectancyInput)) {
                    //Double check Expectancy Input
                    ageValidation();
                }

                if (!validation.blankFieldValidation(mAssets)) {
                    //Double check Assets Input
                    validation.negativeValueValidation(mAssets);
                }

                if (!validation.blankFieldValidation(mExpensesInput)) {
                    //Double check Expenses Input
                    validation.negativeValueValidation(mExpensesInput);
                }

                if (!validation.blankFieldValidation(mIncomeInput)) {
                    //Double check Income Input
                    validation.negativeValueValidation(mIncomeInput);
                }

                if (!validation.blankFieldValidation(mIncrementInput)) {
                    //Double check Increment Input
                    validation.negativeValueValidation(mIncrementInput);
                }

                if (!validation.blankFieldValidation(mInflationInput)) {
                    //Double check Inflation Input
                    validation.negativeValueValidation(mInflationInput);
                }

                //Check if any of the EditText has error
                if (mNameInput.isErrorEnabled() || mIncomeInput.isErrorEnabled() ||
                        mExpectancyInput.isErrorEnabled() || mAgeInput.isErrorEnabled() ||
                        mAssets.isErrorEnabled() || mExpensesInput.isErrorEnabled() ||
                        mRetirementAgeInput.isErrorEnabled() || mIncrementInput.isErrorEnabled() ||
                        mInflationInput.isErrorEnabled()) {

                    Snackbar snackbar = Snackbar.make(mLayout, ErrorMsgConstants.ERR_MSG_ENTER_VALID_INPUT,
                            Snackbar.LENGTH_LONG);

                    TextView textView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);

                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    snackbar.show();
                } else {
                    String employmentStatus = mEmploymentStatusSegmentedButton.getPosition() == 0
                            ? ScreenConstants.SEGMENTED_BUTTON_VALUE_EMPLOYED
                            : ScreenConstants.SEGMENTED_BUTTON_VALUE_SELF_EMPLOYED;
                    String citizenship = mCitizenshipSegmentedButton.getPosition() == 0
                            ? ScreenConstants.SEGMENTED_BUTTON_VALUE_SINGPOREAN
                            : ScreenConstants.SEGMENTED_BUTTON_VALUE_FOREIGNER_OR_PR;

                    DBHelper mydb = new DBHelper(getApplicationContext());

                    try {
                        mydb.deleteAllRecords();
                        mydb.insertUser(mNameInput.getEditText().getText().toString(),
                                Integer.valueOf(mAgeInput.getEditText().getText().toString()),
                                Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()),
                                Integer.valueOf(mExpectancyInput.getEditText().getText().toString()),
                                employmentStatus, citizenship,
                                Float.valueOf(mIncomeInput.getEditText().getText().toString()),
                                Float.valueOf(mExpensesInput.getEditText().getText().toString()),
                                Float.valueOf(mAssets.getEditText().getText().toString()),
                                Integer.valueOf(mIncrementInput.getEditText().getText().toString()),
                                Integer.valueOf(mInflationInput.getEditText().getText().toString()));

                        Log.d(TAG, "onClick: " + mydb.numberOfRows(SQLConstants.USER_TABLE));
                        mydb.close();

                        startActivity(new Intent(FormActivity.this, MainActivity.class));
                    } catch (Exception e) {
                        //show a dialog explaining permission and then request permission
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FormActivity.this);

                        // Setting Dialog Title
                        alertDialog.setTitle("Error");

                        // Setting Dialog Message
                        alertDialog.setMessage(e.getMessage());

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Do nothing
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }
                }
            }
        });
    }

    /**
     * This method will validate the age input
     */
    private void ageValidation() {
        //validate age
        try {
            if (mAgeInput.getEditText() != null) {
                if (mAgeInput.getEditText().getText().toString().isEmpty()) {
                    mAgeInput.setErrorEnabled(false);
                } else if (Integer.valueOf(mAgeInput.getEditText().getText().toString()) > 999) {
                    mAgeInput.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_MORE_THAN_999);
                } else if (Integer.valueOf(mAgeInput.getEditText().getText().toString()) < 1) {
                    mAgeInput.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_LESS_THAN_1);
                } else {
                    mAgeInput.setErrorEnabled(false);
                }
            }
        } catch (NumberFormatException e) {
            //Do nothing
        }

        //validate retirement age
        try {
            if (mRetirementAgeInput.getEditText() != null) {
                if (mRetirementAgeInput.getEditText().getText().toString().isEmpty()) {
                    mRetirementAgeInput.setErrorEnabled(false);
                } else if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) > 999) {
                    mRetirementAgeInput.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_MORE_THAN_999);
                } else if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) < 1) {
                    mRetirementAgeInput.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_LESS_THAN_1);
                } else if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) < 999 &&
                        Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) > 1) {
                    mRetirementAgeInput.setErrorEnabled(false);
                } else {
                    mRetirementAgeInput.setErrorEnabled(false);
                }
            }
        } catch (NumberFormatException e) {
            //Do nothing
        }

        //validate expectancy
        try {
            if (mExpectancyInput.getEditText() != null) {
                if (mExpectancyInput.getEditText().getText().toString().isEmpty()) {
                    mExpectancyInput.setErrorEnabled(false);
                } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) > 999) {
                    mExpectancyInput.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_MORE_THAN_999);
                } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) < 1) {
                    mExpectancyInput.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_LESS_THAN_1);
                } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) <
                        Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString())) {
                    mExpectancyInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_EXPECTANCY);
                } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) <
                        Integer.valueOf(mAgeInput.getEditText().getText().toString()) ||
                        Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) <
                                Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString())) {
                    mExpectancyInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_EXPECTANCY);
                } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) < 999 &&
                        Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) > 1) {
                    mExpectancyInput.setErrorEnabled(false);
                } else {
                    mExpectancyInput.setErrorEnabled(false);
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