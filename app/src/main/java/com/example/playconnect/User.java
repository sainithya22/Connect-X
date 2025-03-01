package com.example.playconnect;

import java.util.ArrayList;
import java.util.List;

public class User {
    String name, gender, phoneNo;
    int age;
    String password;
    List<String> match_ID_organised;
    List <String> match_ID_accepted;
    String key;
    String userID;
    String ImageURL;

    public User(String name, String gender, String phoneNo, int age) {
        this.name = name;
        this.gender = gender;
        this.phoneNo = phoneNo;
        this.age = age;
        this.match_ID_accepted= new ArrayList<>();
        this.match_ID_organised= new ArrayList<>();
    }

    public User(){

    }

    public String getName() {
        return name;
    }
    public void setUserID(String userID){
        this.userID= userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void addMatch_Organised(String matchId){

        this.match_ID_organised.add(matchId);
    }

    public void addMatch_Accepted(String matchId){

        this.match_ID_accepted.add(matchId);
    }

    public void setImageURL(String imageURL) {
        this.ImageURL = imageURL;
    }

    public String getImageURL() {
        return this.ImageURL;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}