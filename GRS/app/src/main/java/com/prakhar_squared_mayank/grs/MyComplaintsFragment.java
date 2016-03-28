package com.prakhar_squared_mayank.grs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyComplaintsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyComplaintsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyComplaintsFragment extends Fragment {
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
        String s = "[{\"complaint_id\":\"21\",\"title\": \"Problem title\",\"description\": \"Problem description\",\"posted_on\":\"23:30 25-10-2016\",\"image_id\":\"\",\"posted_by\": \"U123\",\"upvote_count\":\"38\",\"downvote_count\":\"2\",\"bookmarked\":\"true\"},{\"complaint_id\":\"21\",\"title\": \"Problem title\",\"description\": \"Problem description\",\"posted_on\":\"23:30 25-10-2016\",\"image_id\":\"\",\"posted_by\": \"U123\",\"upvote_count\":\"38\",\"downvote_count\":\"2\",\"bookmarked\":\"true\"},{\"complaint_id\":\"21\",\"title\": \"Problem title\",\"description\": \"Problem description\",\"posted_on\":\"23:30 25-10-2016\",\"image_id\":\"\",\"posted_by\": \"U123\",\"upvote_count\":\"38\",\"downvote_count\":\"2\",\"bookmarked\":\"true\"},{\"complaint_id\":\"21\",\"title\": \"Problem title\",\"description\": \"Problem description\",\"posted_on\":\"23:30 25-10-2016\",\"image_id\":\"\",\"posted_by\": \"U123\",\"upvote_count\":\"38\",\"downvote_count\":\"2\",\"bookmarked\":\"true\"},{\"complaint_id\":\"21\",\"title\": \"Problem title\",\"description\": \"Problem description\",\"posted_on\":\"23:30 25-10-2016\",\"image_id\":\"\",\"posted_by\": \"U123\",\"upvote_count\":\"38\",\"downvote_count\":\"2\",\"bookmarked\":\"true\"},{\"complaint_id\":\"21\",\"title\": \"Problem title\",\"description\": \"Problem description\",\"posted_on\":\"23:30 25-10-2016\",\"image_id\":\"\",\"posted_by\": \"U123\",\"upvote_count\":\"38\",\"downvote_count\":\"2\",\"bookmarked\":\"true\"},{\"complaint_id\":\"21\",\"title\": \"Problem title\",\"description\": \"Problem description\",\"posted_on\":\"23:30 25-10-2016\",\"image_id\":\"\",\"posted_by\": \"U123\",\"upvote_count\":\"38\",\"downvote_count\":\"2\",\"bookmarked\":\"true\"}]";
        JSONArray arr = null;
        try {
            arr = new JSONArray(s);
        }
        catch(JSONException e) {

        }
        updateAdapter(arr);
    }

    void updateAdapter(JSONArray arr) {
        Log.d("mycomplaintsfragment", arr.toString());
        complaintsAdapter.updateData(arr);
    }
}
