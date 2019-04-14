package com.example.android.graphapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.android.graphapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPVerificationActivity extends AppCompatActivity {

    private final String phoneNumber = "+6596929180"; //danny
//    private final String phoneNumber = "+6596879596"; //eric (danny's bro)
//    private final String phoneNumber = "+6596812904";
//    private final String phoneNumber = "+6596812903"; //for emulator

    private String verificationId;
    private FirebaseAuth mAuth;
    private TextInputLayout mOTPInputLayout;
    public static final String KEY_OTP = "KEY_OTP";

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            mOTPInputLayout.getEditText().setText(code);
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPVerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        mAuth = FirebaseAuth.getInstance();

        mOTPInputLayout = findViewById(R.id.otp_input_layout);

        findViewById(R.id.verify_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mOTPInputLayout.getEditText().getText().toString();

                if (code.isEmpty() || code.length() < 6) {
                    mOTPInputLayout.setError("Enter OTP code!");
                    mOTPInputLayout.requestFocus();
                    return;
                } else {
                    mOTPInputLayout.setErrorEnabled(false);
                }

                verifyCode(code);
            }
        });

        if (savedInstanceState != null) {
            mOTPInputLayout.getEditText().setText(savedInstanceState.getString(KEY_OTP));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if current user exist, it will launch FormActivity else it will send a verification code
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(OTPVerificationActivity.this, FormActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            sendVerificationCode(phoneNumber);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_OTP, mOTPInputLayout.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    /**
     * This method send a verification code to the admin and the code is only valid for 2 minutes
     *
     * @param phoneNumber admin phone number
     */
    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                120,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(OTPVerificationActivity.this, FormActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(OTPVerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
