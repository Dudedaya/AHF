package xyz.dudedaya.daggertestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Car car;
    @Inject Bike bike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Using dependency injection to instantiate a new Car object.
        CarComponent carComponent = DaggerCarComponent.create();
        car = carComponent.getCar();
        car.drive();

        //Using dependency injection to inject a field "bike".
        BikeComponent bikeComponent = DaggerBikeComponent.create();
        bikeComponent.inject(this);
        bike.beep();
        //bike fields are also injected and can be used
        Log.d(TAG, "onCreate: bike engine = " + bike.engine.toString());
    }
}
