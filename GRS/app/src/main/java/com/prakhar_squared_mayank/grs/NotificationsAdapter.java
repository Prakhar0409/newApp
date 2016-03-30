package com.prakhar_squared_mayank.grs;

import android.content.Context;
import android.view.LayoutInflater;
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
 * Created by mayank on 29/03/16.
 */
public class NotificationsAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;
    ListView listView;

    public NotificationsAdapter(Context context, LayoutInflater inflater) {
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
            convertView = mInflater.inflate(R.layout.notification_row, parent, false);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.notifTitleTV = (TextView) convertView.findViewById(R.id.text_nr);
            holder.picIV = (ImageView) convertView.findViewById(R.id.pic_nr);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = (JSONObject) getItem(position);

        String title = "Notification Title.";

        if (jsonObject.has("notification")) {
            title = (jsonObject.optString("notification"));
        }


        holder.notifTitleTV.setText(title);

        holder.picIV.setOnClickListener(new View.OnClickListener() {   // TODO: 27/03/16
            @Override
            public void onClick(View view) {

            }
        });

        return convertView;
    }

    private static class ViewHolder {
        public TextView notifTitleTV;
        public ImageView picIV;
    }
}