package com.example.workoutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Thread.sleep;

public class WorkoutTimerActivity extends AppCompatActivity {

//    private static final long START_TIME_IN_MILLIS = 60000;
    private long START_TIME_IN_MILLIS;
    private TextView mTextViewCountDown,workoutName;
    private Button mButtonStartPause;
    private Button mButtonReset,skip,back;
    private CountDownTimer mCountDownTimer,mCountDelayDownTimer;

    private String routine_delay,routine_name,workout_name,workout_time,workout_key,routine_key,wname,wtime;

    private int position,delaycompleted;

    private boolean mTimerRunning;
//    private Thread countdownThread = new Thread();
//    private Thread snapshotThread = new Thread();
    //private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mTimeLeftInMillis,delaymili ;
    ArrayList<String> wNameArr=new ArrayList<String>();
    ArrayList<String> wTimeArr=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_timer);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        workoutName=findViewById(R.id.WorkoutNameText);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        skip = findViewById(R.id.button_skip);
        back = findViewById(R.id.button_back);

     //   delaycompleted=0;
//        Intent w=getIntent();
//        Bundle bundle1=w.getExtras();
//        int positionreturn=bundle1.getInt("positionreturn");
//        delaycompleted=bundle1.getInt("delaycompleted");
//        Log.d("posret", String.valueOf(positionreturn));

        Intent i = getIntent();
        Bundle bundle=i.getExtras();
        routine_name = i.getStringExtra("routine_name");
        routine_delay = i.getStringExtra("routine_delay");
        routine_key=i.getStringExtra("routine_key");
        workout_name = i.getStringExtra("workout_name");
        workout_time = i.getStringExtra("workout_time");
        workout_key= i.getStringExtra("workout_key");
        position=bundle.getInt("position",0);
        wNameArr=(ArrayList)bundle.getParcelableArrayList("wNameArr");
        wTimeArr=(ArrayList)bundle.getParcelableArrayList("wTimeArr");

        delaymili= Long.parseLong(routine_delay) * 1000;


        Log.d("pos", String.valueOf(position));
        Log.d("pos1", String.valueOf(wNameArr));
        Log.d("pos1", String.valueOf(wTimeArr));

//        if(delaycompleted==1)
//        {
//            delaycompleted=0;
//             position= positionreturn;
//        }






//
//        FirebaseDatabase.getInstance().getReference().child("routines").child(routine_key).child("workouts").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    workoutmodel model = snapshot.getValue(workoutmodel.class);
//                    wNameArr.add(model.getName());
//                    wTimeArr.add(model.getTime());
//                }
//
//                Log.d("wname", String.valueOf(wNameArr));
//                Log.d("wtime", String.valueOf(wTimeArr));
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//Log.d("pos", String.valueOf(wNameArr));
       // position=0;
        workoutName.setSelected(true);
        wname=wNameArr.get(position);
        wtime=wTimeArr.get(position);
        workoutName.setText(wname);
        START_TIME_IN_MILLIS = Long.parseLong(wtime) * 1000; // converting time to mili seconds
        mTimeLeftInMillis = START_TIME_IN_MILLIS; // total workout time
        updateCountDownText();

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimerRunning)
                {
//                    mCountDownTimer.cancel();
//                    mTimerRunning = false;
                   pauseTimer();
                }
                mButtonReset.setVisibility(View.INVISIBLE);

    //            delay();

                position=((position+1)%wNameArr.size());
                wname=wNameArr.get(position);
                wtime=wTimeArr.get(position);
                workoutName.setText(wname);
                START_TIME_IN_MILLIS = Long.parseLong(wtime) * 1000; // converting time to mili seconds
                mTimeLeftInMillis = START_TIME_IN_MILLIS; // total workout time
                updateCountDownText();
                //mButtonStartPause.performClick();

            }



        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimerRunning)
                {
                    pauseTimer();
                    mButtonReset.setVisibility(View.INVISIBLE);
                   // mCountDownTimer.cancel();
                    //mTimerRunning = false;
                }
                position=((position-1)<0)?(wNameArr.size()-1):(position-1);
                wname=wNameArr.get(position);
                wtime=wTimeArr.get(position);
                workoutName.setText(wname);
                START_TIME_IN_MILLIS = Long.parseLong(wtime) * 1000; // converting time to mili seconds
                mTimeLeftInMillis = START_TIME_IN_MILLIS; // total workout time
                updateCountDownText();

            }
        });

    }



    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                if (millisUntilFinished < 5000) {
                    Toast.makeText(WorkoutTimerActivity.this, "5 sec left", Toast.LENGTH_SHORT).show();
                }
                if (millisUntilFinished < 1000) {
                    Toast.makeText(WorkoutTimerActivity.this,"Take a Rest "+routine_delay+" sec rest",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFinish() {
//                Toast.makeText(WorkoutTimerActivity.this,"Take a Rest "+routine_delay+" sec rest",Toast.LENGTH_LONG).show();
                mTimerRunning = false;

                mButtonStartPause.setText("Start");
                //mButtonStartPause.setVisibility(View.INVISIBLE);
               // mButtonReset.setVisibility(View.VISIBLE);
//                Toast.makeText(WorkoutTimerActivity.this, "Countdown Timer Finished", Toast.LENGTH_SHORT).show();
//                Intent i=new Intent(WorkoutTimerActivity.this,DelayActivity.class);
//                i.putExtra("delay",routine_delay);
//                i.putExtra("position", ((position+1)%wNameArr.size()));
               // startActivity(i);

                try {

                    sleep(delaymili);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                  skip.performClick();
            }
        }.start();
        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
    }




    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
    }
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }
}
