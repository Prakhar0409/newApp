package com.prakhar_squared_mayank.grs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationsActivity extends AppCompatActivity {
    ListView listView;
    JSONArray data=null;
    private NotificationsAdapter complaintsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        listView = (ListView) findViewById(R.id.notif_listview_an);
        complaintsAdapter = new NotificationsAdapter(this, getLayoutInflater());
        listView.setAdapter(complaintsAdapter);

    }

    void getData() {
        String url1="http://"+Utility.IP+"/notifications/all";

        System.out.println("Url being hit is : " + url1);
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String success="";
                try {

                    if(response.has("notifications") && !response.isNull("notifications")){
                        data = (JSONArray) response.get("notifications");
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
        Log.d("mycomplaintsfragment", arr.toString());
        complaintsAdapter.updateData(arr);
    }
}
