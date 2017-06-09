package pl.lamvak.barcodeexporter;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import pl.lamvak.barcodeexporter.store.DataStore;

public class HomeActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private String mCurrentPhotoPath;

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
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        File photoFile = createImageFile();
                        Uri photoURI = FileProvider.getUriForFile(HomeActivity.this,
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
        });

        Button exportCodesButton = (Button) findViewById(R.id.exportCodes);
        exportCodesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CodesList.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Notification notification = new NotificationCompat.Builder(this).setContentTitle("Scanned File")
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setContentText(dataStore.foo() + ">" + mCurrentPhotoPath).build();
            ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
        }
        Intent intent = new Intent(this, ShowBarcodeActivity.class);
        intent.putExtra("FileName", mCurrentPhotoPath);
        startActivity(intent);
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
}
