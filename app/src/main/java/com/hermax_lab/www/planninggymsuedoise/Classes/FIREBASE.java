package com.hermax_lab.www.planninggymsuedoise.Classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Guillaume on 4/29/16.
 */
public class FIREBASE {
  public static String  REF = "https://gymsuedoisevisu1.firebaseio.com/";
   public static String LOGGED_IN = "Logged in";
   public static String LOGGED_OUT = "Logged out";

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
