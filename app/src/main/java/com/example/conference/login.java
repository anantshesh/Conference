package com.example.conference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class login extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView sighup;

    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
//            LOGS USER IN ONCE IT FINDS HE HAD LOGGED IN!
            startActivity(new Intent(login.this, MainActivity.class));
            finish();
        }

        progressBar = findViewById(R.id.loginP);
        email = findViewById(R.id.Lemail);
        password = findViewById(R.id.Lpassword);

        login = findViewById(R.id.login);

        sighup = findViewById(R.id.Register);

        sighup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, register.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memail = email.getText().toString().trim();
                String mpassword = password.getText().toString().trim();

                if(TextUtils.isEmpty(memail)){
                    email.setError("Invalid Email");
                    return;
                }

                if(TextUtils.isEmpty(mpassword)){
                    password.setError("Invalid Password");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                String email = user.getEmail();
                                String uid = user.getUid();

                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("email", email);
                                hashMap.put("uid", uid);
                               /* hashMap.put("name", "");
                                hashMap.put("phone", "");
                                hashMap.put("district", "");
                                hashMap.put("state", "");
                                hashMap.put("pincode", "");
                                hashMap.put("image", "");*/

                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                DatabaseReference reference = database.getReference("User");

                                reference.child(uid).setValue(hashMap);

                            }



                            Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT ).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        } else {
                            Toast.makeText(login.this,"Error" + task.getException().getMessage(),Toast.LENGTH_SHORT ).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });
    }
}
