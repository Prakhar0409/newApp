package com.prakhar_squared_mayank.grs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    TextView usernameTV, fnameTV, lnameTV, entryTV, contactTV, emailTV, hostelTV;
    ImageView profilePicIV;
    Bitmap scaledBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTV = (TextView) findViewById(R.id.username_ap);
        fnameTV = (TextView) findViewById(R.id.fname_ap);
        lnameTV = (TextView) findViewById(R.id.lname_ap);
        entryTV = (TextView) findViewById(R.id.entry_ap);
        contactTV = (TextView) findViewById(R.id.contact_ap);
        emailTV = (TextView) findViewById(R.id.email_ap);
        hostelTV = (TextView) findViewById(R.id.hostel_ap);

        profilePicIV = (ImageView) findViewById(R.id.profile_pic_ap);
        profilePicIV.setOnClickListener(this);

        setData();
    }

    void setData() {
        JSONObject user = Utility.USER;

        usernameTV.setText(user.optString("username"));
        fnameTV.setText(user.optString("first_name"));
        lnameTV.setText(user.optString("last_name"));
        entryTV.setText(user.optString("registration_key", "nil"));
        contactTV.setText(user.optString("contact", "nil"));
        emailTV.setText(user.optString("email_id", "nil"));
        hostelTV.setText(user.optString("hostel_name", "nil"));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_pic_ap:
                selectUserPic();
                break;
        }
    }
}
