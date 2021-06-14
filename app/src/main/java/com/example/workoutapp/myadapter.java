
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

import java.util.HashMap;
import java.util.Map;

import static androidx.core.content.ContextCompat.startActivities;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder> {

    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull final model model) {


        holder.routineName.setText(model.getRoutineName());
        holder.delay.setText(model.getDelay()+"sec");

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(holder.itemView.getContext(), "hi", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(holder.itemView.getContext(),WorkoutActivity.class);
                i.putExtra("routine_name",model.getRoutineName());
                i.putExtra("routine_delay",model.getDelay());
                i.putExtra("key",getRef(position).getKey());
                v.getContext().startActivity(i);


            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.routineName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                        .setExpanded(true,1100)
                        .create();
//                dialogPlus.show();

                View myview=dialogPlus.getHolderView();


                EditText routineName=myview.findViewById(R.id.routineNameUpdate);
                EditText delay=myview.findViewById(R.id.delayUpdate);
                Button submit=myview.findViewById(R.id.update);

                routineName.setText(model.getRoutineName());
                delay.setText(model.getDelay());
                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("routineName",routineName.getText().toString());
                        map.put("delay",delay.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("routines")
                                .child(getRef(position).getKey()).updateChildren(map)
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

                AlertDialog.Builder builder=new AlertDialog.Builder(holder.routineName.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete..?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("routines")
                                .child(getRef(position).getKey()).removeValue();

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

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView routineName,delay;
        ImageView edit,delete;
        Button show;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            routineName=(TextView)itemView.findViewById(R.id.workoutNameText);
            delay=(TextView)itemView.findViewById(R.id.workoutDelayText);
            edit=(ImageView)itemView.findViewById(R.id.editRoutineBtn);
            delete=(ImageView)itemView.findViewById(R.id.deleteRoutineBtn);
            show=(Button)itemView.findViewById(R.id.show);

        }
    }
}
