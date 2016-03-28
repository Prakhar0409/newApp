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
import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String timelineDataString;
    View mainView;
    JSONArray timelineData;

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
        makeTimeline();

        return mainView;
    }

    public void updateTimeline(JSONArray arr) {
        timelineData = arr;
        makeTimeline();
    }

    public void makeTimeline() {
        String title1="Error with data.", id1="";
        try {
            title1 = timelineData.getJSONObject(0).getString("status_name");
            id1 = timelineData.getJSONObject(0).getString("status_id");
        }
        catch (JSONException e) {

        }
        TextView titleTV = (TextView) mainView.findViewById(R.id.text_ft);
        titleTV.setText(title1);


        for(int index = 1;index < timelineData.length();index++) {
            Log.d("TimelineFragment", "Adding timeline event");
            String title="Error with data.", id="";
            try {
                title = timelineData.getJSONObject(index).getString("status_name");
                id = timelineData.getJSONObject(index).getString("status_id");
            }
            catch (JSONException e) {

            }
            addChildLayout(title, id);
        }
    }

    public void clearLinearLayout() {
        LinearLayout linearLayout = (LinearLayout)mainView.findViewById(R.id.timeline_linear_layout_ft);
        linearLayout.removeAllViewsInLayout();
    }

    public void addChildLayout(String title, String id){

        LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout linearLayout = (LinearLayout)mainView.findViewById(R.id.timeline_linear_layout_ft);

        View view = layoutInflater.inflate(R.layout.timeline_item, null);
        TextView titleTV = (TextView) view.findViewById(R.id.text_ti);
        titleTV.setText(title);

        Log.d("TimelineFragment", "Adding timeline event: " + title);
        linearLayout.addView(view, 0);
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
