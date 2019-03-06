package com.example.android.graphapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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

        //todo static text to be commented
//        mNameInput.getEditText().setText("wang");
//        mAgeInput.getEditText().setText("20");
//        mRetirementAgeInput.getEditText().setText("25");
//        mExpectancyInput.getEditText().setText("30");
//        mIncomeInput.getEditText().setText("1000");
//        mIncrementInput.getEditText().setText("5");
//        mExpensesInput.getEditText().setText("100");
//        mInflationInput.getEditText().setText("5");
//        mAssets.getEditText().setText("10000");

        initData();
        Log.d(TAG, "onCreate: out");
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

                if (!validation.blankFieldValidation(mRetirementAgeInput)) {
                    //Double check Retirement Age Input
                    ageValidation();
                }

                if (!validation.blankFieldValidation(mExpectancyInput)) {
                    //Double check Expectancy Input
                    ageValidation();
                }

                if (!validation.blankFieldValidation(mAgeInput)) {
                    //Double check Age Input
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
                    Snackbar.make(mLayout, ErrorMsgConstants.ERR_MSG_ENTER_VALID_INPUT,
                            Snackbar.LENGTH_LONG).show();
                } else {
                    String employmentStatus = mEmploymentStatusSegmentedButton.getPosition() == 0
                            ? ScreenConstants.SEGMENTED_BUTTON_VALUE_EMPLOYED
                            : ScreenConstants.SEGMENTED_BUTTON_VALUE_SELF_EMPLOYED;
                    String citizenship = mCitizenshipSegmentedButton.getPosition() == 0
                            ? ScreenConstants.SEGMENTED_BUTTON_VALUE_SINGPOREAN
                            : ScreenConstants.SEGMENTED_BUTTON_VALUE_FOREIGNER_OR_PR;

                    DBHelper mydb = new DBHelper(getApplicationContext());

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
                } else if (Integer.valueOf(mAgeInput.getEditText().getText().toString()) < 18) {
                    mAgeInput.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_LESS_THAN_18);
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
                } else if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) < 18) {
                    mRetirementAgeInput.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_LESS_THAN_18);
                } else if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) < 999 &&
                        Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) > 18) {
                    mRetirementAgeInput.setErrorEnabled(false);
                } else if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) <
                        Integer.valueOf(mAgeInput.getEditText().getText().toString())) {
                    mRetirementAgeInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_RETIREMENT_AGE);
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
                } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) < 18) {
                    mExpectancyInput.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_LESS_THAN_18);
                } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) < 999 &&
                        Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) > 18) {
                    mExpectancyInput.setErrorEnabled(false);
                } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) <
                        Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString())) {
                    mExpectancyInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_EXPECTANCY);
                } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) <
                        Integer.valueOf(mAgeInput.getEditText().getText().toString()) ||
                        Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) <
                                Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString())) {
                    mExpectancyInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_EXPECTANCY);
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