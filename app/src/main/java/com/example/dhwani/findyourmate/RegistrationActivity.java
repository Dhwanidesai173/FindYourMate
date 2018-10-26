package com.example.dhwani.findyourmate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static com.example.dhwani.findyourmate.Constants.ONE_TIME_PASSWORD;
import static com.example.dhwani.findyourmate.Constants.generateOTP;

public class RegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    RadioGroup radioGroup;
    Button regBtn;
    String year,month,day;
    EditText regName, regEmail, regMobile, regBdate;
    String name_text, email_text, mobile_text, date_text, gender_text, bdate;
    String temp = "Male";
    RadioButton maleRadioBtn, FemaleRadioBtn;
    Databasehelper db;
    Boolean fromReg = true;
    private AlertDialog dialog;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regBtn = findViewById(R.id.b1);
        regName = findViewById(R.id.ed1);
        regEmail = findViewById(R.id.ed3);
        regMobile = findViewById(R.id.ed2);
        regBdate = findViewById(R.id.ed4);
        maleRadioBtn = findViewById(R.id.r1);
        FemaleRadioBtn = findViewById(R.id.r2);
        radioGroup = findViewById(R.id.redioGrp);
        db = new Databasehelper(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) findViewById(checkedId);
                temp = rb.getText().toString();
            }
        });


        regBdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        mcurrentDate.set(Calendar.YEAR, selectedyear);
                        mcurrentDate.set(Calendar.MONTH, selectedmonth);
                        mcurrentDate.set(Calendar.DAY_OF_MONTH,
                                selectedday);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                        bdate = sdf.format(mcurrentDate.getTime());
                        regBdate.setText(sdf.format(mcurrentDate.getTime()));
                    }
                }, mYear, mMonth, mDay);

                mDatePicker.setTitle(bdate);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();

            }
        });


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCondition()) {
                    generateOTP();
                    Toast.makeText(RegistrationActivity.this, "OTP is: " + ONE_TIME_PASSWORD, Toast.LENGTH_LONG).show();

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegistrationActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_otp, null);
                    final EditText mOtp = (EditText) mView.findViewById(R.id.regOtp);
                    Button mCont = mView.findViewById(R.id.regContinue);
                    TextView regResend = mView.findViewById(R.id.regResendOtp);
                    regResend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            generateOTP();
                            Toast.makeText(RegistrationActivity.this, "OTP is: " + ONE_TIME_PASSWORD, Toast.LENGTH_LONG).show();

                        }
                    });

                    mCont.setOnClickListener(new View.OnClickListener() {


                        @Override
                        public void onClick(View v) {
                            if (mOtp.getText().toString().equals(ONE_TIME_PASSWORD)) {
                                Intent homeIntent = new Intent(RegistrationActivity.this, PasswordActivity.class);
                                fromReg = true;
                                homeIntent.putExtra("name_send", name_text);
                                homeIntent.putExtra("email_send", email_text);
                                homeIntent.putExtra("mobile_send", mobile_text);
                                homeIntent.putExtra("gender_send", gender_text);
                                homeIntent.putExtra("date_send", date_text);
                                homeIntent.putExtra("fromReg", fromReg);
                                startActivity(homeIntent);
                                finish();
                                Toast.makeText(RegistrationActivity.this, "Login Success!!", Toast.LENGTH_SHORT).show();
                            } else if (mOtp.getText().toString().isEmpty()) {
                                Toast.makeText(RegistrationActivity.this, "Enter OTP!!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Wrong OTP!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mBuilder.setView(mView);
                    dialog = mBuilder.create();
                    dialog.show();
                }
            }
        });

    }

    boolean checkCondition() {
        name_text = regName.getText().toString();
        email_text = regEmail.getText().toString();
        mobile_text = regMobile.getText().toString();
        gender_text = temp;
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        date_text = bdate;

        if (!name_text.isEmpty()) {
            if (!email_text.isEmpty()) {
                if (!db.isEmailExist(email_text)) {
                    if (email_text.matches(ePattern)) {
                        if (!mobile_text.isEmpty()) {
                            if (!db.isMobileNumberExist(mobile_text)) {
                                if (mobile_text.length() == 10) {
                                    return true;
                                } else {
                                    Toast.makeText(RegistrationActivity.this, "Enter mobile number properly", Toast.LENGTH_SHORT).show();
                                    regMobile.setError("Enter mobile");
                                }
                            } else {
                                regMobile.setError("Already exist");
                            }
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Enter mobile number properly", Toast.LENGTH_SHORT).show();
                            regMobile.setError("Enter mobile");
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                        regEmail.setError("Enter valid email address");
                    }
                } else {
                    regEmail.setError("Already Exist");

                }
            } else {
                Toast.makeText(RegistrationActivity.this, "Enter the Email ID properly", Toast.LENGTH_SHORT).show();
                regEmail.setError("Enter email");
            }
        } else {
            Toast.makeText(RegistrationActivity.this, "Enter the name", Toast.LENGTH_SHORT).show();
            regName.setError("Enter name");
        }
        return false;
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);

    }

    public void setDate(Calendar cal) {
        final DateFormat dateFormate = DateFormat.getDateInstance(DateFormat.MEDIUM);
        bdate = dateFormate.format(cal.getTime());
        regBdate.setText(dateFormate.format(cal.getTime()));

    }

    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);

        }
    }
}
