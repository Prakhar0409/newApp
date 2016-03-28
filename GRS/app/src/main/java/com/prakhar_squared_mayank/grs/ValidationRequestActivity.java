package com.prakhar_squared_mayank.grs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

public class ValidationRequestActivity extends AppCompatActivity {
    ListView listView;
    ValidationListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation_request);

        listView = (ListView) findViewById(R.id.validation_listview_avr);
        listAdapter = new ValidationListAdapter(this, getLayoutInflater());
        listView.setAdapter(listAdapter);

        getData();
    }

    void getData() {
        // TODO: 28/03/16 Hit URL here
        JSONArray arr = null;
        try {
            arr = new JSONArray("[{\"x\":\"Someone Else\",\"y\":\"heehaa\",\"z\":\"Boss Man\"}]");
        }
        catch(JSONException e) {

        }
        updateAdapter(arr);
    }

    void updateAdapter(JSONArray arr) {
        Log.d("mycomplaintsfragment", arr.toString());
        listAdapter.updateData(arr);
    }
}
