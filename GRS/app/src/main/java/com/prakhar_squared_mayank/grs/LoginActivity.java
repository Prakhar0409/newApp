package com.prakhar_squared_mayank.grs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static String ip="192.168.43.147:8000";//"10.0.2.2:8000";


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

        CookieManager manager = new CookieManager( null, CookiePolicy.ACCEPT_ALL );
        CookieHandler.setDefault(manager);

        Utility.setupUI(LoginActivity.this,findViewById(R.id.loginView));
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
        String url="http://"+Utility.IP+Utility.LOGIN_URL;

        Map<String, String> params = new HashMap();
        params.put("username", useridET.getText().toString().trim());
        params.put("password", passwordET.getText().toString().trim());
        JSONObject parameters = new JSONObject(params);
        System.out.println("Url being hit is : " + url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean success=false;
                try {
                    success= (Boolean) response.getBoolean("success");
                    JSONObject user=null;
                    Log.d("success", "donno");
                    if (success){
                        Log.d("success", "true");
                        user = response.getJSONObject("user");
                        if(user != null){
                            Log.d("user_null", "not null");
                            showComplaintsActivity(user);
                        }
                    }else{
                        Log.d("success", "false");
                    }
                } catch (JSONException e) {
                    Utility.showMsg(getApplicationContext(),"Login Failed");
                    e.printStackTrace();
                }
                System.out.println(response);
                System.out.println(success);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Volley failed");
            }
        }
        );
        RequestQueue v = Volley.newRequestQueue(this);
        v.add(req);
    }

    //function to hide keypad when screen is touched
    public void hideKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    void showComplaintsActivity(JSONObject user) {
        int user_id=0;
        try {
            user_id= user.getInt("id");
            Intent it = new Intent(this, ComplaintsActivity.class);
            Log.d("next activity","yeah");
            it.putExtra("user_id",user_id);
            startActivity(it);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeIp(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Utility.IP=(String) editText.getText().toString().trim();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_al:
                hideKeyboard();
                if(checkData()) {
                    loginUser();
                }
                break;
            case R.id.signup_al:
                hideKeyboard();
                Intent it = new Intent(this, SignUpActivity.class);
                startActivity(it);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.action_settings:
                break;
            case R.id.action_change_ip:
                changeIp();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);


    }
}
