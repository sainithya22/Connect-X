package com.example.playconnect;

import android.location.Address;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Match {
    String sport;
    String date;
    Integer requiredPlayers;
    String  address;
    String time;
    Integer availablePlayers;
    String admin;
    ArrayList<String> player_ids ;

    public Match(){

    }

    public  Match(String sport, String date , Integer players, String time, String address, String admin){
        this.sport = sport;
        this.date=date;
        this.address=address;
        this.requiredPlayers=players;
        this.time=time;
        this.admin=admin;
    }
    public String getSport(){
        return this.sport;
    }
    public String getLocation(){
        return this.address;
    }
    public Integer getNoOfPlayers(){
        return this.requiredPlayers;
    }

    public String getDate(){
        return this.date;
    }

    public String getTime(){
        return this.time;
    }
}
