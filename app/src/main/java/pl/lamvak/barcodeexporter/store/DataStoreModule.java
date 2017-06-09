package pl.lamvak.barcodeexporter.store;

import dagger.Module;
import dagger.Provides;

@Module
public class DataStoreModule {
    @Provides
    public DataStore dataBackedBySQlite() {
        return new SQliteDataStore();
    }
}
