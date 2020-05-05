package com.example.conference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {

    EditText email, name, password, confpass, phone;
    TextView signin;
    Button registerb;

    private FirebaseAuth firebaseAuth;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();
        email = findViewById(R.id.Remail);
        name = findViewById(R.id.Rname);
        password = findViewById(R.id.Rpassword);
        confpass = findViewById(R.id.Rconfpass);
        phone = findViewById(R.id.Rphone);

        signin = findViewById(R.id.Rlogin);

        registerb = findViewById(R.id.register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.registerP);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(register.this, login.class);
                startActivity(i);

            }
        });

        registerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputdata();
            }
        });



    }

    private String sname, semail, sphone, spassword, sconfpass;
    private void inputdata() {
        sname = name.getText().toString().trim();
        semail = email.getText().toString().trim();
        sphone = phone.getText().toString().trim();
        spassword = password.getText().toString().trim();
        sconfpass = confpass.getText().toString().trim();

        if (TextUtils.isEmpty(sname)){
            Toast.makeText(this, "Please enter your Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(semail).matches()){
            Toast.makeText(this, "Please enter a valid Email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(spassword)){
            Toast.makeText(this, "Enter your Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(sconfpass)){
            Toast.makeText(this, "Please confirm your Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(sphone)){
            Toast.makeText(this, "Enter your Phone", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spassword.length()<6){
            Toast.makeText(this, "Password length should contain minimum 6 characters ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!spassword.equals(sconfpass)){
            Toast.makeText(this, "Your Password didn't Match", Toast.LENGTH_SHORT).show();
            return;
        }

        createAccount();


    }

    private void createAccount() {
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(semail,spassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        saveFireData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void saveFireData() {

        String timeStamp = ""+System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", "" +firebaseAuth.getUid());
        hashMap.put("Name", "" +sname );
        hashMap.put("Email", "" + semail);
        hashMap.put("Phone", "" + sphone);
        hashMap.put("timeStamp", "" + timeStamp);
        hashMap.put("meetingId", ""+ timeStamp);
        hashMap.put("online", "true");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(register.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
