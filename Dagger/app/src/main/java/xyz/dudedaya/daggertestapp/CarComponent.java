package xyz.dudedaya.daggertestapp;

import dagger.Component;

@Component
public interface CarComponent {

    //A method to get a new instance of a Car object using dependency injection.
    Car getCar();
}
