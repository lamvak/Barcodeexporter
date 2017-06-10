package pl.lamvak.barcodeexporter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import pl.lamvak.barcodeexporter.store.DataStore;

public class CodesList extends AppCompatActivity {
    ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Inject
    DataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codes_list);

        Button addItemButton = (Button) findViewById(R.id.addItemButton);
        final TextView editInput = (TextView) findViewById(R.id.inputItemText);

        ListView listView = (ListView) findViewById(R.id.barCodesListView);
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CodesList.this, ShowBarcodeActivity.class);
                intent.putExtra("Code", items.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshItems();
    }

    private void refreshItems() {
        items.clear();
        items.addAll(dataStore.loadAllCodes());
        adapter.notifyDataSetChanged();
    }
}
