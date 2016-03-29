package com.prakhar_squared_mayank.grs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;

public class BookmarksActivity extends AppCompatActivity {
    ListView listView;
    BookmarksAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        listView = (ListView) findViewById(R.id.complaint_listview_ab);
        listAdapter = new BookmarksAdapter(this, getLayoutInflater());
        listView.setAdapter(listAdapter);
        listAdapter.setListView(listView);

        getData();
    }

    void getData() {

    }

    void updateAdapter(JSONArray arr) {
        Log.d("bookmarkactivity", arr.toString());
        listAdapter.updateData(arr);
    }
}
