package com.hls.plugins.barcode.activity;

/**
 * Created by panx on 2017/3/16.
 */
public interface IUnOpenWebActivity {
  void stillUnOpen();
  void hasOpen();
  void ToastCenter(String msg);
  void showProgressDialog(boolean flag);
  void goNetworkErrorPage();
}
