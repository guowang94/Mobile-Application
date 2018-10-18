package com.example.android.graphapplication.validations;

import android.support.design.widget.TextInputLayout;
import android.util.Log;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.constants.ErrorMsgConstants;

import java.util.regex.Pattern;

public class Validation {

    private static final String TAG = "Validation";

    String charRegex = "[A-Za-z ]{2,}";

    public boolean matchCharOnly(String str) {
        return Pattern.matches(charRegex, str);
    }

    /**
     * This method will check for blank input field
     *
     * @param textInputLayout is the view
     * @return boolean
     */
    public boolean blankFieldValidation(TextInputLayout textInputLayout) {
        try {
            if (textInputLayout.getEditText().getText().toString().isEmpty()) {
                switch (textInputLayout.getId()) {
                    case R.id.name_input_layout:
                    case R.id.retirement_age_input_layout:
                    case R.id.expectancy_input_layout:
                    case R.id.age_input_layout:
                    case R.id.assets_input_layout:
                    case R.id.fixed_expenses_input_layout:
                    case R.id.variable_expenses_input_layout:
                    case R.id.income_input_layout:
                    case R.id.increment_input_layout:
                    case R.id.inflation_input_layout:
                        textInputLayout.setError(ErrorMsgConstants.ERR_MSG_FIELD_CANNOT_BE_BLANK);
                        return true;
                    default:
                        Log.d(TAG, "blankFieldValidation: None of the TextInputLayout id has been matched." +
                                "TextInputLayout id: " + textInputLayout.getId());
                }
            }
        } catch (NumberFormatException e) {
            //Do nothing
        }
        return false;
    }

    /**
     * This method will validate the input value
     *
     * @param textInputLayout is the view
     */
    public void currencyValidation(TextInputLayout textInputLayout) {
        try {
            if (Float.valueOf(textInputLayout.getEditText().getText().toString()) < 0f) {
                switch (textInputLayout.getId()) {
                    case R.id.assets_input_layout:
                        textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_ASSETS);
                        break;
                    case R.id.income_input_layout:
                        textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_GROSS_MONTHLY_INCOME);
                        break;
                    case R.id.fixed_expenses_input_layout:
                        textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_FIXED_EXPENSES);
                        break;
                    case R.id.variable_expenses_input_layout:
                        textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_VARIABLE_EXPENSES);
                        break;
                    case R.id.increment_input_layout:
                        textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_INCREMENT);
                        break;
                    case R.id.inflation_input_layout:
                        textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_INFLATION);
                        break;
                    default:
                        Log.d(TAG, "currencyValidation: In if() default");
                }
            } else {
                switch (textInputLayout.getId()) {
                    case R.id.assets_input_layout:
                    case R.id.income_input_layout:
                    case R.id.fixed_expenses_input_layout:
                    case R.id.variable_expenses_input_layout:
                    case R.id.increment_input_layout:
                    case R.id.inflation_input_layout:
                        textInputLayout.setErrorEnabled(false);
                        break;
                    default:
                        Log.d(TAG, "currencyValidation: In else() default");
                }
            }
        } catch (NumberFormatException e) {
            switch (textInputLayout.getId()) {
                case R.id.assets_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_ASSETS);
                    break;
                case R.id.income_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_GROSS_MONTHLY_INCOME);
                    break;
                case R.id.fixed_expenses_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_FIXED_EXPENSES);
                    break;
                case R.id.variable_expenses_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_VARIABLE_EXPENSES);
                    break;
                case R.id.increment_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_INCREMENT);
                    break;
                case R.id.inflation_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_INFLATION);
                    break;
                default:
                    Log.d(TAG, "currencyValidation: In catch() default");
            }
        }
    }
}