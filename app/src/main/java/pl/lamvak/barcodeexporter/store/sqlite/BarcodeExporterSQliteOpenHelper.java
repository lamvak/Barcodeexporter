package pl.lamvak.barcodeexporter.store.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.BOTTOM;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.CODE;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.CREATION_TIMESTAMP;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.LEFT;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.RIGHT;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.SOURCE_IMG_REF;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.BarcodeMetaFieldNames.TOP;
import static pl.lamvak.barcodeexporter.store.sqlite.SQliteConstants.TableNames.BARCODE_META;

public class BarcodeExporterSQliteOpenHelper extends SQLiteOpenHelper {
    public BarcodeExporterSQliteOpenHelper(Context context) {
        super(context, "barcodes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BARCODE_META + " (" +
                CODE + " TEXT PRIMARY KEY ASC, " +
                TOP + " INT, " +
                BOTTOM + " INT, " +
                LEFT + " INT, " +
                RIGHT + " INT, " +
                SOURCE_IMG_REF + " TEXT, " +
                CREATION_TIMESTAMP + " INT ASC)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("Database migration not supported yet.");
    }
}
