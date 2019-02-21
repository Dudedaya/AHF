package xyz.dudedaya.daggertestapp;

import dagger.Module;
import dagger.Provides;

@Module
public class WheelsModule {
    //An example how to use dependency injection with third-party classes and libs.
    //Because we can't annotate the constructors with @Inject

    @Provides
    static Rims provideRims() { //Static keyword is optional
                                //If we don't use any instance state of the module
                                //provide methods can be static
                                //for the better performance.
        return new Rims();
    }

    @Provides
    static Tires provideTires() {
        //External classes can be configured here. Also we can use builder methods here.
        Tires tires = new Tires();
        tires.inflate();
        return tires;
    }

    @Provides
    static Wheels provideWheels(Rims rims, Tires tires) {
        //Where rims and tires are provided by the two methods above. So we can configure our objects
        //before passing them along.
        return new Wheels(rims, tires);
    }
}
