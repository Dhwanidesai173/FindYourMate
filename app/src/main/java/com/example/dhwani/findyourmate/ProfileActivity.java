package com.example.dhwani.findyourmate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.dhwani.findyourmate.Constants.encodeToBase64;

public class ProfileActivity extends AppCompatActivity {

    Databasehelper db;
    User user;
    private TextView nameText, myProfile;
    private ListView listView;
    private CircleImageView image;
    String uEmail;
    String uName;
    String[] info;
    String myBase64Image;
    private Button profileSave;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = (TextView) findViewById(R.id.userName);
        image = (CircleImageView) findViewById(R.id.profile_image);
        myProfile = (TextView) findViewById(R.id.myProfile);
        listView = findViewById(R.id.listView);
        profileSave = (Button) findViewById(R.id.profile_save);
        user = new User();
        db = new Databasehelper(ProfileActivity.this);

        uEmail = PrefUtil.getstringPref("email", ProfileActivity.this);
        user = db.getEmailData(uEmail);
        uName = user.getUser_name();
        PrefUtil.putstringPref("name", uName, ProfileActivity.this);
        nameText.setText(uName);
        if (user.getProfileImage() != null) {
            Bitmap bitmap = Constants.decodeBase64(user.getProfileImage());
            image.setImageBitmap(bitmap);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(ProfileActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        Toast.makeText(ProfileActivity.this, "Already have an permission", Toast.LENGTH_SHORT).show();
                        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(ProfileActivity.this);
                    }
                } else {
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(ProfileActivity.this);
                }
            }
        });

        profileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmapImage = ((BitmapDrawable) image.getDrawable()).getBitmap();
                myBase64Image = encodeToBase64(bitmapImage, Bitmap.CompressFormat.JPEG, 100);
                db.insertImage(myBase64Image,uEmail);
            }
        });

        info = new String[]{"Birthdate: "+ user.getBirthdate(), "Email: " + user.getEmail(), "Gender: " + user.getGender(), "Phone Number: " + user.getMobile_number()};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, info);
        listView.setAdapter(arrayAdapter);
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
                    image.setImageBitmap(bitmap);


                } catch (FileNotFoundException e) {

                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
