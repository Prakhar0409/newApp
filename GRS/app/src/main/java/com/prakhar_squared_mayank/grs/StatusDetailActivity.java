package com.prakhar_squared_mayank.grs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Map;

public class StatusDetailActivity extends AppCompatActivity implements View.OnClickListener {
    JSONArray commentData;
    ImageView fab;
    String statusID = "-2", statusTitle, statusDesc;
    TextView title, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        statusID = getIntent().getExtras().getString("STATUS_ID");
        Log.d("SDA", "id received is " + statusID + " size "+getIntent().getExtras().size());
        statusTitle = getIntent().getExtras().getString("STATUS_NAME");
        statusDesc = getIntent().getExtras().getString("STATUS_DESC");

        fab = (ImageView) findViewById(R.id.fab_asd);
        fab.setOnClickListener(this);

        title = (TextView) findViewById(R.id.title_asd);
        title.setText(statusTitle);

        desc = (TextView) findViewById(R.id.desc_asd);
        desc.setText(statusDesc);

        getData();
    }

    public void getData( ){
        String url1="http://"+Utility.IP+Utility.GETSTATUSCOMMENT+"?status_id="+statusID;

        System.out.println("Url being hit comment data is : " + url1);
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String success="";
                try {
                    if(response.has("data") && !response.isNull("data")){
                        commentData = (JSONArray) response.get("data");
                        makeComments();
                        Utility.showMsg(getApplicationContext(), "Data Fetched!");
                    }else{
                        Utility.showMsg(getApplicationContext(), "Fetch data failed");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("data timeline : "+response);

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

    public void makeComments() {
        clearLinearLayout();

        for(int index = 0;index < commentData.length();index++) {
            Log.d("StatusDetailActivity", "Adding comment.");
            String comment="Error with data.", id="", username="";
            try {
                comment = commentData.getJSONObject(index).getString("comment");
                username = commentData.getJSONObject(index).getString("user_name_first")+" "+commentData.getJSONObject(index).getString("user_name_last");
//                id = commentData.getJSONObject(index).getString("comment_id");
            }
            catch (JSONException e) {

            }
            Log.d("SDA", "setting username "+username);
            addChildLayout(comment, id, username);
        }
    }

    public void clearLinearLayout() {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.comment_linear_layout_asd);
        linearLayout.removeAllViewsInLayout();
    }

    public void addChildLayout(final String comment, final String id, final String usernameC){

        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.comment_linear_layout_asd);

        View view = layoutInflater.inflate(R.layout.comment_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView titleTV = (TextView) view.findViewById(R.id.text_ci);
        titleTV.setText(comment);

        TextView usernameTVC = (TextView) view.findViewById(R.id.username_ci);
        Log.d("SDA", "setting usernameC "+usernameC);
        usernameTVC.setText(usernameC);

        Log.d("StatusDetailActivity", "Adding comment event: " + comment);
        linearLayout.addView(view);
        Log.d("StatusDetailActivity", "Count now: " + linearLayout.getChildCount());
    }

    void postNewComment(final String comment) {
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading Status...","Please wait...",false,false);
        String url="http://"+Utility.IP+Utility.POSTSTATUSCOMMENT;
        Log.d("Url hit was:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        getData();
                        //Showing toast message of the response

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("status_id", statusID+"");
                params.put("comment_made", comment);

                //returning parameters
                return params;
            }
        };

        volleySingleton.getInstance(this).getRequestQueue().add(stringRequest);
    }

    void startNewComment() {
        Log.d("Status Detail", "New Comment");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Comment");

        final LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(15, 5, 15, 5);
        final TextView tv = new TextView(this);
        tv.setText("Comment:");
        final EditText input2 = new EditText(this);
        input2.setInputType(InputType.TYPE_CLASS_TEXT);
        input2.setHint("Enter comment here");


        ll.addView(input2);
        builder.setView(ll);

// Set up the buttons
        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!statusID.equals("-2")) {
                    postNewComment(input2.getText().toString());
                }
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fab_asd:
                startNewComment();
                break;
        }
    }
}
