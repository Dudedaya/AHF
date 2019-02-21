package xyz.dudedaya.daggertestapp;

import android.util.Log;

import javax.inject.Inject;

public class Bike {
    private static final String TAG = "Bike";

    @Inject Engine engine;
    //Wheels was removed for sake of simplicity

    @Inject
    public Bike() {
    }

    public void beep() {
        Log.d(TAG, "BEEP!");
    }
}
