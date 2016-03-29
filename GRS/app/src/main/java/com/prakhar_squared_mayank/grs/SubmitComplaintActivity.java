package com.prakhar_squared_mayank.grs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SubmitComplaintActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
    AutoCompleteTextView resolveACT;
    int lev;
    Spinner complaintLevels,complaintDomains;
    List<ComplaintDomain> Domains;
    TextView titleTV, descTV;
    ImageView picIV;
    FrameLayout picFL;
    Bitmap scaledBitmap=null;
    ListView resolveLV;
    String levelSelected="", domainSelected="";
    boolean FlagPicSelected=false;
    private ArrayAdapter<String> adapter;

    //These values show in autocomplete
    String item[]={
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_complaint);


//        domainACT = (AutoCompleteTextView) findViewById(R.id.domain_asc);
        resolveACT = (AutoCompleteTextView) findViewById(R.id.resolving_right_asc);
        titleTV = (TextView) findViewById(R.id.title_asc);
        descTV = (TextView) findViewById(R.id.desc_asc);

        resolveLV = (ListView) findViewById(R.id.resolving_right_listview_asc);

        picIV = (ImageView) findViewById(R.id.pic_asc);
        picFL = (FrameLayout) findViewById(R.id.pic_frame_asc);
        picFL.setOnClickListener(this);


        complaintLevels= (Spinner) findViewById(R.id.level_asc);
        complaintLevels.setOnItemSelectedListener(this);
        complaintDomains= (Spinner) findViewById(R.id.domain_asc);

        adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, item);

        resolveACT.setThreshold(5);

        //Set adapter to AutoCompleteTextView
        resolveACT.setAdapter(adapter);
        //resolveACT.setOnItemSelectedListener(this);


        //resolveACT.setOnItemClickListener(this);



        getData();
        Utility.setupUI(SubmitComplaintActivity.this, findViewById(R.id.newComplaintView));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submit_complaint_menu, menu);
        return true;
    }


    boolean checkData() {
        if(domainSelected.equals("")) {
            Utility.showMsg(this, "Select complaint domain.");
            return false;
        } else if(levelSelected.equals("")) {
            Utility.showMsg(this, "Select complaint level.");
            return false;
        }
        else if(titleTV.getText().toString().equals("")) {
            Utility.showMsg(this, "Set complaint title.");
            return false;
        }
        else if(descTV.getText().toString().equals("")) {
            Utility.showMsg(this, "Set complaint description.");
            return false;
        }
        return true;
    }

    void submitComplaint() {
        if (FlagPicSelected){
            uploadImage();
        }else{
            uploadData(2);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.action_submit_scm:
                if(true){//checkData()) {
                    submitComplaint();
                }
                break;
            case R.id.pic_frame_asc:
                selectUserPic();
                break;
        }
    }


    void selectUserPic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Log.d("SignUp", String.valueOf(bitmap));
                int nh = (int) ( bitmap.getHeight() * (256.0 / bitmap.getWidth()) );
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, 256, nh, true);
                picIV.setImageBitmap(scaledBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FlagPicSelected =true;
    }



    public void uploadData(final int pic_id){
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading data...","Please wait...",false,false);
        String url="http://"+Utility.IP+Utility.COMPLAINTDETAILS;
        //finding the domain_id;
        int domain_id = 0;
        String s=complaintDomains.getSelectedItem().toString();
        for(int i=0;i<Domains.size();i++){
            if(s.equals(Domains.get(i).complaintDomainName)){
                domain_id=Domains.get(i).id;
                break;
            }
        }

        Map<String, String> params = new HashMap();
        params.put("title", titleTV.getText().toString().trim());
        params.put("description", descTV.getText().toString().trim());
        try {
            params.put("posted_by", Integer.toString(Utility.USER.getInt("id")));
        } catch (JSONException e) {
            Log.d("Error","User not retrieveable from Utility");
            e.printStackTrace();
        }
        params.put("image_id", Integer.toString(pic_id));
        params.put("level_id", Integer.toString(lev));
        params.put("domain_id",Integer.toString(domain_id));

        JSONObject parameters = new JSONObject(params);
        Log.d("Url hit was:", url);
        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST, url,parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        Utility.showMsg(getApplicationContext(), "Complaint Posting Successful");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast
                        Utility.showMsg(getApplicationContext(), "VOLLEY ERROR");//, Toast.LENGTH_LONG).show();
                    }
                });
        volleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(req);

    }

    public void MiscStub(){
        getUsersNGroups();
        ListView modeList = new ListView(this);
        String[] stringArray = new String[] { "Bright Mode", "Normal Mode" };
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
        modeList.setAdapter(modeAdapter);
    }

    public void getUsersNGroups(){
        String url="http://"+Utility.IP+Utility.USERSNGROUPS;
        final ProgressDialog loading = ProgressDialog.show(this, "Fetching Users and Groups", "Please wait...", false, false);
        Log.d("Url hit was:", url);
        JsonObjectRequest req= new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loading.dismiss();
                        JSONArray domainsJson;
                        // TODO INCOMPLETE RESPONSE PARSING
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Utility.showMsg(getApplicationContext(), "volley error");//, Toast.LENGTH_LONG).show();
                    }
                });
        volleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(req);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.resolving_right_asc:

                break;
            case R.id.level_asc:
                String item = parent.getItemAtPosition(position).toString();
                    for(int i=0;i<Utility.complaintLevels.size();i++){
                        ComplaintLevel c=Utility.complaintLevels.get(i);
                        if(c.getLevel_name().equals(item)){
                            lev=c.getId();
                            if(item.equals("Miscellaneous")){
                                MiscStub();
                            }
                            break;
                        }
                    }

                Domains = new ArrayList<ComplaintDomain>();
                getDomains();
                Utility.showMsg(parent.getContext(), "Selected: " + item);
                break;

        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading Image...","Please wait...",false,false);
        String url="http://"+Utility.IP+Utility.UPLOADIMAGE;
        Log.d("Url hit was:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        int pic_id=2;
                        try {
                            JSONObject res= new JSONObject(s);
                            JSONObject data=res.getJSONObject("data");
                            pic_id=data.getInt("id");
                            System.out.println("pic_id : " + pic_id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Utility.showMsg(getApplicationContext(), s);
                        uploadData(pic_id);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast
                        System.out.println("Image upload failed");
                        Utility.showMsg(getApplicationContext(), volleyError.getMessage().toString());//, Toast.LENGTH_LONG).show();
                        uploadData(1);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String image = Utility.getStringImage(scaledBitmap);
                String name = "complaintPic."+titleTV.getText().toString().trim();
                Map<String,String> params = new Hashtable<String, String>();
                params.put("picture_name", image);
                params.put("picture_caption", name);
                return params;
            }
        };
        volleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(stringRequest);
    }


    void showComplaintsActivity(int user_id,int pic_id) {
        Intent it = new Intent(this, ComplaintsActivity.class);
        it.putExtra("user_id",user_id);
        it.putExtra("pic_id", pic_id);
        startActivity(it);
    }

    public void setComplaintLevelSpinner() {
        List<String> complaintsLevelNames= new ArrayList<String>();

        for(int i=0; i < Utility.complaintLevels.size() ;i++) {
            complaintsLevelNames.add(Utility.complaintLevels.get(i).getLevel_name());
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, complaintsLevelNames);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        complaintLevels.setAdapter(dataAdapter);
    }

    public void setDomainSpinner() {
        List<String> complaintsDomainNames= new ArrayList<String>();

        for(int i=0; i < Domains.size() ;i++) {
            complaintsDomainNames.add(Domains.get(i).complaintDomainName);
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, complaintsDomainNames);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        complaintDomains.setAdapter(dataAdapter);
    }


    public void getDomains(){
        String url="http://"+Utility.IP+Utility.COMPLAINTDOMAINS+"?level_id="+Integer.toString(lev);
        final ProgressDialog loading = ProgressDialog.show(this, "Fetching data...", "Please wait...", false, false);
        Log.d("Url hit was:", url);
        JsonObjectRequest req= new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        JSONArray domainsJson;
                        try {
                            domainsJson=response.getJSONArray("data");
                            System.out.println(domainsJson);
                            for(int i=0;i<domainsJson.length();i++){
                                ComplaintDomain c=new ComplaintDomain();
                                c.id=domainsJson.getJSONObject(i).getInt("id");
                                c.complaintDomainName=domainsJson.getJSONObject(i).getString("complaint_domain_name");
                                c.level_id=lev;
                                if(Domains==null){Domains=new ArrayList<ComplaintDomain>();}
                                Domains.add(c);
                            }
                            setDomainSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Utility.showMsg(getApplicationContext(), "volley error");//, Toast.LENGTH_LONG).show();
                    }
                });

        volleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(req);
    }


    public void getData(){
        String url="http://"+Utility.IP+Utility.COMPLAINTLEVELS;
        final ProgressDialog loading = ProgressDialog.show(this, "Fetching data...", "Please wait...", false, false);
        Log.d("Url hit was:", url);
        JsonObjectRequest req= new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        JSONArray levelsJson;
                        try {
                            levelsJson=response.getJSONArray("data");
                            System.out.println(levelsJson);
                            for(int i=0;i<levelsJson.length();i++){
                                ComplaintLevel c=new ComplaintLevel();
                                c.setId(levelsJson.getJSONObject(i).getInt("id"));
                                c.setLevel_name(levelsJson.getJSONObject(i).getString("complaint_level_name"));
                                if(Utility.complaintLevels==null){Utility.complaintLevels=new ArrayList<ComplaintLevel>();}
                                Utility.complaintLevels.add(c);
                            }
                            setComplaintLevelSpinner();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast
                        Utility.showMsg(getApplicationContext(), "volley error");//, Toast.LENGTH_LONG).show();
                    }
                });

        volleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(req);
    }
}
