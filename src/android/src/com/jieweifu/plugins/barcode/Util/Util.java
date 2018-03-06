package com.jieweifu.plugins.barcode.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by panx on 2017/3/15.
 */
public class Util {

  public static boolean isNetworkAvailable(Context context){
    ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
    if(networkInfo!=null&&networkInfo.isAvailable()){
      return true;
    }
    return false;
  }

}
