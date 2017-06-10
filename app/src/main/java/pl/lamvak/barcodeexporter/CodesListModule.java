package pl.lamvak.barcodeexporter;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = CodesListSubcomponent.class)
public abstract class CodesListModule {
    @Binds
    @IntoMap
    @ActivityKey(CodesList.class)
    abstract AndroidInjector.Factory<? extends Activity> bindCodeListInjectorFactory(CodesListSubcomponent.Builder builder);
}
