package com.example.dhwani.findyourmate;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class AccountSettingActivity extends AppCompatActivity {
    private CardView a_update, a_delete, a_change_password, a_block, a_privacy;
    Databasehelper db;
    Button done;
    EditText oldpass;
    CheckBox showpass_check;
    String email;
    User user;
    private String reg_pass_text,dialoge_uEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        a_change_password = (CardView) findViewById(R.id.change_pass);
        a_update = (CardView) findViewById(R.id.update_info);
        a_delete = (CardView) findViewById(R.id.delete_acc);
        a_privacy = (CardView) findViewById(R.id.privacy);
        a_block = (CardView) findViewById(R.id.block);

        user = new User();
        user = (User) getIntent().getSerializableExtra("account");


        a_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new Databasehelper(AccountSettingActivity.this);
                db.onDelete(user);
                startActivity(new Intent(AccountSettingActivity.this, LoginActivity.class));
                finish();
            }
        });

        a_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AccountSettingActivity.this, UpdateActivity.class);
                i.putExtra("account", user);
                startActivity(i);
                finish();
            }
        });

        a_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AccountSettingActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.old_password, null);
                oldpass = mView.findViewById(R.id.oldPass);
                Button mDone = mView.findViewById(R.id.cnfPass);
                dialoge_uEmail = PrefUtil.getstringPref("email", AccountSettingActivity.this);
                showpass_check = mView.findViewById(R.id.showpass);
                showpass_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            oldpass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        } else {
                            oldpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

    }

}
