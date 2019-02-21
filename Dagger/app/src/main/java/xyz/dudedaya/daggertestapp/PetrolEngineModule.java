package xyz.dudedaya.daggertestapp;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PetrolEngineModule {

    //We use Binds and abstract classes for better performance and less code.
    //We pass the PetrolEngine directly here because we use @Inject annotation on its constructor.
    //Binds should always be used when we bind an implementation with an interface.
    @Binds
    abstract Engine bindEngine(PetrolEngine engine);

}
