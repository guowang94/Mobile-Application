package com.example.android.graphapplication.constants;

public interface SQLConstants {

    String DATABASE_NAME = "GraphDB.db";
    String USER_TABLE = "user";
    String EVENT_TABLE = "event";
    String MILESTONE_TABLE = "milestone";
    String PLAN_TABLE = "plan";
    String IS_SELECTED = "is_selected";
    String TABLE_ID = "id";

    String USER_TABLE_NAME = "name";
    String USER_TABLE_AGE = "age";
    String USER_TABLE_EXPECTED_RETIREMENT_AGE = "expected_retirement_age";
    String USER_TABLE_EXPECTANCY = "expectancy";
    String USER_TABLE_JOB_STATUS = "job_status";
    String USER_TABLE_CITIZENSHIP = "citizenship";
    String USER_TABLE_INCOME = "income";
    String USER_TABLE_FIXED_EXPENSES = "fixed_expenses";
    String USER_TABLE_VARIABLE_EXPENSES = "variable_expenses";
    String USER_TABLE_INITIAL_ASSETS = "initial_assets";
    String USER_TABLE_TOTAL_ASSETS = "total_assets";
    String USER_TABLE_ORDINARY_ACCOUNT = "ordinary_account";
    String USER_TABLE_SPECIAL_ACCOUNT = "special_account";
    String USER_TABLE_MEDISAVE_ACCOUNT = "medisave_account";
    String USER_TABLE_SHORTFALL_AGE = "shortfall_age";
    String USER_TABLE_BALANCE = "balance";
    String USER_TABLE_SHORTFALL = "shortfall";
    String USER_TABLE_INCREMENT = "increment";
    String USER_TABLE_INFLATION = "inflation";
    String USER_TABLE_EXPENSES_EXCEEDED_INCOME_AGE = "expenses_exceeded_income_age";

    String EVENT_TABLE_EVENT_NAME = "event_name";
    String EVENT_TABLE_EVENT_TYPE = "event_type";
    String EVENT_TABLE_EVENT_AGE = "event_age";
    String EVENT_TABLE_EVENT_DESCRIPTION = "event_description";
    String EVENT_TABLE_EVENT_STATUS = "event_status";
    String EVENT_TABLE_AMOUNT = "amount";
    String EVENT_TABLE_DURATION = "duration";

    String MILESTONE_TABLE_MILESTONE_NAME = "milestone_name";
    String MILESTONE_TABLE_MILESTONE_TYPE = "milestone_type";
    String MILESTONE_TABLE_MILESTONE_AGE = "milestone_age";
    String MILESTONE_TABLE_MILESTONE_DESCRIPTION = "milestone_description";
    String MILESTONE_TABLE_MILESTONE_STATUS = "milestone_status";
    String MILESTONE_TABLE_AMOUNT = "amount";
    String MILESTONE_TABLE_DURATION = "duration";

    String PLAN_TABLE_PLAN_NAME = "plan_name";
    String PLAN_TABLE_PLAN_TYPE = "plan_type";
    String PLAN_TABLE_PAYMENT_TYPE = "payment_type";
    String PLAN_TABLE_PREMIUM_START_AGE = "premium_start_age";
    String PLAN_TABLE_PAYMENT_AMOUNT = "payment_amount";
    String PLAN_TABLE_PLAN_DURATION = "plan_duration";
    String PLAN_TABLE_PAYOUT_AGE = "payout_age";
    String PLAN_TABLE_PAYOUT_AMOUNT = "payout_amount";
    String PLAN_TABLE_PAYOUT_DURATION = "payout_duration";
    String PLAN_TABLE_PLAN_STATUS = "plan_status";

    //SQL Queries
    String CREATE_USER_TABLE = "CREATE TABLE "
            + USER_TABLE + " ("
            + TABLE_ID + " INTEGER PRIMARY KEY, "
            + USER_TABLE_NAME + " TEXT NOT NULL, "
            + USER_TABLE_AGE + " INTEGER NOT NULL, "
            + USER_TABLE_EXPECTED_RETIREMENT_AGE + " INTEGER NOT NULL, "
            + USER_TABLE_EXPECTANCY + " INTEGER NOT NULL, "
            + USER_TABLE_JOB_STATUS + " TEXT NOT NULL, "
            + USER_TABLE_CITIZENSHIP + " TEXT NOT NULL, "
            + USER_TABLE_INCOME + " FLOAT NOT NULL, "
            + USER_TABLE_FIXED_EXPENSES + " FLOAT NOT NULL, "
            + USER_TABLE_VARIABLE_EXPENSES + " FLOAT NOT NULL, "
            + USER_TABLE_INITIAL_ASSETS + " FLOAT NOT NULL, "
            + USER_TABLE_INCREMENT + " INTEGER NOT NULL, "
            + USER_TABLE_INFLATION + " INTEGER NOT NULL, "
            + USER_TABLE_SHORTFALL_AGE + " INTEGER DEFAULT 0, "
            + USER_TABLE_BALANCE + " FLOAT DEFAULT 0.0, "
            + USER_TABLE_SHORTFALL + " FLOAT DEFAULT 0.0, "
            + USER_TABLE_TOTAL_ASSETS + " FLOAT DEFAULT 0.0, "
            + USER_TABLE_EXPENSES_EXCEEDED_INCOME_AGE + " INTEGER DEFAULT 0, "
            + USER_TABLE_ORDINARY_ACCOUNT + " FLOAT DEFAULT 0.0, "
            + USER_TABLE_SPECIAL_ACCOUNT + " FLOAT DEFAULT 0.0, "
            + USER_TABLE_MEDISAVE_ACCOUNT + " FLOAT DEFAULT 0.0);";

    String CREATE_EVENT_TABLE = "CREATE TABLE "
            + EVENT_TABLE + " ("
            + TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EVENT_TABLE_EVENT_NAME + " TEXT NOT NULL, "
            + EVENT_TABLE_EVENT_TYPE + " TEXT NOT NULL, "
            + EVENT_TABLE_EVENT_AGE + " TEXT NOT NULL, "
            + EVENT_TABLE_EVENT_DESCRIPTION + " TEXT , "
            + EVENT_TABLE_EVENT_STATUS + " TEXT NOT NULL, "
            + EVENT_TABLE_AMOUNT + " FLOAT DEFAULT 0.0, "
            + EVENT_TABLE_DURATION + " INTEGER DEFAULT 1, "
            + IS_SELECTED + " INTEGER DEFAULT 0);";

    String CREATE_MILESTONE_TABLE = "CREATE TABLE "
            + MILESTONE_TABLE + " ("
            + TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MILESTONE_TABLE_MILESTONE_NAME + " TEXT NOT NULL, "
            + MILESTONE_TABLE_MILESTONE_TYPE + " TEXT NOT NULL, "
            + MILESTONE_TABLE_MILESTONE_AGE + " TEXT NOT NULL, "
            + MILESTONE_TABLE_MILESTONE_DESCRIPTION + " TEXT , "
            + MILESTONE_TABLE_MILESTONE_STATUS + " TEXT NOT NULL, "
            + MILESTONE_TABLE_AMOUNT + " FLOAT DEFAULT 0.0, "
            + MILESTONE_TABLE_DURATION + " INTEGER DEFAULT 1, "
            + IS_SELECTED + " INTEGER DEFAULT 0);";

    String CREATE_PLAN_TABLE = "CREATE TABLE "
            + PLAN_TABLE + " ("
            + TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PLAN_TABLE_PLAN_NAME + " TEXT NOT NULL, "
            + PLAN_TABLE_PLAN_TYPE + " TEXT NOT NULL, "
            + PLAN_TABLE_PAYMENT_TYPE + " TEXT NOT NULL, "
            + PLAN_TABLE_PREMIUM_START_AGE + " INTEGER NOT NULL, "
            + PLAN_TABLE_PAYMENT_AMOUNT + " FLOAT NOT NULL, "
            + PLAN_TABLE_PLAN_DURATION + " INTEGER DEFAULT 1, "
            + PLAN_TABLE_PAYOUT_AGE + " INTEGER NOT NULL, "
            + PLAN_TABLE_PAYOUT_DURATION + " INTEGER NOT NULL, "
            + PLAN_TABLE_PAYOUT_AMOUNT + " FLOAT NOT NULL, "
            + PLAN_TABLE_PLAN_STATUS + " TEXT NOT NULL, "
            + IS_SELECTED + " INTEGER DEFAULT 0);";

    //SQL Query
    String DELETE_TABLE = "DELETE FROM $1";
    String SELECT_ALL_FROM_TABLE = "SELECT * FROM $1";
    String SELECT_ALL_IS_SELECTED_FROM_TABLE = "SELECT * FROM $1 WHERE " + IS_SELECTED + " = 1";
    String SELECT_ALL_FROM_TABLE_WHERE_ID = "SELECT * FROM $1 WHERE " + TABLE_ID + " = $2";
    String SELECT_ALL_EXISTING_FROM_PLAN_TABLE = "SELECT * FROM " + PLAN_TABLE + " WHERE " + PLAN_TABLE_PLAN_STATUS + " = 'Existing Plan' AND " + IS_SELECTED + " = 1";
    String SELECT_ALL_NON_EXISTING_FROM_PLAN_TABLE = "SELECT * FROM " + PLAN_TABLE + " WHERE " + PLAN_TABLE_PLAN_STATUS + " = 'Non-Existing Plan' AND " + IS_SELECTED + " = 1";
}
