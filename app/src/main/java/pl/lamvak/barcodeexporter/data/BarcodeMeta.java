package pl.lamvak.barcodeexporter.data;

import android.graphics.Rect;

import com.google.android.gms.vision.barcode.Barcode;

public class BarcodeMeta {
    private String code;
    private Rect box;
    private String sourceImageRef;
    private Integer creationTimestamp;

    public BarcodeMeta(String code, Rect box, String sourceImageRef, Integer creationTimestamp) {
        this.code = code;
        this.box = box;
        this.sourceImageRef = sourceImageRef;
        this.creationTimestamp = creationTimestamp;
    }

    public String getCode() {
        return code;
    }

    public Rect getBox() {
        return box;
    }

    public String getSourceImageRef() {
        return sourceImageRef;
    }

    public Integer getCreationTimestamp() {
        return creationTimestamp;
    }

    public static BarcodeMeta from(Barcode barcode, String sourceImageRef, Integer creationTimestamp) {
        return new BarcodeMeta(
                barcode.displayValue,
                new Rect(barcode.getBoundingBox()),
                sourceImageRef,
                creationTimestamp
        );
    }
}
