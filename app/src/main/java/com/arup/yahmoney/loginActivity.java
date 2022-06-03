package com.arup.yahmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class loginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "PhoneAuthActivity";
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;



    EditText phoneEditText, OTPEditText;
    Button reqOTPButton, submitOTP;
    TextView timerTV, phoneNoWarn, OTPWarn;

    String name;
    String phone;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

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


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(loginActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(loginActivity.this, "Sorry, Service Unable", Toast.LENGTH_SHORT).show();
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        submitOTP.setOnClickListener(v -> {
            String OTP = OTPEditText.getText().toString();
            if(OTP.length() != 6) {
                OTPWarn.setVisibility(View.VISIBLE);
                //verifyOTP(OTP);
            }
        });

        reqOTPButton.setOnClickListener(v -> {
            String number = phoneEditText.getText().toString();
            if(number.length() == 10) {
                startPhoneNumberVerification(number);

            }
            else {
                phoneNoWarn.setVisibility(View.VISIBLE);
            }
        });
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

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        phoneNumber = "+91" + phoneNumber;
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();
                        // Update UI
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

    }
}