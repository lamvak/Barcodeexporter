package pl.lamvak.barcodeexporter.store.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pl.lamvak.barcodeexporter.data.BarcodeMeta;
import pl.lamvak.barcodeexporter.store.DataStore;

import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.BOTTOM;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.CODE;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.CREATION_TIMESTAMP;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.LEFT;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.RIGHT;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.SOURCE_IMG_REF;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.TOP;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.TableNames.BARCODE_META;

public class SQliteDataStore implements DataStore {

    private final SQLiteDatabase database;
    private String[] barcodeColumns = {CODE.name(), LEFT.name(), TOP.name(), RIGHT.name(),
            BOTTOM.name(), SOURCE_IMG_REF.name(), CREATION_TIMESTAMP.name()};
    private String[] codeColumn = {CODE.name()};

    public SQliteDataStore(Context context) {
        database = new BarcodeExporterSQliteOpenHelper(context).getWritableDatabase();
    }

    @Override
    public ArrayList<BarcodeMeta> loadAllBarcodes() {
        ArrayList<BarcodeMeta> barcodes = new ArrayList<>();
        Cursor barcodesCursor = database.query(BARCODE_META.name(), barcodeColumns, null, null, null, null, null, null);
        while (barcodesCursor.moveToNext()) {
            barcodes.add(barcodeFromDBCursor(barcodesCursor));
        }
        return barcodes;
    }

    private String stringOf(Cursor cursor, Enum<?> fieldName) {
        return cursor.getString(columnIndex(cursor, fieldName));
    }

    private int intOf(Cursor cursor, Enum<?> fieldName) {
        return cursor.getInt(columnIndex(cursor, fieldName));
    }

    private int columnIndex(Cursor cursor, Enum<?> fieldName) {
        return cursor.getColumnIndex(fieldName.name());
    }

    private BarcodeMeta barcodeFromDBCursor(Cursor barcodesCursor) {
        return new BarcodeMeta(
                stringOf(barcodesCursor, CODE),
                new Rect(intOf(barcodesCursor, LEFT),
                        intOf(barcodesCursor, TOP),
                        intOf(barcodesCursor, RIGHT),
                        intOf(barcodesCursor, BOTTOM)),
                stringOf(barcodesCursor, SOURCE_IMG_REF),
                intOf(barcodesCursor, CREATION_TIMESTAMP)
        );
    }

    @Override
    public void insertOrUpdate(BarcodeMeta barcode) {
        ContentValues metaData = new ContentValues();
        metaData.put(CODE.name(), barcode.getCode());
        metaData.put(LEFT.name(), barcode.getBox().left);
        metaData.put(TOP.name(), barcode.getBox().top);
        metaData.put(RIGHT.name(), barcode.getBox().right);
        metaData.put(BOTTOM.name(), barcode.getBox().bottom);
        metaData.put(SOURCE_IMG_REF.name(), barcode.getSourceImageRef());
        metaData.put(CREATION_TIMESTAMP.name(), barcode.getCreationTimestamp());
        database.insertWithOnConflict(BARCODE_META.name(), null,
                metaData,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public HashSet<String> loadAllSourceImagesReferences() {
        HashSet<String> references = new HashSet<>();
        List<BarcodeMeta> barcodeMetas = loadAllBarcodes();
        for (BarcodeMeta meta : barcodeMetas) {
            references.add(meta.getSourceImageRef());
        }
        return references;
    }

    @Override
    public ArrayList<String> loadAllCodes() {
        ArrayList<String> codes = new ArrayList<>();
        for (BarcodeMeta meta : loadAllBarcodes()) {
            codes.add(meta.getCode());
        }
        return codes;
    }

    @Override
    public BarcodeMeta loadBarcodeWithCode(String barcodeCode) {
        Cursor barcodesCursor = database.query(BARCODE_META.name(), barcodeColumns, CODE.name() + " = ?", new String[]{barcodeCode}, null, null, null, null);
        if (!barcodesCursor.moveToNext()) {
            return null;
        }
        BarcodeMeta meta = barcodeFromDBCursor(barcodesCursor);
        if (barcodesCursor.moveToNext()) {
            throw new RuntimeException("Non-unique barcode code: " + barcodeCode);
        }
        return meta;
    }

}
