package com.prakhar_squared_mayank.grs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SubmitComplaintActivity extends AppCompatActivity {
    AutoCompleteTextView levelACT, domainACT, resolveACT;
    TextView titleTV, descTV;
    ImageView picIV;
    ListView resolveLV;
    String levelSelected="", domainSelected="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_complaint);

        levelACT = (AutoCompleteTextView) findViewById(R.id.level_asc);
        domainACT = (AutoCompleteTextView) findViewById(R.id.domain_asc);
        resolveACT = (AutoCompleteTextView) findViewById(R.id.resolving_right_asc);
        titleTV = (TextView) findViewById(R.id.title_asc);
        descTV = (TextView) findViewById(R.id.desc_asc);
        picIV = (ImageView) findViewById(R.id.pic_asc);
        resolveLV = (ListView) findViewById(R.id.resolving_right_listview_asc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submit_complaint_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_submit_scm) {
            if(checkData()) {
                submitComplaint();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    }
}
