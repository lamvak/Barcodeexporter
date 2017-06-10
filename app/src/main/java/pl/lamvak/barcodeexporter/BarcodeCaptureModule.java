package pl.lamvak.barcodeexporter;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = BarcodeCaptureSubcomponent.class)
public abstract class BarcodeCaptureModule {
    @Binds
    @IntoMap
    @ActivityKey(BarcodeCapture.class)
    abstract AndroidInjector.Factory<? extends Activity> bindBarcodeCaptureInjectorFactory(BarcodeCaptureSubcomponent.Builder builder);
}
