package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.chatapplication.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUsernameActivity extends AppCompatActivity {
EditText userName ;
Button createAccountBtn ;
ProgressBar loginUsernameProgressBar;
String userPhoneNumber;
UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);
        userName = findViewById(R.id.userName);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        loginUsernameProgressBar = findViewById(R.id.loginUsernameProgressBar);
        userPhoneNumber = getIntent().getExtras().getString("phone");
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserName();
            }
        });
    }
    void setUserName(){

        String username=userName.getText().toString();
        if(username.isEmpty()||username.length()<=3){
            userName.setError("Username must be at least 4 characters long");
            return;
        }
        setInProgress(true);
        if(userModel!=null){
            userModel.setUsername(username);
        }else{
            userModel = new UserModel(userPhoneNumber,username, Timestamp.now());
        }
        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent i = new Intent(LoginUsernameActivity.this,MainActivity.class);
                    i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });
    }
    void getUserName(){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                 userModel =    task.getResult().toObject(UserModel.class);
                }
                if(userModel != null){
                    userName.setText(userModel.getUsername());
                }
            }
        });

    }
    public void setInProgress(boolean inProgress){
        if(inProgress){
            loginUsernameProgressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        } else{
            loginUsernameProgressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }
}