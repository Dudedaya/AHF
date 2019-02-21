package xyz.dudedaya.daggertestapp;

import dagger.Component;

@Component (modules = {WheelsModule.class, DieselEngineModule.class})
//We use wheels module because we get them from the third-party lib
//PetrolEngineModule is here because we can't instantiate an Engine interface
//in a car class.
//Also we can swap the PetrolEngineModule.class with the DieselEngineModule.class
//So we would instantiate a Car with a DieselEngine instead. This is good for testing purposes.
public interface CarComponent {

    //A method to get a new instance of a Car object using dependency injection.
    Car getCar();
}
