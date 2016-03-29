package com.prakhar_squared_mayank.grs;

import android.content.Context;
import android.content.Intent;
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
 * Created by mayank on 29/03/16.
 */
public class BookmarksAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;
    ListView listView;


    public BookmarksAdapter(Context context, LayoutInflater inflater) {
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
    public long getItemId(int i) {
        return i;
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
            convertView = mInflater.inflate(R.layout.complaint_row, parent, false);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.titleTV = (TextView) convertView.findViewById(R.id.title_cr);
            holder.descTV = (TextView) convertView.findViewById(R.id.desc_cr);
            holder.upCountTV = (TextView) convertView.findViewById(R.id.up_count_cr);
            holder.downCountTV = (TextView) convertView.findViewById(R.id.down_count_cr);
            holder.complaintPicIV = (ImageView) convertView.findViewById(R.id.image_cr);
            holder.upIV = (ImageView) convertView.findViewById(R.id.up_image_cr);
            holder.downIV = (ImageView) convertView.findViewById(R.id.down_image_cr);
            holder.bookmarkIV = (ImageView) convertView.findViewById(R.id.bookmark_cr);

            holder.mainView = (RelativeLayout) convertView.findViewById(R.id.main_view_cr);
            holder.hideView = (RelativeLayout) convertView.findViewById(R.id.hide_view_cr);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = (JSONObject) getItem(position);

        String title = "";
        String desc = "";
        String upCount = "", downCount = "";
        String complaintID = "";
        boolean bookmarked = true;

        if (jsonObject.has("complaint_title")) {
            if(jsonObject.optBoolean("resolved", false)) {
                title = ("[RESOLVED] "+jsonObject.optString("complaint_title")).toUpperCase();
            }
            else {
                title = (jsonObject.optString("complaint_title")).toUpperCase();
            }
        }

        if (jsonObject.has("complaint_details")) {
            desc = jsonObject.optString("complaint_details");
        }

        if (jsonObject.has("upvotes_count")) {
            upCount = jsonObject.optString("upvotes_count");
        }

        if (jsonObject.has("downvotes_count")) {
            downCount = jsonObject.optString("downvotes_count");
        }

        if (jsonObject.has("complaint_id")) {
            complaintID = jsonObject.optString("complaint_id");
        }

        if (jsonObject.has("bookmarked")) {
            bookmarked = jsonObject.optBoolean("bookmarked");
            if(bookmarked) {
                holder.bookmarkIV.setImageResource(R.drawable.yes_bookmark);
            }
            else {
                holder.bookmarkIV.setImageResource(R.drawable.not_bookmarked);
            }
        }

        holder.titleTV.setText(title);
        final int up = Integer.parseInt(upCount);
        final int down = Integer.parseInt(downCount);

        holder.descTV.setText(desc);
        holder.upCountTV.setText(""+up);
        holder.downCountTV.setText(""+down);

        holder.titleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, ComplaintDetailActivity.class);
                mContext.startActivity(it);
            }
        });

        holder.descTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, ComplaintDetailActivity.class);
                mContext.startActivity(it);
            }
        });

        holder.complaintPicIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, ComplaintDetailActivity.class);
                mContext.startActivity(it);
            }
        });

        holder.upIV.setOnClickListener(new View.OnClickListener() {         // TODO: 27/03/16
            @Override
            public void onClick(View view) {

            }
        });

        holder.downIV.setOnClickListener(new View.OnClickListener() {       // TODO: 27/03/16
            @Override
            public void onClick(View view) {

            }
        });

        holder.bookmarkIV.setOnClickListener(new View.OnClickListener() {   // TODO: 27/03/16
            @Override
            public void onClick(View view) {

            }
        });

        return convertView;
    }

    private static class ViewHolder {
        public TextView titleTV;
        public TextView descTV;
        public TextView upCountTV;
        public TextView downCountTV;
        public ImageView complaintPicIV;
        public ImageView upIV, downIV, bookmarkIV;
        public RelativeLayout mainView, hideView;
    }


}
