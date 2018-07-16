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

import com.example.android.graphapplication.Constants;
import com.example.android.graphapplication.R;
import com.example.android.graphapplication.ReadFileData;
import com.satsuware.usefulviews.LabelledSpinner;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class CreateEventActivity extends AppCompatActivity implements Constants,
        LabelledSpinner.OnItemChosenListener {

    private static final String TAG = "CreateEventActivity";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        //TODO need to find a way to save the data (even after user close the application) (Low Priority)

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

        initData();
        Log.d(TAG, "onCreate: out");
    }

    private void initData() {
        Log.d(TAG, "initData: in");
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this mToolbar
        ActionBar actionBar = getSupportActionBar();
        mToolbarTitle.setText(Constants.TOOLBAR_TITLE_EVENTS);
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));

        // Enable the top left button
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get the content from internal storage file
        Context context = getApplicationContext();
        fileContent = new ReadFileData().readFile(context, FILE_USER_INFO);
        content = new ReadFileData().splitFileContent(fileContent);
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
        String yearRange[] = new String[201];
        int startYear = Calendar.getInstance().get(Calendar.YEAR) - 100;
        for (int i = 0; i < yearRange.length; i++) {
            yearRange[i] = startYear + "";
            startYear++;
        }
        mYearSpinner.setItemsArray(yearRange);
        mYearSpinner.setOnItemChosenListener(this);
        //set default selection to be current year
        mYearSpinner.setSelection(100);
    }

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
            mDurationInputLayout.addView(mDurationEditText);
            mLayout.addView(mDurationInputLayout);

            mCostInputLayout.setHint(getResources().getString(R.string.cost_per_month));
            mCostInputLayout.setId(R.id.cost_input_layout);
            mCostInputLayout.setHintTextAppearance(R.style.input_layout_hint_color);
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
        super.onBackPressed();
    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView,
                             View itemView, int position, long id) {
        switch (labelledSpinner.getId()) {
            case R.id.event_type_spinner:
                Log.i(TAG, "onItemChosen: " + mEventTypeSpinner.getSpinner()
                        .getItemAtPosition(position).toString());
                eventTypeSpinnerValue = mEventTypeSpinner.getSpinner().getItemAtPosition(position).toString();
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
                int eventCount = Integer.valueOf(content.get(CONTENT_EVENT_COUNT)) + 1;

                //Saving data in internal storage
                if (mEventStatusSegmentedButton.getPosition() == 0) {
                    fileContent += "//" + CONTENT_EVENT_STATUS + eventCount + ":" + SEGMENTED_BUTTON_VALUE_ONE_TIME +
                            "//" + CONTENT_EVENT_AMOUNT + eventCount + ":" + mAmountInputLayout.getEditText().getText().toString();
                } else {
                    fileContent += "//" + CONTENT_EVENT_STATUS + eventCount + ":" + SEGMENTED_BUTTON_VALUE_RECURRING +
                            "//" + CONTENT_EVENT_COST + eventCount + ":" + mCostInputLayout.getEditText().getText().toString() +
                            "//" + CONTENT_EVENT_DURATION + eventCount + ":" + mDurationInputLayout.getEditText().getText().toString();
                }

                fileContent += "//" + CONTENT_EVENT_NAME + eventCount + ":" + mEventNameInputLayout.getEditText().getText().toString() +
                        "//" + CONTENT_EVENT_TYPE + eventCount + ":" + eventTypeSpinnerValue +
                        "//" + CONTENT_EVENT_YEAR_OCCURRED + eventCount + ":" + yearSpinnerValue +
                        "//" + CONTENT_EVENT_DESCRIPTION + eventCount + ":" + mEventDescriptionInputLayout.getEditText().getText().toString() +
                        "//" + CONTENT_EVENT_COUNT + ":" + eventCount;

                try {
                    FileOutputStream fileOutputStream = openFileOutput(FILE_USER_INFO, MODE_PRIVATE);
                    fileOutputStream.write(fileContent.getBytes());
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(this, MainActivity.class).putExtra(INTENT_KEY_FRAGMENT_POSITION, 1));
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
