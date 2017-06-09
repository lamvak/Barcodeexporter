package pl.lamvak.barcodeexporter.barcodeexporter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

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

public class ShowBarcodeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_barcode);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        RequestBuilder<Bitmap> bitmapRequestBuilder = Glide.with(this)
                .asBitmap();
        Intent intent = getIntent();
        if (intent.hasExtra("FileName")) {
            bitmapRequestBuilder.load(intent.getStringExtra("FileName"));
        } else {
            bitmapRequestBuilder.load(R.drawable.example_image_2);
        }
        bitmapRequestBuilder
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                                .setBarcodeFormats(
                                    CODE_128 |
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
                                    DRIVER_LICENSE
                        ).build();
                        Log.i("INFO", "BarcodeDetector is operational: " + barcodeDetector.isOperational());

                        Frame frame = new Frame.Builder()
                                .setBitmap(resource).build();
                        SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);
                        Log.i("INFO", "number of barcodes: " + barcodes.size());
                        if (barcodes.size() > 0) {
                            Barcode barcode = barcodes.get(barcodes.keyAt(0));
                            Log.i("INFO", "Marking barcode " + barcode.rawValue);

                            Canvas canvas = new Canvas(resource);
                            Rect rect = barcode.getBoundingBox();
                            Paint paint = new Paint();
                            paint.setColor(Color.RED);
                            paint.setStrokeWidth(19f);
                            paint.setStyle(Paint.Style.STROKE);
                            canvas.drawRect(rect, paint);
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

}
