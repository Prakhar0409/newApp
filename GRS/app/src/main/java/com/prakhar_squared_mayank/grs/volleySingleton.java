package com.prakhar_squared_mayank.grs;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Prakhar on 1/16/2016.
 */

public class volleySingleton{//} extends Application {
    private RequestQueue _RequestQueue;
    private static volleySingleton _volleySingleton;
    private static Context _context;

    public static final String TAG=volleySingleton.class.getName();

    public volleySingleton(Context context){
        _context=context;

        _RequestQueue= getRequestQueue();

    }

    public static synchronized volleySingleton getInstance(Context context){
        if (_volleySingleton== null) {
            _volleySingleton= new volleySingleton(context);
        }
        return _volleySingleton;

    }

    public RequestQueue getRequestQueue(){
        if (_RequestQueue== null) {
            _RequestQueue = Volley.newRequestQueue(_context);//, 10 * 1024 * 1024); // this is for caching request
        }

        return _RequestQueue;
    }

    public <T> void add(Request<T> req){
        getRequestQueue().add(req);
    }
    public void cancel(){
        _RequestQueue.cancelAll(TAG);
    }


}
