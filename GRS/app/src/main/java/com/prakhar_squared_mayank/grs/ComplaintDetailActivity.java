package com.prakhar_squared_mayank.grs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
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

import java.util.Hashtable;
import java.util.Map;

public class ComplaintDetailActivity extends AppCompatActivity implements View.OnClickListener {

    String imageString=null;
    int c_id;
    Bitmap bitmap = null;
    ImageView c_pic;
    FragmentPagerAdapter adapterViewPager;
    JSONArray timelineData, commentData;
    TimelineFragment timeline;
    WallFragment wallF;
    ImageView bookmarkIV, upIV, downIV;
    boolean bookmarkedA = false, resolvable = false, resolved = false;
    TextView c_upcount, c_downcount;
    int voteStatus = -2, complaintID = 0;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);

        Intent it=getIntent();
        String complaintString=it.getStringExtra("Complaint");

        JSONObject Complaint = null;
        try {

            Complaint = new JSONObject(complaintString);

            resolved = Complaint.getBoolean("resolved");
            TextView c_title = (TextView) findViewById(R.id.title_acd);
            if(!resolved) {
                c_title.setText(Complaint.getString("complaint_title"));
            }
            else {
                c_title.setText("[RESOLVED] "+Complaint.getString("complaint_title"));
            }

            TextView c_description=(TextView) findViewById(R.id.complaint_description_acd);
            c_description.setText(Complaint.getString("complaint_details"));

            c_upcount=(TextView) findViewById(R.id.up_count_acd);
            if(!Complaint.has("upvotes_count")) {
                c_upcount.setText("0");
            }
            else {
                c_upcount.setText(Complaint.getString("upvotes_count"));
            }

            c_downcount=(TextView) findViewById(R.id.down_count_acd);
            if(!Complaint.has("downvotes_count")) {
                c_downcount.setText("0");
            }
            else {
                c_downcount.setText(Complaint.getString("downvotes_count"));
            }

            TextView c_status=(TextView) findViewById(R.id.status_acd);
            c_status.setText(Complaint.getString("status_id"));

            TextView c_resolvedOn=(TextView) findViewById(R.id.resolved_acd);
            c_resolvedOn.setText(Complaint.getString("date_resolved"));

            TextView c_Location = (TextView) findViewById(R.id.location_acd);
            c_Location.setVisibility(View.INVISIBLE);
//            c_Location.setText(Complaint.getString("complaint_location"));

            TextView c_postedBy = (TextView) findViewById(R.id.posted_by_acd);
            c_postedBy.setText(Complaint.getString("posted_by"));

            TextView c_PostedOn = (TextView) findViewById(R.id.posted_on_acd);
            c_PostedOn.setText(Complaint.getString("date_posted"));

            bookmarkedA = Complaint.getBoolean("bookmarked");
            Log.d("ComplaintDetailA", "bookmarked: " + bookmarkedA);

            resolvable = Complaint.getBoolean("to_resolve");
            if(!resolvable) {
                if(menu != null) {
                    MenuItem item = menu.findItem(R.id.action_mark_resolved);
                    item.setVisible(false);
                    this.invalidateOptionsMenu();
                }
            }
            else {
                if(menu != null) {
                    MenuItem item = menu.findItem(R.id.action_mark_resolved);
                    item.setVisible(true);
                    this.invalidateOptionsMenu();
                }
            }

            voteStatus = Complaint.optInt("vote_made", -2);
            if(voteStatus == -2) {
                voteStatus = Complaint.optJSONObject("vote_made").optInt("vote_type");
            }
            complaintID = Complaint.optInt("id", 0);

            c_pic = (ImageView) findViewById(R.id.complaint_pic_acd);


            int c_picId=Complaint.getInt("photo_id");
            getImageString(c_picId);
            c_id=Complaint.getInt("id");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        bookmarkIV = (ImageView) findViewById(R.id.bookmark_acd);
        setBookmark(bookmarkedA);
        bookmarkIV.setOnClickListener(this);

        upIV = (ImageView) findViewById(R.id.up_image_acd);
        upIV.setOnClickListener(this);

        downIV = (ImageView) findViewById(R.id.down_image_acd);
        downIV.setOnClickListener(this);

        setVoteStatus();

        try {
            getTimeLineData();
            getCommentData();
            timelineData = new JSONArray("[{\"status_id\"=\"-2\",\"status_name\"=\"Loading data...\"}]");
            commentData = new JSONArray("[{\"comment\":\"Loading data...\",\"comment_id\":\"-2\",\"posted_by\":\"U29\",\"posted_on\":\"23:30 30-10-2016\"}]");
        }
        catch(JSONException e) {

        }
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }

    public void setVoteStatus() {
        if(voteStatus == 0) {
            upIV.setImageResource(R.drawable.up_grey);
            downIV.setImageResource(R.drawable.down_grey);
        }
        else if(voteStatus == -1) {
            upIV.setImageResource(R.drawable.up_grey);
            downIV.setImageResource(R.drawable.down_red);
        }
        else if(voteStatus == 1) {
            upIV.setImageResource(R.drawable.up_red);
            downIV.setImageResource(R.drawable.down_grey);
        }
    }

    public void setBookmark(boolean bookmarked) {
        Log.d("ComplaintDetailA", "bookmarked set: "+bookmarkedA);
        if(bookmarked) {
            bookmarkIV.setImageResource(R.drawable.yes_bookmark);
        }
        else {
            bookmarkIV.setImageResource(R.drawable.not_bookmarked);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.complaint_detail_menu, menu);
        this.menu = menu;

        return true;
    }

    void changeBookmarkStatus() {
        final ProgressDialog loading = ProgressDialog.show(this,"Casting Vote...","Please wait...",false,false);
        String url="http://"+Utility.IP;   //+Utility.CHANGEVOTE+"?complaint_id="+complaintID+"&vote_type="+vo;
        if(bookmarkedA) {
            url = url + Utility.UNFOLLOW;
        }
        else
        {
            url = url + Utility.FOLLOW;
        }
        url = url + "?complaint_id="+complaintID;
        bookmarkedA = !bookmarkedA;

        System.out.println("Url being hit is : " + url);
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                setBookmark(bookmarkedA);
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

    void changeVoteStatus(final int vo) {
        final ProgressDialog loading = ProgressDialog.show(this,"Casting Vote...","Please wait...",false,false);
        String url="http://"+Utility.IP+Utility.CHANGEVOTE+"?complaint_id="+complaintID+"&vote_type="+vo;

        System.out.println("Url being hit is : " + url);
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                updateVoteStatus(vo);

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

    void updateVoteStatus(int vo) {
        if(voteStatus == 0) {
            if(vo == 1) {
                c_upcount.setText((Integer.parseInt(c_upcount.getText().toString()) + 1) + "");
            }
            else if(vo == -1) {
                c_downcount.setText((Integer.parseInt(c_downcount.getText().toString()) + 1) + "");
            }
        }
        else if(voteStatus == 1) {
            if(vo == 1) {
            }
            else if(vo == -1) {
                c_upcount.setText((Integer.parseInt(c_upcount.getText().toString()) - 1) + "");
                c_downcount.setText((Integer.parseInt(c_downcount.getText().toString()) + 1) + "");
            }
        }
        else if(voteStatus == -1) {
            if(vo == 1) {
                c_downcount.setText((Integer.parseInt(c_downcount.getText().toString()) - 1) + "");
                c_upcount.setText((Integer.parseInt(c_upcount.getText().toString()) + 1) + "");
            }
            else if(vo == -1) {
            }
        }
        voteStatus = vo;
        setVoteStatus();
    }

    void markComplaintResoled() {
        final ProgressDialog loading = ProgressDialog.show(this,"Marking Resolved...","Please wait...",false,false);
        String url="http://"+Utility.IP+Utility.MARKRESOLVED;
        Log.d("Url hit was:",url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast
                        Utility.showMsg(getApplicationContext(), volleyError.getMessage().toString());//, Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("complaint_id", complaintID+"");

                //returning parameters
                return params;
            }
        };

        volleySingleton.getInstance(this).getRequestQueue().add(stringRequest);
    }

    void showMarkResolveDialog() {
        Log.d("Timeline fragment", "New Status");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mark Resolved?");

        final LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(55, 5, 15, 5);
        final TextView tv = new TextView(this);
        tv.setText("Are you sure you want to mark complaint as resolved?");
//        final EditText input2 = new EditText(this);
//        input2.setInputType(InputType.TYPE_CLASS_TEXT);
//        input2.setHint("Enter status here");

        ll.addView(tv);
        builder.setView(ll);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(complaintID != -2) {
                    markComplaintResoled();
                }
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_mark_resolved) {
            showMarkResolveDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getTimeLineData( ){
        String url1="http://"+LoginActivity.ip+"/status/complaint?complaint_id="+complaintID;

        System.out.println("Url being hit is : " + url1);
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String success="";
                try {
                    if(response.has("data") && !response.isNull("data")){
                        timelineData = (JSONArray) response.get("data");
                        setTimeline();
                        showToast("Data Fetched!");
                    }else{
                        showToast("Fetch data failed");
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

    public void getCommentData( ){
        String url1="http://"+LoginActivity.ip+"/comments/complaint?complaint_id="+complaintID;

        System.out.println("Url being hit is : " + url1);
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String success="";
                try {
                    if(response.has("data") && !response.isNull("data")){
                        commentData = (JSONArray) response.get("data");
                        setWall();
                        showToast("Data Fetched!");
                    }else{
                        showToast("Fetch data failed");
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

    public Bitmap getImage(String pic) {
        //if (imageString!=null){return }
        if (bitmap!=null){
            return bitmap;
        }
        byte[] imageAsBytes = Base64.decode(pic.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


    public void getImageString(int id){
        String url=LoginActivity.ip+"/api/pictures/id/"+Integer.toString(id)+".json";
        Log.d("Url hit was:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        // loading.dismiss();
                        //Showing toast message of the response
                        try {
                            JSONObject res= new JSONObject(s);
                            JSONArray data=res.getJSONArray("data");
                            JSONObject imageObject=data.getJSONObject(0);
                            if (imageObject!=null){
                                imageString = imageObject.getString("picture");
                                System.out.println("imageString : "+imageString);
                            }
                            changeImage(imageString);

                            showToast("Success ful image chuityapa");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //loading.dismiss();
                        //Showing toast
                        showToast(volleyError.getMessage().toString());//, Toast.LENGTH_LONG).show();
                    }
                });
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    public void changeImage(String imageString){
        Bitmap image=getImage(imageString);
        c_pic.setImageBitmap(image);
    }


    //Shows toast with appropriate responses
    public void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    void setTimeline() {
        Log.d("CDA", "Setting timeline " + timelineData.toString());
        if(timeline != null && timelineData != null) {
            timeline.updateTimeline(timelineData, complaintID);
        }
    }

    void setWall() {
        Log.d("CDA", "Setting wall "+commentData.toString());
        if(wallF != null && commentData != null) {
            wallF.updateWall(commentData, complaintID);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.up_image_acd:
                changeVoteStatus(1);
                break;
            case R.id.down_image_acd:
                changeVoteStatus(-1);
                break;
            case R.id.bookmark_acd:
                changeBookmarkStatus();
                break;
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    timeline = TimelineFragment.newInstance(timelineData);
                    return timeline;
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    wallF = WallFragment.newInstance(commentData);
                    return wallF;
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) {
                return "Timeline";
            }
            else if(position == 1) {
                return "Wall";
            }
            return "Page " + position;
        }

    }
}
