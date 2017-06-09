package pl.lamvak.barcodeexporter;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface HomeActivitySubcomponent extends AndroidInjector<HomeActivity> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<HomeActivity> {}
}
