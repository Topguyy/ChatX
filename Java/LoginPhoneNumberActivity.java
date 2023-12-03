package com.example.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class LoginPhoneNumberActivity extends AppCompatActivity {
CountryCodePicker countryCodePicker;
EditText userPhoneNumber;
Button sendOtpBtn;
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_number);
countryCodePicker = findViewById(R.id.CCP);
userPhoneNumber = findViewById(R.id.userPhoneNumber);
sendOtpBtn = findViewById(R.id.sendOTPBtn);
progressBar = findViewById(R.id.loginPhoneProgressBar);
progressBar.setVisibility(View.GONE);
countryCodePicker.registerCarrierNumberEditText(userPhoneNumber);
sendOtpBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       if(!countryCodePicker.isValidFullNumber()){
           userPhoneNumber.setError("Invalid phone number!");
           return;
       }
       Intent i = new Intent(LoginPhoneNumberActivity.this,LoginOtpActivity.class);
       /*adds extra data to the Intent. The key is "Phone",
         and the value is the full phone number with the country code obtained from */
       i.putExtra("Phone",countryCodePicker.getFullNumberWithPlus());
       startActivity(i);
        progressBar.setVisibility(View.VISIBLE);
    }
});
    }
}