package pl.lamvak.barcodeexporter.barcodeexporter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CodesList extends AppCompatActivity {
    ArrayList<CharSequence> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codes_list);

        Button addItemButton = (Button) findViewById(R.id.addItemButton);
        final TextView editInput = (TextView) findViewById(R.id.inputItemText);

        ListView listView = (ListView) findViewById(R.id.barCodesListView);
        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = editInput.getText();
                Log.i("INFO", "onClick: " + text);
                items.add(text);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
