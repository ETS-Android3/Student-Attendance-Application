package com.example.studentattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout username, password;
    //username = AdminUser
    //password = Admin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Hooks
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    private  Boolean isUsernameValid() {
        String val = Objects.requireNonNull(username.getEditText()).getText().toString();

        if(val.isEmpty()){
            username.setError("Please enter your credentials.");
            return false;
        }
        else if(val.equals("AdminUser")){
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
        username.setError("Enter correct credential.");
        return false;
    }

    private Boolean isPasswordValid() {
        String val = Objects.requireNonNull(password.getEditText()).getText().toString();

        if(val.isEmpty()){
            password.setError("Please enter your credential.");
            return false;
        }
        else if(val.equals("Admin")){
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
        password.setError("Enter correct credential.");
        return false;
    }

    public void login(View view) {

        //check the credentials
        if(!isUsernameValid() | !isPasswordValid()){
            return;
        }

        //on success go to main page
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        onDestroy();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}