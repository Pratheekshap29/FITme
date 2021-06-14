package com.example.workoutapp;

import android.util.Log;

public class model {
    String routineName,delay;

    model(){

    }

    public model(String routineName, String delay) {
        this.routineName = routineName;
        this.delay = delay;

    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }
}
