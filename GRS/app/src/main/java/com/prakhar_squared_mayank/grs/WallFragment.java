package com.prakhar_squared_mayank.grs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.Hashtable;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WallFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WallFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private String wallDataString;
    View mainView;
    JSONArray wallData;
    ImageView fab;
    int complaintID = -2;

    private OnFragmentInteractionListener mListener;

    public WallFragment() {
        // Required empty public constructor
    }

    public static WallFragment newInstance(JSONArray arr) {
        WallFragment fragment = new WallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, arr.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wallDataString = getArguments().getString(ARG_PARAM1);
            try {
                wallData = new JSONArray(wallDataString);
            }
            catch (JSONException e) {

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_wall, container, false);
        fab = (ImageView) mainView.findViewById(R.id.fab_fw);
        fab.setOnClickListener(this);

        makeComments();

        return mainView;
    }

    public void getCommentData( ){
        String url1="http://"+LoginActivity.ip+"/comments/complaint?complaint_id="+complaintID;

        System.out.println("Url being hit for comment data is : " + url1);
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String success="";
                try {
                    if(response.has("data") && !response.isNull("data")){
                        wallData = (JSONArray) response.get("data");
                        if(wallData != null) {
                            updateWall(wallData, complaintID);
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

    public void updateWall(JSONArray arr, int complaintid) {
        wallData = arr;
        complaintID = complaintid;
        makeComments();
    }

    public void makeComments() {
        clearLinearLayout();

        for(int index = 0;index < wallData.length();index++) {
            Log.d("WallFragment", "Adding timeline event");
            String comment="Error with data.", id="", name = "";
            try {
                comment = wallData.getJSONObject(index).getString("comment_made");
                id = wallData.getJSONObject(index).getString("comment_id");
                name = wallData.getJSONObject(index).getString("user_name_first");
            }
            catch (JSONException e) {

            }
            addChildLayout(comment, id, name);
        }
    }

    public void clearLinearLayout() {
        LinearLayout linearLayout = (LinearLayout)mainView.findViewById(R.id.wall_linear_layout_ft);
        linearLayout.removeAllViewsInLayout();
    }

    public void addChildLayout(String comment, String id, String username){

        LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout linearLayout = (LinearLayout)mainView.findViewById(R.id.wall_linear_layout_ft);

        View view = layoutInflater.inflate(R.layout.comment_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView titleTV = (TextView) view.findViewById(R.id.text_ci);
        titleTV.setText(comment);

        TextView usernameTV = (TextView) view.findViewById(R.id.username_ci);
        usernameTV.setText(username);

        Log.d("TimelineFragment", "Adding timeline event: " + comment);
        linearLayout.addView(view);
        Log.d("TimelineFragment", "Count now: " + linearLayout.getChildCount());

    }


    void addNewWallComment() {
        Log.d("Wall fragment", "New Comment");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Comment");

        final LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(15, 5, 15, 5);
        final TextView tv = new TextView(getActivity());
        tv.setText("Wall Comment:");
        final EditText input2 = new EditText(getActivity());
        input2.setInputType(InputType.TYPE_CLASS_TEXT);
        input2.setHint("Enter comment here");

        ll.addView(input2);
        builder.setView(ll);

        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(complaintID != -2) {
                    postNewComment(input2.getText().toString());
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    void postNewComment(final String commentM) {
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Uploading Status...","Please wait...",false,false);
        String url="http://"+Utility.IP+Utility.POSTWALLCOMMENT;
        Log.d("Url hit was:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        getCommentData();
                        //Showing toast message of the response

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast
                        Utility.showMsg(getActivity(), volleyError.getMessage().toString());//, Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("complaint_id", complaintID+"");
                params.put("comment_made", commentM);

                //returning parameters
                return params;
            }
        };

        volleySingleton.getInstance(getActivity()).getRequestQueue().add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fab_fw:
                addNewWallComment();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
