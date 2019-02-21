package xyz.dudedaya.daggertestapp;

import dagger.Component;

@Component
public interface BikeComponent {

    //A method to inject a field to MainActivity
    void inject(MainActivity mainActivity);
}
