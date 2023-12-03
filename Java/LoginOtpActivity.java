package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapplication.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {
String userPhoneNumber ;
EditText userOtp ;
Button nextBtn;
TextView resendOtpTxt ;
ProgressBar loginOtpProgressBar;
FirebaseAuth mAuth ;
Long timeOutSeconds = 60L ;
String verificationCode;
PhoneAuthProvider.ForceResendingToken resendingToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        userOtp = findViewById(R.id.userOtp);
        nextBtn = findViewById(R.id.nextBtn);
        loginOtpProgressBar = findViewById(R.id.loginOtpProgressBar);
        resendOtpTxt = findViewById(R.id.resendOtpTxt);
        mAuth = FirebaseAuth.getInstance();
        userPhoneNumber = getIntent().getExtras().getString("Phone");
        sendOtp(userPhoneNumber, false);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = userOtp.getText().toString();
             PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,enteredOtp);
             signIn(credential);
            }
        });
        resendOtpTxt.setOnClickListener(v ->{
            sendOtp(userPhoneNumber,true);
                });
    }
    public void sendOtp(String userPhoneNumber,boolean isResend){
        //startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(userPhoneNumber)
                .setTimeout(timeOutSeconds,TimeUnit.SECONDS)
                .setActivity(LoginOtpActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        AndroidUtil.showToast(LoginOtpActivity.this,"OTP verification failed!");
                        setInProgress(false);
                    }
                    public void onCodeSent(String s , PhoneAuthProvider.ForceResendingToken forceResendingToken){
                        super.onCodeSent(s,forceResendingToken);
                        verificationCode = s;
                        resendingToken = forceResendingToken;
                        AndroidUtil.showToast(getApplicationContext(),"OTP sent successfully!");
                        setInProgress(false);
                    }
                });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }
    public void setInProgress(boolean inProgress){
        if(inProgress){
            loginOtpProgressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        } else{
            loginOtpProgressBar.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }

    }
    public void signIn(PhoneAuthCredential phoneAuthCredential){
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent i = new Intent(LoginOtpActivity.this,LoginUsernameActivity.class);
                    i.putExtra("phone",userPhoneNumber);
                    startActivity(i);

                }else{
                    AndroidUtil.showToast(getApplicationContext(),"OTP verification failed!");
                    setInProgress(false);
                }
            }
        });
    }
    void startResendTimer(){
        resendOtpTxt.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeOutSeconds--;
                resendOtpTxt.setText("Resend OTP in "+timeOutSeconds +" seconds");
                if(timeOutSeconds<=0){
                    timeOutSeconds =60L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        resendOtpTxt.setEnabled(true);
                    });
                }
            }
        },0,1000);
    }
}