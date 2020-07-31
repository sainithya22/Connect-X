package com.example.playconnect;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class CheckOtp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider phoneAuth;

    final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");

    private EditText  et_otp;
    private Button bt_resend_otp, bt_verify;
    String mobileNoInput;
    private String verificationId;
    public void send_Otp() {
        String phoneNumber = "+91" + mobileNoInput;

        phoneAuth.verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60,// Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                CheckOtp.this, // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(CheckOtp.this, "Auto verification called", Toast.LENGTH_SHORT).show();
                        et_otp.setText(phoneAuthCredential.getSmsCode());
                        verifyUser(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(CheckOtp.this, "Verification failure: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Toast.makeText(CheckOtp.this, "Code sent!", Toast.LENGTH_SHORT).show();
                        verificationId = s;
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_otp);

        mAuth = FirebaseAuth.getInstance();
        phoneAuth = PhoneAuthProvider.getInstance();
        Intent intent = getIntent();
        mobileNoInput= intent.getStringExtra("Mobile_no");


        et_otp = findViewById(R.id.et_otp);
        bt_resend_otp = findViewById(R.id.bt_resend_otp);
        bt_verify = findViewById(R.id.bt_verify);
        send_Otp();

        bt_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_Otp();
            }
        });

        bt_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = et_otp.getEditableText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                verifyUser(credential);
            }
        });
    }

    void verifyUser(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    TextView setPwdView = (TextView) findViewById(R.id.setPwdTextView);
                    setPwdView.setVisibility(View.VISIBLE);
                    TextView passwordRule = (TextView) findViewById(R.id.passwordRule);
                    passwordRule.setVisibility(View.VISIBLE);
                    final Button setPwd = (Button) findViewById(R.id.button7);
                    setPwd.setVisibility(View.VISIBLE);
                    setPwd.setEnabled(true);
                    bt_resend_otp.setVisibility(View.INVISIBLE);
                    bt_resend_otp.setEnabled(false);
                    bt_verify.setVisibility(View.INVISIBLE);
                    bt_verify.setEnabled(false);



                    setPwd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String pwd = setPwd.getText().toString();
                            if(pwd.length()<8){
                                Toast.makeText(getApplicationContext(),"Password must contain atleast 8 characters",Toast.LENGTH_LONG).show();
                            }
                           else{
                                databaseRef.orderByChild("password").equalTo(pwd).addValueEventListener(new ValueEventListener(){
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(!dataSnapshot.exists()) {
                                            //create new user
                                            String id = getIntent().getStringExtra("UserId");
                                            databaseRef.child(id).child("password").setValue(pwd);
                                            Toast.makeText(getApplicationContext(),"Password has been set successfully",Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),"The password is not available",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d("ERROR", databaseError.getMessage()); //Don't ignore errors!
                                    }
                            });
                        }
                    }});


                    Toast.makeText(CheckOtp.this, "Signup Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),GameChoice.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CheckOtp.this, "Signup Failure", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
