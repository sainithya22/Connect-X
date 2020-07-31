package com.example.playconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    intent = new Intent(getApplicationContext(),GameChoice.class);
                    startActivity(intent);
                }
            }
        };
    }
    Intent intent;
    public void login(View view){
        intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    public void signup(View view){
        intent = new Intent(getApplicationContext(),SignUp.class);
        startActivity((intent));
    }
}
