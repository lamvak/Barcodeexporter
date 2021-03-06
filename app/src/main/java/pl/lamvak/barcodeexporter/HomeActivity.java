package pl.lamvak.barcodeexporter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import pl.lamvak.barcodeexporter.proto.BarcodeExporterProtos.Barcode;
import pl.lamvak.barcodeexporter.store.DataStore;

public class HomeActivity extends AppCompatActivity {
    @Inject
    DataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button reviewCodesButton = (Button) findViewById(R.id.reviewCodes);
        reviewCodesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CodesList.class));
            }
        });

        Button takeSnapshots = (Button) findViewById(R.id.takeSnapshots);
        takeSnapshots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BarcodeCapture.class));
            }
        });

        Button exportCodesButton = (Button) findViewById(R.id.exportCodes);
        exportCodesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String exportFilename = "Barcodes_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                try {
                    File exportedData = File.createTempFile(
                        exportFilename,  /* prefix */
                        ".protobar",         /* suffix */
                        storageDir);   /* directory */
                    OutputStream stream = new FileOutputStream(exportedData);
                    dataStore.exportAllBarcodesToStream(stream);
                    stream.close();

                    File zipFile = File.createTempFile(
                            "Export_" + timeStamp + "_",
                            ".zip",
                            storageDir);
                    FileOutputStream zipOutputStream = new FileOutputStream(zipFile);
                    ZipOutputStream zipStream = new ZipOutputStream(zipOutputStream);

                    putFileIntoZipStream(exportedData, zipStream);

                    File imgsDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File[] images = imgsDir.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.getName().endsWith(".jpg");
                        }
                    });

                    for(File image : images) {
                        putFileIntoZipStream(image, zipStream);
                    }

                    zipStream.close();
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button cleanupButton = (Button) findViewById(R.id.cleanupData);
        cleanupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int today = (int) (DateTime.now(DateTimeZone.UTC).getMillis() / 86400000);
                Log.i("INFO", "Cleaning up data; today: " + today);
                int cleanupThreshold = today - 30;
                Log.i("INFO", "cleanupThreshold " + cleanupThreshold);
                List<Barcode> barcodes = dataStore.loadAllBarcodes();
                for (Barcode barcode : barcodes) {
                    long creationTimestamp = barcode.getCreationTimestamp();
                    if (creationTimestamp <= cleanupThreshold) {
                        Log.i("INFO", "removing barcode with timestamp " + creationTimestamp + " (cleanupThreshold = " + cleanupThreshold + ")");
                        dataStore.removeBarcode(barcode.getCode());
                    }
                    else {
                        Log.i("INFO", "retaining barcode with timestamp " + creationTimestamp + " (cleanupThreshold = " + cleanupThreshold + ")");
                    }
                }

                HashSet<String> imageReferences = dataStore.loadAllSourceImagesReferences();
                File imgsDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File[] images = imgsDir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".jpg");
                    }
                });
                for (File image : images) {
                    if (!imageReferences.contains(image.getAbsolutePath())) {
                        image.delete();
                    }
                }
            }
        });
    }

    private void putFileIntoZipStream(File inputFile, ZipOutputStream zipStream) throws IOException {
        ZipEntry entry = new ZipEntry(inputFile.getName());
        zipStream.putNextEntry(entry);

        BufferedInputStream bufferedStream = new BufferedInputStream(new FileInputStream(inputFile));

        copyStream(new FileInputStream(inputFile), zipStream);
        bufferedStream.close();
    }

    private void copyStream(FileInputStream inputStream, ZipOutputStream zipStream) throws IOException {
        byte[] data = new byte[4096];
        int count = 0;
        while ((count = inputStream.read(data)) != -1) {
            zipStream.write(data, 0, count);
        }
    }
}
