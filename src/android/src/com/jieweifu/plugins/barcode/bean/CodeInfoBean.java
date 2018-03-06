package com.jieweifu.plugins.barcode.bean;

/**
 * Created by panx on 2017/3/9.
 */
public class CodeInfoBean {
  public final static String SUCCESS = "0000";
  public final static String CODE = "code";
  public final static String MSG = "msg";
  public final static String ONLINE = "online";
  public final static String EXPENSE_REPORT = "expenseReport";

  private String code;
  private String msg;
  private String online;
  private String expenseReport;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getOnline() {
    return online;
  }

  public void setOnline(String online) {
    this.online = online;
  }

  public String getExpenseReport() {
    return expenseReport;
  }

  public void setExpenseReport(String expenseReport) {
    this.expenseReport = expenseReport;
  }
}
