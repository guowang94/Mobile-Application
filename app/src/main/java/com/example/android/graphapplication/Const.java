package com.example.android.graphapplication;

public interface Const {

    //Fields
    String CONTENT_NAME = "Name";
    String CONTENT_AGE = "Age";
    String CONTENT_CURRENT_ASSETS = "Current Assets";
    String CONTENT_GROSS_MONTHLY_INCOME = "Gross Monthly Income";
    String CONTENT_MONTHLY_INCOME = "Monthly Income";
    String CONTENT_FIXED_EXPENSES = "Fixed Expenses";
    String CONTENT_VARIABLE_EXPENSES = "Variable Expenses";
    String CONTENT_RETIREMENT_AGE = "Retirement Age";
    String CONTENT_AGE_OF_SHORTFALL = "Age Of Shortfall";
    String CONTENT_CPF_DETAILS = "CPF Details";
    String CONTENT_EXPECTANCY = "Expectancy";
    String CONTENT_JOB_STATUS = "Job Status";
    String CONTENT_BALANCE = "Balance";
    String CONTENT_SHORTFALL = "Shortfall";
    String CONTENT_CPF_ORDINARY_ACCOUNT = "CPF Ordinary Account";
    String CONTENT_CPF_SPECIAL_ACCOUNT = "CPF Special Account";
    String CONTENT_CPF_MEDISAVE_ACCOUNT = "CPF Medisave Account";

    //File name
    String FILE_USER_INFO = "userInfo";

    //List Field
    String LIST_SELF_EMPLOYED = "Self-Employed";
    String LIST_WORKING = "Working";

    //Navigation Fields name
    String NAV_PLANS = "Plans";
    String NAV_SUMMARY_DETAILS = "Summary Details";
    String NAV_EVENTS = "Events";
    String NAV_MILESTONES = "Milestones";

    //Toolbar title
    String TOOLBAR_TITLE_SUMMARY = "Summary";
    String TOOLBAR_TITLE_ACCUMULATIVE_CPF = "Accumulative CPF";

    //Graph Legend
    String GRAPH_LEGEND_ASSETS = "Assets";
    String GRAPH_LEGEND_INCOME = "Income";
    String GRAPH_LEGEND_CPF_OA = "CPF OA";
    String GRAPH_LEGEND_CPF_SA = "CPF SA";
    String GRAPH_LEGEND_CPF_MA = "CPF MA";

    //Error Message
    String ERR_MSG_INVALID_NAME = "Please enter valid name";
    String ERR_MSG_INVALID_RETIREMENT_AGE = "Retirement age cannot be less than current age";
    String ERR_MSG_INVALID_EXPECTANCY = "Expectancy cannot be less than current age or retirement age";
    String ERR_MSG_INVALID_ASSETS = "Assets cannot be less than $0";
    String ERR_MSG_INVALID_AGE = "Age cannot be less than 18";
    String ERR_MSG_INVALID_GROSS_MONTHLY_INCOME = "Gross Monthly Income cannot be less than $0";
    String ERR_MSG_INVALID_FIXED_EXPENSES = "Fixed expenses cannot be less than $0";
    String ERR_MSG_INVALID_VARIABLE_EXPENSES = "Variable expenses cannot be less than $0";
    String ERR_MSG_ENTER_VALID_INPUT = "Please ensure all input is correct";
    String ERR_MSG_FIELD_CANNOT_BE_BLANK = "This field cannot be blank";
    String ERR_MSG_AGE_CANNOT_BE_MORE_THAN_999 = "Age cannot be more than 999 years old";
    String ERR_MSG_RETIREMENT_AGE_CANNOT_BE_MORE_THAN_999 = "Retirement age cannot be more than 999 years old";
    String ERR_MSG_EXPECTANCY_CANNOT_BE_MORE_THAN_999 = "Expectancy cannot be more than 999 years old";
    String ERR_MSG_RETIREMENT_AGE_CANNOT_BE_LESS_THAN_18 = "Retirement age cannot be less than 18 years old";
    String ERR_MSG_EXPECTANCY_CANNOT_BE_LESS_THAN_18 = "Expectancy cannot be less than 18 years old";
}
