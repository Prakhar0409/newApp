package com.prakhar_squared_mayank.grs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prakhar on 29/03/16.
 */
public class OtherComplaintsAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;
    ListView listView;

    public OtherComplaintsAdapter(Context context, LayoutInflater inflater) {
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

        if (jsonObject.has("complaint_title")) {
            title = (jsonObject.optString("complaint_title")).toUpperCase();
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

        convertView.setOnTouchListener(new SwipeDetector(holder, position));

        return convertView;
    }

    public void setListView(ListView view) {
        listView = view;
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

    public class SwipeDetector implements View.OnTouchListener {

        private static final int MIN_DISTANCE = 300;
        private static final int MIN_LOCK_DISTANCE = 30; // disallow motion intercept
        private boolean motionInterceptDisallowed = false;
        private float downX, upX;
        private ViewHolder holder;
        private int position;
        private boolean open = false;

        public SwipeDetector(ViewHolder h, int pos) {
            holder = h;
            position = pos;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    return true; // allow other events like Click to be processed
                }

                case MotionEvent.ACTION_MOVE: {
                    upX = event.getX();
                    Log.d("CLA", ""+upX);
                    float deltaX = downX - upX;

                    if (Math.abs(deltaX) > MIN_LOCK_DISTANCE && listView != null && !motionInterceptDisallowed) {
                        listView.requestDisallowInterceptTouchEvent(true);
                        motionInterceptDisallowed = true;
                    }

                    if (deltaX > 0) {
                        holder.hideView.setVisibility(View.GONE);
                    } else {
                        // if first swiped left and then swiped right
                        holder.hideView.setVisibility(View.VISIBLE);
                    }

                    if(!open) {
                        if (deltaX < 0 && deltaX > -170) {
                            swipe(-(int) deltaX);
                        }
                    } else {
                        if (deltaX > 0 && deltaX < 170 && upX > 0) {
                            swipe(-(int) deltaX);
                        }
                    }
                    return true;
                }

                case MotionEvent.ACTION_UP:
                    upX = event.getX();
                    float deltaX = upX - downX;
                    if (Math.abs(deltaX) >= 170) {
                        // left or right
//                        swipeRemove();
                        open = true;
                    } else {
                        open = false;
                        swipeBack();
                    }

                    if (listView != null) {
                        listView.requestDisallowInterceptTouchEvent(false);
                        motionInterceptDisallowed = false;
                    }

                    holder.hideView.setVisibility(View.VISIBLE);
                    return true;

                case MotionEvent.ACTION_CANCEL:
                    holder.hideView.setVisibility(View.VISIBLE);
                    return false;
            }

            return true;
        }

        private void swipeBack() {
            swipe(0);
        }

        private void swipe(int distance) {
            View animationView = holder.mainView;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) animationView.getLayoutParams();
            params.rightMargin = -distance;
            params.leftMargin = distance;
            animationView.setLayoutParams(params);
        }

//        private void swipeRemove() {
//            remove(getItem(position));
//            notifyDataSetChanged();
//        }
    }
}
