package pl.lamvak.barcodeexporter.store.sqlite;

public class SQliteConstants {
    public enum BarcodeMetaFieldNames {
        CODE,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        SOURCE_IMG_REF,
        CREATION_TIMESTAMP
    }
    public enum TableNames {
        BARCODE_META
    }
}
