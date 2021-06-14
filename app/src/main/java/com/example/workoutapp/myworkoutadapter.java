package com.example.workoutapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class myworkoutadapter extends FirebaseRecyclerAdapter<workoutmodel,myworkoutadapter.my_viewholder> {

    public final String key;
    public final String routine_name;
    public final String routine_delay;
    public ArrayList<String> wNameArr=new ArrayList<String>();
    public ArrayList<String> wTimeArr=new ArrayList<String>();

    public myworkoutadapter(@NonNull FirebaseRecyclerOptions<workoutmodel> options, String key, String routine_name, String routine_delay, ArrayList<String> wNameArr, ArrayList<String> wTimeArr) {
        super(options);
        this.key=key;
        this.routine_name=routine_name;
        this.routine_delay=routine_delay;
        this.wNameArr=wNameArr;
        this.wTimeArr=wTimeArr;
    }

    @Override
    protected void onBindViewHolder(@NonNull my_viewholder holder, int position, @NonNull workoutmodel model) {
        holder.workoutName.setText(model.getName());
        holder.time.setText(model.getTime()+"sec");

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.workoutName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontentworkout))
                        .setExpanded(true,1100)
                        .create();
//                dialogPlus.show();

                View myview=dialogPlus.getHolderView();


                EditText workoutName=myview.findViewById(R.id.workoutNameUpdate);
                EditText time=myview.findViewById(R.id.timeUpdate);
                Button submit=myview.findViewById(R.id.updateWorkout);

                workoutName.setText(model.getName());
                time.setText(model.getTime());
                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("name",workoutName.getText().toString());
                        map.put("time",time.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("routines").child(key).child("workouts").child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(holder.workoutName.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete..?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("routines").child(key).child("workouts").child(getRef(position).getKey()).removeValue();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i=new Intent(holder.itemView.getContext(),WorkoutTimerActivity.class);
                i.putExtra("routine_name",routine_name);
                i.putExtra("routine_delay",routine_delay);
                i.putExtra("workout_name",model.getName());
                i.putExtra("workout_time",model.getTime());
                i.putExtra("routine_key",key);
                i.putExtra("workout_key",getRef(position).getKey());
                i.putExtra("position",position);
                i.putExtra("wNameArr",wNameArr);
                i.putExtra("wTimeArr",wTimeArr);
                v.getContext().startActivity(i);
            }
        });



    }

    @NonNull
    @Override
    public my_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowworkout,parent,false);
        return new myworkoutadapter.my_viewholder(view);
    }

    class my_viewholder extends RecyclerView.ViewHolder{

        TextView workoutName,time;
        ImageView edit,delete;
        Button start;

        public my_viewholder(@NonNull View itemView) {
            super(itemView);

            workoutName=(TextView)itemView.findViewById(R.id.workoutNameText);
            time=(TextView)itemView.findViewById(R.id.workoutDelayText);
            edit=(ImageView)itemView.findViewById(R.id.editworkoutBtn);
            delete=(ImageView)itemView.findViewById(R.id.deleteworkoutBtn);
            start=(Button)itemView.findViewById(R.id.startContdownTimer);

        }
    }
}
