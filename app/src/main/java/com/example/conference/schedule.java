package com.example.conference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class schedule extends AppCompatActivity {

    ImageButton back;
    TextView name, meetingId;
    EditText password, date, time;
    CheckBox showpassword;
    Button schedule;
    Spinner hour ;
   // Spinner min;
    Date currentdate = null, currentTime = null;
    private SimpleDateFormat mdformat;
    private int current_year;
    private int current_month;
    private int current_date;

    private int current_hour;
    private int current_min;
    private String mHours;
    private String mMins;


    public Date enteredStartDate = null;
    public Date enteredEndDate = null;



    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        getSupportActionBar().hide();


        mdformat = new SimpleDateFormat("dd/MM/yyyy hh:mm");


        back = findViewById(R.id.scheduleback);

        name = findViewById(R.id.schedulename);
        meetingId = findViewById(R.id.scheduleID);
        password = findViewById(R.id.schedulePass);
        date = findViewById(R.id.scheduleDate);
        time = findViewById(R.id.scheduleTime);
        showpassword = findViewById(R.id.schedulecheck);
        hour = findViewById(R.id.scheduleHour);
        //min = findViewById(R.id.schedulemin);
        schedule = findViewById(R.id.schedule1);

        ArrayAdapter<CharSequence> adapterhours = ArrayAdapter.createFromResource(schedule.this, R.array.hours, android.R.layout.simple_spinner_item);
        adapterhours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //ArrayAdapter<CharSequence> adaptermin = ArrayAdapter.createFromResource(schedule.this, R.array.mins, android.R.layout.simple_spinner_item);
       // adaptermin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hour.setAdapter(adapterhours);
        //min.setAdapter(adaptermin);

        hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mHours = hour.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMins = min.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(schedule.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Query query = databaseReference.orderByChild("uid").equalTo(firebaseUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    String sname = "" +ds.child("Name").getValue();
                    String id = "" +ds.child("timeStamp").getValue();
                    String uid = "" + ds.child("uid").getValue();

                    name.setText(sname);
                    meetingId.setText(id);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

      String date_n = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault()).format(new Date());
        date.setText(date_n);

        final String time_n = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        time.setText(time_n);

        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                try {
                   currentdate = mdformat.parse(mdformat.format(cal.getTime()));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                current_date = cal.get(Calendar.DAY_OF_MONTH);
                current_month = cal.get(Calendar.MONTH);
                current_year = cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(schedule.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth+ "/"  + (month+1) + "/" +year);
                    }
                }, current_year, current_month, current_date);
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                current_hour = calendar.get(Calendar.HOUR_OF_DAY);
                current_min = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(schedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay + ":" + minute);
                    }
                }, current_hour, current_min, false);
                timePickerDialog.show();
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredDateTime = date.getText().toString() + " " + time.getText().toString();

                try {
                    enteredStartDate = mdformat.parse(enteredDateTime);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(enteredStartDate);
                    cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + Integer.parseInt(mHours));
                    enteredEndDate = cal.getTime();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                final String m_pass = password.getText().toString().trim();
                final String m_id = meetingId.getText().toString().trim();
                final String m_name = name.getText().toString().trim();
                final String m_uid = firebaseUser.getUid();

                if (TextUtils.isEmpty(m_pass)){
                    Toast.makeText(schedule.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

               /* if (enteredStartDate == null){
                    Toast.makeText(schedule.this, "Please enter the meeting Start Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentdate.compareTo(enteredEndDate) == 1){
                    Toast.makeText(schedule.this, "Enter date is less then current date", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                else {
                    uploadData(m_pass, m_id, m_name, m_uid,  String.valueOf(enteredStartDate), String.valueOf(enteredEndDate));
                }
            }
        });



    }

    private void uploadData( String m_pass, String m_id ,String mname, String m_uid, String enteredstartDate, String endtereendDate) {
        final String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("Host", mname);
        hashMap.put("Id", m_id);
        hashMap.put("MeetingPassword", m_pass);
        hashMap.put("uid", m_uid);
        hashMap.put("Startdatetime", enteredstartDate);
        hashMap.put("Endtime", endtereendDate);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Meetings");
        ref.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(schedule.this, "Meeting Scheduled", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(schedule.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


}
