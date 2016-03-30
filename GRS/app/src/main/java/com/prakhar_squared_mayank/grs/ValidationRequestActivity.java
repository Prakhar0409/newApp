package com.prakhar_squared_mayank.grs;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        final ProgressDialog loading = ProgressDialog.show(this,"Casting Vote...","Please wait...",false,false);
        String url="http://"+Utility.IP+Utility.VALIDATIONREQUESTS;

        System.out.println("Url being hit is : " + url);
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("validation_list");
                    updateAdapter(arr);
                }
                catch(JSONException e) {

                }
                loading.dismiss();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("volley failed");
                loading.dismiss();
            }
        }
        );
        RequestQueue v = Volley.newRequestQueue(this);
        v.add(req1);
    }

    void updateAdapter(JSONArray arr) {
        Log.d("mycomplaintsfragment", arr.toString());
        listAdapter.updateData(arr);
    }
}
