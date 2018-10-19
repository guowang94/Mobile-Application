package com.example.android.graphapplication.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.constants.ErrorMsgConstants;
import com.example.android.graphapplication.constants.KeyConstants;
import com.example.android.graphapplication.constants.SQLConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.validations.Validation;
import com.satsuware.usefulviews.LabelledSpinner;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class EventActivity extends AppCompatActivity implements
        LabelledSpinner.OnItemChosenListener {

    private static final String TAG = "EventActivity";

    private Toolbar mToolbar;
    private TextInputLayout mEventNameInputLayout;
    private LabelledSpinner mEventTypeSpinner;
    private LabelledSpinner mAgeSpinner;
    private TextInputLayout mEventDescriptionInputLayout;
    private SegmentedButtonGroup mEventStatusSegmentedButton;
    private TextView mToolbarTitle;
    private ConstraintLayout mLayout;
    private TextInputLayout mAmountInputLayout;
    private EditText mAmountEditText;
    private TextInputLayout mCostInputLayout;
    private EditText mCostEditText;
    private TextInputLayout mDurationInputLayout;
    private EditText mDurationEditText;
    private DBHelper mydb;

    private String eventTypeSpinnerValue;
    private String yearSpinnerValue;
    private String eventAction;
    private int currentEventID;
    private String ageRange[];

    private Validation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        //TODO need to set onFocusChangeListener for programmatically created TextInputLayout

        mToolbar = findViewById(R.id.create_event_toolbar);
        mEventNameInputLayout = findViewById(R.id.name_input_layout);
        mEventTypeSpinner = findViewById(R.id.event_type_spinner);
        mAgeSpinner = findViewById(R.id.age_spinner);
        mEventDescriptionInputLayout = findViewById(R.id.description_input_layout);
        mEventStatusSegmentedButton = findViewById(R.id.event_status_segmented_button);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mLayout = findViewById(R.id.layout);

        mAmountInputLayout = new TextInputLayout(new ContextThemeWrapper(
                EventActivity.this, R.style.Widget_Design_TextInputLayout_textColorHint));
        mCostInputLayout = new TextInputLayout(new ContextThemeWrapper(
                EventActivity.this, R.style.Widget_Design_TextInputLayout_textColorHint));
        mDurationInputLayout = new TextInputLayout(new ContextThemeWrapper(
                EventActivity.this, R.style.Widget_Design_TextInputLayout_textColorHint));
        mAmountEditText = new EditText(this);
        mCostEditText = new EditText(this);
        mDurationEditText = new EditText(this);

        mydb = new DBHelper(getApplicationContext());
        validation = new Validation();

        if (getIntent() != null) {
            eventAction = getIntent().getStringExtra(KeyConstants.INTENT_KEY_ACTION);
            currentEventID = getIntent().getIntExtra(
                    KeyConstants.INTENT_KEY_RECYCLER_VIEW_POSITION, -1);
            Log.d(TAG, "onCreate: displayData, " + currentEventID);
        }

        initData();
        Log.d(TAG, "onCreate: out");
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        Log.d(TAG, "initData: in");
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this mToolbar
        ActionBar actionBar = getSupportActionBar();
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_ADD_EVENT);

        // Enable the top left button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Draw TextInputLayout based on Event Status
        drawTextInputLayout(mEventStatusSegmentedButton.getPosition());

        mEventStatusSegmentedButton.setOnPositionChangedListener(
                new SegmentedButtonGroup.OnPositionChangedListener() {
                    @Override
                    public void onPositionChanged(int position) {
                        Log.d(TAG, "onPositionChanged: " + position);
                        drawTextInputLayout(position);
                    }
                });

        setupSpinners();

        if (KeyConstants.INTENT_KEY_VALUE_EDIT.equalsIgnoreCase(eventAction) && currentEventID != -1) {
            displayData();
        }

        Log.d(TAG, "initData: out");
    }

    /**
     * This method will display previous values of the event
     */
    private void displayData() {
        Log.d(TAG, "displayData: in");

        Cursor rs = mydb.getData(SQLConstants.EVENT_TABLE, currentEventID);
        rs.moveToFirst();

        String eventName = rs.getString(rs.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_NAME));
        String eventType = rs.getString(rs.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_TYPE));
        String yearOccurred = rs.getString(rs.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_AGE));
        String description = rs.getString(rs.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_DESCRIPTION));
        String eventStatus = rs.getString(rs.getColumnIndex(SQLConstants.EVENT_TABLE_EVENT_STATUS));
        String amount = String.valueOf(rs.getFloat(rs.getColumnIndex(SQLConstants.EVENT_TABLE_AMOUNT)));
        String duration = String.valueOf(rs.getInt(rs.getColumnIndex(SQLConstants.EVENT_TABLE_DURATION)));

        if (!rs.isClosed()) {
            rs.close();
        }

        Log.d(TAG, "displayData: " + eventName);

        if (mEventNameInputLayout.getEditText() != null) {
            mEventNameInputLayout.getEditText().setText(eventName);
        }

        String[] eventArray = getResources().getStringArray(R.array.event_type_array);
        for (int i = 0; i < eventArray.length; i++) {
            if (eventArray[i].equalsIgnoreCase(eventType)) {
                mEventTypeSpinner.setSelection(i);
            }
        }

        for (int i = 0; i < ageRange.length; i++) {
            if (ageRange[i].equalsIgnoreCase(yearOccurred)) {
                mAgeSpinner.setSelection(i);
            }
        }

        if (description != null && !description.equalsIgnoreCase("")) {
            if (mEventDescriptionInputLayout.getEditText() != null) {
                mEventDescriptionInputLayout.getEditText().setText(description);
            }
        }

        if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equalsIgnoreCase(eventStatus)) {
            mEventStatusSegmentedButton.setPosition(0);
            if (mAmountInputLayout.getEditText() != null) {
                mAmountInputLayout.getEditText().setText(amount);
            }
        } else {
            mEventStatusSegmentedButton.setPosition(1);
            if (mDurationInputLayout.getEditText() != null) {
                mDurationInputLayout.getEditText().setText(duration);
            }
            if (mCostInputLayout.getEditText() != null) {
                mCostInputLayout.getEditText().setText(amount);
            }
        }
        Log.d(TAG, "displayData: out");
    }

    /**
     * This method will draw the view based on selected segmented button
     *
     * @param position of segmented button
     */
    private void drawTextInputLayout(int position) {
        Log.d(TAG, "drawTextInputLayout: in, position: " + position);

        //To add a view, need to add view to layout then clone constraint set
        //then connect constraint and lastly apply constraint
        if (position == 0) {
            Log.d(TAG, "drawTextInputLayout: in if()");

            mLayout.removeView(findViewById(R.id.duration_input_layout));
            mLayout.removeView(findViewById(R.id.cost_per_year_input_layout));

            mAmountInputLayout.setHint(getResources().getString(R.string.amount));
            mAmountInputLayout.setId(R.id.amount_input_layout);
            mAmountEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (mAmountEditText.getParent() == null) {
                mAmountInputLayout.addView(mAmountEditText);
            }
            mLayout.addView(mAmountInputLayout);

            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            mAmountInputLayout.setLayoutParams(layoutParams);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(mLayout);

            constraintSet.connect(mAmountInputLayout.getId(), ConstraintSet.TOP,
                    mEventStatusSegmentedButton.getId(), ConstraintSet.BOTTOM, 16);
            constraintSet.connect(mAmountInputLayout.getId(), ConstraintSet.START,
                    mEventStatusSegmentedButton.getId(), ConstraintSet.START, 0);
            constraintSet.connect(mAmountInputLayout.getId(), ConstraintSet.END,
                    mEventStatusSegmentedButton.getId(), ConstraintSet.END, 0);

            constraintSet.applyTo(mLayout);
        } else {
            Log.d(TAG, "drawTextInputLayout: in else()");

            mLayout.removeView(findViewById(R.id.amount_input_layout));

            mDurationInputLayout.setHint(getResources().getString(R.string.duration_in_year));
            mDurationInputLayout.setId(R.id.duration_input_layout);
            mDurationEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (mDurationEditText.getParent() == null) {
                mDurationInputLayout.addView(mDurationEditText);
            }
            mLayout.addView(mDurationInputLayout);

            mCostInputLayout.setHint(getResources().getString(R.string.cost_per_year));
            mCostInputLayout.setId(R.id.cost_per_year_input_layout);
            mCostEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (mCostEditText.getParent() == null) {
                mCostInputLayout.addView(mCostEditText);
            }
            mLayout.addView(mCostInputLayout);

            mDurationInputLayout.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT));
            mCostInputLayout.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT));

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(mLayout);

            constraintSet.connect(mDurationInputLayout.getId(), ConstraintSet.TOP,
                    mEventStatusSegmentedButton.getId(), ConstraintSet.BOTTOM, 16);
            constraintSet.connect(mDurationInputLayout.getId(), ConstraintSet.START,
                    mEventStatusSegmentedButton.getId(), ConstraintSet.START, 0);
            constraintSet.connect(mDurationInputLayout.getId(), ConstraintSet.END,
                    mEventStatusSegmentedButton.getId(), ConstraintSet.END, 0);

            constraintSet.connect(mCostInputLayout.getId(), ConstraintSet.TOP,
                    mDurationInputLayout.getId(), ConstraintSet.BOTTOM, 16);
            constraintSet.connect(mCostInputLayout.getId(), ConstraintSet.START,
                    mDurationInputLayout.getId(), ConstraintSet.START, 0);
            constraintSet.connect(mCostInputLayout.getId(), ConstraintSet.END,
                    mDurationInputLayout.getId(), ConstraintSet.END, 0);

            constraintSet.applyTo(mLayout);
        }
        Log.d(TAG, "drawTextInputLayout: out");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra(
                KeyConstants.INTENT_KEY_FRAGMENT_POSITION, 1));
    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView,
                             View itemView, int position, long id) {
        switch (labelledSpinner.getId()) {
            case R.id.event_type_spinner:
                Log.i(TAG, "onItemChosen: " + mEventTypeSpinner.getSpinner()
                        .getItemAtPosition(position).toString());
                eventTypeSpinnerValue = mEventTypeSpinner.getSpinner()
                        .getItemAtPosition(position).toString();
                break;
            case R.id.age_spinner:
                Log.i(TAG, "onItemChosen: " + mAgeSpinner.getSpinner()
                        .getItemAtPosition(position).toString());
                yearSpinnerValue = mAgeSpinner.getSpinner().getItemAtPosition(position).toString();
                break;

            default:
                Log.d(TAG, "onItemChosen: None of the Spinner's id are matched");
        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
        // Do something here
    }

    /**
     * This method will create the more option in the action bar
     *
     * @param menu store all the menu items
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method will tap on Option Item
     *
     * @param menuItem store all the menu items
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.save:
                //Validation
                validation.blankFieldValidation(mEventNameInputLayout);

                if (mAmountInputLayout.isAttachedToWindow()) {
                    if (!validation.blankFieldValidation(mAmountInputLayout)) {
                        validation.negativeValueValidation(mAmountInputLayout);
                    }
                }

                if (mDurationInputLayout.isAttachedToWindow()) {
                    if (!validation.blankFieldValidation(mDurationInputLayout)) {
                        validation.negativeValueValidation(mDurationInputLayout);
                    }
                }

                if (mCostInputLayout.isAttachedToWindow()) {
                    if (!validation.blankFieldValidation(mCostInputLayout)) {
                        validation.negativeValueValidation(mCostInputLayout);
                    }
                }

                boolean isErrorEnabled = false;

                if (mEventNameInputLayout.isErrorEnabled()) {
                    isErrorEnabled = true;

                    if (mAmountInputLayout.isAttachedToWindow()) {
                        if (mAmountInputLayout.isErrorEnabled()) {
                            isErrorEnabled = true;
                        }
                    }

                    if (mDurationInputLayout.isAttachedToWindow()) {
                        if (mDurationInputLayout.isErrorEnabled()) {
                            isErrorEnabled = true;
                        }
                    }

                    if (mCostInputLayout.isAttachedToWindow()) {
                        if (mCostInputLayout.isErrorEnabled()) {
                            isErrorEnabled = true;
                        }
                    }
                }

                if (!isErrorEnabled) {
                    String eventName = null;
                    String description = null;
                    String eventStatus = mEventStatusSegmentedButton.getPosition() == 0 ?
                            ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME :
                            ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING;
                    float amount = 0f;
                    int duration = 1;

                    if (mEventNameInputLayout.getEditText() != null) {
                        eventName = mEventNameInputLayout.getEditText().getText().toString();
                    }

                    if (mEventDescriptionInputLayout.getEditText() != null) {
                        description = mEventDescriptionInputLayout.getEditText().getText().toString();
                    }

                    if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(eventStatus)) {
                        if (mAmountInputLayout.getEditText() != null) {
                            amount = Float.valueOf(mAmountInputLayout.getEditText().getText().toString());
                        }
                    } else {
                        if (mDurationInputLayout.getEditText() != null) {
                            duration = Integer.valueOf(mDurationInputLayout.getEditText().getText().toString());
                        }
                        if (mCostInputLayout.getEditText() != null) {
                            amount = Float.valueOf(mCostInputLayout.getEditText().getText().toString());
                        }
                    }

                    if (KeyConstants.INTENT_KEY_VALUE_CREATE.equalsIgnoreCase(eventAction)) {

                        mydb.insertEvent(eventName, eventTypeSpinnerValue, yearSpinnerValue, description,
                                eventStatus, amount, duration);

                    } else if (KeyConstants.INTENT_KEY_VALUE_EDIT.equalsIgnoreCase(eventAction)) {

                        mydb.updateEvent(currentEventID, eventName, eventTypeSpinnerValue, yearSpinnerValue,
                                description, eventStatus, amount, duration);
                    }

                    startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra(
                            KeyConstants.INTENT_KEY_FRAGMENT_POSITION, 1));
                } else {
                    Snackbar.make(mLayout, ErrorMsgConstants.ERR_MSG_ENTER_VALID_INPUT,
                            Snackbar.LENGTH_LONG).show();
                }
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                Log.i(TAG, "onOptionsItemSelected: In default");
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * This method will setup Spinners' item
     */
    private void setupSpinners() {
        mEventTypeSpinner.setItemsArray(R.array.event_type_array);
        mEventTypeSpinner.setOnItemChosenListener(this);

        Cursor rs = mydb.getData(SQLConstants.USER_TABLE, 1);
        rs.moveToFirst();

        int currentAge = rs.getInt(rs.getColumnIndex(SQLConstants.USER_TABLE_AGE));
        int expectancyAge = rs.getInt(rs.getColumnIndex(SQLConstants.USER_TABLE_EXPECTANCY));

        if (!rs.isClosed()) {
            rs.close();
        }

        //Create age range
        ageRange = new String[expectancyAge - currentAge + 1];
        for (int i = 0; i < ageRange.length; i++) {
            ageRange[i] = currentAge + "";
            currentAge++;
        }
        mAgeSpinner.setItemsArray(ageRange);
        mAgeSpinner.setOnItemChosenListener(this);
    }
}
