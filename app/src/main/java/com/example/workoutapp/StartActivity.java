package com.example.workoutapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class StartActivity extends AppCompatActivity {
    TextView titlepage, subtitlepage,btnexercise;
    ImageView imgpage;
    Animation animimgpage,bttone,bttwo,btthree,lefttoright;
    View bgprogress,bgprogresstop;
    private int notificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        animimgpage = AnimationUtils.loadAnimation(this,R.anim.animimgpage);
        bttone = AnimationUtils.loadAnimation(this,R.anim.bttone);
        bttwo = AnimationUtils.loadAnimation(this,R.anim.bttwo);
        btthree = AnimationUtils.loadAnimation(this,R.anim.btthree);
        lefttoright = AnimationUtils.loadAnimation(this,R.anim.lefttoright);



        titlepage = (TextView) findViewById(R.id.titlepage);
        subtitlepage = (TextView) findViewById(R.id.subtitlepage);
        btnexercise=(TextView)findViewById(R.id.btnexercise);
        imgpage = (ImageView) findViewById(R.id.imgpage);
        bgprogress = (View) findViewById(R.id.bgprogress);
        bgprogresstop = (View) findViewById(R.id.bgprogresstop);

        // Intent
        Intent intent = new Intent(StartActivity.this, AlarmReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("message", "Its time for your workout !!");

        // PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                StartActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        // AlarmManager
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);


        // Create time.
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY,8);
        startTime.set(Calendar.MINUTE,0);
        startTime.set(Calendar.SECOND, 0);
        long alarmStartTime = startTime.getTimeInMillis();

        // Set Alarm
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime,alarmManager.INTERVAL_DAY, pendingIntent);
       // Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();


        imgpage.startAnimation(animimgpage);
        titlepage.startAnimation(bttone);
        subtitlepage.startAnimation(bttone);

        btnexercise.startAnimation(btthree);
        bgprogress.startAnimation(bttwo);
        bgprogresstop.startAnimation(lefttoright);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(StartActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        },4000);
    }
}