package pl.lamvak.barcodeexporter.barcodeexporter;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codes_list);

        Button addItemButton = (Button) findViewById(R.id.addItemButton);
        final TextView editInput = (TextView) findViewById(R.id.inputItemText);

        ListView listView = (ListView) findViewById(R.id.barCodesListView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, items);
        listView.setAdapter(adapter);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = editInput.getText();
                Log.i("INFO", "onClick: " + text);
                items.add(text.toString());
                adapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(CodesList.this, ShowBarcodeActivity.class));
            }
        });
    }
}
