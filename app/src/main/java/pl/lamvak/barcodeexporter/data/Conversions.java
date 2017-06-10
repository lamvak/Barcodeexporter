package pl.lamvak.barcodeexporter.data;

import com.google.android.gms.vision.barcode.Barcode;

import pl.lamvak.barcodeexporter.proto.BarcodeExporterProtos;

public class Conversions {
    public static BarcodeExporterProtos.Barcode from(Barcode barcode, String sourceImageRef, Integer creationTimestamp) {
        return BarcodeExporterProtos.Barcode.newBuilder()
                .setCode(barcode.displayValue)
                .setBox(BarcodeExporterProtos.Barcode.Rectangle.newBuilder()
                        .setLeft(barcode.getBoundingBox().left)
                        .setRight(barcode.getBoundingBox().right)
                        .setBottom(barcode.getBoundingBox().bottom)
                        .setTop(barcode.getBoundingBox().top))
                .setSourceImageRef(sourceImageRef)
                .setCreationTimestamp(creationTimestamp)
                .build();
    }
}
