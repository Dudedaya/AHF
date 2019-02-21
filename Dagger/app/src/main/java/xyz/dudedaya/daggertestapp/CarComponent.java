package xyz.dudedaya.daggertestapp;

import dagger.Component;

@Component (modules = WheelsModule.class) //We use wheels module because we get them from the third-party lib
public interface CarComponent {

    //A method to get a new instance of a Car object using dependency injection.
    Car getCar();
}
