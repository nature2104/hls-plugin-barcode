package com.jieweifu.plugins.barcode.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import __ANDROID_PACKAGE__.R;


/**
 * Created by panx on 2017/3/17.
 */
public class DialogToast extends Dialog {
    public DialogToast(Context context) {
        super(context);
    }

    protected DialogToast(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public DialogToast(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String msg;
        private OnClickListener onClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMsg(String msg){
            this.msg = msg;
            return this;
        }

        public Builder setOnClickListener(OnClickListener onClickListener){
            this.onClickListener = onClickListener;
            return this;
        }

        public DialogToast create(){
            LayoutInflater inflater = LayoutInflater.from(context);
            final DialogToast dialogToast = new DialogToast(context,R.style.Custom_Progress);
            View view = inflater.inflate(R.layout.dialog_cancel_layout,null);
            dialogToast.setContentView(view);
            TextView txtMsg =(TextView) view.findViewById(R.id.txt_msg);
            ImageView imgCancel = (ImageView) view.findViewById(R.id.txt_cancel);
            if(msg!=null){
                txtMsg.setText(msg);
            }
            imgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(dialogToast, DialogInterface.BUTTON_NEGATIVE);
                }
            });
            WindowManager.LayoutParams lp = dialogToast.getWindow().getAttributes();
            // 设置背景层透明度
            lp.dimAmount = 0.0f;
            dialogToast.getWindow().setAttributes(lp);
            dialogToast.setCancelable(false);
            return dialogToast;
        }

    }
}
