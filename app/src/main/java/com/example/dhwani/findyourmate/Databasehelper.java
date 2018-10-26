package com.example.dhwani.findyourmate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Databasehelper extends SQLiteOpenHelper {

    public static final String database_name = "user.db";
    public static final String table_name = "userTable";
    public static final String col_name = "name";
    public static final String col_email = "email";
    public static final String col_mobile = "mobile";
    public static final String col_gender = "gender";
    public static final String col_birthdate = "birthdate";
    public static final String col_password = "password";
    public static final String col_profile = "image";
    User user;
    public SQLiteDatabase db;
    String un;

    public Databasehelper(Context context) {
        super(context, database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + table_name + " (" + col_name + " TEXT, " + col_email + " TEXT PRIMARY KEY, " + col_mobile + " TEXT, " + col_gender + " TEXT, " + col_birthdate + " TEXT, " + col_password + " TEXT , " + col_profile + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);

    }

    public boolean insertImage(String image,String email){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_profile,image);
        int update_sql = db.update(table_name,contentValues,col_email +" = ?",new String[]{String.valueOf(email)});

        if (update_sql == -1) {
            return false;
        } else {
            return true;
        }    }

    public boolean insertData(String name, String email, String mobile, String gender, String birthdate, String password) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_name, name);
        contentValues.put(col_email, email);
        contentValues.put(col_mobile, mobile);
        contentValues.put(col_gender, gender);
        contentValues.put(col_birthdate, birthdate);
        contentValues.put(col_password, password);
        long result = db.insert(table_name, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean loginValidation(String email, String password) {
        boolean valid = false;
        db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + col_email + " = '" + email + "' and " + col_password + " = '" + password + "' ", null);
        while (c.moveToNext()) {
            if (c.getString(1) != null) {
                valid = true;
            } else {
                valid = false;
            }
        }

        return valid;
    }

    public boolean isEmailExist(String email) {
        boolean exist = false;
        db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + col_email + " = '" + email + "'", null);
        while (c.moveToNext()) {
            if (c.getString(1) != null) {
                exist = true;
            } else {
                exist = false;
            }
        }

        return exist;
    }

    public boolean isMobileNumberExist(String mobileNumber) {
        boolean exist = false;
        db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + col_mobile + " = '" + mobileNumber + "'", null);
        while (c.moveToNext()) {
            if (c.getString(1) != null) {
                exist = true;
            } else {
                exist = false;
            }
        }

        return exist;
    }

    public ArrayList<User> onDisplay() {

        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table_name, null);

        ArrayList<User> list = new ArrayList<>();
        while (c != null && c.moveToNext()) {

            User user = new User();
            user.setUser_name(c.getString(0));
            user.setEmail(c.getString(1));
            user.setMobile_number(c.getString(2));
            user.setGender(c.getString(3));
            user.setBirthdate(c.getString(4));
            user.setPassword(c.getString(5));
            user.setProfileImage(c.getString(6));
            Log.d("SQLIte", user.getUser_name());
            list.add(user);
        }
        return list;
    }

    public int onDelete(User user) {
        db = getWritableDatabase();
        int delete_sql = db.delete(table_name, col_email + "= ?", new String[]{String.valueOf(user.getEmail())});
        return delete_sql;
    }

    public boolean UpdateForgotPasswordData(String name, String email, String mobile, String gender, String birthdate, String password) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_name, name);
        contentValues.put(col_email, email);
        contentValues.put(col_mobile, mobile);
        contentValues.put(col_gender, gender);
        contentValues.put(col_birthdate, birthdate);
        contentValues.put(col_password, password);
        long result = db.update(table_name, contentValues, col_email + " = ?", new String[]{String.valueOf(email)});
        close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public User getEmailData(String email) {
        user = new User();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + table_name + " WHERE " + col_email + " = '" + email + "'", null);

        while (cursor != null && cursor.moveToNext()) {
            user.setUser_name(cursor.getString(0));
            user.setEmail(cursor.getString(1));
            user.setMobile_number(cursor.getString(2));
            user.setGender(cursor.getString(3));
            user.setBirthdate(cursor.getString(4));
            user.setPassword(cursor.getString(5));
            user.setProfileImage(cursor.getString(6));
        }
        return user;
    }

    public boolean onUpdate(String name,String phone,String image,String email) {

        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col_name,name);
        values.put(col_mobile,phone);
        values.put(col_profile,image);
        int update_sql = db.update(table_name,values,col_email +" = ?",new String[]{String.valueOf(email)});

        if (update_sql == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean onUpdatePassword(String password, String email){
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col_password,password);
        long update_sql = db.update(table_name,values,col_email +" = ?",new String[]{String.valueOf(email)});

        if(update_sql == -1){
            return false;
        }else{
            return true;
        }


    }

    //searching

    public List<User> getUser(){
        db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlselect = {col_name,col_email,col_mobile,col_gender,col_birthdate,col_profile};

        qb.setTables(table_name);
        Cursor cursor = qb.query(db,sqlselect,null,null,null,null,null);

        List<User> result = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setUser_name(cursor.getString(cursor.getColumnIndex(col_name)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(col_email)));
                user.setMobile_number(cursor.getString(cursor.getColumnIndex(col_mobile)));
                user.setGender(cursor.getString(cursor.getColumnIndex(col_gender)));
                user.setBirthdate(cursor.getString(cursor.getColumnIndex(col_birthdate)));
                user.setProfileImage(cursor.getString(cursor.getColumnIndex(col_profile)));


                result.add(user);
            }while (cursor.moveToNext());
        }
        return result;
    }

    public List<String> getNames(){
        db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlselect = {col_name};

        qb.setTables(table_name);
        Cursor cursor = qb.query(db,sqlselect,null,null,null,null,null);

        List<String> result = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{


                result.add(cursor.getString(cursor.getColumnIndex(col_name)));
            }while (cursor.moveToNext());
        }
        return result;
    }

    public List<User> getFriendByName(String name){
        db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlselect = {col_name,col_email,col_mobile,col_gender,col_birthdate,col_profile};

        qb.setTables(table_name);
        Cursor cursor = qb.query(db,sqlselect,col_name+" LIKE ?",new String[]{"%"+name+"%"},null,null,null);

        List<User> result = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setUser_name(cursor.getString(cursor.getColumnIndex(col_name)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(col_email)));
                user.setMobile_number(cursor.getString(cursor.getColumnIndex(col_mobile)));
                user.setGender(cursor.getString(cursor.getColumnIndex(col_gender)));
                user.setBirthdate(cursor.getString(cursor.getColumnIndex(col_birthdate)));
                user.setProfileImage(cursor.getString(cursor.getColumnIndex(col_profile)));
                result.add(user);
            }while (cursor.moveToNext());
        }
        return result;

    }
}

