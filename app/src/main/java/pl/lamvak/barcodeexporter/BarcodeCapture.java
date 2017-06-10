package pl.lamvak.barcodeexporter;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import pl.lamvak.barcodeexporter.data.Conversions;
import pl.lamvak.barcodeexporter.proto.BarcodeExporterProtos;
import pl.lamvak.barcodeexporter.store.DataStore;

import static com.google.android.gms.vision.barcode.Barcode.CALENDAR_EVENT;
import static com.google.android.gms.vision.barcode.Barcode.CODABAR;
import static com.google.android.gms.vision.barcode.Barcode.CODE_128;
import static com.google.android.gms.vision.barcode.Barcode.CODE_39;
import static com.google.android.gms.vision.barcode.Barcode.CODE_93;
import static com.google.android.gms.vision.barcode.Barcode.CONTACT_INFO;
import static com.google.android.gms.vision.barcode.Barcode.DATA_MATRIX;
import static com.google.android.gms.vision.barcode.Barcode.DRIVER_LICENSE;
import static com.google.android.gms.vision.barcode.Barcode.EAN_13;
import static com.google.android.gms.vision.barcode.Barcode.EAN_8;
import static com.google.android.gms.vision.barcode.Barcode.EMAIL;
import static com.google.android.gms.vision.barcode.Barcode.GEO;
import static com.google.android.gms.vision.barcode.Barcode.ISBN;
import static com.google.android.gms.vision.barcode.Barcode.ITF;
import static com.google.android.gms.vision.barcode.Barcode.PDF417;
import static com.google.android.gms.vision.barcode.Barcode.PHONE;
import static com.google.android.gms.vision.barcode.Barcode.PRODUCT;
import static com.google.android.gms.vision.barcode.Barcode.QR_CODE;
import static com.google.android.gms.vision.barcode.Barcode.SMS;
import static com.google.android.gms.vision.barcode.Barcode.TEXT;
import static com.google.android.gms.vision.barcode.Barcode.UPC_A;
import static com.google.android.gms.vision.barcode.Barcode.UPC_E;
import static com.google.android.gms.vision.barcode.Barcode.URL;
import static com.google.android.gms.vision.barcode.Barcode.WIFI;

public class BarcodeCapture extends AppCompatActivity {
    private ImageView imageView;
    private Button retryButton;
    private Button saveAndNextButton;
    private Button cancelButton;

    private String mCurrentPhotoPath;
    private SparseArray<Barcode> barcodes;
    private int supportedFormats = CODE_128 |
            CODE_39 |
            CODE_93 |
            CODABAR |
            DATA_MATRIX |
            EAN_13 |
            EAN_8 |
            ITF |
            QR_CODE |
            UPC_A |
            UPC_E |
            PDF417 |
            CONTACT_INFO |
            EMAIL |
            ISBN |
            PHONE |
            PRODUCT |
            SMS |
            TEXT |
            URL |
            WIFI |
            GEO |
            CALENDAR_EVENT |
            DRIVER_LICENSE;
    int REQUEST_IMAGE_CAPTURE = 43;

    @Inject
    DataStore dataStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_capture);

        imageView = (ImageView) findViewById(R.id.pinchImageView);
        Log.i("INFO", "imageView: " + imageView);

        retryButton = (Button) findViewById(R.id.retry);
        Log.i("INFO", "retryButton: " + retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reCapture();
            }
        });

        saveAndNextButton = (Button) findViewById(R.id.saveAndNext);
        saveAndNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveResults();
                mCurrentPhotoPath = null;
                barcodes = null;
                reCapture();
            }
        });

        cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentPhotoPath == null) {
            reCapture();
        }
    }

    private void saveResults() {
        if (barcodes == null) {
            return;
        }
        for (int i = 0; i < barcodes.size(); i++) {
            Barcode barcode = barcodes.get(barcodes.keyAt(i));
            BarcodeExporterProtos.Barcode meta = Conversions.from(barcode, mCurrentPhotoPath,
                    (int) (DateTime.now(DateTimeZone.UTC).getMillis() / 86400000));
            dataStore.insertOrUpdate(meta);
        }
    }

    private void reCapture() {
        Log.i("INFO", "BarcodeCapture: reCapture()");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                Uri photoURI = FileProvider.getUriForFile(BarcodeCapture.this,
                        "pl.lamvak.barcodeexporter", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new RuntimeException("Camera not available");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("INFO", "BarcodeCapture: onActivityResult("
                + requestCode + ", " + resultCode + ")");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Log.i("INFO", "BarcodeCapture: loading image");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap srcBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);

                BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(supportedFormats).build();

                Frame frame = new Frame.Builder()
                        .setBitmap(srcBitmap).build();

                barcodes = barcodeDetector.detect(frame);

                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStrokeWidth(19f);
                paint.setAlpha(100);
                paint.setStyle(Paint.Style.FILL);
                Canvas canvas = new Canvas(srcBitmap);

                if (barcodes.size() > 0) {
                    for (int i = 0; i < barcodes.size(); i++) {
                        Barcode barcode = barcodes.get(barcodes.keyAt(i));
                        Rect rect = barcode.getBoundingBox();
                        canvas.drawRect(rect, paint);
                    }
                }


            int sizeMax = Math.max(srcBitmap.getWidth(), srcBitmap.getHeight());
            float scale = 1;
            if (sizeMax > 4096) {
                scale = 4096f / sizeMax;
            }
            final Bitmap copy = Bitmap.createScaledBitmap(srcBitmap, (int)(srcBitmap.getWidth() * scale), (int)(srcBitmap.getHeight() * scale), true);
            imageView.setImageBitmap(copy);
        }
        else {
            if (requestCode != REQUEST_IMAGE_CAPTURE) {
                notifyAboutMissingCode("Unknown request returned");
            }
            else {
                notifyAboutMissingCode("Capture cancelled with code " + resultCode);
            }
            finish();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void notifyAboutMissingCode(String message) {
        Notification notification = new NotificationCompat.Builder(this).setContentTitle("Error in BarcodeExporterGlideModule")
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentText(message).build();
        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
    }
}
