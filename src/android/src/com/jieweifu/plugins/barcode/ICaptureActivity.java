package com.jieweifu.plugins.barcode;

import org.json.JSONObject;

/**
 * Created by panx on 2017/3/16.
 */
public interface ICaptureActivity {
  void goNetworkErrorPage();

  void goUnOpenWebPage();

  void reStartActivity();

  void scanFinish(JSONObject result);

  void ToastCenter(String msg);

  void showProgressDialog(boolean flag);
}
