package com.example.dhwani.findyourmate;

import android.content.Intent;
import android.preference.EditTextPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.dhwani.findyourmate.Constants.ONE_TIME_PASSWORD;
import static com.example.dhwani.findyourmate.Constants.emailPattern;
import static com.example.dhwani.findyourmate.Constants.generateOTP;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText Email;
    private TextView Enter_email;
    private Button next_btn;
    User user;
    Databasehelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        db = new Databasehelper(ForgotPasswordActivity.this);
        Email = (EditText) findViewById(R.id.EmailId);
        Enter_email = (TextView) findViewById(R.id.textView4);
        next_btn = (Button) findViewById(R.id.button);
        user = new User();
        db = new Databasehelper(ForgotPasswordActivity.this);


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextPreference forgetLayout = null;
                if (Email.getText().toString().matches(emailPattern)) {
                    if (db.isEmailExist(Email.getText().toString())) {
                        generateOTP();
                        Toast.makeText(ForgotPasswordActivity.this, "OTP is: "+ONE_TIME_PASSWORD, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ForgotPasswordActivity.this, OneTimePasswordActivity.class);
                        i.putExtra(Constants.IntentForgotEmail, Email.getText().toString());
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(ForgotPasswordActivity.this, "Cant Find Your Account", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Enter Valid Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
