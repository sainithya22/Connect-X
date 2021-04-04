package com.example.playconnect;

import android.location.Address;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Match {
    public String sport;
    public String id;
    public String date;
    public Integer requiredPlayers;
    public String  address;
    public String time;
    public Integer availablePlayers;
    public String admin;
    public List<String> player_ids;

    public Match(){

    }

    public  Match(String sport, String date , Integer players, String time, String address, String admin, String id){
        this.sport = sport;
        this.date=date;
        this.address=address;
        this.requiredPlayers= players;
        this.availablePlayers = 1;
        this.time=time;
        this.admin=admin;
        this.id=id;
        this.player_ids=new ArrayList<>();
        this.player_ids.add(admin);
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
    public Integer getAvailablePlayers(){return this.availablePlayers;}
    public String getDate(){
        return this.date;
    }
    public String getTime(){
        return this.time;
    }
    public String getMatchID(){return this.id;}
    public  void addPlayer(String playerid){
        this.player_ids.add(playerid);
    }
    public List<String> getPlayer_ids(){return this.player_ids;}

}
