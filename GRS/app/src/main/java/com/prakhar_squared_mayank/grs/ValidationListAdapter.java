package com.prakhar_squared_mayank.grs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by mayank on 28/03/16.
 */
public class ValidationListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;
    ListView listView;

    public ValidationListAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateData(JSONArray jsonArray) {
        // update the adapter's dataset
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.validation_request_row, parent, false);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.usernameTV = (TextView) convertView.findViewById(R.id.username_vrr);
            holder.groupTV = (TextView) convertView.findViewById(R.id.group_vrr);
            holder.positionTV = (TextView) convertView.findViewById(R.id.post_vrr);
            holder.acceptIV = (ImageView) convertView.findViewById(R.id.accept_vrr);
            holder.rejectIV = (ImageView) convertView.findViewById(R.id.reject_vrr);
            holder.profilePicIV = (ImageView) convertView.findViewById(R.id.profile_pic_vrr);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = (JSONObject) getItem(position);

        String username = "";
        String group = "";
        String post = "";

        if (jsonObject.has("x")) {                  // TODO: 28/03/16
            username = (jsonObject.optString("x")).toUpperCase();
        }

        if (jsonObject.has("y")) {                  // TODO: 28/03/16
            group = jsonObject.optString("y");
        }

        if (jsonObject.has("z")) {                  // TODO: 28/03/16
            post = jsonObject.optString("z");
        }


        holder.usernameTV.setText(username);

        holder.groupTV.setText(group);
        holder.positionTV.setText(post);

        holder.acceptIV.setOnClickListener(new View.OnClickListener() {         // TODO: 27/03/16
            @Override
            public void onClick(View view) {

            }
        });

        holder.rejectIV.setOnClickListener(new View.OnClickListener() {       // TODO: 27/03/16
            @Override
            public void onClick(View view) {

            }
        });

        return convertView;
    }

    private static class ViewHolder {
        public TextView usernameTV;
        public TextView groupTV;
        public TextView positionTV;
        public ImageView acceptIV;
        public ImageView rejectIV, profilePicIV;
    }
}

