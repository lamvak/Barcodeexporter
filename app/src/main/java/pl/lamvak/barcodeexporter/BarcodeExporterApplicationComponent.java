package pl.lamvak.barcodeexporter;

import dagger.Component;
import pl.lamvak.barcodeexporter.store.DataStoreModule;

@Component(modules = {HomeActivityModule.class, DataStoreModule.class})
public interface BarcodeExporterApplicationComponent {
    void inject(BarcodeExporterApplication barcodeExporterApplication);
}
