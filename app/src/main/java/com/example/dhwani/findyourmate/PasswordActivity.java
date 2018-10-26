package com.example.dhwani.findyourmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    Button passRegBtn;
    EditText passLayout;
    String reg_pass_text;
    CheckBox regShowPass;
    Databasehelper userDb;
    String n,e,m,g,b,n1,e1,m1,g1,b1,e2;
    String myBase64Image;
    Activity context = PasswordActivity.this;
    User user;
    private boolean d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        final Bundle bundle = getIntent().getExtras();
        passRegBtn = findViewById(R.id.passRegBtn);
        passLayout = findViewById(R.id.regPassLayout);
        regShowPass = findViewById(R.id.regShowPass);
        userDb = new Databasehelper(PasswordActivity.this);

        regShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    passLayout.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    passLayout.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });


        passRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean x = getIntent().getBooleanExtra("fromReg",false);
                if (x ){
                    if (pass()){

                        n = getIntent().getStringExtra("name_send");
                        e = getIntent().getStringExtra("email_send");
                        m = getIntent().getStringExtra("mobile_send");
                        g = getIntent().getStringExtra("gender_send");
                        b = getIntent().getStringExtra("date_send");

                        PrefUtil.putbooleanPref(PrefUtil.PRE_LOGINCHECK,true,PasswordActivity.this);

                        PrefUtil.putstringPref("name",n,context);
                        PrefUtil.putstringPref("email",e,context);
                        PrefUtil.putstringPref("mobile",m,context);
                        PrefUtil.putstringPref("gender",g,context);
                        PrefUtil.putstringPref("birthdate",b,context);

                        Intent loginIntent = new Intent(PasswordActivity.this, NavigationDrawer.class);
                        startActivity(loginIntent);
                        AddData();
                        finish();

                    }
                }else if (getIntent().getBooleanExtra("isForget",false)){
                    //From forget Activity.
                    if (pass()){

                        PrefUtil.putstringPref("name",n1,context);
                        PrefUtil.putstringPref("email",e1,context);
                        PrefUtil.putstringPref("mobile",m1,context);
                        PrefUtil.putstringPref("gender",g1,context);
                        PrefUtil.putstringPref("birthdate",b1,context);
                        Intent loginIntent = new Intent(PasswordActivity.this, NavigationDrawer.class);
                        startActivity(loginIntent);
                        finish();
                        AddPassword();
                    }
                }else {

                }
            }
        });

    }
    boolean pass(){
        reg_pass_text = passLayout.getText().toString();
        if(!reg_pass_text.isEmpty()){
            if(reg_pass_text.length() >= 6){
                return true;
            }else{
                passLayout.setError("Length should be more then 6!");
            }

        }else{
            passLayout.setError("Enter Password!");
        }

        return false;
    }

    public void AddData(){
        boolean isInserted = userDb.insertData(n,e,m,g,b,reg_pass_text);
        if(isInserted){
            Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Data not Inserted!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddPassword(){
        user = new User();
        String email = getIntent().getStringExtra("emailSend");

        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        userDb = new Databasehelper(PasswordActivity.this);
        user = userDb.getEmailData(email);
        n1 = user.getUser_name();
        e1 = user.getEmail();
        m1 = user.getMobile_number();
        g1 = user.getGender();
        b1 = user.getBirthdate();
        boolean isTrue = userDb.UpdateForgotPasswordData(n1,e1,m1,g1,b1,reg_pass_text);
        if(isTrue){
            Toast.makeText(this, "Password set", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PasswordActivity.this,LoginActivity.class));
            finish();
        }else{
            Toast.makeText(this, "Password is not set", Toast.LENGTH_SHORT).show();
        }
    }

    public void UpdatePassword(){
        user = new User();
        user = (User)getIntent().getSerializableExtra("changePass");
        e2 = user.getEmail();
        boolean isTrue = userDb.onUpdatePassword(reg_pass_text,e2);
        if(isTrue){
            Toast.makeText(this, "Password set", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PasswordActivity.this,LoginActivity.class));
            finish();
        }else{
            Toast.makeText(this, "Password is not set", Toast.LENGTH_SHORT).show();
        }
    }
}