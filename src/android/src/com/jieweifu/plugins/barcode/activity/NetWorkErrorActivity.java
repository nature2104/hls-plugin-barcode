package com.jieweifu.plugins.barcode.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import __ANDROID_PACKAGE__.R;
import com.jieweifu.plugins.barcode.bean.ArgsBean;

/**
 * Created by panx on 2017/3/15.
 */
public class NetWorkErrorActivity extends Activity implements View.OnClickListener{
  private ArgsBean argsBean;

  private TextView txtTipNetworkError;
  private TextView txtTip1;
  private TextView txtTip2;
  private ImageView imgBtnBack;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_network_error);
    readIntent();
    initView();
    initListener();
    initData();
  }

  private void readIntent(){
    argsBean = getIntent().getParcelableExtra("ARGS");
  }

  private void initView(){
    txtTipNetworkError = (TextView)findViewById(R.id.txt_tipNetworkError);
    txtTip1 = (TextView)findViewById(R.id.txt_tip1);
    txtTip2 = (TextView)findViewById(R.id.txt_tip2);
    imgBtnBack = (ImageView) findViewById(R.id.imgBtnBack);
  }

  private void initListener(){
    imgBtnBack.setOnClickListener(this);
  }

  private void initData(){
    txtTipNetworkError.setText(argsBean.getPageInfoBean().getTipNetworkError());
    txtTip1.setText(argsBean.getPageInfoBean().getFooterFirst());
    txtTip2.setText(argsBean.getPageInfoBean().getFooterSecond());
  }

  @Override
  public void onClick(View view) {
    int id = view.getId();
    switch (id){
      case R.id.imgBtnBack:{
        onBackPressed();
        break;
      }default:
        break;
    }
  }
}
