package pl.lamvak.barcodeexporter;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application application() {
        return application;
    }

    @Provides
    Context applicationContext() {
        return application;
    }
}
