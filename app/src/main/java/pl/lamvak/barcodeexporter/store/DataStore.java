package pl.lamvak.barcodeexporter.store;

import java.util.ArrayList;
import java.util.HashSet;

import pl.lamvak.barcodeexporter.data.BarcodeMeta;

public interface DataStore {
    ArrayList<BarcodeMeta> loadAllBarcodes();
    void insertOrUpdate(BarcodeMeta barcode);
    HashSet<String> loadAllSourceImagesReferences();
    ArrayList<String> loadAllCodes();
    BarcodeMeta loadBarcodeWithCode(String barcodeCode);
}
