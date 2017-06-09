package pl.lamvak.barcodeexporter;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = HomeActivitySubcomponent.class)
public abstract class HomeActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(HomeActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindHomeActivityInjectorFactory(HomeActivitySubcomponent.Builder builder);
}
