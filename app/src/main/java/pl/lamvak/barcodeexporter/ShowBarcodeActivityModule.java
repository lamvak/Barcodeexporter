package pl.lamvak.barcodeexporter;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = ShowBarcodeActivitySubcomponent.class)
public abstract class ShowBarcodeActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(ShowBarcodeActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindShowBarcodeActivityInjectorFactory(ShowBarcodeActivitySubcomponent.Builder builder);
}
