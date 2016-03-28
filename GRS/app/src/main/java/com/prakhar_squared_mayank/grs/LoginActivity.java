package com.prakhar_squared_mayank.grs;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static String ip="http://192.168.2.21:8000";//"10.0.2.2:8000";

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
        String url=ip+"/user/login.json";

        Map<String, String> params = new HashMap();
        params.put("username", useridET.getText().toString().trim());
        params.put("password", passwordET.getText().toString().trim());

        JSONObject parameters = new JSONObject(params);
        System.out.println("Url being hit is : " + url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String success="";
                try {
                    success= (String) response.get("success");
                    JSONObject user= (JSONObject) response.get("user");
                    if(success.equals("True")){
                        showComplaintsActivity(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(response);
                System.out.println(success);
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
        v.add(req);



    }

    //Shows toast with appropriate responses
    public void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    void showComplaintsActivity(JSONObject user) {
        int user_id=0;
        try {
            user_id= (int) user.get("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent it = new Intent(this, ComplaintsActivity.class);
        it.putExtra("user_id",user_id);
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
