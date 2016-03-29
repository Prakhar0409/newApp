package com.prakhar_squared_mayank.grs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookmarksActivity extends AppCompatActivity {
    ListView listView;
    TextView message;
    BookmarksAdapter listAdapter;
    JSONArray data=null;

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
        String url1="http://"+LoginActivity.ip+Utility.BOOKMARKCOMPLAINTS;

        System.out.println("Url being hit is for bookmark : " + url1);
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String success="";
                try {

                    if(response.has("data") && !response.isNull("data")){
                        data = (JSONArray) response.get("data");
                        Utility.showMsg(getApplicationContext(), "Data Fetched!");
                        for (int i=0;i<data.length();i++){
                            System.out.println("data "+Integer.toString(i)+" : "+data.get(i));
                        }
                        updateAdapter(data);
                    }else{
                        Utility.showMsg(getApplicationContext(), "Fetch data failed");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("data1111 : "+response);
                System.out.println(success);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("volley failed");
            }
        }
        );
        RequestQueue v = Volley.newRequestQueue(this);
        v.add(req1);
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
