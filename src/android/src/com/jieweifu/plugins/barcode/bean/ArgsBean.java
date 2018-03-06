package com.jieweifu.plugins.barcode.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by panx on 2017/3/15.
 */
public class ArgsBean implements Parcelable {
  public final static String REVIEW = "REVIEW";
  public final static String AUDIT = "AUDIT";
  public final static String BACK = "BACK";
  public final static String AUDIT_PASS = "AUDIT_PASS";
  public final static String RECEIVE ="RECEIVE";
  public final static String SCAN ="SCAN";
  public final static String INVOICE = "INVOICE";
  private PageInfoBean pageInfoBean;
  private String operateMethod;
  private ArrayList<HttpInfoBean> httpInfos;

  public ArgsBean() {

  }

  protected ArgsBean(Parcel in) {
    pageInfoBean = in.readParcelable(PageInfoBean.class.getClassLoader());
    operateMethod = in.readString();
    httpInfos = in.createTypedArrayList(HttpInfoBean.CREATOR);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(pageInfoBean, flags);
    dest.writeString(operateMethod);
    dest.writeTypedList(httpInfos);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ArgsBean> CREATOR = new Creator<ArgsBean>() {
    @Override
    public ArgsBean createFromParcel(Parcel in) {
      return new ArgsBean(in);
    }

    @Override
    public ArgsBean[] newArray(int size) {
      return new ArgsBean[size];
    }
  };

  public PageInfoBean getPageInfoBean() {
    return pageInfoBean;
  }

  public void setPageInfoBean(PageInfoBean pageInfoBean) {
    this.pageInfoBean = pageInfoBean;
  }

  public String getOperateMethod() {
    return operateMethod;
  }

  public void setOperateMethod(String operateMethod) {
    this.operateMethod = operateMethod;
  }

  public ArrayList<HttpInfoBean> getHttpInfos() {
    return httpInfos;
  }

  public void setHttpInfos(ArrayList<HttpInfoBean> httpInfos) {
    this.httpInfos = httpInfos;
  }
}
