package pl.lamvak.barcodeexporter;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface ShowBarcodeActivitySubcomponent extends AndroidInjector<ShowBarcodeActivity> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<ShowBarcodeActivity> {}
}
