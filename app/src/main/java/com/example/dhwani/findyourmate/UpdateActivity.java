package com.example.dhwani.findyourmate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static com.example.dhwani.findyourmate.Constants.encodeToBase64;

public class UpdateActivity extends AppCompatActivity {
    User user;
    Databasehelper db;
    EditText updateName, updateEmail, updateMobile;
    Button updateContBtn;
    CircleImageView circleImageView;
    String update_email_txt, update_name_txt, update_mobile_txt;
    String name_update, mobile_update;
    String myBase64Image;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        user = new User();
        db = new Databasehelper(UpdateActivity.this);
        circleImageView = (CircleImageView) findViewById(R.id.profile_image);
        updateName = findViewById(R.id.u_name);
        updateEmail = findViewById(R.id.u_email);
        updateMobile = findViewById(R.id.u_phone);
        updateContBtn = findViewById(R.id.save);

        user = (User) getIntent().getSerializableExtra("account");
        update_name_txt = user.getUser_name();
        update_email_txt = user.getEmail();
        update_mobile_txt = user.getMobile_number();
        updateName.setText(update_name_txt);
        updateEmail.setText(update_email_txt);
        updateEmail.setEnabled(false);
        updateMobile.setText(update_mobile_txt);

        if (user.getProfileImage() != null) {
            Bitmap bitmap = Constants.decodeBase64(user.getProfileImage());
            circleImageView.setImageBitmap(bitmap);
        }

       // Bitmap bitmap = Constants.decodeBase64(user.getProfileImage());
       // circleImageView.setImageBitmap(bitmap);

        updateContBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmapImage = ((BitmapDrawable) circleImageView.getDrawable()).getBitmap();
                myBase64Image = encodeToBase64(bitmapImage, Bitmap.CompressFormat.JPEG, 100);

                name_update = updateName.getText().toString();
                mobile_update = updateMobile.getText().toString();

                if (isValid()) {
                    UpdateData();
                    user.setProfileImage(myBase64Image);
                    user.setUser_name(name_update);
                    user.setMobile_number(mobile_update);
                }
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(UpdateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(UpdateActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        Toast.makeText(UpdateActivity.this, "Already have an permission", Toast.LENGTH_SHORT).show();
                        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(UpdateActivity.this);
                    }
                } else {
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(UpdateActivity.this);
                }
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri updateImageUri = result.getUri();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(updateImageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    circleImageView.setImageBitmap(bitmap);


                } catch (FileNotFoundException e) {

                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isValid() {

        if (!name_update.isEmpty()) {
            if (!mobile_update.isEmpty()) {
                return true;
            } else {
                updateMobile.setError("Enter mobile number");
            }
        } else {
            updateName.setError("Enter name");
        }

        return false;
    }

    public void UpdateData() {
        db = new Databasehelper(UpdateActivity.this);
        boolean isTrue = db.onUpdate(name_update, mobile_update, myBase64Image, update_email_txt);
        if (isTrue) {
            Intent i = new Intent(UpdateActivity.this, ProfileActivity.class);
            startActivity(i);
            finish();
            Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();

        }
    }
}
