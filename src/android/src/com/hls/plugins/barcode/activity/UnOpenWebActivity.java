package com.hls.plugins.barcode.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import __ANDROID_PACKAGE__.R;
import com.hls.plugins.barcode.Util.Util;
import com.hls.plugins.barcode.bean.ArgsBean;

/**
 * Created by panx on 2017/3/15.
 */
public class UnOpenWebActivity extends Activity implements View.OnClickListener,IUnOpenWebActivity {
  private ImageView imgBtnBack;
  private TextView txtTipOffline;
  private TextView txtOpen;
  private TextView txtTip1;
  private TextView txtTip2;
  private TextView scanTitile;
  private ArgsBean argsBean;
  private DialogToast dialogToast;
  private UnOpenWebActivityPresenter unOpenWebActivityPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_unopen_web);
    unOpenWebActivityPresenter = new UnOpenWebActivityPresenter(this,this);
    readIntent();
    initView();
    initData();
    initListener();
  }


  private void readIntent() {
    argsBean = getIntent().getParcelableExtra("ARGS");
  }

  private void initView() {
    txtTipOffline = (TextView) findViewById(R.id.txt_tipOffLine);
    txtOpen = (TextView) findViewById(R.id.txt_open);
    txtTip1 = (TextView) findViewById(R.id.txt_tip1);
    txtTip2 = (TextView) findViewById(R.id.txt_tip2);
    imgBtnBack = (ImageView) findViewById(R.id.imgBtnBack);
    scanTitile=(TextView) findViewById(R.id.scanTitile);

  }

  private void initData() {
    txtTipOffline.setText(argsBean.getPageInfoBean().getTipOffline());
    txtOpen.setText(argsBean.getPageInfoBean().getOpenButton());
    txtTip1.setText(argsBean.getPageInfoBean().getFooterFirst());
    txtTip2.setText(argsBean.getPageInfoBean().getFooterSecond());
    scanTitile.setText(argsBean.getPageInfoBean().getBarTitle());

  }

  private void initListener(){
    txtOpen.setOnClickListener(this);
    imgBtnBack.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    int id = view.getId();
    switch (id){
      case R.id.imgBtnBack:
        onBackPressed();
        break;
      case R.id.txt_open:
        unOpenWebActivityPresenter.toValidation(argsBean);
        break;
      default:
        break;
    }
  }

  /**
   * 扔未打开
   */
  @Override
  public void stillUnOpen(){
    ToastCenter(argsBean.getPageInfoBean().getTipOffline());
  }

  @Override
  public void hasOpen(){
    onBackPressed();
  }

  @Override
  public void ToastCenter(String msg) {
    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
  }

  @Override
  public void showProgressDialog(boolean isShow) {
    if(dialogToast == null&&isShow){
      DialogToast.Builder builder = new DialogToast.Builder(this);
      dialogToast = builder.setMsg(argsBean.getPageInfoBean().getTipLoading())
        .setOnClickListener(new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
          }
        }).create();
      dialogToast.show();
    }else if(isShow){
      dialogToast.show();
    }else if(!isShow&&dialogToast!=null){
      dialogToast.dismiss();
    }
  }

  @Override
  public void goNetworkErrorPage() {
    Intent intent = new Intent(this, NetWorkErrorActivity.class);
    intent.putExtra("ARGS",argsBean);
    startActivity(intent);
  }

}
