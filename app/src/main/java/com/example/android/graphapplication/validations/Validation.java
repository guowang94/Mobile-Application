package com.example.android.graphapplication.validations;

import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.constants.ErrorMsgConstants;

import java.util.regex.Pattern;

public class Validation {

    private static final String TAG = "Validation";

    private boolean matchCharForName(String str) {
        return Pattern.matches("[A-Za-z ]{2,}", str);
    }

    private boolean matchCharOnly(String str) {
        return Pattern.matches("[A-Za-z ]+", str);
    }

    /**
     * This method will return onFocusChangeListener for negative value validation
     *
     * @param textInputLayout TextInputLayout
     * @return onFocusChangeListener
     */
    public View.OnFocusChangeListener onFocusChangeListenerForNegativeValueValidation(final TextInputLayout textInputLayout) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    negativeValueValidation(textInputLayout);
                }
            }
        };
    }

    /**
     * This method will return onFocusChangeListener for blank field validation
     *
     * @param textInputLayout TextInputLayout
     * @return onFocusChangeListener
     */
    public View.OnFocusChangeListener onFocusChangeListenerForBlankFieldValidation(final TextInputLayout textInputLayout) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    blankFieldValidation(textInputLayout);
                }
            }
        };
    }

    /**
     * This method will return onFocusChangeListener for name validation
     *
     * @param textInputLayout TextInputLayout
     * @return onFocusChangeListener
     */
    public View.OnFocusChangeListener onFocusChangeListenerForNameValidation(final TextInputLayout textInputLayout) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    nameValidation(textInputLayout);
                }
            }
        };
    }

    public View.OnFocusChangeListener onFocusChangeListenerForDescriptionValidation(final TextInputLayout textInputLayout) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    descriptionValidation(textInputLayout);
                }
            }
        };
    }

    /**
     * This method will check for blank input field
     *
     * @param textInputLayout is the view
     * @return boolean
     */
    public boolean blankFieldValidation(TextInputLayout textInputLayout) {
        try {
            if (textInputLayout.getEditText() != null) {
                if (textInputLayout.getEditText().getText().toString().isEmpty()) {
                    switch (textInputLayout.getId()) {
                        case R.id.user_name_input_layout:
                        case R.id.retirement_age_input_layout:
                        case R.id.expectancy_input_layout:
                        case R.id.age_input_layout:
                        case R.id.assets_input_layout:
                        case R.id.expenses_input_layout:
                        case R.id.income_input_layout:
                        case R.id.increment_input_layout:
                        case R.id.inflation_input_layout:
                        case R.id.event_name_input_layout:
                        case R.id.milestone_name_input_layout:
                        case R.id.amount_input_layout:
                        case R.id.duration_input_layout:
                        case R.id.cost_per_year_input_layout:
                        case R.id.plan_name_input_layout:
                        case R.id.plan_type_input_layout:
                        case R.id.premium_start_age_input_layout:
                        case R.id.premium_amount_input_layout:
                        case R.id.premium_duration_input_layout:
                        case R.id.payout_age_input_layout:
                        case R.id.payout_amount_input_layout:
                        case R.id.payout_duration_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_FIELD_CANNOT_BE_BLANK);
                            return true;
                        default:
                            Log.d(TAG, "blankFieldValidation: None of the TextInputLayout id has been matched." +
                                    "TextInputLayout id: " + textInputLayout.getId());
                    }
                } else {
                    switch (textInputLayout.getId()) {
                        case R.id.user_name_input_layout:
                        case R.id.retirement_age_input_layout:
                        case R.id.expectancy_input_layout:
                        case R.id.age_input_layout:
                        case R.id.assets_input_layout:
                        case R.id.expenses_input_layout:
                        case R.id.income_input_layout:
                        case R.id.increment_input_layout:
                        case R.id.inflation_input_layout:
                        case R.id.event_name_input_layout:
                        case R.id.milestone_name_input_layout:
                        case R.id.amount_input_layout:
                        case R.id.duration_input_layout:
                        case R.id.cost_per_year_input_layout:
                        case R.id.plan_name_input_layout:
                        case R.id.plan_type_input_layout:
                        case R.id.premium_start_age_input_layout:
                        case R.id.premium_amount_input_layout:
                        case R.id.premium_duration_input_layout:
                        case R.id.payout_age_input_layout:
                        case R.id.payout_amount_input_layout:
                        case R.id.payout_duration_input_layout:
                            textInputLayout.setErrorEnabled(false);
                            return false;
                        default:
                            Log.d(TAG, "blankFieldValidation: None of the TextInputLayout id has been matched." +
                                    "TextInputLayout id: " + textInputLayout.getId());
                    }
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
    public void negativeValueValidation(TextInputLayout textInputLayout) {
        try {
            if (textInputLayout.getEditText() != null) {
                if (textInputLayout.getEditText().getText().toString().isEmpty()) {
                    switch (textInputLayout.getId()) {
                        case R.id.assets_input_layout:
                        case R.id.income_input_layout:
                        case R.id.expenses_input_layout:
                        case R.id.increment_input_layout:
                        case R.id.inflation_input_layout:
                        case R.id.amount_input_layout:
                        case R.id.duration_input_layout:
                        case R.id.cost_per_year_input_layout:
                        case R.id.premium_amount_input_layout:
                        case R.id.premium_duration_input_layout:
                        case R.id.payout_amount_input_layout:
                        case R.id.payout_duration_input_layout:
                            textInputLayout.setErrorEnabled(false);
                            break;
                        default:
                            Log.d(TAG, "negativeValueValidation: None of the TextInputLayout id has been matched." +
                                    "TextInputLayout id: " + textInputLayout.getId());
                    }
                } else if (Float.valueOf(textInputLayout.getEditText().getText().toString()) < 0f) {
                    switch (textInputLayout.getId()) {
                        case R.id.assets_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_ASSETS);
                            break;
                        case R.id.income_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_GROSS_MONTHLY_INCOME);
                            break;
                        case R.id.expenses_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_EXPENSES);
                            break;
                        case R.id.increment_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_INCREMENT);
                            break;
                        case R.id.inflation_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_INFLATION);
                            break;
                        case R.id.amount_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_AMOUNT);
                            break;
                        case R.id.duration_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_DURATION);
                            break;
                        case R.id.cost_per_year_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_COST_PER_YEAR);
                            break;
                        case R.id.premium_amount_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_PAYMENT_AMOUNT);
                            break;
                        case R.id.premium_duration_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_DURATION);
                            break;
                        case R.id.payout_amount_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_PAYOUT_AMOUNT);
                            break;
                        case R.id.payout_duration_input_layout:
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_DURATION);
                            break;
                        default:
                            Log.d(TAG, "negativeValueValidation: None of the TextInputLayout id has been matched." +
                                    "TextInputLayout id: " + textInputLayout.getId());
                    }
                } else {
                    switch (textInputLayout.getId()) {
                        case R.id.assets_input_layout:
                        case R.id.income_input_layout:
                        case R.id.expenses_input_layout:
                        case R.id.increment_input_layout:
                        case R.id.inflation_input_layout:
                        case R.id.amount_input_layout:
                        case R.id.duration_input_layout:
                        case R.id.cost_per_year_input_layout:
                        case R.id.premium_amount_input_layout:
                        case R.id.premium_duration_input_layout:
                        case R.id.payout_amount_input_layout:
                        case R.id.payout_duration_input_layout:
                            textInputLayout.setErrorEnabled(false);
                            break;
                        default:
                            Log.d(TAG, "negativeValueValidation: None of the TextInputLayout id has been matched." +
                                    "TextInputLayout id: " + textInputLayout.getId());
                    }
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
                case R.id.expenses_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_EXPENSES);
                    break;
                case R.id.increment_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_INCREMENT);
                    break;
                case R.id.inflation_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_INFLATION);
                    break;
                case R.id.amount_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_AMOUNT);
                    break;
                case R.id.duration_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_DURATION);
                    break;
                case R.id.cost_per_year_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_COST_PER_YEAR);
                    break;
                case R.id.premium_amount_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_PAYMENT_AMOUNT);
                    break;
                case R.id.premium_duration_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_DURATION);
                    break;
                case R.id.payout_amount_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_PAYOUT_AMOUNT);
                    break;
                case R.id.payout_duration_input_layout:
                    textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_DURATION);
                    break;
                default:
                    Log.d(TAG, "negativeValueValidation: None of the TextInputLayout id has been matched." +
                            "TextInputLayout id: " + textInputLayout.getId());
            }
        }
    }

    /**
     * This method will validate the name input
     *
     * @param textInputLayout
     */
    public void nameValidation(TextInputLayout textInputLayout) {
        try {
            switch (textInputLayout.getId()) {
                case R.id.user_name_input_layout:
                    if (textInputLayout.getEditText() != null) {
                        if ((!matchCharForName(textInputLayout.getEditText().getText().toString())) &&
                                textInputLayout.getEditText().getText().length() > 0) {
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_NAME);
                        } else {
                            textInputLayout.setErrorEnabled(false);
                        }
                    }
                    break;
                case R.id.event_name_input_layout:
                case R.id.milestone_name_input_layout:
                case R.id.plan_name_input_layout:
                    // Because there is no restriction when creating name
                    textInputLayout.setErrorEnabled(false);
                    break;
                default:
                    Log.d(TAG, "nameValidation: None of the TextInputLayout id has been matched." +
                            "TextInputLayout id: " + textInputLayout.getId());
            }
        } catch (Exception e) {
            //Do nothing
        }
    }

    /**
     * This method will validate the name input
     *
     * @param textInputLayout
     */
    public void descriptionValidation(TextInputLayout textInputLayout) {
        try {
            switch (textInputLayout.getId()) {
                case R.id.event_description_input_layout:
                case R.id.milestone_description_input_layout:
                    if (textInputLayout.getEditText() != null) {
                        if ((!matchCharOnly(textInputLayout.getEditText().getText().toString())) &&
                                textInputLayout.getEditText().getText().length() > 0) {
                            textInputLayout.setError(ErrorMsgConstants.ERR_MSG_INVALID_DESCRIPTION);
                        } else {
                            textInputLayout.setErrorEnabled(false);
                        }
                    }
                    break;
                default:
                    Log.d(TAG, "descriptionValidation: None of the TextInputLayout id has been matched." +
                            "TextInputLayout id: " + textInputLayout.getId());
            }
        } catch (Exception e) {
            //Do nothing
        }
    }
}