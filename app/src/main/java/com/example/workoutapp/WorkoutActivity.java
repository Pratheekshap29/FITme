package com.example.workoutapp;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity {

    private ArrayList<String> wNameArr=new ArrayList<String>();
    private ArrayList<String> wTimeArr=new ArrayList<String>();

        TextView routineName,delay;
    RecyclerView recview;
    myworkoutadapter adapter;


    FloatingActionButton faddw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        routineName=(TextView)findViewById(R.id.RoutineNameTitle);
        delay=(TextView)findViewById(R.id.DelayTitle);


        Intent i = getIntent();
        String routine_name = i.getStringExtra("routine_name");
        String routine_delay = i.getStringExtra("routine_delay");
        String key= i.getStringExtra("key");

        routineName.setText(routine_name);
        delay.setText(routine_delay);

        recview=(RecyclerView)findViewById(R.id.recview2);

        recview.setLayoutManager(new LinearLayoutManager(this));

     // Log.w(String.valueOf(0), key);

        
        FirebaseRecyclerOptions<workoutmodel> options =
                new FirebaseRecyclerOptions.Builder<workoutmodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("routines").child(key).child("workouts"), workoutmodel.class).build();
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("routines").child(key).child("workouts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    workoutmodel model = snapshot.getValue(workoutmodel.class);
                    wNameArr.add(model.getName());
                    wTimeArr.add(model.getTime());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapter=new myworkoutadapter(options,key,routine_name,routine_delay,wNameArr,wTimeArr);
        recview.setAdapter(adapter);
        faddw=(FloatingActionButton)findViewById(R.id.addBtn2);
        faddw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),addworkout.class);
                i.putExtra("key",key);
                startActivity(i);
            }
        });
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(WorkoutActivity.this,WorkoutTimerActivity.class);
//                i.putExtra("key",key);
//                i.putExtra("routine_name",routine_name);
//                i.putExtra("delay_delay",routine_delay);
//                startActivity(i);
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}