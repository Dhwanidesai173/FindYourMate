package com.example.dhwani.findyourmate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class Constants {
    public static String sStoreBoolean;
    public static String PRE_LOGINCHECK = "PRE_LOGINCHECK";
    public static String PUT_EMAIL_IN_PREF = "PREF_EMAIL";
    public static String PUT_Phone_IN_PREF = "PREF_Phone";

    public static String FNAME = "";
    public static String LNAME = "";
    public static String BIRTHDAY = "";
    public static String GENDER = "";
    public static String MOBILE = "";
    public static String EMAIL = "";
    public static String PASSWORD = "";
    public static String ONE_TIME_PASSWORD = "";

    public static String IntentRegistrationMobile = "RegistrationMobile";
    public static String IntentRegistrationEmail = "RegistrationEmail";
    public static String IntentForgotEmail = "ForgetEmail";
    public static String IntentForgotEmailPassword = "ForgetEmailPassword";
    public static String IntentForgotMobile = "ForgetMobile";
    public static String IntentForgotMobilePassword = "ForgetMobilePassword";
    public static String IntentLoginMobile = "LoginMobile";
    public static String IntentLoginEmail = "LoginEmail";
    public static String IntentModelEmail = "ModelEmail";
    public static String IntentModelPhone = "ModelPhone";
    public static String IntentModelPhoneFinal = "ModelPhoneFinal";
    public static String IntentIsPhone = "intentIsMobile";
    public static String IntentIsEmail = "intentIsEmail";

    public static String IntentUserMobile = "intentUserMobile";
    public static String IntentUserEmail = "intentUserEmail";

    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static String PhonePattern = "^\\+[0-9]{10,13}$\n";
    public static String IntentIsMobile = "m";
    public static String IntentEmailMobile = "EmailMobileOtp";
    public static String IntentIsEmailPhone = "IntentIsEmailPhone";


    public static String data = "PreData";
    public static String done = "PreBooleanDone";


    public static boolean checkEmail(String text) {
        boolean isEmail = false;
        if (text.matches(emailPattern)) {
            isEmail = true;
        }
        return isEmail;
    }

    public static boolean isValidMobile(String phone) {
        String regEx = "^[0-9]{10}$";
        return phone.matches(regEx);
    }

    public static void generateOTP() {
        Random random = new Random();
        int otp_number = random.nextInt(10000);
        Constants.ONE_TIME_PASSWORD = String.valueOf(otp_number);
        Log.e("TAG", "generateOTP: " + otp_number);
    }


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decoBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decoBytes, 0, decoBytes.length);
    }
}
