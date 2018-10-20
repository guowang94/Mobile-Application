package com.example.android.graphapplication.constants;

public interface ErrorMsgConstants {
    //Form Validation Message
    String ERR_MSG_INVALID_NAME = "Please enter valid name";
    String ERR_MSG_INVALID_RETIREMENT_AGE = "Retirement age cannot be less than current age";
    String ERR_MSG_INVALID_EXPECTANCY = "Expectancy cannot be less than current age or retirement age";
    String ERR_MSG_INVALID_PAYOUT_AGE = "Payout age cannot be less than premium age";
    String ERR_MSG_INVALID_ASSETS = "Assets cannot be less than $0";
    String ERR_MSG_INVALID_GROSS_MONTHLY_INCOME = "Gross Monthly Income cannot be less than $0";
    String ERR_MSG_INVALID_FIXED_EXPENSES = "Fixed expenses cannot be less than $0";
    String ERR_MSG_INVALID_VARIABLE_EXPENSES = "Variable expenses cannot be less than $0";
    String ERR_MSG_INVALID_INCREMENT = "Increment rate cannot be less than 0";
    String ERR_MSG_INVALID_INFLATION = "Inflation rate cannot be less than 0";
    String ERR_MSG_INVALID_DURATION = "Duration cannot be less than 0";
    String ERR_MSG_INVALID_COST_PER_YEAR = "Cost per year cannot be less than $0";
    String ERR_MSG_INVALID_AMOUNT = "Amount cannot be less than $0";
    String ERR_MSG_INVALID_PAYMENT_AMOUNT = "Payment amount cannot be less than $0";
    String ERR_MSG_INVALID_PAYOUT_AMOUNT = "Payout amount cannot be less than $0";
    String ERR_MSG_ENTER_VALID_INPUT = "Please ensure all input is correct";
    String ERR_MSG_FIELD_CANNOT_BE_BLANK = "This field cannot be blank";
    String ERR_MSG_AGE_CANNOT_BE_LESS_THAN_18 = "Age cannot be less than 18";
    String ERR_MSG_AGE_CANNOT_BE_MORE_THAN_999 = "Age cannot be more than 999 years old";
    String ERR_MSG_AGE_CANNOT_BE_MORE_THAN_EXPECTANCY = "Age cannot be more than Expectancy";
}
