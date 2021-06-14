package com.example.workoutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongConsumer;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private EditText email,password,username,confirmpassword;
    TextView alreadyhaveanaccount;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        confirmpassword=(EditText)findViewById(R.id.confirmPassword);
        username=(EditText)findViewById(R.id.userName);
        register=(Button)findViewById(R.id.Register);
        alreadyhaveanaccount=(TextView)findViewById(R.id.alreadyhaveanaccount);

        auth=FirebaseAuth.getInstance();

        alreadyhaveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email=email.getText().toString().trim();
                String txt_password=password.getText().toString().trim();
                String txt_username=username.getText().toString().trim();
                String txt_confirmpassword=confirmpassword.getText().toString().trim();
//                List<String> r=new ArrayList<String>();
//                r.add("default");

                HashMap<String, Object> map = new HashMap<>();


                HashMap<String, Object> routines = new HashMap<>();
                HashMap<String, Object> defaultRoutine = new HashMap<>();
                HashMap<String, Object> workouts = new HashMap<>();
                HashMap<String, Object> w1 = new HashMap<>();
                HashMap<String, Object> w2 = new HashMap<>();
                HashMap<String, Object> w3 = new HashMap<>();
                HashMap<String, Object> w4 = new HashMap<>();
                HashMap<String, Object> w5 = new HashMap<>();

                map.put("userName", txt_username);
                map.put("routines", routines);

                routines.put("DefaultRoutine", defaultRoutine);

                defaultRoutine.put("delay", "10");
                defaultRoutine.put("routineName", "Default");
                defaultRoutine.put("workouts", workouts);

                workouts.put("w1", w1);
                workouts.put("w2", w2);
                workouts.put("w3", w3);
                workouts.put("w4", w4);
                workouts.put("w5", w5);

                w1.put("name", "jumping jacks");
                w1.put("time", "10");

                w2.put("name", "plank");
                w2.put("time", "5");

                w3.put("name", "high knees");
                w3.put("time", "25");

                w4.put("name", "crunches");
                w4.put("time", "30");

                w5.put("name", "lunges");
                w5.put("time", "30");


               // myRef.setValue(user);

                if(TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_password)||TextUtils.isEmpty(txt_username)||TextUtils.isEmpty(txt_confirmpassword))
                {
                    Toast.makeText(RegisterActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                }
                else if(txt_password.length()<6)
                {
                    Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                }
                else if(!txt_password.equals(txt_confirmpassword))
                {
                    Toast.makeText(RegisterActivity.this, "Password fields do not match. Reenter password ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    registerUser(txt_email,txt_password,map);
                }
            }
        });
    }

    private void registerUser(String email, String password, HashMap<String, Object> map) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
//                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(map)

//                    FirebaseDatabase.getInstance().getReference("users").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful())
//                            {
//                                Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
//                                finish();
//                            }
//                            else
//                            {
//                                Toast.makeText(RegisterActivity.this,"Default routine failed", Toast.LENGTH_SHORT).show();
//
//                            }
//
//
//                        }
//                    });
                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,"Default routine failed", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });

                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}