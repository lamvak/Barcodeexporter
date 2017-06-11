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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.widget.ImageView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import pl.lamvak.barcodeexporter.proto.BarcodeExporterProtos.Barcode;
import pl.lamvak.barcodeexporter.store.DataStore;

public class ShowBarcodeActivity extends AppCompatActivity {
    private ImageView imageView;

    @Inject
    DataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_barcode);

        imageView = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        final String barcodeCode = intent.getStringExtra("Code");
        if (barcodeCode == null || barcodeCode.isEmpty()) {
            notifyAboutMissingCode("Can't show barcode -- missing code ID");
            throw new RuntimeException("Missing extra `Code` for ShowBarcodeActivity");
        }

        final Barcode barcode = dataStore.loadBarcodeWithCode(barcodeCode);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap srcBitmap = BitmapFactory.decodeFile(barcode.getSourceImageRef(), options);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(19f);
        paint.setAlpha(100);
        paint.setStyle(Paint.Style.FILL);
        Canvas canvas = new Canvas(srcBitmap);

        Rect rect = new Rect((int)(barcode.getBox().getLeft()), (int)(barcode.getBox().getTop()),
                (int)(barcode.getBox().getRight()), (int)(barcode.getBox().getBottom()));
        canvas.drawRect(rect, paint);

        int sizeMax = Math.max(srcBitmap.getWidth(), srcBitmap.getHeight());
        float scale = 1;
        if (sizeMax > 4096) {
            scale = 4096f / sizeMax;
        }
        final Bitmap copy = Bitmap.createScaledBitmap(srcBitmap, (int)(srcBitmap.getWidth() * scale), (int)(srcBitmap.getHeight() * scale), true);
        ShowBarcodeActivity.this.imageView.setImageBitmap(copy);
    }

    private void notifyAboutMissingCode(String message) {
        Notification notification = new NotificationCompat.Builder(this).setContentTitle("Error in Barcode loading")
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentText(message).build();
        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
    }

}
