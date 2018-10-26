package com.example.dhwani.findyourmate;

import java.io.Serializable;
public class User implements Serializable {

    int id;
    String user_name;
    String email;
    String password;
    String mobile_number;
    String birthdate;
    String gender;
    String image;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfileImage() {
        return image;
    }

    public void setProfileImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" + "first_name='" + user_name + '\'' + ", email='" + email + '\'' + ", mobile_number='" + mobile_number + '\'' + ", gender='" + gender + '\'' + ", birthdate='" + birthdate + '\'' + ", password='" + password + '\'' + ", profilePhoto='" + image + '\'' + '}';
    }


}
