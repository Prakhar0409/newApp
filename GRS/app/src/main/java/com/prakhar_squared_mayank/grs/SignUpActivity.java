package com.prakhar_squared_mayank.grs;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText usernameET, passwordET, confPasswordET, hostelET;
    EditText fnameET, lnameET, uidET, contactET, emailET;
    Button signUpButton;
    TextView loginTV;
    ImageView profilePicIV;
    FrameLayout profilePicFL;
    Bitmap scaledBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameET = (EditText) findViewById(R.id.username_asu);
        passwordET = (EditText) findViewById(R.id.pass_asu);
        confPasswordET = (EditText) findViewById(R.id.conf_pass_asu);
        hostelET = (EditText) findViewById(R.id.hostel_asu);
        fnameET = (EditText) findViewById(R.id.fname_asu);
        lnameET = (EditText) findViewById(R.id.lname_asu);
        uidET = (EditText) findViewById(R.id.uid_asu);
        contactET = (EditText) findViewById(R.id.contact_asu);
        emailET = (EditText) findViewById(R.id.email_asu);

        signUpButton = (Button) findViewById(R.id.signup_asu);
        signUpButton.setOnClickListener(this);

        loginTV = (TextView) findViewById(R.id.login_asu);
        loginTV.setOnClickListener(this);

        profilePicIV = (ImageView) findViewById(R.id.pic_asu);

        profilePicFL = (FrameLayout) findViewById(R.id.pic_frame_asu);
        profilePicFL.setOnClickListener(this);
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
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(bmp==null){
            Log.d("NULL BMP","NULL");
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        //Showing the progress dialog
        //final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        String url=LoginActivity.ip+"/api/pictures.json";
        Log.d("Url hit was:",url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        // loading.dismiss();
                        //Showing toast message of the response
                        try {
                            JSONObject res= new JSONObject(s);
                            JSONObject data=res.getJSONObject("data");
                            int pic_id=data.getInt("id");
                            System.out.println("pic_id : "+pic_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        showToast(s);
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(scaledBitmap);

                //Getting Image Name
                String name = "profilePic."+usernameET.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("picture", image);
                params.put("picture_title", name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    boolean checkData() {           // TODO: 26/03/16  
        return true;
    }

    void signupUser() {             // TODO: 26/03/16
        uploadImage();
        showComplaintsActivity();
    }

    void showComplaintsActivity() {
        Intent it = new Intent(this, ComplaintsActivity.class);
        startActivity(it);
    }

    //Shows toast with appropriate responses
    public void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
}
