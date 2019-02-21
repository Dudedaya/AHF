package xyz.dudedaya.daggertestapp;

import android.util.Log;

import javax.inject.Inject;

public class Car {
    private static final String TAG = "Car";

    private Engine engine;
    private Wheels wheels;

    //Constructor injection example.
    @Inject
    public Car(Engine engine, Wheels wheels) {
        this.engine = engine;
        this.wheels = wheels;
    }

    public void drive() {
        Log.d(TAG, "driving...");
    }

    //Method injection example.
    //If constructor is injected fields and methods injected automatically in that order.
    //Constructor -> fields -> methods
    //In other case we have to call injection from a component.
    @Inject
    public void enableRemote(Remote remote) {
        remote.setListener(this);
    }
}
