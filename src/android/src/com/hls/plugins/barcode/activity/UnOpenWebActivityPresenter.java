package com.hls.plugins.barcode.activity;

import android.content.Context;

import com.hls.plugins.barcode.Util.Util;
import com.hls.plugins.barcode.bean.ArgsBean;
import com.hls.plugins.barcode.http.HttpManager;
import com.hls.plugins.barcode.http.ResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by panx on 2017/3/16.
 */
public class UnOpenWebActivityPresenter {

  private IUnOpenWebActivity iUnOpenWebActivity;
  private Context context;
  public UnOpenWebActivityPresenter(IUnOpenWebActivity iUnOpenWebActivity,Context context) {
    this.iUnOpenWebActivity = iUnOpenWebActivity;
    this.context = context;
  }

  public void toValidation(ArgsBean argsBean){
    iUnOpenWebActivity.showProgressDialog(true);
    HttpManager.get(argsBean.getHttpInfos().get(1), new ResultCallback<String>() {
      @Override
      public void onError(Exception e, String error) {
        iUnOpenWebActivity.showProgressDialog(false);
        if(!Util.isNetworkAvailable(context)){
          iUnOpenWebActivity.goNetworkErrorPage();
        }else{
          iUnOpenWebActivity.ToastCenter(error);
        }
      }

      @Override
      public void onResponse(String response, String result) {
        iUnOpenWebActivity.showProgressDialog(false);
        doResultBiz(response);
      }
    });
  }

  private void doResultBiz(String response){
    try {
      JSONObject object = new JSONObject(response);
      int online = object.getInt("online");
      if(online!=1){
        iUnOpenWebActivity.stillUnOpen();
      }else{
        iUnOpenWebActivity.hasOpen();
      }
    } catch (JSONException e) {
      iUnOpenWebActivity.ToastCenter(HttpManager.NETWORK_ERROR);
      e.printStackTrace();
    }
  }

}
