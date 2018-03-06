package com.hls.plugins.barcode;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.widget.Toast;

import com.hls.plugins.barcode.Util.Util;
import com.hls.plugins.barcode.activity.NetWorkErrorActivity;
import com.hls.plugins.barcode.activity.UnOpenWebActivity;
import com.hls.plugins.barcode.bean.ArgsBean;
import com.hls.plugins.barcode.bean.ArgsBiz;

/**
 * Created by Administrator on 2016/3/16.
 */
public class BarcodePlugin extends CordovaPlugin {

  public static CallbackContext cbContext = null;

  private static final String LOG_TAG = "BarcodeScanner";
  private String[] permissions = {Manifest.permission.CAMERA};
  private JSONArray args;
  @Override
  public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {
    cbContext = callbackContext;
    if (action.equals("startScan")) {
      this.args =args;
      if (!hasPermission()) {
        requestPermissions(0);
      } else {
        scan(args);
      }
    } else if (action.equals("hasPermission")) {
      if (!hasPermission()) {
        requestPermissions(0);
      }
      cbContext.success(hasPermission() + "");
      return true;
    }

    return true;
  }


  /**
   * Starts an intent to scan and decode a barcode.
   */
  public void scan(final JSONArray args) {
    try {
      ArgsBiz argsBiz = new ArgsBiz();
      ArgsBean argsBean ;
      argsBean = argsBiz.getArgsBeanByJSONObject(args.getJSONObject(0));
      if (!Util.isNetworkAvailable(cordova.getActivity())) {
        Intent intent = new Intent(cordova.getActivity(), NetWorkErrorActivity.class);
        intent.putExtra("ARGS",argsBean);
        this.cordova.getActivity().startActivity(intent);
        return;
      }
      if(argsBean!=null) {
        Intent intentScan = new Intent(this.cordova.getActivity().getApplicationContext(), CaptureActivity.class);
        intentScan.putExtra("ARGS", argsBean);
        this.cordova.getActivity().startActivity(intentScan);
      }else{
        cbContext.success("请检查参数");
      }
    } catch (JSONException e) {
      e.printStackTrace();
      cbContext.success("请检查参数");

    }

  }

  /**
   * check application's permissions
   */
  public boolean hasPermission() {
    for (String p : permissions) {
      if (!PermissionHelper.hasPermission(this, p)) {
        return false;
      } else {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
          mCamera = Camera.open();
          Camera.Parameters mParameters = mCamera.getParameters(); // 针对魅族手机
          mCamera.setParameters(mParameters);
        } catch (Exception e) {
          isCanUse = false;
        }

        if (mCamera != null) {
          try {
            mCamera.release();
          } catch (Exception e) {
            e.printStackTrace();
            return isCanUse;
          }
        }
        return isCanUse;
      }
    }
    return true;
  }

  /**
   * We override this so that we can access the permissions variable, which no
   * longer exists in the parent class, since we can't initialize it reliably
   * in the constructor!
   *
   * @param requestCode The code to get request action
   */
  public void requestPermissions(int requestCode) {
    PermissionHelper.requestPermissions(this, requestCode, permissions);
  }

  /**
   * processes the result of permission request
   *
   * @param requestCode  The code to get request action
   * @param permissions  The collection of permissions
   * @param grantResults The result of grant
   */
  public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults)
    throws JSONException {
    PluginResult result;
//    Toast.makeText(this.cordova.getActivity().getApplicationContext(), grantResults.toString(), 3).show();
    for (int r : grantResults) {
      if (r == PackageManager.PERMISSION_DENIED) {
        Log.d(LOG_TAG, "Permission Denied!");
        result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION);
        cbContext.sendPluginResult(result);
        return;
      }
    }
    if(args!=null){
      scan(args);
    }

  }
}
