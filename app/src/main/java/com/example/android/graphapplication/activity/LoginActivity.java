package com.example.android.graphapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.constants.ErrorMsgConstants;
import com.example.android.graphapplication.db.DBHelper;
import com.example.android.graphapplication.validations.Validation;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    public static final String applicationPassword = "20Sm@rtAdvisor19";
    public static final String KEY_PASSWORD = "password";
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private TextInputLayout mLoginTextInputLayout;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginTextInputLayout = findViewById(R.id.login_text_input_layout);
        mLoginButton = findViewById(R.id.login_button);
        mToolbar = findViewById(R.id.login_toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);

        if (savedInstanceState != null) {
            mLoginTextInputLayout.getEditText().setText(savedInstanceState.getString(KEY_PASSWORD));
        }

        initData();

        //todo static value to be commented
//        mLoginTextInputLayout.getEditText().setText(applicationPassword);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_PASSWORD, mLoginTextInputLayout.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void initData() {
        Log.d(TAG, "initData: in");
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this toolbar
        mToolbarTitle.setText(getResources().getString(R.string.app_name));

        final DBHelper mydb = new DBHelper(getApplicationContext());

        if (mydb.hasUserLoginBefore()) {
            Intent intent = new Intent(LoginActivity.this, FormActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation validation = new Validation();

                if (!validation.blankFieldValidation(mLoginTextInputLayout)) {
                    if (!applicationPassword.equals(mLoginTextInputLayout.getEditText().getText().toString())) {
                        mLoginTextInputLayout.setError(ErrorMsgConstants.ERR_MSG_WRONG_PASSWORD);
                    } else {
                        mLoginTextInputLayout.setErrorEnabled(false);
                    }
                }

                if (!mLoginTextInputLayout.isErrorEnabled()) {
                    mydb.insertIntoOTL(mLoginTextInputLayout.getEditText().getText().toString());

                    Intent intent = new Intent(LoginActivity.this, FormActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
        Log.d(TAG, "initData: out");
    }
}
