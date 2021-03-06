package com.example.android.graphapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private static final String TAG = "OTPVerificationActivity";

    //    private final String phoneNumber = "+6596929180"; //danny
//    private final String phoneNumber = "+6596879596"; //eric (danny's bro)
    private final String phoneNumber = "+6596812904";
//    private final String phoneNumber = "+6592285193";
//    private final String phoneNumber = "+6596812903"; //for emulator

    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;
    private FirebaseAuth mAuth;
    private TextInputLayout mOTPInputLayout;
    public static final String KEY_OTP = "KEY_OTP";

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Log.d(TAG, "onCodeSent: in");
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            mForceResendingToken = forceResendingToken;
            Log.d(TAG, "onCodeSent: out");
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: in");
            String code = phoneAuthCredential.getSmsCode();
            mOTPInputLayout.getEditText().setText(code);
            if (code != null) {
                verifyCode(code);
            }
            Log.d(TAG, "onVerificationCompleted: out, code: " + code);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.d(TAG, "onVerificationFailed: in");
            Toast.makeText(OTPVerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onVerificationFailed: out");
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String s) {
            Log.d(TAG, "onCodeAutoRetrievalTimeOut: in");
            super.onCodeAutoRetrievalTimeOut(s);
            Log.d(TAG, "onCodeAutoRetrievalTimeOut: out");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        mAuth = FirebaseAuth.getInstance();

//        mAuth.getCurrentUser().delete();
        mAuth.signOut();

        mOTPInputLayout = findViewById(R.id.otp_input_layout);

        findViewById(R.id.verify_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mOTPInputLayout.getEditText().getText().toString();

                if (code.equals("") || code.length() < 6) {
                    mOTPInputLayout.setError("Enter OTP code!");
                    mOTPInputLayout.requestFocus();
                    return;
                } else {
                    mOTPInputLayout.setErrorEnabled(false);
                }

                verifyCode(code);
            }
        });

        findViewById(R.id.resend_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(phoneNumber, mForceResendingToken);
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
        Toast.makeText(getApplicationContext(), "Sending OTP...", Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                120,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBack,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
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
