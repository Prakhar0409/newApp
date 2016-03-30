package com.prakhar_squared_mayank.grs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    EditText usernameET, passwordET, confPasswordET, hostelET,yearOfJoiningET;
    JSONArray hostelsJson=null;
    EditText fnameET, lnameET, uidET, contactET, emailET;
    List<String> hostelList=null;
    boolean FlagPicSelected=false;
    Button signUpButton;
    TextView loginTV;
    ImageView profilePicIV;
    FrameLayout profilePicFL;
    Bitmap scaledBitmap;
    Spinner hostels;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        session = new SessionManager(getApplicationContext());

        usernameET = (EditText) findViewById(R.id.username_asu);
        passwordET = (EditText) findViewById(R.id.pass_asu);
        confPasswordET = (EditText) findViewById(R.id.conf_pass_asu);
        //hostelET = (EditText) findViewById(R.id.hostel_asu);
        fnameET = (EditText) findViewById(R.id.fname_asu);
        lnameET = (EditText) findViewById(R.id.lname_asu);

        contactET = (EditText) findViewById(R.id.contact_asu);
        emailET = (EditText) findViewById(R.id.email_asu);
        yearOfJoiningET=(EditText) findViewById(R.id.year_of_joining_asu);
        signUpButton = (Button) findViewById(R.id.signup_asu);
        signUpButton.setOnClickListener(this);

        loginTV = (TextView) findViewById(R.id.login_asu);
        loginTV.setOnClickListener(this);

        profilePicIV = (ImageView) findViewById(R.id.pic_asu);
        profilePicFL = (FrameLayout) findViewById(R.id.pic_frame_asu);
        profilePicFL.setOnClickListener(this);

        hostels = (Spinner) findViewById(R.id.hostel_asu);

        // Spinner click listener
        hostels.setOnItemSelectedListener(this);

        getData();
        Utility.setupUI(SignUpActivity.this, findViewById(R.id.signupView));

    }

    void setHostelSpinner(JSONArray arr) {
        // Spinner Drop down elements
        hostelList = new ArrayList<String>();
        hostelList.add("None");

        for(int i=0;i<arr.length();i++) {
            String name = "";
            try {
                JSONObject hostel = arr.getJSONObject(i);
                String hostelName = hostel.getString("hostel_name");
                hostelList.add(hostelName);
            }
            catch(JSONException e) {

            }
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hostelList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        hostels.setAdapter(dataAdapter);
    }

    public void getData(){
        String url="http://"+Utility.IP+Utility.HOSTELS;
        final ProgressDialog loading = ProgressDialog.show(this, "Fetching data...", "Please wait...", false, false);
        Log.d("Url hit was:", url);
        JsonObjectRequest req= new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        try {
                            hostelsJson=response.getJSONArray("data");
                            System.out.println(hostelsJson);
                            setHostelSpinner(hostelsJson);

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

    void selectUserPic() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
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
                profilePicIV.setImageBitmap(scaledBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FlagPicSelected =true;
    }

    public void uploadData(final int pic_id){
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading data...","Please wait...",false,false);
        String url="http://"+Utility.IP+Utility.UPLOADUSERDATA;
        Map<String, String> params = new HashMap();
        String hostel="None";
        if(hostels.getSelectedItem()!=null) {
            hostel = hostels.getSelectedItem().toString();
        }
        int hostel_id=0;
        if(hostelsJson!=null) {
            for (int i = 0; i < hostelsJson.length(); i++) {
                String name = "";
                try {
                    JSONObject hostelJ = hostelsJson.getJSONObject(i);
                    String hostelName = hostelJ.getString("hostel_name");
                    if (hostelName.equals(hostel)) {
                        hostel_id = hostelJ.getInt("id");
                    }
                } catch (JSONException e) {
                    Log.d("eror", "aagayi");
                    e.printStackTrace();
                }
            }
        }
        params.put("first_name", fnameET.getText().toString().trim());
        params.put("last_name", lnameET.getText().toString().trim());
        params.put("username", usernameET.getText().toString().trim());
        params.put("password", passwordET.getText().toString().trim());

        //params.put("registration_key", "1");
        //params.put("registration_id", "1");
        //params.put("reset_password_key", "not_allowed_currently");
        params.put("email_id", emailET.getText().toString().trim());
        params.put("hostel_id", Integer.toString(hostel_id));
        //params.put("degree_name", degree);
        params.put("picture_id", Integer.toString(pic_id));
        System.out.println("Picture ID BITCH!::"+Integer.toString(pic_id));

        params.put("year_of_degree", yearOfJoiningET.getText().toString().trim());
        JSONObject parameters = new JSONObject(params);
        Log.d("Url hit was:", url);
        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST, url,parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        int user_id=0;
                        try {
                            JSONObject user = response.getJSONObject("user");
                            if(user !=null){
                                if(!user.isNull("id")){
                                    user_id=user.getInt("id");
                                    Utility.showMsg(getApplicationContext(), "Signed up with UserID : " + Integer.toString(user_id));
                                    System.out.println("Signed up with UserID : " + Integer.toString(user_id));
                                    session.logoutUser();

                                    //showComplaintsActivity(user_id, pic_id,user);
                                }else{
                                    Utility.showMsg(getApplicationContext(), ": Unable to register : ");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(response);
                        Utility.showMsg(getApplicationContext(), response.toString());
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

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading Image...","Please wait...",false,false);
        String url="http://"+Utility.IP+Utility.UPLOADIMAGE;
        Log.d("Url hit was:",url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                         loading.dismiss();
                        //Showing toast message of the response
                        int pic_id=0;
                        try {
                            JSONObject res= new JSONObject(s);
                            //JSONObject data=res.getJSONObject("image");
                            if(!res.isNull("image")){
                                pic_id=res.getInt("image");
                            }

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
                //Converting Bitmap to String
                String image = Utility.getStringImage(scaledBitmap);

                //Getting Image Name
                String name = "profilePic."+usernameET.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("picture_name", image);
                params.put("picture_caption", name);

                //returning parameters
                return params;
            }
        };

        volleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(stringRequest);
    }

    boolean checkData() {           // TODO: 26/03/16
        if(usernameET.getText().toString().equals("")) {
            Utility.showMsg(this, "Enter User ID");
            System.out.println("ENRYER UESER ID");
            return false;
        }
        if(passwordET.getText().toString().equals("")) {
            Utility.showMsg(this, "Enter password");
            System.out.println("ENRYER PASS");
            return false;
        }
        if(fnameET.getText().toString().equals("")) {
            Utility.showMsg(this, "Enter First Name");
            System.out.println("FIRST NAME MAMAU");
            return false;
        }

        if( !passwordET.getText().toString().trim().equals(confPasswordET.getText().toString().trim())) {
            Utility.showMsg(this, "Password and Confirm password do not match");
            System.out.println("done");
            return false;
        }
        if(!isEmailValid(emailET.getText().toString())) {
            Utility.showMsg(this, "Enter valid email");
            return false;
        }
        return true;
    }

    void signupUser() {             // TODO: 26/03/16
        if (FlagPicSelected){
            uploadImage();
        }
        else{
            uploadData(1);
        }


    }

    void showComplaintsActivity(int user_id,int pic_id,JSONObject user) {


        Intent it = new Intent(this, ComplaintsActivity.class);
        it.putExtra("user_id",user_id);
        it.putExtra("pic_id",pic_id);
        startActivity(it);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.login_asu:
                finish();
                break;
            case R.id.signup_asu:
                if(checkData()) {
                    signupUser();
                }
                break;
            case R.id.pic_frame_asu:
                selectUserPic();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;

    }
}
