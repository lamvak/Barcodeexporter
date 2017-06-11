package pl.lamvak.barcodeexporter.store;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static pl.lamvak.barcodeexporter.proto.BarcodeExporterProtos.Barcode;

public interface DataStore {
    List<Barcode> loadAllBarcodes();
    void insertOrUpdate(Barcode barcode);
    HashSet<String> loadAllSourceImagesReferences();
    ArrayList<String> loadAllCodes();
    Barcode loadBarcodeWithCode(String barcodeCode);
    void exportAllBarcodesToStream(OutputStream stream) throws IOException;

    void removeBarcode(String code);
}
