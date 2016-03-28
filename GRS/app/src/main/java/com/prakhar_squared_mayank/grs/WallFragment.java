package com.prakhar_squared_mayank.grs;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WallFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WallFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String wallDataString;
    View mainView;
    JSONArray wallData;

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

        makeComments();

        return mainView;
    }

    public void updateTimeline(JSONArray arr) {
        wallData = arr;
        makeComments();
    }

    public void makeComments() {
//        String title1="Error with data.", id1="";
//        try {
//            title1 = wallData.getJSONObject(0).getString("status_name");
//            id1 = wallData.getJSONObject(0).getString("status_id");
//        }
//        catch (JSONException e) {
//
//        }
//        TextView titleTV = (TextView) mainView.findViewById(R.id.text_ft);
//        titleTV.setText(title1);


        for(int index = 0;index < wallData.length();index++) {
            Log.d("WallFragment", "Adding timeline event");
            String comment="Error with data.", id="";
            try {
                comment = wallData.getJSONObject(index).getString("comment");
                id = wallData.getJSONObject(index).getString("comment_id");
            }
            catch (JSONException e) {

            }
            addChildLayout(comment, id);
        }
    }

    public void clearLinearLayout() {
        LinearLayout linearLayout = (LinearLayout)mainView.findViewById(R.id.wall_linear_layout_ft);
        linearLayout.removeAllViewsInLayout();
    }

    public void addChildLayout(String comment, String id){

        LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout linearLayout = (LinearLayout)mainView.findViewById(R.id.wall_linear_layout_ft);

        View view = layoutInflater.inflate(R.layout.comment_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView titleTV = (TextView) view.findViewById(R.id.text_ci);
        titleTV.setText(comment);

        Log.d("TimelineFragment", "Adding timeline event: " + comment);
        linearLayout.addView(view);
        Log.d("TimelineFragment", "Count now: " + linearLayout.getChildCount());

//        linearLayout.add
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
