package com.arup.yahmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class loginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String otp;
    EditText phoneEditText, OTPEditText;
    Button reqOTPButton, submitOTP;
    TextView timerTV, phoneNoWarn, OTPWarn;

    String name;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("nameFromLogin", "ARUP");
        intent.putExtra("phoneFromLogin", "9732919663");
        startActivity(intent);

        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

        reqOTPButton = findViewById(R.id.request_otp);
        submitOTP = findViewById(R.id.submit_otp);
        timerTV = findViewById(R.id.login_timer);
        phoneEditText = findViewById(R.id.phoneno_login);
        OTPEditText = findViewById(R.id.otp_login);

        phoneNoWarn = findViewById(R.id.phoneNoWarn);
        OTPWarn = findViewById(R.id.OTPWarn);

        EnableDisableButton(reqOTPButton, true);
        EnableDisableButton(submitOTP, false);
        OTPEditText.setEnabled(false);

        submitOTP.setOnClickListener(v -> {
            String OTP = OTPEditText.getText().toString();
            if(OTP.length() != 6) {
                OTPWarn.setVisibility(View.VISIBLE);
                verifyOTP(OTP);
            }
        });

        reqOTPButton.setOnClickListener(v -> {
            String number = phoneEditText.getText().toString();
            if(number.length() == 10) {
                sendOTP(number);

            }
            else {
                phoneNoWarn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void verifyOTP(String otp) {
    }

    private void sendOTP(String number) {
        number = "+91" + number;
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void EnableDisableButton(View view, boolean enable) {
        view.setEnabled(enable);
        view.setBackgroundResource(enable ? R.drawable.button : R.drawable.button_disable);
    }



    private void openActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("nameFromLogin", name);
        intent.putExtra("phoneFromLogin", phone);
        startActivity(intent);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String otp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(otp, forceResendingToken);
            loginActivity.this.otp = otp;
        }
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            /*if (code != null) {
                edtOTP.setText(code);
                verifyCode(code);
            }*/
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.d("Error", e.getMessage());
        }
    };
}