package com.example.android.graphapplication.activity;

import android.content.Intent;
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
import com.example.android.graphapplication.constants.ScreenConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.model.CommonModel;
import com.example.android.graphapplication.model.UserModel;
import com.example.android.graphapplication.validations.Validation;
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
    private int ageSpinnerValue;
    private String milestoneAction;
    private int currentMilestoneID;
    private String ageRange[];

    private Validation validation;

    public static final String KEY_MILESTONE_NAME = "MILESTONE_NAME";
    public static final String KEY_MILESTONE_TYPE = "MILESTONE_TYPE";
    public static final String KEY_MILESTONE_AGE = "MILESTONE_AGE";
    public static final String KEY_MILESTONE_DESCRIPTION = "MILESTONE_DESCRIPTION";
    public static final String KEY_MILESTONE_AMOUNT = "MILESTONE_AMOUNT";
    public static final String KEY_MILESTONE_DURATION = "MILESTONE_DURATION";
    private int savedInstanceStateMilestoneType = -1;
    private int savedInstanceStateMilestoneAge = -1;
    private String savedInstanceStateAmount;
    private String savedInstanceStateDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_milestone);

        mToolbar = findViewById(R.id.create_milestone_toolbar);
        mMilestoneNameInputLayout = findViewById(R.id.milestone_name_input_layout);
        mMilestoneTypeSpinner = findViewById(R.id.milestone_type_spinner);
        mAgeSpinner = findViewById(R.id.age_spinner);
        mMilestoneDescriptionInputLayout = findViewById(R.id.description_input_layout);
        mMilestoneStatusSegmentedButton = findViewById(R.id.milestone_status_segmented_button);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mLayout = findViewById(R.id.layout);

        mAmountInputLayout = new TextInputLayout(new ContextThemeWrapper(
                MilestoneActivity.this, R.style.Widget_Design_TextInputLayout_textColorHint));
        mCostInputLayout = new TextInputLayout(new ContextThemeWrapper(
                MilestoneActivity.this, R.style.Widget_Design_TextInputLayout_textColorHint));
        mDurationInputLayout = new TextInputLayout(new ContextThemeWrapper(
                MilestoneActivity.this, R.style.Widget_Design_TextInputLayout_textColorHint));
        mAmountEditText = new EditText(this);
        mCostEditText = new EditText(this);
        mDurationEditText = new EditText(this);

        if (getIntent() != null) {
            milestoneAction = getIntent().getStringExtra(KeyConstants.INTENT_KEY_ACTION);
            currentMilestoneID = getIntent().getIntExtra(
                    KeyConstants.INTENT_KEY_RECYCLER_VIEW_POSITION, -1);
            Log.d(TAG, "onCreate: displayData, " + currentMilestoneID);
        }

        if (savedInstanceState != null) {
            savedInstanceStateMilestoneType = savedInstanceState.getInt(KEY_MILESTONE_TYPE);
            savedInstanceStateMilestoneAge = savedInstanceState.getInt(KEY_MILESTONE_AGE);
            mMilestoneNameInputLayout.getEditText().setText(savedInstanceState.getString(KEY_MILESTONE_NAME));
            mMilestoneDescriptionInputLayout.getEditText().setText(savedInstanceState.getString(KEY_MILESTONE_DESCRIPTION));
            if (mMilestoneStatusSegmentedButton.getPosition() == 0) {
                savedInstanceStateAmount = savedInstanceState.getString(KEY_MILESTONE_AMOUNT);
            } else {
                savedInstanceStateDuration = savedInstanceState.getString(KEY_MILESTONE_DURATION);
                savedInstanceStateAmount = savedInstanceState.getString(KEY_MILESTONE_AMOUNT);
            }
        }

        initData();
        Log.d(TAG, "onCreate: out");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_MILESTONE_NAME, mMilestoneNameInputLayout.getEditText().getText().toString());
        outState.putInt(KEY_MILESTONE_TYPE, mMilestoneTypeSpinner.getSpinner().getSelectedItemPosition());
        outState.putInt(KEY_MILESTONE_AGE, mAgeSpinner.getSpinner().getSelectedItemPosition());
        outState.putString(KEY_MILESTONE_DESCRIPTION, mMilestoneDescriptionInputLayout.getEditText().getText().toString());
        if (mMilestoneStatusSegmentedButton.getPosition() == 0) {
            outState.putString(KEY_MILESTONE_AMOUNT, mAmountInputLayout.getEditText().getText().toString());
        } else {
            outState.putString(KEY_MILESTONE_DURATION, mDurationInputLayout.getEditText().getText().toString());
            outState.putString(KEY_MILESTONE_AMOUNT, mCostInputLayout.getEditText().getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * This method will initialise the data for the activity
     */
    private void initData() {
        mydb = new DBHelper(getApplicationContext());
        validation = new Validation();

        Log.d(TAG, "initData: in");
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this mToolbar
        ActionBar actionBar = getSupportActionBar();
        mToolbarTitle.setText(ScreenConstants.TOOLBAR_TITLE_ADD_MILESTONE);

        // Enable the top left button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Draw TextInputLayout based on Milestone Status
        drawTextInputLayout(mMilestoneStatusSegmentedButton.getPosition());

        mMilestoneStatusSegmentedButton.setOnPositionChangedListener(
                new SegmentedButtonGroup.OnPositionChangedListener() {
                    @Override
                    public void onPositionChanged(int position) {
                        Log.d(TAG, "onPositionChanged: " + position);
                        drawTextInputLayout(position);
                    }
                });

        setupSpinners();

        if (savedInstanceStateMilestoneType != -1) {
            mMilestoneTypeSpinner.setSelection(savedInstanceStateMilestoneType);
        }

        if (savedInstanceStateMilestoneAge != -1) {
            mAgeSpinner.setSelection(savedInstanceStateMilestoneAge);
        }

        if (mMilestoneStatusSegmentedButton.getPosition() == 0) {
            mAmountInputLayout.getEditText().setText(savedInstanceStateAmount);
        } else {
            mDurationInputLayout.getEditText().setText(savedInstanceStateDuration);
            mCostInputLayout.getEditText().setText(savedInstanceStateAmount);
        }

        //Validation
        if (mMilestoneNameInputLayout.getEditText() != null) {
            mMilestoneNameInputLayout.getEditText().setOnFocusChangeListener(
                    validation.onFocusChangeListenerForNameValidation(mMilestoneNameInputLayout));
        }

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

        CommonModel milestoneModel = mydb.getMilestoneDetails(currentMilestoneID);

        String milestoneName = milestoneModel.getName();
        String milestoneType = milestoneModel.getType();
        String ageOccurred = String.valueOf(milestoneModel.getAge());
        String description = milestoneModel.getDescription();
        String milestoneStatus = milestoneModel.getStatus();
        String amount = String.valueOf(milestoneModel.getAmount());
        String duration = String.valueOf(milestoneModel.getDuration());

        Log.d(TAG, "displayData: " + milestoneName);

        if (mMilestoneNameInputLayout.getEditText() != null) {
            mMilestoneNameInputLayout.getEditText().setText(milestoneName);
        }

        String[] milestoneArray = getResources().getStringArray(R.array.milestone_type_array);
        for (int i = 0; i < milestoneArray.length; i++) {
            if (milestoneArray[i].equalsIgnoreCase(milestoneType)) {
                mMilestoneTypeSpinner.setSelection(i);
            }
        }

        for (int i = 0; i < ageRange.length; i++) {
            if (ageRange[i].equalsIgnoreCase(ageOccurred)) {
                mAgeSpinner.setSelection(i);
            }
        }

        if (description != null && !description.equalsIgnoreCase("")) {
            if (mMilestoneDescriptionInputLayout.getEditText() != null) {
                mMilestoneDescriptionInputLayout.getEditText().setText(description);
            }
        }

        if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equalsIgnoreCase(milestoneStatus)) {
            mMilestoneStatusSegmentedButton.setPosition(0);
            if (mAmountInputLayout.getEditText() != null) {
                mAmountInputLayout.getEditText().setText(amount);
            }
        } else {
            mMilestoneStatusSegmentedButton.setPosition(1);
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
        // then connect constraint and lastly apply constraint
        if (position == 0) {
            Log.d(TAG, "drawTextInputLayout: in if()");

            mLayout.removeView(findViewById(R.id.duration_input_layout));
            mLayout.removeView(findViewById(R.id.cost_per_year_input_layout));

            mAmountInputLayout.setHint(getResources().getString(R.string.amount));
            mAmountInputLayout.setId(R.id.amount_input_layout);
            mAmountInputLayout.setHintTextAppearance(R.style.input_layout_hint_color);
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
                    mMilestoneStatusSegmentedButton.getId(), ConstraintSet.BOTTOM, 16);
            constraintSet.connect(mAmountInputLayout.getId(), ConstraintSet.START,
                    mMilestoneStatusSegmentedButton.getId(), ConstraintSet.START, 0);
            constraintSet.connect(mAmountInputLayout.getId(), ConstraintSet.END,
                    mMilestoneStatusSegmentedButton.getId(), ConstraintSet.END, 0);

            constraintSet.applyTo(mLayout);

            // Validation
            if (mAmountInputLayout.getEditText() != null) {
                mAmountInputLayout.getEditText().setOnFocusChangeListener(
                        validation.onFocusChangeListenerForNegativeValueValidation(mAmountInputLayout));
            }
        } else {
            Log.d(TAG, "drawTextInputLayout: in else()");

            mLayout.removeView(findViewById(R.id.amount_input_layout));

            mDurationInputLayout.setHint(getResources().getString(R.string.duration_in_year));
            mDurationInputLayout.setId(R.id.duration_input_layout);
            mDurationInputLayout.setHintTextAppearance(R.style.input_layout_hint_color);
            mDurationEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (mDurationEditText.getParent() == null) {
                mDurationInputLayout.addView(mDurationEditText);
            }
            mLayout.addView(mDurationInputLayout);

            mCostInputLayout.setHint(getResources().getString(R.string.cost_per_year));
            mCostInputLayout.setId(R.id.cost_per_year_input_layout);
            mCostInputLayout.setHintTextAppearance(R.style.input_layout_hint_color);
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

            // Validation
            if (mDurationInputLayout.getEditText() != null) {
                mDurationInputLayout.getEditText().setOnFocusChangeListener(
                        validation.onFocusChangeListenerForNegativeValueValidation(mDurationInputLayout));
            }
            if (mCostInputLayout.getEditText() != null) {
                mCostInputLayout.getEditText().setOnFocusChangeListener(
                        validation.onFocusChangeListenerForNegativeValueValidation(mCostInputLayout));
            }
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
                ageSpinnerValue = Integer.valueOf(mAgeSpinner.getSpinner().getItemAtPosition(position).toString());
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
                validation.blankFieldValidation(mMilestoneNameInputLayout);

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

                if (mMilestoneNameInputLayout.isErrorEnabled()) {
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
                    String milestoneName = null;
                    String description = null;
                    String milestoneStatus = mMilestoneStatusSegmentedButton.getPosition() == 0 ?
                            ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME :
                            ScreenConstants.SEGMENTED_BUTTON_VALUE_RECURRING;
                    float amount = 0f;
                    int duration = 1;

                    if (mMilestoneNameInputLayout.getEditText() != null) {
                        milestoneName = mMilestoneNameInputLayout.getEditText().getText().toString();
                    }

                    if (mMilestoneDescriptionInputLayout.getEditText() != null) {
                        description = mMilestoneDescriptionInputLayout.getEditText().getText().toString();
                    }

                    if (ScreenConstants.SEGMENTED_BUTTON_VALUE_ONE_TIME.equals(milestoneStatus)) {
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

                    if ("Create".equalsIgnoreCase(milestoneAction)) {

                        mydb.insertMilestone(milestoneName, milestoneTypeSpinnerValue, ageSpinnerValue,
                                description, milestoneStatus, amount, duration);

                    } else if ("Edit".equalsIgnoreCase(milestoneAction)) {

                        mydb.updateMilestone(currentMilestoneID, milestoneName, milestoneTypeSpinnerValue,
                                ageSpinnerValue, description, milestoneStatus, amount, duration);
                    }

                    startActivity(new Intent(this, MainActivity.class).putExtra(
                            KeyConstants.INTENT_KEY_FRAGMENT_POSITION, 2));
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
     * This method to setup Spinners' item
     */
    private void setupSpinners() {
        mMilestoneTypeSpinner.setItemsArray(R.array.milestone_type_array);
        mMilestoneTypeSpinner.setOnItemChosenListener(this);

        UserModel userModel = mydb.getAllUser().get(0);

        int currentAge = userModel.getAge();
        int expectancyAge = userModel.getExpectancy();

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
