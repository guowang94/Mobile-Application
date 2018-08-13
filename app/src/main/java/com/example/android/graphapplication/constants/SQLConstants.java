package com.example.android.graphapplication.constants;

public interface SQLConstants {

    String DATABASE_NAME = "GraphDB.db";
    String USER_TABLE = "user";
    String EVENT_TABLE = "event";

    String USER_TABLE_ID = "id";
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

    String EVENT_TABLE_EVENT_ID = "id";
    String EVENT_TABLE_EVENT_NAME = "event_name";
    String EVENT_TABLE_EVENT_TYPE = "event_type";
    String EVENT_TABLE_EVENT_YEAR = "event_year";
    String EVENT_TABLE_EVENT_DESCRIPTION = "event_description";
    String EVENT_TABLE_EVENT_STATUS = "event_status";
    String EVENT_TABLE_AMOUNT = "amount";
    String EVENT_TABLE_DURATION = "duration";
    String EVENT_TABLE_COST_PER_MONTH = "cost_per_month";

    //SQL Queries
    String CREATE_USER_TABLE = "CREATE TABLE "
            + USER_TABLE + " ("
            + USER_TABLE_ID + " INTEGER PRIMARY KEY, "
            + USER_TABLE_NAME + " TEXT NOT NULL, "
            + USER_TABLE_AGE + " INTEGER NOT NULL, "
            + USER_TABLE_EXPECTED_RETIREMENT_AGE + " INTEGER NOT NULL,"
            + USER_TABLE_EXPECTANCY + " INTEGER NOT NULL, "
            + USER_TABLE_JOB_STATUS + " TEXT NOT NULL, "
            + USER_TABLE_CITIZENSHIP + " TEXT NOT NULL,"
            + USER_TABLE_INCOME + " FLOAT NOT NULL, "
            + USER_TABLE_FIXED_EXPENSES + " FLOAT NOT NULL, "
            + USER_TABLE_VARIABLE_EXPENSES + " FLOAT NOT NULL,"
            + USER_TABLE_INITIAL_ASSETS + " FLOAT NOT NULL, "
            + USER_TABLE_INCREMENT + " INTEGER NOT NULL, "
            + USER_TABLE_INFLATION + " INTEGER NOT NULL, "
            + USER_TABLE_SHORTFALL_AGE + " INTEGER DEFAULT 0, "
            + USER_TABLE_BALANCE + " FLOAT DEFAULT 0.0, "
            + USER_TABLE_SHORTFALL + " FLOAT DEFAULT 0.0, "
            + USER_TABLE_TOTAL_ASSETS + " FLOAT DEFAULT 0.0, "
            + USER_TABLE_EXPENSES_EXCEEDED_INCOME_AGE + " INTEGER DEFAULT 0, "
            + USER_TABLE_ORDINARY_ACCOUNT + " FLOAT DEFAULT 0.0, "
            + USER_TABLE_SPECIAL_ACCOUNT + " FLOAT DEFAULT 0.0,"
            + USER_TABLE_MEDISAVE_ACCOUNT + " FLOAT DEFAULT 0.0);";

    String CREATE_EVENT_TABLE = "CREATE TABLE "
            + EVENT_TABLE + " ("
            + EVENT_TABLE_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EVENT_TABLE_EVENT_NAME + " TEXT NOT NULL, "
            + EVENT_TABLE_EVENT_TYPE + " TEXT NOT NULL, "
            + EVENT_TABLE_EVENT_YEAR + " TEXT NOT NULL, "
            + EVENT_TABLE_EVENT_DESCRIPTION + " TEXT ,"
            + EVENT_TABLE_EVENT_STATUS + " TEXT NOT NULL, "
            + EVENT_TABLE_AMOUNT + " FLOAT DEFAULT 0.0, "
            + EVENT_TABLE_DURATION + " INTEGER DEFAULT 0,"
            + EVENT_TABLE_COST_PER_MONTH + " FLOAT DEFAULT 0.0);";

    String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + USER_TABLE;
    String DROP_EVENT_TABLE = "DROP TABLE IF EXISTS " + EVENT_TABLE;
    String DELETE_USER_TABLE = "DELETE FROM "+ USER_TABLE;
    String DELETE_EVENT_TABLE = "DELETE FROM "+ EVENT_TABLE;
}
