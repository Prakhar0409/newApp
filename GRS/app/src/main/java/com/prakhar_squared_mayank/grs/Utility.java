package com.prakhar_squared_mayank.grs;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by mayank on 26/03/16.
 */
public class Utility {
    public static String LOGIN_URL = "";
    public static boolean DEBUG = true;

    public static void showMsg(Context ctx, String m)
    {
        Toast.makeText(ctx, m, Toast.LENGTH_SHORT);
    }
}
