package com.prakhar_squared_mayank.grs;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StatusDetailActivity extends AppCompatActivity implements View.OnClickListener {
    JSONArray commentData;
    ImageView fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        fab = (ImageView) findViewById(R.id.fab_asd);
        fab.setOnClickListener(this);
        getData();
    }

    void getData() {
        try {
            commentData = new JSONArray("[{\"comment\":\"Great Job!!\",\"comment_id\":\"31\",\"posted_by\":\"U29\",\"posted_on\":\"23:30 30-10-2016\"},{\"comment\":\"Appreciate it. This is a long bitchy comment. Fuck Yea. This is some m'fuckin test. And we bloody pass it. Oh yeah!!\",\"comment_id\":\"32\",\"posted_by\":\"U291\",\"posted_on\":\"23:30 01-11-2016\"},{\"comment\":\"lol tumse na ho payega :D!!\",\"comment_id\":\"31\",\"posted_by\":\"U29\",\"posted_on\":\"23:30 30-10-2016\"},{\"comment\":\"Great Job f u and what u gotta say man ^^^!!\",\"comment_id\":\"31\",\"posted_by\":\"U29\",\"posted_on\":\"23:30 30-10-2016\"},{\"comment\":\"Great Job yo awesome hurray!!\",\"comment_id\":\"31\",\"posted_by\":\"U29\",\"posted_on\":\"23:30 30-10-2016\"}]");
        }
        catch(JSONException e) {

        }
        makeComments();
    }

    public void makeComments() {

        for(int index = 0;index < commentData.length();index++) {
            Log.d("StatusDetailActivity", "Adding comment.");
            String comment="Error with data.", id="";
            try {
                comment = commentData.getJSONObject(index).getString("comment");
                id = commentData.getJSONObject(index).getString("comment_id");
            }
            catch (JSONException e) {

            }
            addChildLayout(comment, id);
        }
    }

    public void clearLinearLayout() {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.comment_linear_layout_asd);
        linearLayout.removeAllViewsInLayout();
    }

    public void addChildLayout(final String comment, final String id){

        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.comment_linear_layout_asd);

        View view = layoutInflater.inflate(R.layout.comment_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView titleTV = (TextView) view.findViewById(R.id.text_ci);
        titleTV.setText(comment);

        Log.d("StatusDetailActivity", "Adding comment event: " + comment);
        linearLayout.addView(view);
        Log.d("StatusDetailActivity", "Count now: " + linearLayout.getChildCount());

//        linearLayout.add
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
