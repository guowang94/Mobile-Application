package com.example.android.graphapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.graphapplication.constants.SQLConstants;
import com.example.android.graphapplication.constants.ScreenConstants;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, SQLConstants.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(SQLConstants.CREATE_USER_TABLE);
        db.execSQL(SQLConstants.CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL(SQLConstants.DROP_USER_TABLE);
        db.execSQL(SQLConstants.DROP_EVENT_TABLE);
        onCreate(db);
    }

    /**
     * This method will insert data into User Table
     *
     * @param name
     * @param age
     * @param expectedRetirementAge
     * @param expectancy
     * @param jobStatus
     * @param citizenship
     * @param monthlyIncome
     * @param fixedExpenses
     * @param variableExpenses
     * @param assets
     * @param increment
     * @param inflation
     * @return boolean
     */
    public boolean insertUser(String name, int age, int expectedRetirementAge, int expectancy,
                              String jobStatus, String citizenship, float monthlyIncome,
                              float fixedExpenses, float variableExpenses, float assets,
                              int increment, int inflation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.USER_TABLE_ID, 1);
        contentValues.put(SQLConstants.USER_TABLE_NAME, name);
        contentValues.put(SQLConstants.USER_TABLE_AGE, age);
        contentValues.put(SQLConstants.USER_TABLE_EXPECTED_RETIREMENT_AGE, expectedRetirementAge);
        contentValues.put(SQLConstants.USER_TABLE_EXPECTANCY, expectancy);
        contentValues.put(SQLConstants.USER_TABLE_JOB_STATUS, jobStatus);
        contentValues.put(SQLConstants.USER_TABLE_CITIZENSHIP, citizenship);
        contentValues.put(SQLConstants.USER_TABLE_INCOME, monthlyIncome);
        contentValues.put(SQLConstants.USER_TABLE_FIXED_EXPENSES, fixedExpenses);
        contentValues.put(SQLConstants.USER_TABLE_VARIABLE_EXPENSES, variableExpenses);
        contentValues.put(SQLConstants.USER_TABLE_INITIAL_ASSETS, assets);
        contentValues.put(SQLConstants.USER_TABLE_INCREMENT, increment);
        contentValues.put(SQLConstants.USER_TABLE_INFLATION, inflation);
        db.insert(SQLConstants.USER_TABLE, null, contentValues);
        return true;
    }

    /**
     * This method will update CPF account
     *
     * @param ordinaryAccount
     * @param specialAccount
     * @param medisaveAccount
     * @param balance
     * @param assets
     * @param shortfallAge
     * @param expensesExceededIncomeAge
     * @return boolean
     */
    public boolean updateUser(float ordinaryAccount, float specialAccount, float medisaveAccount,
                              float balance, float assets, int shortfallAge, int expensesExceededIncomeAge) {
        float shortfall = assets < 0 ? assets : 0;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.USER_TABLE_ORDINARY_ACCOUNT, ordinaryAccount);
        contentValues.put(SQLConstants.USER_TABLE_SPECIAL_ACCOUNT, specialAccount);
        contentValues.put(SQLConstants.USER_TABLE_MEDISAVE_ACCOUNT, medisaveAccount);
        contentValues.put(SQLConstants.USER_TABLE_BALANCE, balance);
        contentValues.put(SQLConstants.USER_TABLE_TOTAL_ASSETS, assets);
        contentValues.put(SQLConstants.USER_TABLE_SHORTFALL_AGE, shortfallAge);
        contentValues.put(SQLConstants.USER_TABLE_SHORTFALL, shortfall);
        contentValues.put(SQLConstants.USER_TABLE_EXPENSES_EXCEEDED_INCOME_AGE, expensesExceededIncomeAge);

        db.update(SQLConstants.USER_TABLE, contentValues,
                SQLConstants.USER_TABLE_ID + " = ? ", new String[]{"1"});
        return true;
    }

    /**
     * This method will insert data into Event Table
     *
     * @param eventName
     * @param eventType
     * @param eventDescription
     * @param eventStatus
     * @param amount
     * @param duration
     * @param costPerMonth
     * @return boolean
     */
    public boolean insertEvent(String eventName, String eventType, String eventDescription,
                               String eventStatus, float amount, int duration, float costPerMonth) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_NAME, eventName);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_TYPE, eventType);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_DESCRIPTION, eventDescription);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_STATUS, eventStatus);
        if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(eventStatus)) {
            contentValues.put(SQLConstants.EVENT_TABLE_AMOUNT, amount);
        } else {
            contentValues.put(SQLConstants.EVENT_TABLE_DURATION, duration);
            contentValues.put(SQLConstants.EVENT_TABLE_COST_PER_MONTH, costPerMonth);
        }
        db.insert(SQLConstants.EVENT_TABLE, null, contentValues);
        return true;
    }

    /**
     * This method will update Event details
     *
     * @param eventID
     * @param eventName
     * @param eventType
     * @param eventDescription
     * @param eventStatus
     * @param amount
     * @param duration
     * @param costPerMonth
     * @return boolean
     */
    public boolean updateEvent(int eventID, String eventName, String eventType, String eventDescription,
                               String eventStatus, float amount, int duration, float costPerMonth) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_NAME, eventName);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_TYPE, eventType);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_DESCRIPTION, eventDescription);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_STATUS, eventStatus);
        if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(eventStatus)) {
            contentValues.put(SQLConstants.EVENT_TABLE_AMOUNT, amount);
        } else {
            contentValues.put(SQLConstants.EVENT_TABLE_DURATION, duration);
            contentValues.put(SQLConstants.EVENT_TABLE_COST_PER_MONTH, costPerMonth);
        }
        db.update(SQLConstants.EVENT_TABLE, contentValues,
                SQLConstants.EVENT_TABLE_EVENT_ID + " = ? ",
                new String[]{String.valueOf(eventID)});
        return true;
    }

    /**
     * This method to delete Event record
     *
     * @param id
     * @return
     */
    public Integer deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SQLConstants.EVENT_TABLE, "id = ? ",
                new String[]{String.valueOf(id)});
    }

    /**
     * This method to return number of rows in the table
     *
     * @return int
     */
    public int numberOfRows(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, tableName);
    }

    /**
     * This method to get Data based on id
     *
     * @param tableName
     * @param id
     * @return Cursor
     */
    public Cursor getData(String tableName, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(
                "SELECT * FROM " + tableName + " WHERE id=" + id, null);
        return res;
    }

    /**
     * This method to get all records
     *
     * @return ArrayList
     */
    public ArrayList<String> getAllEvent() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + SQLConstants.EVENT_TABLE, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    /**
     * This method to get all records
     *
     * @return ArrayList
     */
    public ArrayList<String> getAllUser() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + SQLConstants.USER_TABLE, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(SQLConstants.USER_TABLE_ID)));
            res.moveToNext();
        }
        return array_list;
    }

    /**
     * This method to delete all records
     */
    public void deleteAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQLConstants.DELETE_USER_TABLE);
        db.execSQL(SQLConstants.DELETE_EVENT_TABLE);
    }
}
