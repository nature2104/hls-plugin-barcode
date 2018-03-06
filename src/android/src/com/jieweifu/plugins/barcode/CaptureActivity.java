package com.jieweifu.plugins.barcode;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import __ANDROID_PACKAGE__.R;

import com.jieweifu.plugins.barcode.Util.Util;
import com.jieweifu.plugins.barcode.activity.DialogToast;
import com.jieweifu.plugins.barcode.activity.NetWorkErrorActivity;
import com.jieweifu.plugins.barcode.activity.UnOpenWebActivity;
import com.jieweifu.plugins.barcode.bean.ArgsBean;
import com.jieweifu.plugins.barcode.camera.CameraManager;
import com.jieweifu.plugins.barcode.decode.CaptureActivityHandler;
import com.jieweifu.plugins.barcode.decode.InactivityTimer;
import com.jieweifu.plugins.barcode.http.HttpManager;
import com.jieweifu.plugins.barcode.http.ResultCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CaptureActivity extends Activity implements Callback, ICaptureActivity, View.OnClickListener {
    private final static String TAG = "CaptureActivity";
    private ProgressDialog progressDialog;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.50f;
    private boolean vibrate;
    private int x = 0;
    private int y = 0;
    private int cropWidth = 0;
    private int cropHeight = 0;
    private RelativeLayout mContainer = null;
    private RelativeLayout mCropLayout = null;
    private TextView tv_type = null;
    private TextView tv_lable1 = null;
    private TextView tv_lable2 = null;
    private TextView tv_scanTip = null;
    private LinearLayout lytInput = null;
    private LinearLayout lytLight = null;
    private TextView mTipLight;
    private ImageView mImgLight;
    private ImageView imgTip = null;
    private CapturePresenter capturePresenter;
    private ArgsBean argsBean;
    private DialogToast dialogToast;
    private boolean finishFlag;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public void setCropWidth(int cropWidth) {
        this.cropWidth = cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.cropHeight = cropHeight;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);
        capturePresenter = new CapturePresenter(this, this);
        readIntent();
        // 初始化 CameraManager
        CameraManager.init(getApplication());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
        mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_lable1 = (TextView) findViewById(R.id.txt_tip1);
        tv_lable2 = (TextView) findViewById(R.id.txt_tip2);
        tv_scanTip = (TextView) findViewById(R.id.txtScanTip);
        lytInput = (LinearLayout) findViewById(R.id.lyt_input);
        lytLight = (LinearLayout) findViewById(R.id.lyt_light);
        imgTip = (ImageView) findViewById(R.id.img_tip);
        mTipLight = (TextView) findViewById(R.id.txt_light);
        mImgLight = (ImageView) findViewById(R.id.img_light);
        initData();
        ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        mQrLineView.startAnimation(animation);
        setButtonListeners();
    }

    private void readIntent() {
        argsBean = getIntent().getParcelableExtra("ARGS");
        flag = getIntent().getBooleanExtra("light", true);
        if (!flag) {
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    CameraManager.get().openLight();
                }
            });
        }
    }

    private void initData() {
        tv_type.setText(argsBean.getPageInfoBean().getTitle());
        tv_lable1.setText(argsBean.getPageInfoBean().getFooterFirst());
        tv_lable2.setText(argsBean.getPageInfoBean().getFooterSecond());
        tv_scanTip.setText(argsBean.getPageInfoBean().getTipScan());
        if (argsBean.getPageInfoBean().isExplainFlag()) {
            imgTip.setVisibility(View.VISIBLE);
        } else {
            imgTip.setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.txt_title)).setText(argsBean.getPageInfoBean().getBarTitle());
        if (argsBean.getPageInfoBean().isInputFlag()) {
            lytInput.setVisibility(View.VISIBLE);
        } else {
            lytInput.setVisibility(View.GONE);
        }
        if (argsBean.getPageInfoBean().isLightFlag()) {
            lytLight.setVisibility(View.VISIBLE);
        } else {
            lytLight.setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.txt_input)).setText(argsBean.getPageInfoBean().getTipInput());
        mTipLight.setText(argsBean.getPageInfoBean().getTipLightOn() + "");
    }

    boolean flag = true;

    protected void light() {
        if (flag == true) {
            flag = false;
            CameraManager.get().openLight();
            mTipLight.setText(argsBean.getPageInfoBean().getTipLightOff() + "");
            mImgLight.setImageDrawable(getResources().getDrawable(R.drawable.light_close));
            mTipLight.setTextColor(Color.parseColor("#1978d2"));

        } else {
            flag = true;
            CameraManager.get().offLight();
            mTipLight.setText(argsBean.getPageInfoBean().getTipLightOn() + "");
            mImgLight.setImageDrawable(getResources().getDrawable(R.drawable.light));
            mTipLight.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    protected void setButtonListeners() {

        findViewById(R.id.imgBtnBack).setOnClickListener(this);
        lytInput.setOnClickListener(this);
        lytLight.setOnClickListener(this);
        findViewById(R.id.rel_tip).setOnClickListener(this);

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JSONObject object = new JSONObject();
        try {
            object.put("type", "CLOSE");
            object.put("message", "{}");
            BarcodePlugin.cbContext.success(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("huangjie", "回掉");
        BarcodePlugin.cbContext.success(object);
        finish();
    }

    @Override
    public void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    public void handleDecode(final String result) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        if (ArgsBean.SCAN.equals(argsBean.getOperateMethod())) {
            JSONObject object = new JSONObject();
            try {
                object.put("type", ArgsBean.SCAN);
                JSONObject messageObject = new JSONObject();
                messageObject.put("code", result);
                object.put("message", messageObject);
                Log.e(TAG, object.toString() + "");
                scanFinish(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        } else if (argsBean.getHttpInfos() == null) {
            BarcodePlugin.cbContext.success(result);
            finish();
            return;
        } else
            capturePresenter.toValidation(argsBean, result);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);

            Point point = CameraManager.get().getCameraResolution();
            int width = point.y;
            int height = point.x;

            int x = mCropLayout.getLeft() * width / mContainer.getWidth();
            int y = mCropLayout.getTop() * height / mContainer.getHeight();

            int cropWidth = mCropLayout.getWidth() * width
                    / mContainer.getWidth();
            int cropHeight = mCropLayout.getHeight() * height
                    / mContainer.getHeight();

            setX(x);
            setY(y);
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);

        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(CaptureActivity.this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public Handler getHandler() {
        return handler;
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 将后端数据返回给前端并退出
     *
     * @param result
     */
    @Override
    public void scanFinish(final JSONObject result) {
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BarcodePlugin.cbContext.success(result);
                finish();
            }
        }, 700);
    }

    @Override
    public void ToastCenter(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showProgressDialog(boolean isShow) {
        if (dialogToast == null && isShow) {
            DialogToast.Builder builder = new DialogToast.Builder(this);
            dialogToast = builder.setMsg(argsBean.getPageInfoBean().getTipLoading())
                    .setOnClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            reStartActivity();
                        }
                    }).create();
            dialogToast.show();
        } else if (isShow) {
            dialogToast.show();
        } else if (!isShow && dialogToast != null) {
            dialogToast.dismiss();
        }

    }

    @Override
    public void goNetworkErrorPage() {
        Intent intent = new Intent(this, NetWorkErrorActivity.class);
        intent.putExtra("ARGS", argsBean);
        startActivity(intent);
    }

    @Override
    public void goUnOpenWebPage() {
        Intent intent = new Intent(this, UnOpenWebActivity.class);
        intent.putExtra("ARGS", argsBean);
        startActivity(intent);
    }

    @Override
    public void reStartActivity() {
        Intent intent = getIntent();
        intent.putExtra("light", flag);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void input() {
        JSONObject object = new JSONObject();
        try {
            object.put("type", "INPUT");
            object.put("message", "{}");
            BarcodePlugin.cbContext.success(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finish();
    }


    @Override
    public void finish() {
        if (argsBean.getPageInfoBean().getDelayTime() != 0 && !finishFlag) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    finishFlag = true;
                    CaptureActivity.this.finish();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, argsBean.getPageInfoBean().getDelayTime());
        } else {
            super.finish();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imgBtnBack:
                onBackPressed();
                break;
            case R.id.lyt_input:
                input();
                break;
            case R.id.lyt_light:
                light();
                break;
            case R.id.rel_tip:
                JSONObject object = new JSONObject();
                try {
                    object.put("type", "EXPLAIN");
                    object.put("message", new JSONObject());
                    scanFinish(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
