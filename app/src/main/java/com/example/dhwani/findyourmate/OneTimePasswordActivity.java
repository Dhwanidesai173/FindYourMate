package com.example.dhwani.findyourmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.dhwani.findyourmate.Constants.ONE_TIME_PASSWORD;
import static com.example.dhwani.findyourmate.R.id.emailText;

public class OneTimePasswordActivity extends AppCompatActivity  {

    TextView emailText, resendOtp;
    EditText otpText;
    User user;
    Databasehelper db;
    Button nextMove;
    Boolean isEmail,isForgot = false;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_password2);

        emailText = (TextView) findViewById(R.id.emailText);
        resendOtp = (TextView) findViewById(R.id.resendOtp);
        otpText = (EditText) findViewById(R.id.otpText);
        nextMove = (Button) findViewById(R.id.nextBtn);

        getIntentValues();
        if (user.getEmail()!= null)
        {
            emailText.setText(user.getEmail());
        }
        nextMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpText.getText().toString().equals(Constants.ONE_TIME_PASSWORD)) {
                    //Below line insert data into database.
                    if (isForgot) {
                        if (isEmail) {
                            Intent i = new Intent(OneTimePasswordActivity.this, PasswordActivity.class);
                            i.putExtra("emailSend", user.getEmail());
                            i.putExtra("isForget",isForgot);
                            startActivity(i);
                            Log.i("Test", "onClick: " + getIntent().getStringExtra(Constants.IntentForgotEmailPassword));
                        }
                    }

                } else {
                    otpText.setError("Otp was wrong");
                }
            }
        });

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.generateOTP();
                Toast.makeText(OneTimePasswordActivity.this, "OTP is: " + ONE_TIME_PASSWORD, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void getIntentValues() {
       // Todo : Email
        if (getIntent().getSerializableExtra(Constants.IntentModelPhone) != null)
        {
            user = (User) getIntent().getSerializableExtra(Constants.IntentModelPhone);
            isEmail = true;
        }

//        Todo : Forgot Using Email
        else if (getIntent().getStringExtra(Constants.IntentForgotEmail)!= null)
        {
            user = new User();
            user.setEmail(getIntent().getStringExtra(Constants.IntentForgotEmail));
            isForgot = true;
            isEmail = true;
        }
    }
}



