package com.example.conference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.conference.Adapters.upcomingMeetingAdapter;
import com.example.conference.Model.upcomingMeetings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class upcoming extends AppCompatActivity {

    private List<upcomingMeetings>  upcomingMeetings = new ArrayList<upcomingMeetings>();
    private upcomingMeetingAdapter upcomingMeetingAdapter;
    RecyclerView recyclerView;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.meetings);
        back = findViewById(R.id.back);




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(upcoming.this, MainActivity.class);
                startActivity(intent);
            }
        });


        showMeetings();

    }

    private void showMeetings() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(upcoming.this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        upcomingMeetings.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Meetings");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    upcomingMeetings check = ds.getValue(upcomingMeetings.class);


                        upcomingMeetings.add(check);
                        upcomingMeetingAdapter = new upcomingMeetingAdapter(upcoming.this, upcomingMeetings);
                        recyclerView.setAdapter(upcomingMeetingAdapter);






                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }
}
