package com.example.android.graphapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.graphapplication.constants.SQLConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.model.CommonModel;
import com.example.android.graphapplication.model.PlanModel;
import com.example.android.graphapplication.model.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, SQLConstants.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLConstants.CREATE_USER_TABLE);
        db.execSQL(SQLConstants.CREATE_EVENT_TABLE);
        db.execSQL(SQLConstants.CREATE_MILESTONE_TABLE);
        db.execSQL(SQLConstants.CREATE_PLAN_TABLE);
//        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(SQLConstants.DROP_USER_TABLE);
                db.execSQL(SQLConstants.DROP_EVENT_TABLE);
                db.execSQL(SQLConstants.CREATE_USER_TABLE);
                db.execSQL(SQLConstants.CREATE_EVENT_TABLE);
//                db.close();
                break;
            default:
                Log.d(TAG, "onUpgrade: Unknown database version: " + DATABASE_VERSION);
        }
    }

    /**
     * This method will insert data into User Table
     *
     * @param name                  Client's name
     * @param age                   Client's age
     * @param expectedRetirementAge Clients's expected retirement age
     * @param expectancy            Client's expected to live age
     * @param jobStatus             employed/self-employed
     * @param citizenship           Singaporean/Foreigner
     * @param monthlyIncome         Gross Monthly Income
     * @param expenses              eg. bills, loans, entertainment, transport
     * @param assets                Current assets
     * @param increment             Salary increment rate
     * @param inflation             Currency inflation rate
     */
    public void insertUser(String name, int age, int expectedRetirementAge, int expectancy,
                           String jobStatus, String citizenship, float monthlyIncome,
                           float expenses, float assets,
                           int increment, int inflation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.TABLE_ID, 1);
        contentValues.put(SQLConstants.USER_TABLE_NAME, name);
        contentValues.put(SQLConstants.USER_TABLE_AGE, age);
        contentValues.put(SQLConstants.USER_TABLE_EXPECTED_RETIREMENT_AGE, expectedRetirementAge);
        contentValues.put(SQLConstants.USER_TABLE_EXPECTANCY, expectancy);
        contentValues.put(SQLConstants.USER_TABLE_JOB_STATUS, jobStatus);
        contentValues.put(SQLConstants.USER_TABLE_CITIZENSHIP, citizenship);
        contentValues.put(SQLConstants.USER_TABLE_INCOME, monthlyIncome);
        contentValues.put(SQLConstants.USER_TABLE_EXPENSES, expenses);
        contentValues.put(SQLConstants.USER_TABLE_INITIAL_ASSETS, assets);
        contentValues.put(SQLConstants.USER_TABLE_INCREMENT, increment);
        contentValues.put(SQLConstants.USER_TABLE_INFLATION, inflation);
        db.insert(SQLConstants.USER_TABLE, null, contentValues);
//        db.close();
    }

    /**
     * This method will update CPF account
     *
     * @param ordinaryAccount           CPF Ordinary Account
     * @param specialAccount            CPF Special Account
     * @param medisaveAccount           CPF Medisave Account
     * @param balance                   Money left at the time of retirement
     * @param assets                    Total Assets
     * @param shortfallAge              When assets is negative
     * @param expensesExceededIncomeAge Age of client when Expenses is more than Income
     */
    public void updateUser(float ordinaryAccount, float specialAccount, float medisaveAccount,
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
                SQLConstants.TABLE_ID + " = ? ", new String[]{"1"});
//        db.close();
    }

    /**
     * This method to get all records
     *
     * @return list of user details
     */
    public List<UserModel> getAllUser() {
        List<UserModel> userModelList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_FROM_USER_TABLE, null);
        res.moveToFirst();
        String[] columnNames = res.getColumnNames();
        Log.d(TAG, "getAllUser: " + Arrays.toString(columnNames));

        while (!res.isAfterLast()) {
            UserModel userModel = new UserModel();
            userModel.setName(res.getString(res.getColumnIndex(SQLConstants.USER_TABLE_NAME)));
            userModel.setMonthlyIncome(res.getFloat(res.getColumnIndex(SQLConstants.USER_TABLE_INCOME)));
            userModel.setInitialAssets(res.getFloat(res.getColumnIndex(SQLConstants.USER_TABLE_INITIAL_ASSETS)));
            userModel.setTotalAssets(res.getFloat(res.getColumnIndex(SQLConstants.USER_TABLE_TOTAL_ASSETS)));
            userModel.setJobStatus(res.getString(res.getColumnIndex(SQLConstants.USER_TABLE_JOB_STATUS)));
            userModel.setCitizenship(res.getString(res.getColumnIndex(SQLConstants.USER_TABLE_CITIZENSHIP)));
            userModel.setAge(res.getInt(res.getColumnIndex(SQLConstants.USER_TABLE_AGE)));
            userModel.setExpectedRetirementAge(res.getInt(res.getColumnIndex(SQLConstants.USER_TABLE_EXPECTED_RETIREMENT_AGE)));
            userModel.setExpectancy(res.getInt(res.getColumnIndex(SQLConstants.USER_TABLE_EXPECTANCY)));
            userModel.setBalance(res.getFloat(res.getColumnIndex(SQLConstants.USER_TABLE_BALANCE)));
            userModel.setShortfall(res.getFloat(res.getColumnIndex(SQLConstants.USER_TABLE_SHORTFALL)));
            userModel.setShortfallAge(res.getInt(res.getColumnIndex(SQLConstants.USER_TABLE_SHORTFALL_AGE)));
            userModel.setSpecialAccount(res.getFloat(res.getColumnIndex(SQLConstants.USER_TABLE_SPECIAL_ACCOUNT)));
            userModel.setMedisaveAccount(res.getFloat(res.getColumnIndex(SQLConstants.USER_TABLE_MEDISAVE_ACCOUNT)));
            userModel.setExpensesExceededIncomeAge(res.getInt(res.getColumnIndex(SQLConstants.USER_TABLE_EXPENSES_EXCEEDED_INCOME_AGE)));
            userModel.setExpenses(res.getFloat(res.getColumnIndex(SQLConstants.USER_TABLE_EXPENSES)));
            userModel.setIncrement(res.getInt(res.getColumnIndex(SQLConstants.USER_TABLE_INCREMENT)));
            userModel.setInflation(res.getInt(res.getColumnIndex(SQLConstants.USER_TABLE_INFLATION)));
            userModelList.add(userModel);
            res.moveToNext();
        }
        res.close();
//        db.close();
        Log.d(TAG, "getAllUser: " + userModelList.toString());
        return userModelList;
    }

    /**
     * This method will insert data into Event Table
     *
     * @param eventName        Event name
     * @param eventType        Event type
     * @param eventAge         Age when event occurred
     * @param eventDescription Description of event
     * @param eventStatus      One time payment/recurring
     * @param amount           Amount of money to pay
     * @param duration         Duration of the event
     * @param noIncomeStatus   No income status
     */
    public void insertEvent(String eventName, String eventType, int eventAge, String eventDescription,
                            String eventStatus, float amount, int duration, int noIncomeStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_NAME, eventName);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_TYPE, eventType);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_AGE, eventAge);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_DESCRIPTION, eventDescription);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_STATUS, eventStatus);
        contentValues.put(SQLConstants.EVENT_TABLE_AMOUNT, amount);
        if (!ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(eventStatus)) {
            contentValues.put(SQLConstants.EVENT_TABLE_DURATION, duration);
        }
        contentValues.put(SQLConstants.EVENT_TABLE_NO_INCOME_STATUS, noIncomeStatus);
        db.insert(SQLConstants.EVENT_TABLE, null, contentValues);
//        db.close();
    }

    /**
     * This method will update Event details
     *
     * @param eventID          ID of the edited event
     * @param eventName        Edited event name
     * @param eventType        Edited event type
     * @param eventAge         Edited age when event occurred
     * @param eventDescription Edited description of event
     * @param eventStatus      One time payment/recurring
     * @param amount           Edited amount to pay
     * @param duration         Edited duration of event
     * @param noIncomeStatus   Edited no income status
     */
    public void updateEvent(int eventID, String eventName, String eventType, int eventAge,
                            String eventDescription, String eventStatus, float amount,
                            int duration, int noIncomeStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_NAME, eventName);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_TYPE, eventType);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_AGE, eventAge);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_DESCRIPTION, eventDescription);
        contentValues.put(SQLConstants.EVENT_TABLE_EVENT_STATUS, eventStatus);
        contentValues.put(SQLConstants.EVENT_TABLE_AMOUNT, amount);
        if (!ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(eventStatus)) {
            contentValues.put(SQLConstants.EVENT_TABLE_DURATION, duration);
        }
        contentValues.put(SQLConstants.EVENT_TABLE_NO_INCOME_STATUS, noIncomeStatus);
        db.update(SQLConstants.EVENT_TABLE, contentValues,
                SQLConstants.TABLE_ID + " = ? ",
                new String[]{String.valueOf(eventID)});
//        db.close();
    }

    /**
     * This method to update Event IsSelected status
     *
     * @param eventID    ID of the edited event
     * @param isSelected Updated status of isSelected variable
     */
    public void updateEventIsSelectedStatus(int eventID, int isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.IS_SELECTED, isSelected);
        db.update(SQLConstants.EVENT_TABLE, contentValues,
                SQLConstants.TABLE_ID + " = ? ",
                new String[]{String.valueOf(eventID)});
//        db.close();
    }

    /**
     * This method to delete Event record
     *
     * @param id ID of to be deleted Event
     */
    public void deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SQLConstants.EVENT_TABLE, "id = ? ", new String[]{String.valueOf(id)});
//        db.close();
    }

    /**
     * This method to get all records
     *
     * @return list of events
     */
    public List<CommonModel> getAllEvent() {
        List<CommonModel> eventsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_FROM_EVENT_TABLE, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            CommonModel eventModel = new CommonModel();
            eventModel.setId(res.getInt(res.getColumnIndex(SQLConstants.TABLE_ID)));
            eventModel.setName(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_NAME)));
            eventModel.setAge(res.getInt(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_AGE)));
            eventModel.setType(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_TYPE)));
            eventModel.setStatus(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_STATUS)));
            eventModel.setAmount(res.getFloat(res.getColumnIndex(SQLConstants.EVENT_TABLE_AMOUNT)));
            eventModel.setDuration(res.getInt(res.getColumnIndex(SQLConstants.EVENT_TABLE_DURATION)));
            eventModel.setNoIncomeStatus(res.getInt(res.getColumnIndex(SQLConstants.EVENT_TABLE_NO_INCOME_STATUS)));
            eventModel.setIsSelected(res.getInt(res.getColumnIndex(SQLConstants.IS_SELECTED)));
            eventsList.add(eventModel);
            res.moveToNext();
        }
        res.close();
//        db.close();
        return eventsList;
    }

    /**
     * This method to get event details
     *
     * @param id id of current event
     * @return event details
     */
    public CommonModel getEventDetails(int id) {
        CommonModel eventModel = new CommonModel();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_FROM_EVENT_TABLE_WHERE_ID + id, null);
        res.moveToFirst();

        eventModel.setId(res.getInt(res.getColumnIndex(SQLConstants.TABLE_ID)));
        eventModel.setName(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_NAME)));
        eventModel.setAge(res.getInt(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_AGE)));
        eventModel.setType(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_TYPE)));
        eventModel.setDescription(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_DESCRIPTION)));
        eventModel.setStatus(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_STATUS)));
        eventModel.setDuration(res.getInt(res.getColumnIndex(SQLConstants.EVENT_TABLE_DURATION)));
        eventModel.setAmount(res.getFloat(res.getColumnIndex(SQLConstants.EVENT_TABLE_AMOUNT)));
        eventModel.setNoIncomeStatus(res.getInt(res.getColumnIndex(SQLConstants.EVENT_TABLE_NO_INCOME_STATUS)));
        eventModel.setIsSelected(res.getInt(res.getColumnIndex(SQLConstants.IS_SELECTED)));

        res.close();
//        db.close();
        return eventModel;
    }

    /**
     * This method to get all selected event
     *
     * @return list of selected event
     */
    public List<CommonModel> getAllSelectedEvent() {
        List<CommonModel> eventModelsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_IS_SELECTED_FROM_EVENT_TABLE, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            CommonModel eventModel = new CommonModel();
            eventModel.setId(res.getInt(res.getColumnIndex(SQLConstants.TABLE_ID)));
            eventModel.setName(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_NAME)));
            eventModel.setAge(res.getInt(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_AGE)));
            eventModel.setType(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_TYPE)));
            eventModel.setStatus(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_STATUS)));
            eventModel.setAmount(res.getFloat(res.getColumnIndex(SQLConstants.EVENT_TABLE_AMOUNT)));
            eventModel.setDuration(res.getInt(res.getColumnIndex(SQLConstants.EVENT_TABLE_DURATION)));
            eventModel.setNoIncomeStatus(res.getInt(res.getColumnIndex(SQLConstants.EVENT_TABLE_NO_INCOME_STATUS)));
            eventModel.setIsSelected(res.getInt(res.getColumnIndex(SQLConstants.IS_SELECTED)));
            eventModelsList.add(eventModel);
            res.moveToNext();
        }
        res.close();
//        db.close();
        return eventModelsList;
    }

    /**
     * This method to get all selected and no income event
     *
     * @return list of selected event
     */
    public List<CommonModel> getAllSelectedAndNoIncomeEvent() {
        List<CommonModel> eventModelsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_IS_SELECTED_AND_NO_INCOME_FROM_EVENT_TABLE, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            CommonModel eventModel = new CommonModel();
            eventModel.setId(res.getInt(res.getColumnIndex(SQLConstants.TABLE_ID)));
            eventModel.setName(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_NAME)));
            eventModel.setAge(res.getInt(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_AGE)));
            eventModel.setType(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_TYPE)));
            eventModel.setStatus(res.getString(res.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_STATUS)));
            eventModel.setAmount(res.getFloat(res.getColumnIndex(SQLConstants.EVENT_TABLE_AMOUNT)));
            eventModel.setDuration(res.getInt(res.getColumnIndex(SQLConstants.EVENT_TABLE_DURATION)));
            eventModel.setNoIncomeStatus(res.getInt(res.getColumnIndex(SQLConstants.EVENT_TABLE_NO_INCOME_STATUS)));
            eventModel.setIsSelected(res.getInt(res.getColumnIndex(SQLConstants.IS_SELECTED)));
            eventModelsList.add(eventModel);
            res.moveToNext();
        }
        res.close();
//        db.close();
        return eventModelsList;
    }

    /**
     * This method will insert data into Milestone Table
     *
     * @param milestoneName        Milestone name
     * @param milestoneType        Milestone type
     * @param milestoneAge         Age when milestone occurred
     * @param milestoneDescription Description of milestone
     * @param milestoneStatus      One time payment/recurring
     * @param amount               Amount to pay
     * @param duration             Duration of milestone
     */
    public void insertMilestone(String milestoneName, String milestoneType, int milestoneAge, String milestoneDescription,
                                String milestoneStatus, float amount, int duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.MILESTONE_TABLE_MILESTONE_NAME, milestoneName);
        contentValues.put(SQLConstants.MILESTONE_TABLE_MILESTONE_TYPE, milestoneType);
        contentValues.put(SQLConstants.MILESTONE_TABLE_MILESTONE_AGE, milestoneAge);
        contentValues.put(SQLConstants.MILESTONE_TABLE_MILESTONE_DESCRIPTION, milestoneDescription);
        contentValues.put(SQLConstants.MILESTONE_TABLE_MILESTONE_STATUS, milestoneStatus);
        contentValues.put(SQLConstants.MILESTONE_TABLE_AMOUNT, amount);
        if (!ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(milestoneStatus)) {
            contentValues.put(SQLConstants.MILESTONE_TABLE_DURATION, duration);
        }
        db.insert(SQLConstants.MILESTONE_TABLE, null, contentValues);
//        db.close();
    }

    /**
     * This method will update Milestone details
     *
     * @param milestoneID          ID of edited milestone
     * @param milestoneName        Edited milestone name
     * @param milestoneType        Edited milestone type
     * @param milestoneAge         Edited age when milestone occurred
     * @param milestoneDescription Edited description of milestone
     * @param milestoneStatus      One time payment/recurring
     * @param amount               Edited amount to pay
     * @param duration             Edited duration of milestone
     */
    public void updateMilestone(int milestoneID, String milestoneName, String milestoneType, int milestoneAge,
                                String milestoneDescription, String milestoneStatus, float amount,
                                int duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.MILESTONE_TABLE_MILESTONE_NAME, milestoneName);
        contentValues.put(SQLConstants.MILESTONE_TABLE_MILESTONE_TYPE, milestoneType);
        contentValues.put(SQLConstants.MILESTONE_TABLE_MILESTONE_AGE, milestoneAge);
        contentValues.put(SQLConstants.MILESTONE_TABLE_MILESTONE_DESCRIPTION, milestoneDescription);
        contentValues.put(SQLConstants.MILESTONE_TABLE_MILESTONE_STATUS, milestoneStatus);
        contentValues.put(SQLConstants.MILESTONE_TABLE_AMOUNT, amount);
        if (!ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(milestoneStatus)) {
            contentValues.put(SQLConstants.MILESTONE_TABLE_DURATION, duration);
        }
        db.update(SQLConstants.MILESTONE_TABLE, contentValues,
                SQLConstants.TABLE_ID + " = ? ",
                new String[]{String.valueOf(milestoneID)});
//        db.close();
    }

    /**
     * This method to update Milestone IsSelected status
     *
     * @param milestoneID ID of edited milestone
     * @param isSelected  Updated status of isSelected variable
     */
    public void updateMilestoneIsSelectedStatus(int milestoneID, int isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.IS_SELECTED, isSelected);
        db.update(SQLConstants.MILESTONE_TABLE, contentValues,
                SQLConstants.TABLE_ID + " = ? ",
                new String[]{String.valueOf(milestoneID)});
//        db.close();
    }

    /**
     * This method to delete Milestone record
     *
     * @param id ID of to be deleted Milestone
     */
    public void deleteMilestone(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SQLConstants.MILESTONE_TABLE, "id = ? ", new String[]{String.valueOf(id)});
//        db.close();
    }

    /**
     * This method to get all records
     *
     * @return list of Milestones
     */
    public List<CommonModel> getAllMilestone() {
        List<CommonModel> milestoneModelsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_FROM_MILESTONE_TABLE, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            CommonModel milestoneModel = new CommonModel();
            milestoneModel.setId(res.getInt(res.getColumnIndex(SQLConstants.TABLE_ID)));
            milestoneModel.setName(res.getString(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_NAME)));
            milestoneModel.setAge(res.getInt(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_AGE)));
            milestoneModel.setType(res.getString(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_TYPE)));
            milestoneModel.setStatus(res.getString(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_STATUS)));
            milestoneModel.setAmount(res.getFloat(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_AMOUNT)));
            milestoneModel.setDuration(res.getInt(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_DURATION)));
            milestoneModel.setIsSelected(res.getInt(res.getColumnIndex(SQLConstants.IS_SELECTED)));
            milestoneModelsList.add(milestoneModel);
            res.moveToNext();
        }
        res.close();
//        db.close();
        return milestoneModelsList;
    }

    /**
     * This method to get milestone details
     *
     * @param id id of current milestone
     * @return milestone details
     */
    public CommonModel getMilestoneDetails(int id) {
        CommonModel milestoneModel = new CommonModel();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_FROM_MILESTONE_TABLE_WHERE_ID + id, null);
        res.moveToFirst();

        milestoneModel.setId(res.getInt(res.getColumnIndex(SQLConstants.TABLE_ID)));
        milestoneModel.setName(res.getString(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_NAME)));
        milestoneModel.setAge(res.getInt(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_AGE)));
        milestoneModel.setType(res.getString(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_TYPE)));
        milestoneModel.setDescription(res.getString(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_DESCRIPTION)));
        milestoneModel.setStatus(res.getString(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_STATUS)));
        milestoneModel.setDuration(res.getInt(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_DURATION)));
        milestoneModel.setAmount(res.getFloat(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_AMOUNT)));
        milestoneModel.setIsSelected(res.getInt(res.getColumnIndex(SQLConstants.IS_SELECTED)));

        res.close();
//        db.close();
        return milestoneModel;
    }

    /**
     * This method to get all selected milestone
     *
     * @return list of selected milestone
     */
    public List<CommonModel> getAllSelectedMilestone() {
        List<CommonModel> milestoneModelsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_IS_SELECTED_FROM_MILESTONE_TABLE, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            CommonModel milestoneModel = new CommonModel();
            milestoneModel.setId(res.getInt(res.getColumnIndex(SQLConstants.TABLE_ID)));
            milestoneModel.setName(res.getString(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_NAME)));
            milestoneModel.setAge(res.getInt(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_AGE)));
            milestoneModel.setType(res.getString(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_TYPE)));
            milestoneModel.setStatus(res.getString(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_STATUS)));
            milestoneModel.setAmount(res.getFloat(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_AMOUNT)));
            milestoneModel.setDuration(res.getInt(res.getColumnIndex(SQLConstants.MILESTONE_TABLE_DURATION)));
            milestoneModel.setIsSelected(res.getInt(res.getColumnIndex(SQLConstants.IS_SELECTED)));

            milestoneModelsList.add(milestoneModel);
            res.moveToNext();
        }
        res.close();
//        db.close();
        return milestoneModelsList;
    }

    /**
     * This method will insert data into Plan Table
     *
     * @param planName        Plan name
     * @param planType        Plan type
     * @param paymentType     One time payment/recurring
     * @param premiumStartAge Age when premium is bought
     * @param paymentAmount   Payment amount
     * @param planDuration    Plan duration
     * @param payoutAge       Age when payout is given
     * @param payoutAmount    Payout amount
     * @param payoutDuration  Payout duration
     * @param planStatus      Existing/Non-Existing
     */
    public void insertPlan(String planName, String planType, String paymentType, int premiumStartAge,
                           float paymentAmount, int planDuration, int payoutAge, float payoutAmount,
                           int payoutDuration, String planStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.PLAN_TABLE_PLAN_NAME, planName);
        contentValues.put(SQLConstants.PLAN_TABLE_PLAN_TYPE, planType);
        contentValues.put(SQLConstants.PLAN_TABLE_PAYMENT_TYPE, paymentType);
        contentValues.put(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE, premiumStartAge);
        contentValues.put(SQLConstants.PLAN_TABLE_PAYMENT_AMOUNT, paymentAmount);
        contentValues.put(SQLConstants.PLAN_TABLE_PAYOUT_AGE, payoutAge);
        contentValues.put(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT, payoutAmount);
        contentValues.put(SQLConstants.PLAN_TABLE_PAYOUT_DURATION, payoutDuration);
        contentValues.put(SQLConstants.PLAN_TABLE_PLAN_STATUS, planStatus);

        if (planDuration != -1) {
            contentValues.put(SQLConstants.PLAN_TABLE_PLAN_DURATION, planDuration);
        }
        db.insert(SQLConstants.PLAN_TABLE, null, contentValues);
//        db.close();
    }

    /**
     * This method will update Plan details
     *
     * @param planID          ID of edited plan
     * @param planName        Edited plan name
     * @param planType        Edited plan type
     * @param paymentType     Edited payment type
     * @param premiumStartAge Edited age when premium is bought
     * @param paymentAmount   Edited payment amount
     * @param planDuration    Edited plan duration
     * @param payoutAge       Edited age when payout is given
     * @param payoutAmount    Edited payout amount
     * @param payoutDuration  Edited payout duration
     * @param planStatus      Edited plan status
     */
    public void updatePlan(int planID, String planName, String planType, String paymentType,
                           int premiumStartAge, float paymentAmount, int planDuration,
                           int payoutAge, float payoutAmount, int payoutDuration, String planStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.PLAN_TABLE_PLAN_NAME, planName);
        contentValues.put(SQLConstants.PLAN_TABLE_PLAN_TYPE, planType);
        contentValues.put(SQLConstants.PLAN_TABLE_PAYMENT_TYPE, paymentType);
        contentValues.put(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE, premiumStartAge);
        contentValues.put(SQLConstants.PLAN_TABLE_PAYMENT_AMOUNT, paymentAmount);
        contentValues.put(SQLConstants.PLAN_TABLE_PAYOUT_AGE, payoutAge);
        contentValues.put(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT, payoutAmount);
        contentValues.put(SQLConstants.PLAN_TABLE_PAYOUT_DURATION, payoutDuration);
        contentValues.put(SQLConstants.PLAN_TABLE_PLAN_STATUS, planStatus);

        if (planDuration != -1) {
            contentValues.put(SQLConstants.PLAN_TABLE_PLAN_DURATION, planDuration);
        }
        db.update(SQLConstants.PLAN_TABLE, contentValues,
                SQLConstants.TABLE_ID + " = ? ",
                new String[]{String.valueOf(planID)});
//        db.close();
    }

    /**
     * This method to update Plan IsSelected status
     *
     * @param planID     ID of edited plan
     * @param isSelected Updated status of isSelected variable
     */
    public void updatePlanIsSelectedStatus(int planID, int isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstants.IS_SELECTED, isSelected);
        db.update(SQLConstants.PLAN_TABLE, contentValues,
                SQLConstants.TABLE_ID + " = ? ",
                new String[]{String.valueOf(planID)});
//        db.close();
    }

    /**
     * This method to delete Plan record
     *
     * @param id ID of to be deleted plan
     */
    public void deletePlan(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SQLConstants.PLAN_TABLE, "id = ? ", new String[]{String.valueOf(id)});
//        db.close();
    }

    /**
     * This method to get all records
     *
     * @return list of Plans
     */
    public List<PlanModel> getAllPlan() {
        List<PlanModel> planModelsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_FROM_PLAN_TABLE, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            PlanModel planModel = new PlanModel();
            planModel.setId(res.getInt(res.getColumnIndex(SQLConstants.TABLE_ID)));
            planModel.setPlanName(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_NAME)));
            planModel.setPlanType(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_TYPE)));
            planModel.setPaymentType(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYMENT_TYPE)));
            planModel.setPremiumStartAge(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE)));
            planModel.setPaymentAmount(res.getFloat(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYMENT_AMOUNT)));
            planModel.setPlanDuration(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_DURATION)));
            planModel.setPayoutAge(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_AGE)));
            planModel.setPayoutAmount(res.getFloat(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT)));
            planModel.setPayoutDuration(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_DURATION)));
            planModel.setPlanStatus(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_STATUS)));
            planModel.setIsSelected(res.getInt(res.getColumnIndex(SQLConstants.IS_SELECTED)));

            planModelsList.add(planModel);
            res.moveToNext();
        }
        res.close();
//        db.close();
        return planModelsList;
    }

    /**
     * This method to get all selected plan
     *
     * @return list of selected plan
     */
    public List<PlanModel> getAllSelectedPlan() {
        List<PlanModel> planModelsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_IS_SELECTED_FROM_PLAN_TABLE, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            PlanModel planModel = new PlanModel();
            planModel.setId(res.getInt(res.getColumnIndex(SQLConstants.TABLE_ID)));
            planModel.setPlanName(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_NAME)));
            planModel.setPlanType(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_TYPE)));
            planModel.setPaymentType(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYMENT_TYPE)));
            planModel.setPremiumStartAge(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE)));
            planModel.setPaymentAmount(res.getFloat(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYMENT_AMOUNT)));
            planModel.setPlanDuration(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_DURATION)));
            planModel.setPayoutAge(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_AGE)));
            planModel.setPayoutAmount(res.getFloat(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT)));
            planModel.setPayoutDuration(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_DURATION)));
            planModel.setPlanStatus(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_STATUS)));
            planModel.setIsSelected(res.getInt(res.getColumnIndex(SQLConstants.IS_SELECTED)));
            planModelsList.add(planModel);
            res.moveToNext();
        }
        res.close();
//        db.close();
        return planModelsList;
    }

    /**
     * This method to get all existing plan
     *
     * @return list of selected plan
     */
    public List<PlanModel> getAllExistingPlan() {
        List<PlanModel> planModelsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_EXISTING_FROM_PLAN_TABLE, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            PlanModel planModel = new PlanModel();
            planModel.setId(res.getInt(res.getColumnIndex(SQLConstants.TABLE_ID)));
            planModel.setPlanName(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_NAME)));
            planModel.setPlanType(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_TYPE)));
            planModel.setPaymentType(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYMENT_TYPE)));
            planModel.setPremiumStartAge(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE)));
            planModel.setPaymentAmount(res.getFloat(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYMENT_AMOUNT)));
            planModel.setPlanDuration(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_DURATION)));
            planModel.setPayoutAge(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_AGE)));
            planModel.setPayoutAmount(res.getFloat(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT)));
            planModel.setPayoutDuration(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_DURATION)));
            planModel.setPlanStatus(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_STATUS)));
            planModel.setIsSelected(res.getInt(res.getColumnIndex(SQLConstants.IS_SELECTED)));
            planModelsList.add(planModel);
            res.moveToNext();
        }
        res.close();
//        db.close();
        return planModelsList;
    }

    /**
     * This method to get all non-existing plan
     *
     * @return list of selected plan
     */
    public List<PlanModel> getAllNonExistingPlan() {
        List<PlanModel> planModelsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_NON_EXISTING_FROM_PLAN_TABLE, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            PlanModel planModel = new PlanModel();
            planModel.setId(res.getInt(res.getColumnIndex(SQLConstants.TABLE_ID)));
            planModel.setPlanName(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_NAME)));
            planModel.setPlanType(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_TYPE)));
            planModel.setPaymentType(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYMENT_TYPE)));
            planModel.setPremiumStartAge(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE)));
            planModel.setPaymentAmount(res.getFloat(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYMENT_AMOUNT)));
            planModel.setPlanDuration(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_DURATION)));
            planModel.setPayoutAge(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_AGE)));
            planModel.setPayoutAmount(res.getFloat(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT)));
            planModel.setPayoutDuration(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_DURATION)));
            planModel.setPlanStatus(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_STATUS)));
            planModel.setIsSelected(res.getInt(res.getColumnIndex(SQLConstants.IS_SELECTED)));
            planModelsList.add(planModel);
            res.moveToNext();
        }
        res.close();
//        db.close();
        return planModelsList;
    }

    /**
     * This method to get plan details
     *
     * @param id id of current plan
     * @return plan details
     */
    public PlanModel getPlanDetails(int id) {
        PlanModel planModel = new PlanModel();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQLConstants.SELECT_ALL_FROM_PLAN_TABLE_WHERE_ID + id, null);
        res.moveToFirst();

        planModel.setId(res.getInt(res.getColumnIndex(SQLConstants.TABLE_ID)));
        planModel.setPlanName(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_NAME)));
        planModel.setPlanType(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_TYPE)));
        planModel.setPaymentType(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYMENT_TYPE)));
        planModel.setPremiumStartAge(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PREMIUM_START_AGE)));
        planModel.setPaymentAmount(res.getFloat(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYMENT_AMOUNT)));
        planModel.setPlanDuration(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_DURATION)));
        planModel.setPayoutAge(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_AGE)));
        planModel.setPayoutDuration(res.getInt(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_DURATION)));
        planModel.setPayoutAmount(res.getFloat(res.getColumnIndex(SQLConstants.PLAN_TABLE_PAYOUT_AMOUNT)));
        planModel.setPlanStatus(res.getString(res.getColumnIndex(SQLConstants.PLAN_TABLE_PLAN_STATUS)));
        planModel.setIsSelected(res.getInt(res.getColumnIndex(SQLConstants.IS_SELECTED)));

        res.close();
//        db.close();
        return planModel;
    }

    /**
     * This method to return number of rows in the table
     *
     * @return number of rows
     */
    public int numberOfRows(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numberOfRows = (int) DatabaseUtils.queryNumEntries(db, tableName);
//        db.close();
        return numberOfRows;
    }

    /**
     * This method to delete all records
     */
    public void deleteAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQLConstants.DELETE_USER_TABLE);
        //todo need to uncomment
        db.execSQL(SQLConstants.DELETE_EVENT_TABLE);
        db.execSQL(SQLConstants.DELETE_MILESTONE_TABLE);
        db.execSQL(SQLConstants.DELETE_PLAN_TABLE);
//        db.close();
    }
}
