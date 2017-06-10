package pl.lamvak.barcodeexporter;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface BarcodeCaptureSubcomponent extends AndroidInjector<BarcodeCapture> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<BarcodeCapture> {}
}
