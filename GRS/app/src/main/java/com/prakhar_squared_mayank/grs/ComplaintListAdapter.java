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
 * Created by mayank on 27/03/16.
 */
public class ComplaintListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;
    ListView listView;

    public ComplaintListAdapter(Context context, LayoutInflater inflater) {
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
        final JSONObject jsonObject = (JSONObject) getItem(position);

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


        boolean bookmarked = false;
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
        if(upCount.equals("null")) {
            upCount = "0";
        }
        if(downCount.equals("null")) {
            downCount = "0";
        }
        final int up = Integer.parseInt(upCount);
        final int down = Integer.parseInt(downCount);

        holder.descTV.setText(desc);
        holder.upCountTV.setText(""+up);
        holder.downCountTV.setText(""+down);

        holder.titleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ComplaintDetailActivity.class);
                intent.putExtra("Complaint", jsonObject.toString());
                Log.d("Complaint Detailed", jsonObject.toString());
                mContext.startActivity(intent);

//                mContext.startActivity(it);
            }
        });

        holder.descTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ComplaintDetailActivity.class);
                intent.putExtra("Complaint", jsonObject.toString());
                Log.d("Complaint Detailed", jsonObject.toString());
                mContext.startActivity(intent);
            }
        });

        holder.complaintPicIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ComplaintDetailActivity.class);
                intent.putExtra("Complaint", jsonObject.toString());
                Log.d("Complaint Detailed", jsonObject.toString());
                mContext.startActivity(intent);
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

        private static final int MIN_DISTANCE = 170;
        private static final int MIN_LOCK_DISTANCE = 30; // disallow motion intercept
        private boolean motionInterceptDisallowed = false;
        private float downX, upX;
        private ViewHolder holder;
        private int position;
        private boolean closed=true, hooked=false, open=false, closing=false, locked=false;

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
                    if(locked) return false;

                    if(closed) {
                        if(deltaX > 0) {
                            holder.hideView.setVisibility(View.GONE);
                            swipe(0);
                        }
                        else if (Math.abs(deltaX) > MIN_LOCK_DISTANCE && listView != null && !motionInterceptDisallowed) {
                            listView.requestDisallowInterceptTouchEvent(true);
                            motionInterceptDisallowed = true;
                            hooked = true;
                            closed = false;
                        }
                        else {
                            holder.hideView.setVisibility(View.VISIBLE);
                            swipe(-(int) deltaX);
                        }
                    }
                    else if(hooked && !open) {
                        if(Math.abs(deltaX) < MIN_LOCK_DISTANCE) {
                            hooked = false;
                            closed = true;
                        }
                        if(Math.abs(deltaX) < MIN_DISTANCE) {
                            swipe(-(int) deltaX);
                        }
                        else {
                            hooked = true;
                            open = true;
                            if(closing) {
                                swipe(0);
                                locked = true;
                            }
                            else {
                                swipe(MIN_DISTANCE);
                            }
                        }
                    }
                    else if(hooked && open) {
                        if(deltaX > MIN_DISTANCE && deltaX < 0) {
                            swipe(-(int) deltaX);
                            hooked = true;
                            open = false;
                        }
                        else if(deltaX > 0) {
                            swipe(-(int) deltaX);
                            hooked = true;
                            open = false;
                            closing = true;
                        }
                        else {
                            swipe(MIN_DISTANCE);
                        }
                    }

//                    if (Math.abs(deltaX) > MIN_LOCK_DISTANCE && listView != null && !motionInterceptDisallowed) {
//                        listView.requestDisallowInterceptTouchEvent(true);
//                        motionInterceptDisallowed = true;
//                    }
//
//                    if (deltaX > 0) {
//                        holder.hideView.setVisibility(View.GONE);
//                    } else {
//                        // if first swiped left and then swiped right
//                        holder.hideView.setVisibility(View.VISIBLE);
//                    }
//
//                    if(!open) {
//                        if (deltaX < 0 && deltaX > -170) {
//                            swipe(-(int) deltaX);
//                        }
//                    } else {
//                        if (deltaX > 0 && deltaX < 170 && upX > 0) {
//                            swipe(-(int) deltaX);
//                        }
//                    }
                    return true;
                }

                case MotionEvent.ACTION_UP: {
                    upX = event.getX();
                    float deltaX = upX - downX;
                    if(closed) {
                        swipe(0);
                    }
                    else if(hooked && !open) {
                        swipe(0);
                    }
                    else if(hooked && open) {
                        swipe(MIN_DISTANCE);
                    }
                    locked = true;
//                    if (Math.abs(deltaX) >= 170) {
//                        // left or right
////                        swipeRemove();
//                        open = true;
//                    } else {
//                        open = false;
//                        swipeBack();
//                    }

                    if (listView != null) {
                        listView.requestDisallowInterceptTouchEvent(false);
                        motionInterceptDisallowed = false;
                    }

                    holder.hideView.setVisibility(View.VISIBLE);
                    return true;
                }

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
