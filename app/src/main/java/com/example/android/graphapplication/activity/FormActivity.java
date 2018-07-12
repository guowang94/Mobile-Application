package com.example.android.graphapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.graphapplication.Constants;
import com.example.android.graphapplication.R;
import com.example.android.graphapplication.validations.Validation;

import java.io.FileOutputStream;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class FormActivity extends AppCompatActivity implements Constants/*, LabelledSpinner.OnItemChosenListener*/ {

    private static final String TAG = "FormActivity";
    private TextInputLayout mNameInput;
    private TextInputLayout mAgeInput;
    private TextInputLayout mCurrentAssets;
    private TextInputLayout mGrossMonthlyIncomeInput;
    private TextInputLayout mFixedExpensesInput;
    private TextInputLayout mVariableExpensesInput;
    private TextInputLayout mRetirementAgeInput;
    private TextInputLayout mExpectancyInput;
    private SegmentedButtonGroup mEmploymentStatusSegmentedButton;
    private SegmentedButtonGroup mCitizenshipSegmentedButton;
    private Button mComputeButton;
    private Toolbar mToolBar;
    private ConstraintLayout mLayout;

    private Validation validation = new Validation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mNameInput = findViewById(R.id.name_input_layout);
        mAgeInput = findViewById(R.id.age_input_layout);
        mCurrentAssets = findViewById(R.id.current_assets_input_layout);
        mGrossMonthlyIncomeInput = findViewById(R.id.gross_monthly_income_input_layout);
        mFixedExpensesInput = findViewById(R.id.fixed_expenses_input_layout);
        mVariableExpensesInput = findViewById(R.id.variable_expenses_input_layout);
        mRetirementAgeInput = findViewById(R.id.retirement_age_input_layout);
        mExpectancyInput = findViewById(R.id.expectancy_input_layout);
        mEmploymentStatusSegmentedButton = findViewById(R.id.employment_status_segmented_button);
        mCitizenshipSegmentedButton = findViewById(R.id.citizenship_segmented_button);
        mComputeButton = findViewById(R.id.compute_button);
        mToolBar = findViewById(R.id.toolbar);
        mLayout = findViewById(R.id.layout);

//        mNameInput.getEditText().setText("");
//        mAgeInput.getEditText().setText("");
//        mCurrentAssets.getEditText().setText("");
//        mGrossMonthlyIncomeInput.getEditText().setText("");
//        mFixedExpensesInput.getEditText().setText("");
//        mVariableExpensesInput.getEditText().setText("");
//        mRetirementAgeInput.getEditText().setText("");
//        mExpectancyInput.getEditText().setText("");

        setSupportActionBar(mToolBar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(TOOLBAR_TITLE_ENTER_YOUR_DETAILS);

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


        mCurrentAssets.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    currencyValidation(mCurrentAssets);
                }
            }
        });

        mGrossMonthlyIncomeInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    currencyValidation(mGrossMonthlyIncomeInput);
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

//        mJobStatusSpinner = findViewById(R.id.job_status_spinner);
//        mJobStatusSpinner.setItemsArray(R.array.employment_status_array);
//        mJobStatusSpinner.setOnItemChosenListener(this);

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
                    if (!blankFieldValidation(mCurrentAssets)) {
                        //Double check Assets Input
                        currencyValidation(mCurrentAssets);
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
                    if (!blankFieldValidation(mGrossMonthlyIncomeInput)) {
                        //Double check Gross Income Input
                        currencyValidation(mGrossMonthlyIncomeInput);
                    }
                } catch (Exception e) {
                    //Do nothing
                }

                //Check if any of the EditText has error
                if (mNameInput.isErrorEnabled() || mRetirementAgeInput.isErrorEnabled() || mExpectancyInput.isErrorEnabled() ||
                        mAgeInput.isErrorEnabled() || mCurrentAssets.isErrorEnabled() || mFixedExpensesInput.isErrorEnabled() ||
                        mVariableExpensesInput.isErrorEnabled() || mGrossMonthlyIncomeInput.isErrorEnabled()) {
                    Snackbar.make(mLayout, ERR_MSG_ENTER_VALID_INPUT, Snackbar.LENGTH_LONG).show();
                } else {
                    String employmentStatus = mEmploymentStatusSegmentedButton.getPosition() == 0
                            ? SEGMENTED_BUTTON_VALUE_SELF_EMPLOYED : SEGMENTED_BUTTON_VALUE_EMPLOYED;
                    String citizenship = mCitizenshipSegmentedButton.getPosition() == 0
                            ? SEGMENTED_BUTTON_VALUE_SINGPOREAN : SEGMENTED_BUTTON_VALUE_FOREIGNER_OR_PR;

                    //Saving data in internal storage
                    String fileContent = CONTENT_NAME + ":" + mNameInput.getEditText().getText().toString() +
                            "//" + CONTENT_AGE + ":" + mAgeInput.getEditText().getText().toString() +
                            "//" + CONTENT_CURRENT_ASSETS + ":" + mCurrentAssets.getEditText().getText().toString() +
                            "//" + CONTENT_GROSS_MONTHLY_INCOME + ":" + mGrossMonthlyIncomeInput.getEditText().getText().toString() +
                            "//" + CONTENT_FIXED_EXPENSES + ":" + mFixedExpensesInput.getEditText().getText().toString() +
                            "//" + CONTENT_VARIABLE_EXPENSES + ":" + mVariableExpensesInput.getEditText().getText().toString() +
                            "//" + CONTENT_RETIREMENT_AGE + ":" + mRetirementAgeInput.getEditText().getText().toString() +
                            "//" + CONTENT_EXPECTANCY + ":" + mExpectancyInput.getEditText().getText().toString() +
                            "//" + CONTENT_JOB_STATUS + ":" + employmentStatus +
                            "//" + CONTENT_CITIZENSHIP_STATUS + ":" + citizenship;

                    try {
                        FileOutputStream fileOutputStream = openFileOutput(FILE_USER_INFO, MODE_PRIVATE);
                        fileOutputStream.write(fileContent.getBytes());
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    startActivity(new Intent(FormActivity.this, MainActivity.class));
                }
            }
        });
        Log.d(TAG, "onCreate: out");
    }

    /**
     * This method will validate the name input
     */
    private void nameValidation() {
        try {
            if ((!validation.matchCharOnly(mNameInput.getEditText().getText().toString())) &&
                    mNameInput.getEditText().getText().length() > 0) {
                mNameInput.setError(ERR_MSG_INVALID_NAME);
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
                mAgeInput.setError(ERR_MSG_AGE_CANNOT_BE_MORE_THAN_999);
            } else if (Integer.valueOf(mAgeInput.getEditText().getText().toString()) < 18) {
                mAgeInput.setError(ERR_MSG_INVALID_AGE);
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
                mRetirementAgeInput.setError(ERR_MSG_RETIREMENT_AGE_CANNOT_BE_MORE_THAN_999);
            } else if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) < 18) {
                mRetirementAgeInput.setError(ERR_MSG_RETIREMENT_AGE_CANNOT_BE_LESS_THAN_18);
            } else if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) < 999 &&
                    Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) > 18) {
                mRetirementAgeInput.setErrorEnabled(false);
            } else {
                mRetirementAgeInput.setErrorEnabled(false);
            }

            if (!mRetirementAgeInput.isErrorEnabled()) {
                if (Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString()) <
                        Integer.valueOf(mAgeInput.getEditText().getText().toString())) {
                    mRetirementAgeInput.setError(ERR_MSG_INVALID_RETIREMENT_AGE);
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
                mExpectancyInput.setError(ERR_MSG_EXPECTANCY_CANNOT_BE_MORE_THAN_999);
            } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) < 18) {
                mExpectancyInput.setError(ERR_MSG_EXPECTANCY_CANNOT_BE_LESS_THAN_18);
            } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) < 999 &&
                    Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) > 18) {
                mExpectancyInput.setErrorEnabled(false);
            } else {
                mExpectancyInput.setErrorEnabled(false);
            }

            if (!mExpectancyInput.isErrorEnabled()) {
                if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) <
                        Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString())) {
                    mExpectancyInput.setError(ERR_MSG_INVALID_EXPECTANCY);
                } else if (Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) <
                        Integer.valueOf(mAgeInput.getEditText().getText().toString()) ||
                        Integer.valueOf(mExpectancyInput.getEditText().getText().toString()) <
                                Integer.valueOf(mRetirementAgeInput.getEditText().getText().toString())) {
                    mExpectancyInput.setError(ERR_MSG_INVALID_EXPECTANCY);
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
     * @param textInputLayout
     */
    private void currencyValidation(TextInputLayout textInputLayout) {
        try {
            if (Float.valueOf(textInputLayout.getEditText().getText().toString()) < 0f) {
                switch (textInputLayout.getId()) {
                    case R.id.current_assets_input_layout:
                        mCurrentAssets.setError(ERR_MSG_INVALID_ASSETS);
                        break;
                    case R.id.gross_monthly_income_input_layout:
                        mGrossMonthlyIncomeInput.setError(ERR_MSG_INVALID_GROSS_MONTHLY_INCOME);
                        break;
                    case R.id.fixed_expenses_input_layout:
                        mFixedExpensesInput.setError(ERR_MSG_INVALID_FIXED_EXPENSES);
                        break;
                    case R.id.variable_expenses_input_layout:
                        mVariableExpensesInput.setError(ERR_MSG_INVALID_VARIABLE_EXPENSES);
                        break;
                    default:
                        System.out.println("In if() default: currencyValidation()");
                }
            } else {
                switch (textInputLayout.getId()) {
                    case R.id.current_assets_input_layout:
                        mCurrentAssets.setErrorEnabled(false);
                        break;
                    case R.id.gross_monthly_income_input_layout:
                        mGrossMonthlyIncomeInput.setErrorEnabled(false);
                        break;
                    case R.id.fixed_expenses_input_layout:
                        mFixedExpensesInput.setErrorEnabled(false);
                        break;
                    case R.id.variable_expenses_input_layout:
                        mVariableExpensesInput.setErrorEnabled(false);
                        break;
                    default:
                        System.out.println("In else() default: currencyValidation()");
                }
            }
        } catch (NumberFormatException e) {
            switch (textInputLayout.getId()) {
                case R.id.current_assets_input_layout:
                    mCurrentAssets.setError(ERR_MSG_INVALID_ASSETS);
                    break;
                case R.id.gross_monthly_income_input_layout:
                    mGrossMonthlyIncomeInput.setError(ERR_MSG_INVALID_GROSS_MONTHLY_INCOME);
                    break;
                case R.id.fixed_expenses_input_layout:
                    mFixedExpensesInput.setError(ERR_MSG_INVALID_FIXED_EXPENSES);
                    break;
                case R.id.variable_expenses_input_layout:
                    mVariableExpensesInput.setError(ERR_MSG_INVALID_VARIABLE_EXPENSES);
                    break;
                default:
                    System.out.println("In if() default: currencyValidation()");
            }
        }
    }

    /**
     * This method will check for blank input field
     *
     * @param textInputLayout
     * @return
     */
    private boolean blankFieldValidation(TextInputLayout textInputLayout) {
        try {
            if (textInputLayout.getEditText().getText().toString().isEmpty()) {
                switch (textInputLayout.getId()) {
                    case R.id.name_input_layout:
                        mNameInput.setError(ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.retirement_age_input_layout:
                        mRetirementAgeInput.setError(ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.expectancy_input_layout:
                        mExpectancyInput.setError(ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.age_input_layout:
                        mAgeInput.setError(ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.current_assets_input_layout:
                        mCurrentAssets.setError(ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.fixed_expenses_input_layout:
                        mFixedExpensesInput.setError(ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.variable_expenses_input_layout:
                        mVariableExpensesInput.setError(ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    case R.id.gross_monthly_income_input_layout:
                        mGrossMonthlyIncomeInput.setError(ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    default:
                        System.out.println("In default: blankFieldValidation()");
                }
            }
        } catch (NumberFormatException e) {
            //Do nothing
        }
        return false;
    }
}

//This code is for spinner, but I will be using this in another screen
//    @Override
//    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
//        switch (labelledSpinner.getId()) {
//            case R.id.job_status_spinner:
//                Log.i(TAG, "onCreate: test " + mJobStatusSpinner.getSpinner().getItemAtPosition(position).toString());
//                jobStatusSpinnerValue = mJobStatusSpinner.getSpinner().getItemAtPosition(position).toString();
//                break;
//        }
//    }
//
//    @Override
//    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
//        // Do something here
//    }