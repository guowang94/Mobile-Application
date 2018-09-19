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

public class FormActivity extends AppCompatActivity /*, LabelledSpinner.OnItemChosenListener*/ {

    private static final String TAG = "FormActivity";
    private TextInputLayout mNameInput;
    private TextInputLayout mAgeInput;
    private TextInputLayout mAssets;
    private TextInputLayout mIncomeInput;
    private TextInputLayout mFixedExpensesInput;
    private TextInputLayout mVariableExpensesInput;
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

    private Validation validation = new Validation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mNameInput = findViewById(R.id.name_input_layout);
        mAgeInput = findViewById(R.id.age_input_layout);
        mAssets = findViewById(R.id.assets_input_layout);
        mIncomeInput = findViewById(R.id.income_input_layout);
        mFixedExpensesInput = findViewById(R.id.fixed_expenses_input_layout);
        mVariableExpensesInput = findViewById(R.id.variable_expenses_input_layout);
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

//        mNameInput.getEditText().setText("");
//        mAgeInput.getEditText().setText("");
//        mRetirementAgeInput.getEditText().setText("");
//        mExpectancyInput.getEditText().setText("");
//        mIncomeInput.getEditText().setText("");
//        mIncrementInput.getEditText().setText("");
//        mFixedExpensesInput.getEditText().setText("");
//        mVariableExpensesInput.getEditText().setText("");
//        mInflationInput.getEditText().setText("");
//        mAssets.getEditText().setText("");

        initData();
        Log.d(TAG, "onCreate: out");
    }

    /**
     * This method will setup toolbar and set onClickListener
     */
    private void initData() {
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this toolbar
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_ENTER_USER_DETAILS);

        mNameInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    nameValidation();
                }
            }
        });

        mAgeInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ageValidation();
                }
            }
        });

        mAssets.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    currencyValidation(mAssets);
                }
            }
        });

        mIncomeInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    currencyValidation(mIncomeInput);
                }
            }
        });

        mFixedExpensesInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    currencyValidation(mFixedExpensesInput);
                }
            }
        });

        mVariableExpensesInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    currencyValidation(mVariableExpensesInput);
                }
            }
        });

        mRetirementAgeInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ageValidation();
                }
            }
        });

        mExpectancyInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ageValidation();
                }
            }
        });

        mComputeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (!blankFieldValidation(mNameInput)) {
                        //Double check Name Input
                        nameValidation();
                    }
                } catch (Exception e) {
                    //Do nothing
                }

                try {
                    if (!blankFieldValidation(mRetirementAgeInput)) {
                        //Double check Retirement Age Input
                        ageValidation();
                    }
                } catch (Exception e) {
                    //Do nothing
                }

                try {
                    if (!blankFieldValidation(mExpectancyInput)) {
                        //Double check Expectancy Input
                        ageValidation();
                    }
                } catch (Exception e) {
                    //Do nothing
                }

                try {
                    if (!blankFieldValidation(mAgeInput)) {
                        //Double check Age Input
                        ageValidation();
                    }
                } catch (Exception e) {
                    //Do nothing
                }

                try {
                    if (!blankFieldValidation(mAssets)) {
                        //Double check Assets Input
                        currencyValidation(mAssets);
                    }
                } catch (Exception e) {
                    //Do nothing
                }

                try {
                    if (!blankFieldValidation(mFixedExpensesInput)) {
                        //Double check Fixed Expenses Input
                        currencyValidation(mFixedExpensesInput);
                    }
                } catch (Exception e) {
                    //Do nothing
                }

                try {
                    if (!blankFieldValidation(mVariableExpensesInput)) {
                        //Double check Variable Expenses Input
                        currencyValidation(mVariableExpensesInput);
                    }
                } catch (Exception e) {
                    //Do nothing
                }

                try {
                    if (!blankFieldValidation(mIncomeInput)) {
                        //Double check Gross Income Input
                        currencyValidation(mIncomeInput);
                    }
                } catch (Exception e) {
                    //Do nothing
                }

                try {
                    blankFieldValidation(mIncrementInput);
                } catch (Exception e) {
                    //Do nothing
                }

                try {
                    blankFieldValidation(mInflationInput);
                } catch (Exception e) {
                    //Do nothing
                }

                //Check if any of the EditText has error
                if (mNameInput.isErrorEnabled() || mIncomeInput.isErrorEnabled() ||
                        mExpectancyInput.isErrorEnabled() || mAgeInput.isErrorEnabled() ||
                        mAssets.isErrorEnabled() || mFixedExpensesInput.isErrorEnabled() ||
                        mVariableExpensesInput.isErrorEnabled() || mRetirementAgeInput.isErrorEnabled() ||
                        mIncrementInput.isErrorEnabled() || mInflationInput.isErrorEnabled()) {
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
                            Float.valueOf(mFixedExpensesInput.getEditText().getText().toString()),
                            Float.valueOf(mVariableExpensesInput.getEditText().getText().toString()),
                            Float.valueOf(mAssets.getEditText().getText().toString()),
                            Integer.valueOf(mIncrementInput.getEditText().getText().toString()),
                            Integer.valueOf(mInflationInput.getEditText().getText().toString()));

                    Log.d(TAG, "onClick: " + mydb.numberOfRows(SQLConstants.USER_TABLE));

                    startActivity(new Intent(FormActivity.this, MainActivity.class));
                }
            }
        });
    }

    /**
     * This method will validate the name input
     */
    private void nameValidation() {
        try {
            if ((!validation.matchCharOnly(mNameInput.getEditText().getText().toString())) &&
                    mNameInput.getEditText().getText().length() > 0) {
                mNameInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_NAME);
            } else {
                mNameInput.setErrorEnabled(false);
            }
        } catch (Exception e) {
            //Do nothing
        }
    }

    /**
     * This method will validate the age input
     */
    private void ageValidation() {
        //validate age
        try {
            if (mAgeInput.getEditText().getText().toString().isEmpty()) {
                mAgeInput.setErrorEnabled(false);
            } else if (Integer.valueOf(mAgeInput.getEditText().getText().toString()) > 999) {
                mAgeInput.setError(ErrorMsgConstants.ERR_MSG_AGE_CANNOT_BE_MORE_THAN_999);
            } else if (Integer.valueOf(mAgeInput.getEditText().getText().toString()) < 18) {
                mAgeInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_AGE);
            } else {
                mAgeInput.setErrorEnabled(false);
            }
        } catch (NumberFormatException e) {
            //Do nothing
        }

        try {
            //validate retirement age
            if (mRetirementAgeInput.getEditText().getText().toString().isEmpty()) {
                mRetirementAgeInput.setErrorEnabled(false);
            } else if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) > 999) {
                mRetirementAgeInput.setError(ErrorMsgConstants.ERR_MSG_RETIREMENT_AGE_CANNOT_BE_MORE_THAN_999);
            } else if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) < 18) {
                mRetirementAgeInput.setError(ErrorMsgConstants.ERR_MSG_RETIREMENT_AGE_CANNOT_BE_LESS_THAN_18);
            } else if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) < 999 &&
                    Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) > 18) {
                mRetirementAgeInput.setErrorEnabled(false);
            } else {
                mRetirementAgeInput.setErrorEnabled(false);
            }

            if (!mRetirementAgeInput.isErrorEnabled()) {
                if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) <
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
            if (mExpectancyInput.getEditText().getText().toString().isEmpty()) {
                mExpectancyInput.setErrorEnabled(false);
            } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) > 999) {
                mExpectancyInput.setError(ErrorMsgConstants.ERR_MSG_EXPECTANCY_CANNOT_BE_MORE_THAN_999);
            } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) < 18) {
                mExpectancyInput.setError(ErrorMsgConstants.ERR_MSG_EXPECTANCY_CANNOT_BE_LESS_THAN_18);
            } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) < 999 &&
                    Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) > 18) {
                mExpectancyInput.setErrorEnabled(false);
            } else {
                mExpectancyInput.setErrorEnabled(false);
            }

            if (!mExpectancyInput.isErrorEnabled()) {
                if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) <
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
     * This method will validate the input value
     *
     * @param textInputLayout is the view
     */
    private void currencyValidation(TextInputLayout textInputLayout) {
        try {
            if (Float.valueOf(textInputLayout.getEditText().getText().toString()) < 0f) {
                switch (textInputLayout.getId()) {
                    case R.id.assets_input_layout:
                        mAssets.setError(ErrorMsgConstants.ERR_MSG_INVALID_ASSETS);
                        break;
                    case R.id.income_input_layout:
                        mIncomeInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_GROSS_MONTHLY_INCOME);
                        break;
                    case R.id.fixed_expenses_input_layout:
                        mFixedExpensesInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_FIXED_EXPENSES);
                        break;
                    case R.id.variable_expenses_input_layout:
                        mVariableExpensesInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_VARIABLE_EXPENSES);
                        break;
                    default:
                        Log.d(TAG, "currencyValidation: In if() default");
                }
            } else {
                switch (textInputLayout.getId()) {
                    case R.id.assets_input_layout:
                        mAssets.setErrorEnabled(false);
                        break;
                    case R.id.income_input_layout:
                        mIncomeInput.setErrorEnabled(false);
                        break;
                    case R.id.fixed_expenses_input_layout:
                        mFixedExpensesInput.setErrorEnabled(false);
                        break;
                    case R.id.variable_expenses_input_layout:
                        mVariableExpensesInput.setErrorEnabled(false);
                        break;
                    default:
                        Log.d(TAG, "currencyValidation: In else() default");
                }
            }
        } catch (NumberFormatException e) {
            switch (textInputLayout.getId()) {
                case R.id.assets_input_layout:
                    mAssets.setError(ErrorMsgConstants.ERR_MSG_INVALID_ASSETS);
                    break;
                case R.id.income_input_layout:
                    mIncomeInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_GROSS_MONTHLY_INCOME);
                    break;
                case R.id.fixed_expenses_input_layout:
                    mFixedExpensesInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_FIXED_EXPENSES);
                    break;
                case R.id.variable_expenses_input_layout:
                    mVariableExpensesInput.setError(ErrorMsgConstants.ERR_MSG_INVALID_VARIABLE_EXPENSES);
                    break;
                default:
                    Log.d(TAG, "currencyValidation: In catch() default");
            }
        }
    }

    /**
     * This method will check for blank input field
     *
     * @param textInputLayout is the view
     * @return boolean
     */
    private boolean blankFieldValidation(TextInputLayout textInputLayout) {
        try {
            if (textInputLayout.getEditText().getText().toString().isEmpty()) {
                switch (textInputLayout.getId()) {
                    case R.id.name_input_layout:
                        mNameInput.setError(ErrorMsgConstants.ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.retirement_age_input_layout:
                        mRetirementAgeInput.setError(ErrorMsgConstants.ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.expectancy_input_layout:
                        mExpectancyInput.setError(ErrorMsgConstants.ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.age_input_layout:
                        mAgeInput.setError(ErrorMsgConstants.ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.assets_input_layout:
                        mAssets.setError(ErrorMsgConstants.ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.fixed_expenses_input_layout:
                        mFixedExpensesInput.setError(ErrorMsgConstants.ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.variable_expenses_input_layout:
                        mVariableExpensesInput.setError(ErrorMsgConstants.ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.income_input_layout:
                        mIncomeInput.setError(ErrorMsgConstants.ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.increment_input_layout:
                        mIncrementInput.setError(ErrorMsgConstants.ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.inflation_input_layout:
                        mInflationInput.setError(ErrorMsgConstants.ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    default:
                        Log.d(TAG, "blankFieldValidation: in default");
                }
            }
        } catch (NumberFormatException e) {
            //Do nothing
        }
        return false;
    }
}