package com.jieweifu.plugins.barcode;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jieweifu.plugins.barcode.Util.Util;
import com.jieweifu.plugins.barcode.bean.ArgsBean;
import com.jieweifu.plugins.barcode.bean.CodeInfoBean;
import com.jieweifu.plugins.barcode.http.HttpManager;
import com.jieweifu.plugins.barcode.http.ResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by panx on 2017/3/16.
 */
public class CapturePresenter {
  private ICaptureActivity iCaptureActivity;
  private Context context;
  private final static String TAG = "CapturePresenter";
  private ArgsBean argsBean;

  public CapturePresenter(ICaptureActivity iCaptureActivity,Context context) {
    this.iCaptureActivity = iCaptureActivity;
    this.context = context;
  }

  /**
   * 向后端发送验证请求
   * @param argsBean
   * @param code
     */
  public void toValidation(ArgsBean argsBean,final String code) {

    this.argsBean = makeArguments(argsBean, code);
    if (argsBean == null) return;
    iCaptureActivity.showProgressDialog(true);
    HttpManager.post(argsBean.getHttpInfos().get(0), new ResultCallback<String>() {
      //条码处理失败
      @Override
      public void onError(Exception e, String error) {
        iCaptureActivity.showProgressDialog(false);
        iCaptureActivity.ToastCenter(error);
        if(!Util.isNetworkAvailable(context)){
          iCaptureActivity.goNetworkErrorPage();
        }else{
          iCaptureActivity.reStartActivity();
        }
      }

      //条码处理成功
      @Override
      public void onResponse(String response, String extra) {
        iCaptureActivity.showProgressDialog(false);
        CodeInfoBean codeInfoBean = doCodeResult(response);
        if(codeInfoBean!=null) {
          doResultBiz(codeInfoBean, response,code);
        }else{
          iCaptureActivity.ToastCenter(HttpManager.NETWORK_ERROR);
        }
      }
    });
  }

  /**
   * 处理网络请求参数
   * @param argsBean
   * @param code
   * @return
     */
  private ArgsBean makeArguments(ArgsBean argsBean, String code) {
    String data = argsBean.getHttpInfos().get(0).getData();
    try {
      JSONObject object = new JSONObject(data);
      object.put("code", code);
      object.put("operate", argsBean.getOperateMethod());
      argsBean.getHttpInfos().get(0).setData(object.toString());
    } catch (JSONException e) {
      e.printStackTrace();
      Log.e(TAG, "wrog arguments for http data");
      return null;
    }
    return argsBean;
  }

  /**
   * 处理扫码结果
   */
  private CodeInfoBean doCodeResult(String response) {
    Log.e(TAG, response);
    try {
      JSONObject object = new JSONObject(response);
      CodeInfoBean codeInfoBean = new CodeInfoBean();
      codeInfoBean.setCode(object.getString(CodeInfoBean.CODE));
      codeInfoBean.setMsg(object.getString(CodeInfoBean.MSG));
      codeInfoBean.setOnline(object.optString(CodeInfoBean.ONLINE));
      codeInfoBean.setExpenseReport(object.optString(CodeInfoBean.EXPENSE_REPORT));
      return codeInfoBean;
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 根据扫码结果做出相应的反馈
   */
  private void doResultBiz(CodeInfoBean codeInfoBean, String response,String code) {
    String operateMethod = argsBean.getOperateMethod();
    if (operateMethod != null && operateMethod.equals(ArgsBean.REVIEW)) {
      doReviewBiz(codeInfoBean, response);
    } else if (operateMethod != null && operateMethod.equals(ArgsBean.AUDIT_PASS)) {
      doAuditPassBiz(codeInfoBean, response);
    } else if (operateMethod != null && operateMethod.equals(ArgsBean.AUDIT)) {
      doAuditBiz(codeInfoBean, response, code);
    }else if (operateMethod != null && operateMethod.equals(ArgsBean.BACK)) {
      doBackBiz(codeInfoBean, response, code);
    }
    else if (operateMethod != null && operateMethod.equals(ArgsBean.RECEIVE)) {
      doReceiveBiz(codeInfoBean,response);
    } else if (operateMethod != null && operateMethod.equals(ArgsBean.INVOICE)){
      doInvoiceBiz(codeInfoBean,response,code);
    }
  }

  /**
   * 处理REVIEW的业务逻辑
   */
  private void doReviewBiz(CodeInfoBean codeInfoBean, String response) {
    if (codeInfoBean.getCode() != null && codeInfoBean.getCode().equals(CodeInfoBean.SUCCESS)) {
      //审核成功
      JSONObject object = new JSONObject();
      try {
        object.put("type", ArgsBean.REVIEW);
        JSONObject responseObject = new JSONObject(response);
        object.put("message", responseObject);
        iCaptureActivity.ToastCenter(codeInfoBean.getMsg());
        iCaptureActivity.scanFinish(object);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    } else if (codeInfoBean.getOnline() == null || !codeInfoBean.getOnline().equals("1")) {
      iCaptureActivity.goUnOpenWebPage();//未打开中控页面
    } else {
      //审核没有成功
      iCaptureActivity.ToastCenter(codeInfoBean.getMsg());
      iCaptureActivity.reStartActivity();
    }
  }

  /**
   * 处理ADDIT_PASS逻辑
   */
  private void doAuditPassBiz(CodeInfoBean codeInfoBean, String response) {
    if(codeInfoBean.getCode()!=null && codeInfoBean.getCode().equals(CodeInfoBean.SUCCESS)){
      try {
        JSONObject object = new JSONObject(codeInfoBean.getExpenseReport());
        String applicantName = object.getString("applicantName");
        String businessCode = object.getString("businessCode");
        String msg = String.format("%s\n%s\n%s",applicantName,businessCode,codeInfoBean.getMsg());
        iCaptureActivity.ToastCenter(msg);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      iCaptureActivity.reStartActivity();
    }else{
      iCaptureActivity.ToastCenter(codeInfoBean.getMsg());
      iCaptureActivity.reStartActivity();
    }
  }

  /**
   * 处理AUDIT逻辑
   */
  private void doAuditBiz(CodeInfoBean codeInfoBean, String response,String code) {
    if(codeInfoBean.getCode()!=null && codeInfoBean.getCode().equals(CodeInfoBean.SUCCESS)){
      JSONObject object = new JSONObject();
      try {

        object.put("type","AUDIT");
        JSONObject msgObject = new JSONObject();
        msgObject.put("code",code);
        JSONObject responseObject = new JSONObject(response);
        msgObject.put("response",responseObject);
        object.put("message",msgObject);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      iCaptureActivity.ToastCenter(codeInfoBean.getMsg());
      iCaptureActivity.scanFinish(object);
    }else{
      iCaptureActivity.ToastCenter(codeInfoBean.getMsg());
      iCaptureActivity.reStartActivity();
    }
  }

   /**
     * 处理BACK逻辑
     */
    private void doBackBiz(CodeInfoBean codeInfoBean, String response,String code) {
      if(codeInfoBean.getCode()!=null && codeInfoBean.getCode().equals(CodeInfoBean.SUCCESS)){
        JSONObject object = new JSONObject();
        try {

          object.put("type","BACK");
          JSONObject msgObject = new JSONObject();
          msgObject.put("code",code);
          JSONObject responseObject = new JSONObject(response);
          msgObject.put("response",responseObject);
          object.put("message",msgObject);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        iCaptureActivity.ToastCenter(codeInfoBean.getMsg());
        iCaptureActivity.scanFinish(object);
      }else{
        iCaptureActivity.ToastCenter(codeInfoBean.getMsg());
        iCaptureActivity.reStartActivity();
      }
    }

  /**
   * 处理INVOICE逻辑
   */
  private void doInvoiceBiz(CodeInfoBean codeInfoBean, String response,String code) {
    if(codeInfoBean.getCode()!=null && codeInfoBean.getCode().equals(CodeInfoBean.SUCCESS)){
      JSONObject object = new JSONObject();
      try {

        object.put("type",ArgsBean.INVOICE);
        JSONObject msgObject = new JSONObject();
        msgObject.put("code",code);
        JSONObject responseObject = new JSONObject(response);
        msgObject.put("response",responseObject);
        object.put("message",msgObject);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      iCaptureActivity.ToastCenter(codeInfoBean.getMsg());
      iCaptureActivity.scanFinish(object);
    }else{
      iCaptureActivity.ToastCenter(codeInfoBean.getMsg());
      iCaptureActivity.reStartActivity();
    }
  }

  /**
   * 处理RECEIVE逻辑
   */
  private void doReceiveBiz(CodeInfoBean codeInfoBean, String response) {
    iCaptureActivity.ToastCenter(codeInfoBean.getMsg());
    iCaptureActivity.reStartActivity();
  }

}
