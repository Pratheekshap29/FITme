package com.example.workoutapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class DelayActivity extends AppCompatActivity {

    private long DELAY_TIME_IN_MILLIS;
    private long mDelayTimeLeftInMillis ;
    private CountDownTimer mDelayCountDownTimer;
    String delay;
    int position;
    TextView delayText,delayTimeText;
    Button skipRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay);
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        delay=i.getStringExtra("delay");
        //position=bundle.getInt("position",0);
        //Log.d("delay",delay+"and"+position);

        delayText=(TextView)findViewById(R.id.delayText);
        delayTimeText=(TextView)findViewById(R.id.delayCountdownText);
        skipRest=(Button)findViewById(R.id.skipRest);


        DELAY_TIME_IN_MILLIS = Long.parseLong(delay) * 1000; // converting time to mili seconds
        mDelayTimeLeftInMillis = DELAY_TIME_IN_MILLIS; // total workout time
        updateDelayCountDownText();
        startTimer();


    }
    private void startTimer() {
        mDelayCountDownTimer = new CountDownTimer(mDelayTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mDelayTimeLeftInMillis = millisUntilFinished;
                updateDelayCountDownText();
                if (millisUntilFinished < 5000) {
                    Toast.makeText(DelayActivity.this, "5 sec left", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFinish() {
               // mTimerRunning = false;

                //mButtonStartPause.setText("Start");
                //mButtonStartPause.setVisibility(View.INVISIBLE);
                // mButtonReset.setVisibility(View.VISIBLE);
//                Toast.makeText(WorkoutTimerActivity.this, "Countdown Timer Finished", Toast.LENGTH_SHORT).show();
               Intent w=new Intent(DelayActivity.this,WorkoutTimerActivity.class);
//                i.putExtra("delay",routine_delay);
               // w.putExtra("positionreturn",position);
                //Log.d("prr", String.valueOf(position));
               // w.putExtra("delaycompeleted",1);
                startActivity(w);
//                startActivity(i);
//               skip.performClick();
            }
        }.start();
//        mTimerRunning = true;
//        mButtonStartPause.setText("pause");
//        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void updateDelayCountDownText() {
        int minutes = (int) (mDelayTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mDelayTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        delayTimeText.setText(timeLeftFormatted);
    }
}