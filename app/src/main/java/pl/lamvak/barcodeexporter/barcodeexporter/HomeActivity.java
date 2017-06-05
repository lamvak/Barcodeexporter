package pl.lamvak.barcodeexporter.barcodeexporter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                startActivity(new Intent(HomeActivity.this, CodesList.class));
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
}
