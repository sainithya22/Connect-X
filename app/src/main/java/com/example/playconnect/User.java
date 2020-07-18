package com.example.playconnect;

public class User {
    String name, gender, emailAddress;
    int age;

    public User(String name, String gender, String emailAddress, int age) {
        this.name = name;
        this.gender = gender;
        this.emailAddress = emailAddress;
        this.age = age;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}

    public String getEmailAddress() {return emailAddress;}
    public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress;}

    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}
}