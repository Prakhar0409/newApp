package com.prakhar_squared_mayank.grs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

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

public class ComplaintDetailActivity extends AppCompatActivity {

    String imageString=null;
    int c_id;
    Bitmap bitmap = null;
    ImageView c_pic;
    FragmentPagerAdapter adapterViewPager;
    JSONArray timelineData, commentData;
    TimelineFragment timeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);

        Intent it=getIntent();
        String complaintString=it.getStringExtra("Complaint");

        JSONObject Complaint = null;
        try {
            Complaint = new JSONObject(complaintString);
            TextView c_title = (TextView) findViewById(R.id.title_acd);  c_title.setText(Complaint.getString("complaint_title"));
            TextView c_description=(TextView) findViewById(R.id.complaint_description_acd);  c_description.setText(Complaint.getString("complaint_details"));
            TextView c_upcount=(TextView) findViewById(R.id.up_count_acd);  c_upcount.setText(Complaint.getString("upvotes_count"));
            TextView c_downcount=(TextView) findViewById(R.id.down_count_acd);  c_downcount.setText(Complaint.getString("downvotes_count"));
            TextView c_status=(TextView) findViewById(R.id.status_acd);  c_status.setText(Complaint.getString("status_id"));
            TextView c_resolvedOn=(TextView) findViewById(R.id.resolved_acd);

                c_resolvedOn.setText(Complaint.getString("date_resolved"));

            TextView c_Location = (TextView) findViewById(R.id.location_acd); c_Location.setText(Complaint.getString("complaint_location"));
            TextView c_postedBy = (TextView) findViewById(R.id.posted_by_acd); c_postedBy.setText(Complaint.getString("posted_by"));
            TextView c_PostedOn = (TextView) findViewById(R.id.posted_on_acd);  c_PostedOn.setText(Complaint.getString("date_posted"));
            c_pic = (ImageView) findViewById(R.id.complaint_pic_acd);


            int c_picId=Complaint.getInt("photo_id");
            getImageString(c_picId);
            c_id=Complaint.getInt("id");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            getTimeLineData();
            timelineData = new JSONArray("[{\"status_id\"=\"2\",\"status_name\"=\"Received supplies\"},{\"status_id\"=\"34\",\"status_name\"=\"Work almost done\"},{\"status_id\"=\"34\",\"status_name\"=\"Work almost done2\"}]");
            commentData = new JSONArray("[{\"comment\":\"Great Job!!\",\"comment_id\":\"31\",\"posted_by\":\"U29\",\"posted_on\":\"23:30 30-10-2016\"},{\"comment\":\"Appreciate it. This is a long bitchy comment. Fuck Yea. This is some m'fuckin test. And we bloody pass it. Oh yeah!!\",\"comment_id\":\"32\",\"posted_by\":\"U291\",\"posted_on\":\"23:30 01-11-2016\"}]");
        }
        catch(JSONException e) {

        }
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

//        getData();

    }


    public void getTimeLineData( ){
//        String url1=LoginActivity.ip+"/complaints/concerning/"+((ComplaintsActivity)getActivity()).user_id+".json";
//
//        System.out.println("Url being hit is : " + url1);
//        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                String success="";
//                try {
//                    success= (String) response.get("success");
//
//                    success= (String) response.get("success");
//                    if(success.equals("True")){
//                        data = (JSONArray) response.get("data");
//                        showToast("Data Fetched!");
//                        for (int i=0;i<data.length();i++){
//                            System.out.println("data "+Integer.toString(i)+" : "+data.get(i));
//                        }
//                        updateAdapter(data);
//                    }else{
//                        showToast("Fetch data failed");
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("data1111 : "+response);
//                System.out.println(success);
//
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                System.out.println("volley failed");
//            }
//        }
//        );
//        RequestQueue v = Volley.newRequestQueue(getActivity());
//        v.add(req1);
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





    void getData() {
        try {
            timelineData = new JSONArray("[{\"status_id\"=\"2\",\"status_name\"=\"Received supplies\"},{\"status_id\"=\"34\",\"status_name\"=\"Work almost done\"},{\"status_id\"=\"34\",\"status_name\"=\"Work almost done2\"}]");
        }
        catch(JSONException e) {

        }
        setTimeline();
    }

    void setTimeline() {
        if(timeline != null) {
            timeline.updateTimeline(timelineData);
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
                    return WallFragment.newInstance(commentData);
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
