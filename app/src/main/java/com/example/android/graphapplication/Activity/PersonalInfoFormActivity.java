package com.example.android.graphapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.graphapplication.Const;
import com.example.android.graphapplication.R;

public class PersonalInfoFormActivity extends AppCompatActivity implements Const {

    private static final String TAG = "PersonalInfoFormActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_form);
    }
}
