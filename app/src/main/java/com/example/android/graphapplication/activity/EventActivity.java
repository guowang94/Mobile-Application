package com.example.android.graphapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.graphapplication.DAOFile;
import com.example.android.graphapplication.R;
import com.example.android.graphapplication.constants.KeyConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.Calendar;
import java.util.HashMap;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class EventActivity extends AppCompatActivity implements
        LabelledSpinner.OnItemChosenListener {

    private static final String TAG = "EventActivity";

    private Toolbar mToolbar;
    private TextInputLayout mEventNameInputLayout;
    private LabelledSpinner mEventTypeSpinner;
    private LabelledSpinner mYearSpinner;
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

    private String eventTypeSpinnerValue;
    private String yearSpinnerValue;
    private String fileContent;
    private HashMap<String, String> content;
    private String eventAction;
    private int currentEventID;
    private String yearRange[];

    //Before Edited Values
    private String eventName;
    private String eventType;
    private String yearOccurred;
    private String description;
    private String eventStatus;
    private String amount;
    private String duration;
    private String cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        //TODO need to find a way to save the data (even after user close the application) (Low Priority)
        //TODO need to add validation for all the fields (Low Priority)

        mToolbar = findViewById(R.id.create_event_toolbar);
        mEventNameInputLayout = findViewById(R.id.name_input_layout);
        mEventTypeSpinner = findViewById(R.id.event_type_spinner);
        mYearSpinner = findViewById(R.id.year_spinner);
        mEventDescriptionInputLayout = findViewById(R.id.description_input_layout);
        mEventStatusSegmentedButton = findViewById(R.id.event_status_segmented_button);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mLayout = findViewById(R.id.layout);
        mAmountInputLayout = new TextInputLayout(this);
        mAmountEditText = new EditText(this);
        mCostInputLayout = new TextInputLayout(this);
        mCostEditText = new EditText(this);
        mDurationInputLayout = new TextInputLayout(this);
        mDurationEditText = new EditText(this);

        if (getIntent() != null) {
            eventAction = getIntent().getStringExtra(KeyConstants.INTENT_KEY_EVENT_ACTION);
            currentEventID = getIntent().getIntExtra(
                    KeyConstants.INTENT_KEY_RECYCLER_VIEW_POSITION, -1);
            currentEventID = currentEventID == -1 ? currentEventID : currentEventID + 1;
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
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_EVENTS);
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));

        // Enable the top left button
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get the content from internal storage file
        Context context = getApplicationContext();
        fileContent = new DAOFile().readFile(context, KeyConstants.FILE_USER_INFO);
        content = new DAOFile().splitFileContent(fileContent);
        Log.i(TAG, "initData: " + content);
        Log.d(TAG, "initData: out");

        drawTextInputLayout(mEventStatusSegmentedButton.getPosition());

        mEventStatusSegmentedButton.setOnPositionChangedListener(
                new SegmentedButtonGroup.OnPositionChangedListener() {
                    @Override
                    public void onPositionChanged(int position) {
                        Log.d(TAG, "onPositionChanged: " + position);
                        drawTextInputLayout(position);
                    }
                });

        mEventTypeSpinner.setItemsArray(R.array.event_type_array);
        mEventTypeSpinner.setOnItemChosenListener(this);

        //Create year range
        int currentAge = Integer.valueOf(content.get(KeyConstants.CONTENT_AGE));
        int expectancyAge = Integer.valueOf(content.get(KeyConstants.CONTENT_EXPECTANCY));
        yearRange = new String[expectancyAge - currentAge + 1];
        int startYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < yearRange.length; i++) {
            yearRange[i] = startYear + "";
            startYear++;
        }
        mYearSpinner.setItemsArray(yearRange);
        mYearSpinner.setOnItemChosenListener(this);

        //If the Event Action is edit, then it will display previous data
        displayData();
    }

    /**
     * This method will display previous values of the event
     */
    private void displayData() {
        Log.d(TAG, "displayData: in");
        if ("Edit".equalsIgnoreCase(eventAction) && currentEventID != -1) {
            eventName = content.get(KeyConstants.CONTENT_EVENT_NAME + currentEventID);
            eventType = content.get(KeyConstants.CONTENT_EVENT_TYPE + currentEventID);
            yearOccurred = content.get(KeyConstants.CONTENT_EVENT_YEAR_OCCURRED + currentEventID);
            description = content.get(KeyConstants.CONTENT_EVENT_DESCRIPTION + currentEventID);
            eventStatus = content.get(KeyConstants.CONTENT_EVENT_STATUS + currentEventID);
            amount = content.get(KeyConstants.CONTENT_EVENT_AMOUNT + currentEventID);
            duration = content.get(KeyConstants.CONTENT_EVENT_DURATION + currentEventID);
            cost = content.get(KeyConstants.CONTENT_EVENT_COST_PER_MONTH + currentEventID);

            mEventNameInputLayout.getEditText().setText(eventName);

            String[] eventArray = getResources().getStringArray(R.array.event_type_array);
            for (int i = 0; i < eventArray.length; i++) {
                if (eventArray[i].equalsIgnoreCase(eventType)) {
                    mEventTypeSpinner.setSelection(i);
                }
            }

            for (int i = 0; i < yearRange.length; i++) {
                if (yearRange[i].equalsIgnoreCase(yearOccurred)) {
                    mYearSpinner.setSelection(i);
                }
            }

            if (description != null && !description.equalsIgnoreCase("empty string")) {
                mEventDescriptionInputLayout.getEditText().setText(description);
            }

            if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equalsIgnoreCase(eventStatus)) {
                mEventStatusSegmentedButton.setPosition(0);
                mAmountInputLayout.getEditText().setText(amount);
            } else {
                mEventStatusSegmentedButton.setPosition(1);
                mDurationInputLayout.getEditText().setText(duration);
                mCostInputLayout.getEditText().setText(cost);
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
        // then connect constraint and lastly apply constraint
        if (position == 0) {
            Log.d(TAG, "drawTextInputLayout: in if()");

            mLayout.removeView(findViewById(R.id.duration_input_layout));
            mLayout.removeView(findViewById(R.id.cost_input_layout));

            mAmountInputLayout.setHint(getResources().getString(R.string.amount));
            mAmountInputLayout.setId(R.id.amount_input_layout);
            mAmountInputLayout.setHintTextAppearance(R.style.input_layout_hint_color);
            if (mAmountEditText.getParent() == null)
                mAmountInputLayout.addView(mAmountEditText);
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

            mDurationInputLayout.setHint(getResources().getString(R.string.duration));
            mDurationInputLayout.setId(R.id.duration_input_layout);
            mDurationInputLayout.setHintTextAppearance(R.style.input_layout_hint_color);
            if (mDurationEditText.getParent() == null)
                mDurationInputLayout.addView(mDurationEditText);
            mLayout.addView(mDurationInputLayout);

            mCostInputLayout.setHint(getResources().getString(R.string.cost_per_month));
            mCostInputLayout.setId(R.id.cost_input_layout);
            mCostInputLayout.setHintTextAppearance(R.style.input_layout_hint_color);
            if (mCostEditText.getParent() == null)
                mCostInputLayout.addView(mCostEditText);
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
        startActivity(new Intent(this, MainActivity.class).putExtra(
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
            case R.id.year_spinner:
                Log.i(TAG, "onItemChosen: " + mYearSpinner.getSpinner()
                        .getItemAtPosition(position).toString());
                yearSpinnerValue = mYearSpinner.getSpinner().getItemAtPosition(position).toString();
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
        getMenuInflater().inflate(R.menu.create_event_menu, menu);
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
                String description = mEventDescriptionInputLayout.getEditText().getText().toString();
                description = description.equals("") ? "empty string" : description;

                if ("Create".equalsIgnoreCase(eventAction)) {
                    int eventCount = Integer.valueOf(content.get(KeyConstants.CONTENT_EVENT_COUNT)) + 1;

                    if (mEventStatusSegmentedButton.getPosition() == 0) {
                        fileContent += "//" + KeyConstants.CONTENT_EVENT_STATUS + eventCount +
                                ":" + ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME +
                                "//" + KeyConstants.CONTENT_EVENT_AMOUNT + eventCount +
                                ":" + mAmountInputLayout.getEditText().getText().toString();
                    } else {
                        fileContent += "//" + KeyConstants.CONTENT_EVENT_STATUS + eventCount +
                                ":" + ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING +
                                "//" + KeyConstants.CONTENT_EVENT_COST_PER_MONTH + eventCount +
                                ":" + mCostInputLayout.getEditText().getText().toString() +
                                "//" + KeyConstants.CONTENT_EVENT_DURATION + eventCount +
                                ":" + mDurationInputLayout.getEditText().getText().toString();
                    }

                    fileContent += "//" + KeyConstants.CONTENT_EVENT_NAME + eventCount +
                            ":" + mEventNameInputLayout.getEditText().getText().toString() +
                            "//" + KeyConstants.CONTENT_EVENT_TYPE + eventCount +
                            ":" + eventTypeSpinnerValue +
                            "//" + KeyConstants.CONTENT_EVENT_YEAR_OCCURRED + eventCount +
                            ":" + yearSpinnerValue +
                            "//" + KeyConstants.CONTENT_EVENT_DESCRIPTION + eventCount +
                            ":" + description +
                            "//" + KeyConstants.CONTENT_EVENT_COUNT + ":" + eventCount;

                } else if ("Edit".equalsIgnoreCase(eventAction)){
                    fileContent = fileContent
                            .replace(KeyConstants.CONTENT_EVENT_NAME + currentEventID +
                                            ":" + eventName,
                                    KeyConstants.CONTENT_EVENT_NAME + currentEventID +
                                            ":" + mEventNameInputLayout.getEditText().getText().toString())
                            .replace(KeyConstants.CONTENT_EVENT_TYPE + currentEventID +
                                            ":" + eventType,
                                    KeyConstants.CONTENT_EVENT_TYPE + currentEventID +
                                            ":" + eventTypeSpinnerValue)
                            .replace(KeyConstants.CONTENT_EVENT_YEAR_OCCURRED + currentEventID +
                                    ":" + yearOccurred,
                                    KeyConstants.CONTENT_EVENT_YEAR_OCCURRED + currentEventID +
                                            ":" + yearSpinnerValue)
                            .replace(KeyConstants.CONTENT_EVENT_DESCRIPTION + currentEventID +
                                    ":" + this.description,
                                    KeyConstants.CONTENT_EVENT_DESCRIPTION + currentEventID +
                                            ":" + description);

                    if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(eventStatus) &&
                            mEventStatusSegmentedButton.getPosition() == 0) {

                        //Replace amount value
                        fileContent = fileContent.replace(
                                KeyConstants.CONTENT_EVENT_AMOUNT + currentEventID +
                                        ":" + amount,
                                KeyConstants.CONTENT_EVENT_AMOUNT + currentEventID +
                                        ":" + mAmountInputLayout.getEditText().getText().toString());

                    } else if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(eventStatus) &&
                    mEventStatusSegmentedButton.getPosition() == 1) {

                        //Replace event status and add in cost per month and duration value
                        fileContent = fileContent.replace(
                                KeyConstants.CONTENT_EVENT_STATUS + currentEventID +
                                        ":" + eventStatus,
                                KeyConstants.CONTENT_EVENT_STATUS + currentEventID +
                                        ":" + ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING);

                        fileContent += "//" + KeyConstants.CONTENT_EVENT_COST_PER_MONTH + currentEventID +
                                ":" + mCostInputLayout.getEditText().getText().toString() +
                                "//" + KeyConstants.CONTENT_EVENT_DURATION + currentEventID +
                                ":" + mDurationInputLayout.getEditText().getText().toString();

                    } else if (ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING.equals(eventStatus) &&
                            mEventStatusSegmentedButton.getPosition() == 1) {

                        //Replace cost per month and duration value
                        fileContent = fileContent.replace(
                                KeyConstants.CONTENT_EVENT_COST_PER_MONTH + currentEventID +
                                        ":" + cost,
                                KeyConstants.CONTENT_EVENT_COST_PER_MONTH + currentEventID +
                                        ":" + mCostInputLayout.getEditText().getText().toString())
                                .replace(KeyConstants.CONTENT_EVENT_DURATION + currentEventID +
                                        ":" + duration,
                                        KeyConstants.CONTENT_EVENT_DURATION + currentEventID +
                                                ":" + mDurationInputLayout.getEditText().getText().toString());

                    } else if (ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING.equals(eventStatus) &&
                    mEventStatusSegmentedButton.getPosition() == 0) {

                        //Replace event status and add in amount value
                        fileContent = fileContent.replace(
                                KeyConstants.CONTENT_EVENT_STATUS + currentEventID +
                                        ":" + eventStatus,
                                KeyConstants.CONTENT_EVENT_STATUS + currentEventID +
                                        ":" + ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME);

                        fileContent += "//" + KeyConstants.CONTENT_EVENT_AMOUNT + currentEventID +
                                ":" + mAmountInputLayout.getEditText().getText().toString();
                    }
                }

                //Saving data in internal storage
                new DAOFile().saveDate(fileContent, getApplicationContext());

                startActivity(new Intent(this, MainActivity.class).putExtra(
                        KeyConstants.INTENT_KEY_FRAGMENT_POSITION, 1));
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                Log.i(TAG, "onOptionsItemSelected: In default");
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
