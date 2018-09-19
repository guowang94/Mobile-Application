package com.example.android.graphapplication.activity;

import android.content.Intent;
import android.database.Cursor;
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

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.constants.KeyConstants;
import com.example.android.graphapplication.constants.SQLConstants;
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.satsuware.usefulviews.LabelledSpinner;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class MilestoneActivity extends AppCompatActivity implements
        LabelledSpinner.OnItemChosenListener {

    private static final String TAG = "MilestoneActivity";

    private Toolbar mToolbar;
    private TextInputLayout mMilestoneNameInputLayout;
    private LabelledSpinner mMilestoneTypeSpinner;
    private LabelledSpinner mAgeSpinner;
    private TextInputLayout mMilestoneDescriptionInputLayout;
    private SegmentedButtonGroup mMilestoneStatusSegmentedButton;
    private TextView mToolbarTitle;
    private ConstraintLayout mLayout;
    private TextInputLayout mAmountInputLayout;
    private EditText mAmountEditText;
    private TextInputLayout mCostInputLayout;
    private EditText mCostEditText;
    private TextInputLayout mDurationInputLayout;
    private EditText mDurationEditText;
    private DBHelper mydb;

    private String milestoneTypeSpinnerValue;
    private String ageSpinnerValue;
    private String milestoneAction;
    private int currentMilestoneID;
    private String ageRange[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_milestone);
        //TODO need to add validation for all the fields (Low Priority)

        mToolbar = findViewById(R.id.create_milestone_toolbar);
        mMilestoneNameInputLayout = findViewById(R.id.name_input_layout);
        mMilestoneTypeSpinner = findViewById(R.id.milestone_type_spinner);
        mAgeSpinner = findViewById(R.id.age_spinner);
        mMilestoneDescriptionInputLayout = findViewById(R.id.description_input_layout);
        mMilestoneStatusSegmentedButton = findViewById(R.id.milestone_status_segmented_button);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mLayout = findViewById(R.id.layout);
        mAmountInputLayout = new TextInputLayout(this);
        mAmountEditText = new EditText(this);
        mCostInputLayout = new TextInputLayout(this);
        mCostEditText = new EditText(this);
        mDurationInputLayout = new TextInputLayout(this);
        mDurationEditText = new EditText(this);

        mydb = new DBHelper(getApplicationContext());

        if (getIntent() != null) {
            milestoneAction = getIntent().getStringExtra(KeyConstants.INTENT_KEY_ACTION);
            currentMilestoneID = getIntent().getIntExtra(
                    KeyConstants.INTENT_KEY_RECYCLER_VIEW_POSITION, -1);
            Log.d(TAG, "onCreate: displayData, " + currentMilestoneID);
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
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_ADD_MILESTONE);

        // Enable the top left button
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawTextInputLayout(mMilestoneStatusSegmentedButton.getPosition());

        mMilestoneStatusSegmentedButton.setOnPositionChangedListener(
                new SegmentedButtonGroup.OnPositionChangedListener() {
                    @Override
                    public void onPositionChanged(int position) {
                        Log.d(TAG, "onPositionChanged: " + position);
                        drawTextInputLayout(position);
                    }
                });

        mMilestoneTypeSpinner.setItemsArray(R.array.milestone_type_array);
        mMilestoneTypeSpinner.setOnItemChosenListener(this);

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

        if ("Edit".equalsIgnoreCase(milestoneAction) && currentMilestoneID != -1) {
            displayData();
        }
        Log.d(TAG, "initData: out");
    }

    /**
     * This method will display previous values of the milestone
     */
    private void displayData() {
        Log.d(TAG, "displayData: in");

        Cursor rs = mydb.getData(SQLConstants.MILESTONE_TABLE, currentMilestoneID);
        rs.moveToFirst();

        String milestoneName = rs.getString(rs.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_NAME));
        String milestoneType = rs.getString(rs.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_TYPE));
        String yearOccurred = rs.getString(rs.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_AGE));
        String description = rs.getString(rs.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_DESCRIPTION));
        String milestoneStatus = rs.getString(rs.getColumnIndex(SQLConstants.MILESTONE_TABLE_MILESTONE_STATUS));
        String amount = String.valueOf(rs.getFloat(rs.getColumnIndex(SQLConstants.MILESTONE_TABLE_AMOUNT)));
        String duration = String.valueOf(rs.getInt(rs.getColumnIndex(SQLConstants.MILESTONE_TABLE_DURATION)));

        if (!rs.isClosed()) {
            rs.close();
        }

        Log.d(TAG, "displayData: " + milestoneName);

        mMilestoneNameInputLayout.getEditText().setText(milestoneName);

        String[] milestoneArray = getResources().getStringArray(R.array.milestone_type_array);
        for (int i = 0; i < milestoneArray.length; i++) {
            if (milestoneArray[i].equalsIgnoreCase(milestoneType)) {
                mMilestoneTypeSpinner.setSelection(i);
            }
        }

        for (int i = 0; i < ageRange.length; i++) {
            if (ageRange[i].equalsIgnoreCase(yearOccurred)) {
                mAgeSpinner.setSelection(i);
            }
        }

        if (description != null && !description.equalsIgnoreCase("")) {
            mMilestoneDescriptionInputLayout.getEditText().setText(description);
        }

        if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equalsIgnoreCase(milestoneStatus)) {
            mMilestoneStatusSegmentedButton.setPosition(0);
            mAmountInputLayout.getEditText().setText(amount);
        } else {
            mMilestoneStatusSegmentedButton.setPosition(1);
            mDurationInputLayout.getEditText().setText(duration);
            mCostInputLayout.getEditText().setText(amount);
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
                    mMilestoneStatusSegmentedButton.getId(), ConstraintSet.BOTTOM, 16);
            constraintSet.connect(mAmountInputLayout.getId(), ConstraintSet.START,
                    mMilestoneStatusSegmentedButton.getId(), ConstraintSet.START, 0);
            constraintSet.connect(mAmountInputLayout.getId(), ConstraintSet.END,
                    mMilestoneStatusSegmentedButton.getId(), ConstraintSet.END, 0);

            constraintSet.applyTo(mLayout);
        } else {
            Log.d(TAG, "drawTextInputLayout: in else()");

            mLayout.removeView(findViewById(R.id.amount_input_layout));

            mDurationInputLayout.setHint(getResources().getString(R.string.duration_in_year));
            mDurationInputLayout.setId(R.id.duration_input_layout);
            mDurationInputLayout.setHintTextAppearance(R.style.input_layout_hint_color);
            if (mDurationEditText.getParent() == null) {
                mDurationInputLayout.addView(mDurationEditText);
            }
            mLayout.addView(mDurationInputLayout);

            mCostInputLayout.setHint(getResources().getString(R.string.cost_per_year));
            mCostInputLayout.setId(R.id.cost_input_layout);
            mCostInputLayout.setHintTextAppearance(R.style.input_layout_hint_color);
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
                    mMilestoneStatusSegmentedButton.getId(), ConstraintSet.BOTTOM, 16);
            constraintSet.connect(mDurationInputLayout.getId(), ConstraintSet.START,
                    mMilestoneStatusSegmentedButton.getId(), ConstraintSet.START, 0);
            constraintSet.connect(mDurationInputLayout.getId(), ConstraintSet.END,
                    mMilestoneStatusSegmentedButton.getId(), ConstraintSet.END, 0);

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
                KeyConstants.INTENT_KEY_FRAGMENT_POSITION, 2));
    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView,
                             View itemView, int position, long id) {
        switch (labelledSpinner.getId()) {
            case R.id.milestone_type_spinner:
                Log.i(TAG, "onItemChosen: " + mMilestoneTypeSpinner.getSpinner()
                        .getItemAtPosition(position).toString());
                milestoneTypeSpinnerValue = mMilestoneTypeSpinner.getSpinner()
                        .getItemAtPosition(position).toString();
                break;
            case R.id.age_spinner:
                Log.i(TAG, "onItemChosen: " + mAgeSpinner.getSpinner()
                        .getItemAtPosition(position).toString());
                ageSpinnerValue = mAgeSpinner.getSpinner().getItemAtPosition(position).toString();
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
                String milestoneName = mMilestoneNameInputLayout.getEditText().getText().toString();
                String description = mMilestoneDescriptionInputLayout.getEditText().getText().toString();
                String milestoneStatus = mMilestoneStatusSegmentedButton.getPosition() == 0 ?
                        ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME :
                        ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING;
                float amount;
                int duration = 0;

                if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(milestoneStatus)) {
                    amount = Float.valueOf(mAmountInputLayout.getEditText().getText().toString());
                } else {
                    duration = Integer.valueOf(mDurationInputLayout.getEditText().getText().toString());
                    amount = Float.valueOf(mCostInputLayout.getEditText().getText().toString());
                }

                if ("Create".equalsIgnoreCase(milestoneAction)) {

                    mydb.insertMilestone(milestoneName, milestoneTypeSpinnerValue, ageSpinnerValue,
                            description, milestoneStatus, amount, duration);

                } else if ("Edit".equalsIgnoreCase(milestoneAction)) {

                    mydb.updateMilestone(currentMilestoneID, milestoneName, milestoneTypeSpinnerValue,
                            ageSpinnerValue, description, milestoneStatus, amount, duration);
                }

                startActivity(new Intent(this, MainActivity.class).putExtra(
                        KeyConstants.INTENT_KEY_FRAGMENT_POSITION, 2));
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
