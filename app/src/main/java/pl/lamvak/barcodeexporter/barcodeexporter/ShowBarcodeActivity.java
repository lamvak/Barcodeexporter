package pl.lamvak.barcodeexporter.barcodeexporter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.security.MessageDigest;

public class ShowBarcodeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_barcode);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Glide.with(this)
                .asBitmap()
                .load(R.drawable.example_image_2)
                .apply(RequestOptions.bitmapTransform(new BitmapTransformation() {
                    @Override
                    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                        return null;
                    }

                    @Override
                    public void updateDiskCacheKey(MessageDigest messageDigest) {

                    }
                }))
                .into(imageView);
    }
}
