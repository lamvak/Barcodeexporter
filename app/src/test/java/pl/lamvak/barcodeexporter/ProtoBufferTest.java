package pl.lamvak.barcodeexporter;

import junit.framework.Assert;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.annotation.Resource;

import pl.lamvak.barcodeexporter.proto.BarcodeExporterProtos;
import pl.lamvak.barcodeexporter.proto.BarcodeExporterProtos.Barcode;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class ProtoBufferTest {
    @Resource(mappedName = "Barcodes_20170611_135440_-885693734.protobar")
    URL protoExport = getClass().getClassLoader().getResource("Barcodes_20170611_135440_-885693734.protobar");

    @Test
    public void createSimpleProto() {
        Barcode.newBuilder()
                .setCode("qwerty")
                .setCreationTimestamp(42)
                .setSourceImageRef("file:///one.jpg")
                .setBox(Barcode.Rectangle.newBuilder()
                        .setLeft(0)
                        .setTop(0)
                        .setRight(100)
                        .setBottom(100))
                .build();
    }

    @Test
    public void readFromEmptyStream() throws IOException {
        Barcode barcode = Barcode.parseDelimitedFrom(
                new ByteArrayInputStream(new byte[0]));

        System.out.println("barcode = " + barcode);
    }

    @Test
    public void readSimpleProtoExport() throws IOException {
        assertNotNull(protoExport);
        InputStream inputStream = protoExport.openStream();

        Barcode barcode = Barcode.parseDelimitedFrom(inputStream);
        assertNotNull(barcode);
        System.out.println("barcode = " + barcode);
        assertEquals("7680442590289", barcode.getCode());

        assertNull(Barcode.parseDelimitedFrom(inputStream));
        inputStream.close();
    }
}
