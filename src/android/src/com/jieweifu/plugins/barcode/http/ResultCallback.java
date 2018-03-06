package com.jieweifu.plugins.barcode.http;

/**
 * Created by panx on 2017/3/8.
 */
public abstract class ResultCallback<T> {
  public abstract void onError(Exception e,String error);

  public abstract void onResponse(T response,String result);

}
