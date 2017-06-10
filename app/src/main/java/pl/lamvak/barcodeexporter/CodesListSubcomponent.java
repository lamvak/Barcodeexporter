package pl.lamvak.barcodeexporter;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface CodesListSubcomponent extends AndroidInjector<CodesList> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<CodesList> {}
}
