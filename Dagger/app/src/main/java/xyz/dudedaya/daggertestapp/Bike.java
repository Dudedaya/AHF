package xyz.dudedaya.daggertestapp;

import android.util.Log;

import javax.inject.Inject;

public class Bike {
    private static final String TAG = "Bike";

    @Inject Engine engine;
    @Inject Wheels wheels;

    @Inject
    public Bike() {
    }

    public void beep() {
        Log.d(TAG, "BEEP!");
    }
}
