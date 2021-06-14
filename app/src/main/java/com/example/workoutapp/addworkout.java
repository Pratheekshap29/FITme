package com.example.workoutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public  class addworkout extends AppCompatActivity  {
    EditText workoutName,time;
    Button add,back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addworkout);

        Intent i = getIntent();
        String key = i.getStringExtra("key");

        workoutName=(EditText)findViewById(R.id.addWorkoutName);
        time=(EditText)findViewById(R.id.addTime);
        add=(Button)findViewById(R.id.addWorkoutToDB);
        back=(Button)findViewById(R.id.backbtnw);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),WorkoutActivity.class));
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processinsertworkout(key);
            }
        });
    }

    private void processinsertworkout(String key) {
        Map<String,Object> map= new HashMap<>();
        map.put("name",workoutName.getText().toString());
        map.put("time",time.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("routines").child(key).child("workouts").push().setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        workoutName.setText("");
                        time.setText("");
                        Toast.makeText(getApplicationContext(),"Inserted Successfully",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Could not be inserted",Toast.LENGTH_LONG).show();

                    }
                });

    }
}