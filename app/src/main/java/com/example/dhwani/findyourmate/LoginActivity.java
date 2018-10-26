package com.example.dhwani.findyourmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout rellay1,rellay2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };
    Button loginBtn ;
    EditText loginEmail, loginPass;
    CheckBox showPassword;
    int block = 5;
    TextView forgetPasssrord,signupBtn;
    String login_email_text, login_pass_text;
    Databasehelper userDb;
    User user;

    String email_pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail = (EditText) findViewById(R.id.uname);
        loginPass = (EditText) findViewById(R.id.upass);
        loginBtn = findViewById(R.id.log_btn);
        forgetPasssrord = findViewById(R.id.textView3);
        showPassword = findViewById(R.id.checkBox);
        signupBtn = findViewById(R.id.reg_btn);
        userDb = new Databasehelper(LoginActivity.this);
        user = new User();

        rellay1 = (RelativeLayout) findViewById(R.id.relLay1);
        rellay2 = (RelativeLayout) findViewById(R.id.relLay2);

        handler.postDelayed(runnable,2000);
        //For visible the password to user
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    loginPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    loginPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        //Move to register activity.
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(regIntent);
            }
        });

        //Forget Password
        forgetPasssrord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid()){
                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(LoginActivity.this, NavigationDrawer.class);

                    PrefUtil.putbooleanPref(PrefUtil.PRE_LOGINCHECK,true,getApplicationContext());
                    PrefUtil.putstringPref("email",login_email_text,LoginActivity.this);
                    startActivity(mainIntent);
                    finish();
                }else {

                    //Block the login Button if the user enter wrong password for 5 times.
                    block--;
                    if (block == 0) {
                        loginBtn.setEnabled(false);
                        Toast.makeText(LoginActivity.this, "Login Block!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity.this, block+" try left!!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


    }

    //Validate the email and password.
    boolean valid(){

        boolean isValid = false;

        login_email_text = loginEmail.getText().toString();
        login_pass_text = loginPass.getText().toString();


        if(!login_email_text.isEmpty()){
            if(login_email_text.matches(email_pattern)){
                if(!login_pass_text.isEmpty()){
                    if (userDb.loginValidation(login_email_text,login_pass_text)) {
                        isValid = true;
                    }else {
                        loginPass.setError("Password not matched");
                    }
                }else{
                    loginPass.setError("Enter Password!!");
                }

            }else{
                loginEmail.setError("Enter Email Properly!");
            }

        }else{
            loginEmail.setError("Enter Email!");
        }

        return isValid;
    }

}