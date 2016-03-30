package com.prakhar_squared_mayank.grs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import org.w3c.dom.Text;

import java.util.Hashtable;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private String timelineDataString;
    View mainView;
    JSONArray timelineData;
    ImageView fab;
    int complaintID = -2;

    private OnFragmentInteractionListener mListener;

    public TimelineFragment() {
        // Required empty public constructor
    }

    public static TimelineFragment newInstance(JSONArray arr) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, arr.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timelineDataString = getArguments().getString(ARG_PARAM1);
            try {
                timelineData = new JSONArray(timelineDataString);
            }
            catch (JSONException e) {

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_timeline, container, false);
        fab = (ImageView) mainView.findViewById(R.id.fab_ft);
        fab.setOnClickListener(this);
        makeTimeline();

        return mainView;
    }

    public void getTimeLineData( ){
        String url1="http://"+LoginActivity.ip+"/status/complaint?complaint_id="+complaintID;

        System.out.println("Url being hit is : " + url1);
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String success="";
                try {
                    if(response.has("data") && !response.isNull("data")){
                        timelineData = (JSONArray) response.get("data");
                        if(timelineData != null) {
                            updateTimeline(timelineData, complaintID);
                        }
                    }else{
                        Utility.showMsg(getActivity(), "Fetch data failed");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("data timeline : "+response);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("volley failed");
            }
        }
        );
        RequestQueue v = Volley.newRequestQueue(getActivity());
        v.add(req1);
    }

    public void updateTimeline(JSONArray arr, int complaintid) {
        timelineData = arr;
        complaintID = complaintid;
        makeTimeline();
    }

    public void makeTimeline() {
        clearLinearLayout();
        String title1="No data.", id1="", desc1="";
        try {
            title1 = timelineData.getJSONObject(0).getString("status_name");
            id1 = timelineData.getJSONObject(0).getInt("id")+"";
            desc1 = timelineData.getJSONObject(0).getString("detailed_status");
        }
        catch (JSONException e) {

        }
        TextView titleTV = (TextView) mainView.findViewById(R.id.text_ft);
        titleTV.setText(title1);
        final String title2=title1, id2=id1, desc2=desc1;
        if(!title1.equals("No data.")) {
            ImageView image = (ImageView) mainView.findViewById(R.id.image_ft);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TF", "Go to detailed status");
                    Intent it = new Intent(getActivity(), StatusDetailActivity.class);
                    it.putExtra("STATUS_ID", id2);
                    it.putExtra("STATUS_NAME", title2);
                    it.putExtra("STATUS_DESC", desc2);
                    getActivity().startActivity(it);
                }
            });
        }


        for(int index = 1;index < timelineData.length();index++) {
            Log.d("TimelineFragment", "Adding timeline event");
            String title="Error with data.", id="", desc="";
            try {
                title = timelineData.getJSONObject(index).getString("status_name");
                id = timelineData.getJSONObject(index).getInt("id")+"";
                desc = timelineData.getJSONObject(index).getString("detailed_status");
            }
            catch (JSONException e) {

            }
            addChildLayout(title, id, desc);
        }
    }

    public void clearLinearLayout() {
        LinearLayout linearLayout = (LinearLayout)mainView.findViewById(R.id.timeline_linear_layout_ft);
        linearLayout.removeAllViewsInLayout();
    }

    public void addChildLayout(final String title, final String id, final String desc){

        Log.d("TF", "The status id is: "+id);
        LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout linearLayout = (LinearLayout)mainView.findViewById(R.id.timeline_linear_layout_ft);

        View view = layoutInflater.inflate(R.layout.timeline_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        TextView titleTV = (TextView) view.findViewById(R.id.text_ti);
        titleTV.setText(title);

        ImageView image = (ImageView) view.findViewById(R.id.image_ti);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TF", "Go to detailed status" + id + "," + title + "," + desc);
                Intent it = new Intent(getActivity(), StatusDetailActivity.class);
                it.putExtra("STATUS_ID", id);
                it.putExtra("STATUS_NAME", title);
                it.putExtra("STATUS_DESC", desc);
                getActivity().startActivity(it);
            }
        });

        Log.d("TimelineFragment", "Adding timeline event: " + title);
        linearLayout.addView(view);
        Log.d("TimelineFragment", "Count now: " + linearLayout.getChildCount());
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void postNewStatus(final String status, final String status_desc) {
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Uploading Status...","Please wait...",false,false);
        String url="http://"+Utility.IP+Utility.POSTSTATUS;
        Log.d("Url hit was:",url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        getTimeLineData();
                        //Showing toast message of the response

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("complaint_id_", complaintID+"");
                params.put("status_name", status);
                params.put("detailed_status", status_desc);

                //returning parameters
                return params;
            }
        };

        volleySingleton.getInstance(getActivity()).getRequestQueue().add(stringRequest);
    }

    void addNewStatus() {
        Log.d("Timeline fragment", "New Status");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Status");

        final LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(15, 5, 15, 5);
        final TextView tv = new TextView(getActivity());
        tv.setText("Status:");
        final EditText input2 = new EditText(getActivity());
        input2.setInputType(InputType.TYPE_CLASS_TEXT);
        input2.setHint("Status title.");
        final EditText input3 = new EditText(getActivity());
        input3.setInputType(InputType.TYPE_CLASS_TEXT);
        input3.setHint("Status description.");

        ll.addView(input2);
        ll.addView(input3);
        builder.setView(ll);

        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(complaintID != -2) {
                    postNewStatus(input2.getText().toString(), input3.getText().toString());
                }
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fab_ft:
                addNewStatus();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
