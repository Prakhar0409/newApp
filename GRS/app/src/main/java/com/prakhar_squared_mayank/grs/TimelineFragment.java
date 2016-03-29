package com.prakhar_squared_mayank.grs;

import android.app.Activity;
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
public class TimelineFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private String timelineDataString;
    View mainView;
    JSONArray timelineData;
    ImageView fab;

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

    public void updateTimeline(JSONArray arr) {
        timelineData = arr;
        makeTimeline();
    }

    public void makeTimeline() {
        clearLinearLayout();
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

    public void addChildLayout(final String title, final String id){

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
                Intent it = new Intent(getActivity(), StatusDetailActivity.class);
                it.putExtra("STATUS_ID", id);
                it.putExtra("STATUS_NAME", title);
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

    void addNewStatus() {
        Log.d("Timeline fragment", "New Status");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Status");

        final LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(15, 5, 15, 5);
        final TextView tv = new TextView(getActivity());
        tv.setText("Status title:");
        final EditText input2 = new EditText(getActivity());
        input2.setInputType(InputType.TYPE_CLASS_TEXT);
        input2.setHint("Enter status here");

        ll.addView(input2);
        builder.setView(ll);

        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
