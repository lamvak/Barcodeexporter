package pl.lamvak.barcodeexporter.store;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import pl.lamvak.barcodeexporter.store.sqlite.SQliteDataStore;

@Module
public class DataStoreModule {
    @Provides
    public DataStore dataBackedBySQlite(Context context) {
        return new SQliteDataStore(context);
    }
}
