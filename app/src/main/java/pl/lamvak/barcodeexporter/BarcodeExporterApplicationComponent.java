package pl.lamvak.barcodeexporter;

import dagger.Component;
import pl.lamvak.barcodeexporter.store.DataStoreModule;

@Component(modules = {ApplicationModule.class, HomeActivityModule.class, DataStoreModule.class,
        ShowBarcodeActivityModule.class, CodesListModule.class, BarcodeCaptureModule.class,
})
public interface BarcodeExporterApplicationComponent {
    void inject(BarcodeExporterApplication barcodeExporterApplication);
}
