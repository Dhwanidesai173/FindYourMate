package com.example.dhwani.findyourmate;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dhwani.findyourmate.LoginActivity;
import com.example.dhwani.findyourmate.R;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_OUT_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent;
                boolean isRe = PrefUtil.getbooleanPref(PrefUtil.PRE_LOGINCHECK, getApplicationContext());
                if (PrefUtil.getbooleanPref(PrefUtil.PRE_LOGINCHECK, getApplicationContext())) {
                    homeIntent= new Intent(SplashActivity.this, NavigationDrawer.class);
                } else {
                    homeIntent= new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_OUT_TIME);
    }
}
