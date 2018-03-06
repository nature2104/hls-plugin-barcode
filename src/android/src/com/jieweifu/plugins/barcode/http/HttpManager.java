package com.jieweifu.plugins.barcode.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.jieweifu.plugins.barcode.bean.ArgsBean;
import com.jieweifu.plugins.barcode.bean.CodeInfoBean;
import com.jieweifu.plugins.barcode.bean.HttpInfoBean;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by panx on 2017/3/8.
 */
public class HttpManager {

  public static final String TAG = "HttpManager";
  public static final String NETWORK_ERROR = "请求发生错误，请稍后再试！";
  public static Handler handler = new Handler(Looper.getMainLooper());

  public static void get(final HttpInfoBean httpInfoBean, final ResultCallback<String> callback){
    new Thread(){
      @Override
      public void run() {
        super.run();
        doGet(httpInfoBean,callback);
      }
    }.start();

  }

  private static void doGet(HttpInfoBean httpInfoBean, final ResultCallback<String> callback){
    HttpUtil.get(httpInfoBean, new ResultCallback<String>() {
      @Override
      public void onError(Exception e, String error) {
        handlerError(e,callback);
      }

      @Override
      public void onResponse(String response, String result) {
        handlerSuccess(response,callback);
      }
    });
  }

  public static void post(final HttpInfoBean httpInfoBean, final ResultCallback<String> callback) {
    new Thread() {
      @Override
      public void run() {
        super.run();
        doPost(httpInfoBean, callback);
      }
    }.start();
  }

  private static void doPost(HttpInfoBean httpInfoBean, final ResultCallback<String> callback) {
    HttpUtil.post(httpInfoBean, new ResultCallback<String>() {
      @Override
      public void onError(Exception e, String error) {
        handlerError(e, callback);
      }

      @Override
      public void onResponse(String response,String result) {
        handlerSuccess(response, callback);
      }
    });
  }

  /**
   * 网络请求成功时的处理
   * @param response
   * @param callback
     */
  private static void handlerSuccess(final String response, final ResultCallback callback) {
    handler.post(new Runnable() {
      @Override
      public void run() {
        callback.onResponse(response,"");
      }
    });
  }

  /**
   * 网络请求失败的处理
   * @param e
   * @param callback
     */
  private static void handlerError(final Exception e, final ResultCallback callback) {
    handler.post(new Runnable() {
      @Override
      public void run() {
        callback.onError(e, NETWORK_ERROR);
      }
    });
  }
}
