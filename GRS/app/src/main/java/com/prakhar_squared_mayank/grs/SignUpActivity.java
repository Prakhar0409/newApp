package com.prakhar_squared_mayank.grs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText usernameET, passwordET, confPasswordET, hostelET;
    EditText fnameET, lnameET, uidET, contactET, emailET;
    Button signUpButton;
    TextView loginTV;
    ImageView profilePicIV;
    FrameLayout profilePicFL;

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
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 256, nh, true);

                profilePicIV.setImageBitmap(scaled);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    boolean checkData() {           // TODO: 26/03/16  
        return true;
    }
    
    void signupUser() {             // TODO: 26/03/16  
        showComplaintsActivity();
    }
    
    void showComplaintsActivity() {
        Intent it = new Intent(this, ComplaintsActivity.class);
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
}
