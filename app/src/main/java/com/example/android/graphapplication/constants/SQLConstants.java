package com.example.android.graphapplication.constants;

public interface SQLConstants {

    String DATABASE_NAME = "GraphDB.db";
    String USER_TABLE = "user";
    String EVENT_TABLE = "event";
    String MILESTONE_TABLE = "milestone";
    String PLAN_TABLE = "plan";
    String ONE_TIME_LOGIN_TABLE = "one_time_login";
    String IS_SELECTED = "is_selected";
    String TABLE_ID = "id";

    String USER_TABLE_NAME = "name";
    String USER_TABLE_AGE = "age";
    String USER_TABLE_EXPECTED_RETIREMENT_AGE = "expected_retirement_age";
    String USER_TABLE_EXPECTANCY = "expectancy";
    String USER_TABLE_JOB_STATUS = "job_status";
    String USER_TABLE_CITIZENSHIP = "citizenship";
    String USER_TABLE_INCOME = "income";
    String USER_TABLE_EXPENSES = "expenses";
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
    String EVENT_TABLE_NO_INCOME_STATUS = "no_income_status";

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

    String OTL_TABLE_PASSWORD = "password";

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
            + USER_TABLE_EXPENSES + " FLOAT NOT NULL, "
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
            + EVENT_TABLE_EVENT_AGE + " INT NOT NULL, "
            + EVENT_TABLE_EVENT_DESCRIPTION + " TEXT DEFAULT NULL, "
            + EVENT_TABLE_EVENT_STATUS + " TEXT NOT NULL, "
            + EVENT_TABLE_AMOUNT + " FLOAT DEFAULT 0.0, "
            + EVENT_TABLE_DURATION + " INTEGER DEFAULT 1, "
            + EVENT_TABLE_NO_INCOME_STATUS + " INTEGER DEFAULT 0, "
            + IS_SELECTED + " INTEGER DEFAULT 0);";

    String CREATE_MILESTONE_TABLE = "CREATE TABLE "
            + MILESTONE_TABLE + " ("
            + TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MILESTONE_TABLE_MILESTONE_NAME + " TEXT NOT NULL, "
            + MILESTONE_TABLE_MILESTONE_TYPE + " TEXT NOT NULL, "
            + MILESTONE_TABLE_MILESTONE_AGE + " INT NOT NULL, "
            + MILESTONE_TABLE_MILESTONE_DESCRIPTION + " TEXT DEFAULT NULL, "
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

    String CREATE_OTL_TABLE = "CREATE TABLE "
            + ONE_TIME_LOGIN_TABLE + " ("
            + TABLE_ID + " INTEGER PRIMARY KEY, "
            + OTL_TABLE_PASSWORD + " TEXT NOT NULL)";

    //SQL Query
    String DELETE_USER_TABLE = "DELETE FROM " + USER_TABLE + " WHERE EXISTS (SELECT * FROM " + USER_TABLE + ")";
    String DELETE_EVENT_TABLE = "DELETE FROM " + EVENT_TABLE + " WHERE EXISTS (SELECT * FROM " + EVENT_TABLE + ")";
    String DELETE_MILESTONE_TABLE = "DELETE FROM " + MILESTONE_TABLE + " WHERE EXISTS (SELECT * FROM " + MILESTONE_TABLE + ")";
    String DELETE_PLAN_TABLE = "DELETE FROM " + PLAN_TABLE + " WHERE EXISTS (SELECT * FROM " + PLAN_TABLE + ")";

    String SELECT_ALL_FROM_USER_TABLE = "SELECT * FROM " + USER_TABLE;
    String SELECT_ALL_FROM_EVENT_TABLE = "SELECT * FROM " + EVENT_TABLE;
    String SELECT_ALL_FROM_MILESTONE_TABLE = "SELECT * FROM " + MILESTONE_TABLE;
    String SELECT_ALL_FROM_PLAN_TABLE = "SELECT * FROM " + PLAN_TABLE;
    String SELECT_ALL_FROM_OTL_TABLE = "SELECT * FROM " + ONE_TIME_LOGIN_TABLE;

    String SELECT_ALL_IS_SELECTED_FROM_EVENT_TABLE = "SELECT * FROM " + EVENT_TABLE + " WHERE " + IS_SELECTED + " = 1";
    String SELECT_ALL_IS_SELECTED_FROM_MILESTONE_TABLE = "SELECT * FROM " + MILESTONE_TABLE + " WHERE " + IS_SELECTED + " = 1";
    String SELECT_ALL_IS_SELECTED_FROM_PLAN_TABLE = "SELECT * FROM " + PLAN_TABLE + " WHERE " + IS_SELECTED + " = 1";
    String SELECT_ALL_IS_SELECTED_AND_NO_INCOME_FROM_EVENT_TABLE = "SELECT * FROM " + EVENT_TABLE +
            " WHERE " + IS_SELECTED + " = 1 AND " + EVENT_TABLE_NO_INCOME_STATUS + " = 1";

    String SELECT_ALL_EXISTING_FROM_PLAN_TABLE = "SELECT * FROM " + PLAN_TABLE +
            " WHERE " + PLAN_TABLE_PLAN_STATUS + " = '" + ScreenConstants.SEGMENTED_BUTTON_VALUE_EXISTING_PLAN +
            "' AND " + IS_SELECTED + " = 1";
    String SELECT_ALL_NON_EXISTING_FROM_PLAN_TABLE = "SELECT * FROM " + PLAN_TABLE +
            " WHERE " + PLAN_TABLE_PLAN_STATUS + " = '" + ScreenConstants.SEGMENTED_BUTTON_VALUE_NON_EXISTING_PLAN +
            "' AND " + IS_SELECTED + " = 1";

    String SELECT_ALL_FROM_EVENT_TABLE_WHERE_ID = "SELECT * FROM " + EVENT_TABLE + " WHERE " + TABLE_ID + " = ";
    String SELECT_ALL_FROM_MILESTONE_TABLE_WHERE_ID = "SELECT * FROM " + MILESTONE_TABLE + " WHERE " + TABLE_ID + " = ";
    String SELECT_ALL_FROM_PLAN_TABLE_WHERE_ID = "SELECT * FROM " + PLAN_TABLE + " WHERE " + TABLE_ID + " = ";

    String DROP_USER_TABLE = "DROP TABLE " + USER_TABLE;
    String DROP_EVENT_TABLE = "DROP TABLE " + EVENT_TABLE;
}
