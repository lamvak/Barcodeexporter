package pl.lamvak.barcodeexporter.barcodeexporter;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CodesList extends AppCompatActivity {
    ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codes_list);

        Button addItemButton = (Button) findViewById(R.id.addItemButton);
        final TextView editInput = (TextView) findViewById(R.id.inputItemText);

        ListView listView = (ListView) findViewById(R.id.barCodesListView);
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, items);
        listView.setAdapter(adapter);

        helper = new SQLiteOpenHelper(getApplicationContext(), "barcodes", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE IF NOT EXISTS BARCODES (" +
                        "CODE TEXT PRIMARY KEY ASC, " +
                        "TOP REAL, " +
                        "BOTTOM REAL, " +
                        "LEFT REAL, " +
                        "RIGHT REAL, " +
                        "SOURCE_IMG_REF TEXT, " +
                        "CREATION_TIMESTAMP INT ASC)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            }
        };

        database = helper.getWritableDatabase();

        Cursor barcodes = database.query("BARCODES", new String[]{"CODE"}, null, null, null, null, null, null);
        while (barcodes.moveToNext()) {
            Log.i("INFO", "reading data from db at row " + barcodes.getPosition());
            String code = barcodes.getString(0);
            addItemToModel(code);
        }

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editInput.getText().toString();
                addItemToModel(text);
                ContentValues values = new ContentValues();
                values.put("CODE", text);
                database.insert("BARCODES", null, values);
                Log.i("INFO", "onClick: " + text);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(CodesList.this, ShowBarcodeActivity.class));
            }
        });
    }

    private void addItemToModel(CharSequence text) {
        items.add(text.toString());
        adapter.notifyDataSetChanged();
    }
}
