package com.example.playconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class GameChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_choice);
    }
    Intent intent;
    /*public void switch_online(View view){
        intent = new Intent(getApplicationContext(), Online.class);
        startActivity(intent);
    }*/

    public void switch_physical(View view){
        intent = new Intent(getApplicationContext(), Offline.class);
        startActivity(intent);
    }
}
