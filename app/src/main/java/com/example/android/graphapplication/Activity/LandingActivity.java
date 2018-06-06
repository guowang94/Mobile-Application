package com.example.android.graphapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.graphapplication.Const;
import com.example.android.graphapplication.R;

public class LandingActivity  extends AppCompatActivity implements Const {

    private Button mManagePolicyButton;
    private Button mComputeClientInfoButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mManagePolicyButton = findViewById(R.id.manage_policy_button);
        mComputeClientInfoButton = findViewById(R.id.compute_client_info_button);

        mComputeClientInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, FormActivity.class));
            }
        });
    }
}
