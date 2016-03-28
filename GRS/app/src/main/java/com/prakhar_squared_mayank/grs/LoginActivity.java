package com.prakhar_squared_mayank.grs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText useridET, passwordET;
    TextView signupTV;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        useridET = (EditText) findViewById(R.id.userid_al);
        passwordET = (EditText) findViewById(R.id.pass_al);

        loginButton = (Button) findViewById(R.id.login_al);
        loginButton.setOnClickListener(this);

        signupTV = (TextView) findViewById(R.id.signup_al);
        signupTV.setOnClickListener(this);
    }

    boolean checkData() {
        if(Utility.DEBUG) {
            return true;
        }
        if(useridET.getText().toString().equals("")) {
            Utility.showMsg(this, "Enter User ID");
            return false;
        }
        if(passwordET.getText().toString().equals("")) {
            Utility.showMsg(this, "Enter password");
            return false;
        }
        return true;
    }

    void loginUser() {
        showComplaintsActivity();
    }

    void showComplaintsActivity() {
        Intent it = new Intent(this, ComplaintsActivity.class);
        startActivity(it);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_al:
                if(checkData()) {
                    loginUser();
                }
                break;
            case R.id.signup_al:
                Intent it = new Intent(this, SignUpActivity.class);
                startActivity(it);
        }
    }
}
