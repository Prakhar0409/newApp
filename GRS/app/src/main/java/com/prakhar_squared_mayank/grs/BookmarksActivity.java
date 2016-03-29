package com.prakhar_squared_mayank.grs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

public class BookmarksActivity extends AppCompatActivity {
    ListView listView;
    TextView message;
    BookmarksAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        listView = (ListView) findViewById(R.id.complaint_listview_ab);
        listAdapter = new BookmarksAdapter(this, getLayoutInflater());
        listView.setAdapter(listAdapter);

        message = (TextView) findViewById(R.id.msg_ab);
        message.setText("Loading Data...");

        listView.setVisibility(View.INVISIBLE);

        getData();
    }

    void getData() {

    }

    void updateAdapter(JSONArray arr) {
        Log.d("bookmarkactivity", arr.toString());
        if(arr.length() == 0) {
            message.setText("No Bookmarked Complaints.");
            listView.setVisibility(View.INVISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
        }
        listAdapter.updateData(arr);
    }
}
