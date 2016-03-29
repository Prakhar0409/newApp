package com.prakhar_squared_mayank.grs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyComplaintsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyComplaintsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyComplaintsFragment extends Fragment {
    JSONArray data=null;
    private ListView complaintsLV;
    private ComplaintListAdapter complaintsAdapter;

    public MyComplaintsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_complaints, container, false);

        complaintsLV = (ListView) v.findViewById(R.id.complaint_listview_fmc);
        complaintsAdapter = new ComplaintListAdapter(getActivity(), inflater);
        complaintsLV.setAdapter(complaintsAdapter);
        complaintsAdapter.setListView(complaintsLV);


        getData();
        return v;
    }

    void getData() {
        String url1=LoginActivity.ip+"/complaints/concerning/"+((ComplaintsActivity)getActivity()).user_id+".json";

        System.out.println("Url being hit is : " + url1);
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String success="";
                try {
                    success= (String) response.get("success");

                    success= (String) response.get("success");
                    if(success.equals("True")){
                        data = (JSONArray) response.get("data");
                        showToast("Data Fetched!");
                        for (int i=0;i<data.length();i++){
                            System.out.println("data "+Integer.toString(i)+" : "+data.get(i));
                        }
                        updateAdapter(data);
                    }else{
                        showToast("Fetch data failed");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("data1111 : "+response);
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
        RequestQueue v = Volley.newRequestQueue(getActivity());
        v.add(req1);

    }

    //Shows toast with appropriate responses
    public void showToast(String msg)
    {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    void updateAdapter(JSONArray arr) {
        Log.d("mycomplaintsfragment", arr.toString());
        complaintsAdapter.updateData(arr);
    }


//    public void onItemClick(AdapterViewCompat<?> parent, View view, int position, long id) {
//        Log.d("OnItemClickConcerning",Integer.toString(position));
//
//        Intent intent=new Intent(getActivity(),ComplaintDetailActivity.class);
//        JSONObject c= null;
//        try {
//            if (data!=null) {
//                c = (JSONObject) data.get(position);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if(c!=null) {
//            intent.putExtra("Complaint", c.toString());
//            Log.d("Complaint Detailed", c.toString());
//            startActivity(intent);
//        }
//
//    }
}
