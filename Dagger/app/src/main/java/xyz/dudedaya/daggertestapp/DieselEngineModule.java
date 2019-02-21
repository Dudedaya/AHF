package xyz.dudedaya.daggertestapp;

import dagger.Module;
import dagger.Provides;

@Module
public class DieselEngineModule {
    //We can also inject values at runtime.
    private int horsePower;

    public DieselEngineModule(int horsePower) {
        this.horsePower = horsePower;
    }

    @Provides
    Engine provideEngine() {
        return new DieselEngine(horsePower);
    }

}
