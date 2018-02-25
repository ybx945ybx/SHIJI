package cn.yiya.shiji.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import cn.yiya.shiji.R;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.config.HttpOptions;
import cn.yiya.shiji.utils.MyPreference;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.dialog.Effectstype;
import cn.yiya.shiji.views.dialog.NiftyDialogBuilder;

/**
 * Created by chenjian on 2016/5/27.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {
    protected BaseApplication mApp;
    int mScreenWidth;
    int mScreenHeight;
    public Dialog progressDialog;

    // 无内容和无网络背景
    public RelativeLayout rlytDefaultNullView;
    public TextView tvDefaultNull;
    public LinearLayout llytDefaultOffNet;
    public TextView tvReload;
    public TextView tvShopping;
    public FrameLayout parent;

    // 掉接口跳转的按钮连续点击2秒内被认为是一次
    public static final int MIN_CLICK_DELAY_TIME = 2000;
    public long lastClickTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = BaseApplication.getInstance();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;


    }

    // 添加无内容和无网路图
    public void addDefaultNullView() {
        rlytDefaultNullView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.default_null_layout, null);
        tvDefaultNull = (TextView) rlytDefaultNullView.findViewById(R.id.tv_default_null);
        tvShopping = (TextView) rlytDefaultNullView.findViewById(R.id.tv_shopping);
        llytDefaultOffNet = (LinearLayout) rlytDefaultNullView.findViewById(R.id.llyt_off_net);
        tvReload = (TextView) rlytDefaultNullView.findViewById(R.id.tv_reload);
        View view = getWindow().getDecorView().findViewById(R.id.layout_root);
        if (view == null) {
            return;
        }
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof FrameLayout) {

            parent = (FrameLayout) viewParent;
            parent.addView(rlytDefaultNullView);
        }
    }

    // 只初始化无网络图
    public void initOffLineView(View.OnClickListener onClickListener) {
        tvReload.setOnClickListener(onClickListener);
    }

    /**
     * 初始化视图
     **/
    protected abstract void initViews();

    /**
     * 初始化事件
     **/
    protected abstract void initEvents();

    /**
     * 初始化数据
     **/
    protected abstract void init();

    @Override
    protected void onResume() {
        //友盟统计
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //友盟统计
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
        super.onPause();
    }

    public void showPreDialog(String str, boolean bTouchCancel) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = ProgressDialog.creatRequestDialog(this, str, bTouchCancel);
        progressDialog.show();
    }

    /**
     * 加载前对话框
     *
     * @param str 提示文字，默认点击不消失
     */
    public void showPreDialog(String str) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = ProgressDialog.creatRequestDialog(this, str, false);
        progressDialog.show();
    }

    public void hidePreDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void hidePreDialog(Context context) {
        //is it the same context from the caller ?
        Log.w("ProgressDIalog dismiss", "the dialog is from"+progressDialog.getContext());

        Class caller_context= context.getClass();
        Activity call_Act = (Activity)context;
        Class progress_context= progressDialog.getContext().getClass();

        Boolean is_act= ( (progressDialog.getContext()) instanceof  Activity )?true:false;
        Boolean is_ctw= ( (progressDialog.getContext()) instanceof ContextThemeWrapper)?true:false;

        if (is_ctw) {
            ContextThemeWrapper cthw=(ContextThemeWrapper) progressDialog.getContext();
            Boolean is_same_acivity_with_Caller= ((Activity)(cthw).getBaseContext() ==  call_Act )?true:false;

            if (is_same_acivity_with_Caller){
                progressDialog.dismiss();
                progressDialog = null;
            }
            else {
                Log.e("ProgressDIalog dismiss", "the dialog is NOT from the same context! Can't touch.."+((Activity)(cthw).getBaseContext()).getClass());
                progressDialog = null;
            }
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //右进左出动画
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onStop() {
        mApp.saveSession();
        mApp.saveCookie();
        super.onStop();
    }

    public void showTips(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Debug输出Log日志
     **/
    protected void showLogDebug(String tag, String msg) {
        if (HttpOptions.ShowLog) {
            Log.d(tag, msg);
        }
    }

    /**
     * Error输出Log日志
     **/
    protected void showLogError(String tag, String msg) {
        if (HttpOptions.ShowLog) {
            Log.e(tag, msg);
        }
    }

    /**
     * 含有标题、内容、图标、两个按钮的对话框
     **/
    protected AlertDialog showMutiAlertDialog(String title, String message,
                                              int icon, String positiveText,
                                              DialogInterface.OnClickListener onPositiveClickListener,
                                              String negativeText,
                                              DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message).setIcon(icon)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .show();
        return alertDialog;
    }

    // 一个确定按钮的对话框
    protected void showSingleDialog(String msg, DialogInterface.OnClickListener onPositiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle("提示");
        builder.setPositiveButton("确定", onPositiveClickListener);
        builder.create().show();
    }

    // 两个按钮的对话框
    protected AlertDialog showSimpleDialog(String msg, DialogInterface.OnClickListener onPositiveClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("确定", onPositiveClickListener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
        return alertDialog;
    }

    // 显示自定义对话框
    protected NiftyDialogBuilder showCustomDialog(String title, String message, String positiveText,
                                                  final ProgressDialog.DialogClick listenerSecond,
                                                  String negativeText,
                                                  final ProgressDialog.DialogClick listenerFirst) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withTitle(title)                                  //.withTitle(null)  no title
                .withTitleColor("#333333")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage(message)                     //.withMessage(null)  no Msg
                .withMessageColor("#333333")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)                               //def
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withDuration(400)                                          //def
                .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                .withButton1Text(negativeText)
                .withButton2Text(positiveText)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        if (listenerFirst != null) {
                            listenerFirst.CancelClick();
                        }
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        if (listenerSecond != null) {
                            listenerSecond.OkClick();
                        }
                    }
                })
                .show();
        return dialogBuilder;
    }

    // 显示自定义带确定和取消按钮的对话框
    protected NiftyDialogBuilder showCustomMutiDialog(String message, final ProgressDialog.DialogClick listener) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withTitle("提示")                                  //.withTitle(null)  no title
                .withTitleColor("#333333")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage(message)                     //.withMessage(null)  no Msg
                .withMessageColor("#333333")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)                               //def
//                .withIcon(ContextCompat.getDrawable(this, R.drawable.ic_launcher))
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withDuration(400)                                          //def
                .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                .withButton1Text("取消")
                .withButton2Text("确定")
//                .setCustomView(R.layout.custom_view, this)
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        if (listener != null) {
                            listener.OkClick();
                        }
                    }
                })
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .show();
        return dialogBuilder;
    }

    // 只显示确定按钮的对话框
    protected NiftyDialogBuilder showCustomSingleDialog(String message, final ProgressDialog.DialogClick listener) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withTitle("提示")                                  //.withTitle(null)  no title
                .withTitleColor("#333333")                                  //def
                .withDividerColor("#10000000")                              //def
                .withMessage(message)                     //.withMessage(null)  no Msg
                .withMessageColor("#333333")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFFFFFFF")                               //def  | withDialogColor(int resid)                               //def
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withDuration(400)                                          //def
                .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                .withButton2Text("确定")
//                .setCustomView(R.layout.custom_view, this)
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        if (listener != null) {
                            listener.OkClick();
                        }
                    }
                })
                .show();
        return dialogBuilder;
    }

    //添加操作引导
    public void addGuideImage(final View referenceViewUp, final View referenceViewDown, int upLayoutId, int downLayoutId, final int type, final String tipsUp, final String tipsDown, String key) {
        View view = getWindow().getDecorView().findViewById(R.id.layout_root);//查找通过setContentView上的根布局
        if (view == null) return;
        if (MyPreference.takeSharedPreferences(this, key)) {
            return;//引导过了
        }
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof FrameLayout) {
            final FrameLayout frameLayout = (FrameLayout) viewParent;
            LayoutInflater inflater = LayoutInflater.from(this);
            final RelativeLayout guideView = (RelativeLayout) inflater.inflate(R.layout.guide_tips, null);
            final View tipsUpView, tipsDownView;
            final ImageView ivArrowUp, ivArrowDown;
            final TextView tvTipsUp, tvTipsDown;
            //获取参照物控件的位置
            if (key.equals(MyPreference.WORK_DETAIL_FOLLOW_TAGS) && MyPreference.takeSharedPreferences(this, MyPreference.WORK_DETAIL_FOLLOW)) {
            } else {
                if (referenceViewUp != null && upLayoutId != 0) {
                    ViewTreeObserver vtoUp = referenceViewUp.getViewTreeObserver();
                    tipsUpView = inflater.inflate(upLayoutId, null);
                    ivArrowUp = (ImageView) tipsUpView.findViewById(R.id.guide_arrow);
                    tvTipsUp = (TextView) tipsUpView.findViewById(R.id.guide_tips);
                    guideView.addView(tipsUpView);
                    vtoUp.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            referenceViewUp.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            tvTipsUp.setText(tipsUp);
                            int[] location = new int[2];
                            referenceViewUp.getLocationInWindow(location);
                            int x = location[0];
                            int y = location[1];
                            int widt = referenceViewUp.getWidth();
                            int heigh = referenceViewUp.getHeight();
                            int tipsWidth = tvTipsUp.getWidth();
                            LinearLayout.LayoutParams llparamsArrow = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            llparamsArrow.leftMargin = (int) x + widt / 2 - ivArrowUp.getWidth() / 2;
                            ivArrowUp.setLayoutParams(llparamsArrow);

                            LinearLayout.LayoutParams llparamsTips = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            llparamsTips.leftMargin = (int) x + widt / 2 - ivArrowUp.getWidth() / 2 - tipsWidth * x / mScreenWidth;
                            tvTipsUp.setLayoutParams(llparamsTips);

                            RelativeLayout.LayoutParams reparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            reparams.topMargin = (int) y + heigh / 2;
                            tipsUpView.setLayoutParams(reparams);

                            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                            alphaAnimation.setFillAfter(true);
                            alphaAnimation.setDuration(1000);
                            alphaAnimation.setStartOffset(500);
                            tipsUpView.setVisibility(View.VISIBLE);
                            tipsUpView.setAnimation(alphaAnimation);
                            alphaAnimation.startNow();
                        }
                    });
                }
            }
            if (referenceViewDown != null && downLayoutId != 0) {
                ViewTreeObserver vtoDown = referenceViewDown.getViewTreeObserver();
                tipsDownView = inflater.inflate(downLayoutId, null);
                ivArrowDown = (ImageView) tipsDownView.findViewById(R.id.guide_arrow);
                tvTipsDown = (TextView) tipsDownView.findViewById(R.id.guide_tips);
                guideView.addView(tipsDownView);
                vtoDown.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        referenceViewDown.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        tvTipsDown.setText(tipsDown);
                        int[] location = new int[2];
                        referenceViewDown.getLocationInWindow(location);
                        int x = location[0];
                        int y = location[1];
                        int widt = referenceViewDown.getWidth();
                        int heigh = referenceViewDown.getHeight();
                        int tipsWidth = tvTipsDown.getWidth();
                        LinearLayout.LayoutParams llparamsArrow = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        llparamsArrow.leftMargin = (int) x + widt / 2 - ivArrowDown.getWidth() / 2;
                        ivArrowDown.setLayoutParams(llparamsArrow);

                        LinearLayout.LayoutParams llparamsTips = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        llparamsTips.leftMargin = (int) x + widt / 2 - ivArrowDown.getWidth() / 2 - tipsWidth * x / mScreenWidth;
                        tvTipsDown.setLayoutParams(llparamsTips);

                        RelativeLayout.LayoutParams reparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        reparams.topMargin = (int) y - heigh / 2 - tipsDownView.getHeight();
                        tipsDownView.setLayoutParams(reparams);

                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                        alphaAnimation.setFillAfter(true);
                        alphaAnimation.setDuration(1000);
                        alphaAnimation.setStartOffset(500);
                        tipsDownView.setVisibility(View.VISIBLE);
                        tipsDownView.setAnimation(alphaAnimation);
                        alphaAnimation.startNow();
                    }
                });
            }

            //点击屏幕引导消失
            guideView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (guideView.getVisibility() == View.VISIBLE) {
                        AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
                        alphaAnimation1.setFillAfter(true);
                        alphaAnimation1.setDuration(1000);
                        guideView.setVisibility(View.INVISIBLE);
                        guideView.setAnimation(alphaAnimation1);
                        alphaAnimation1.startNow();
                        frameLayout.removeView(guideView);
                    }
                    if (type == 1) {
                        if (referenceViewDown.getVisibility() == View.VISIBLE) {
                            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                            alphaAnimation.setFillAfter(true);
                            alphaAnimation.setDuration(1000);
                            referenceViewDown.setVisibility(View.INVISIBLE);
                            referenceViewDown.setAnimation(alphaAnimation);
                            alphaAnimation.startNow();
                        }
                    }
                }
            });

            if (type == 1) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setFillAfter(true);
                alphaAnimation.setDuration(1000);
                alphaAnimation.setStartOffset(500);
                referenceViewDown.setVisibility(View.VISIBLE);
                referenceViewDown.setAnimation(alphaAnimation);
                alphaAnimation.startNow();
            }

            frameLayout.addView(guideView);                                    // 添加引导图片
            MyPreference.saveSharedPreferences(this, key, true);        // 设置为已引导

            //显示引导3秒后自动消失
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 0:
                            if (guideView.getVisibility() == View.VISIBLE) {
                                AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
                                alphaAnimation1.setFillAfter(true);
                                alphaAnimation1.setDuration(1000);
                                guideView.setVisibility(View.INVISIBLE);
                                guideView.setAnimation(alphaAnimation1);
                                alphaAnimation1.startNow();
                            }

                            frameLayout.removeView(guideView);

                            if (type == 1) {
                                if (referenceViewDown.getVisibility() == View.VISIBLE) {
                                    AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                                    alphaAnimation.setFillAfter(true);
                                    alphaAnimation.setDuration(1000);
                                    referenceViewDown.setVisibility(View.INVISIBLE);
                                    referenceViewDown.setAnimation(alphaAnimation);
                                    alphaAnimation.startNow();
                                }
                            }
                            break;
                    }
                }
            };
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            }, 3000);
        }
    }

    public void addGuide(final LinearLayout llytTips, String spName) {
        if (!MyPreference.takeSharedPreferences(this, spName)) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setStartOffset(500);
            llytTips.setVisibility(View.VISIBLE);
            llytTips.setAnimation(alphaAnimation);
            alphaAnimation.startNow();
            MyPreference.saveSharedPreferences(this, spName, true);
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 10:
                            if (llytTips.getVisibility() == View.VISIBLE) {
                                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                                alphaAnimation.setFillAfter(true);
                                alphaAnimation.setDuration(1000);
                                llytTips.setVisibility(View.INVISIBLE);
                                llytTips.setAnimation(alphaAnimation);
                                alphaAnimation.startNow();
                            }
                            break;
                    }
                }
            };

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    mHandler.sendEmptyMessage(10);
                }
            }, 3000);
        }
    }

    // 初始化无内容和无网络提示图标、提示信息和重新加载点击事件
    public void initDefaultNullView(int drawableId, String tips, View.OnClickListener onClickListener) {
        tvDefaultNull.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
        tvDefaultNull.setText(tips);
        tvReload.setOnClickListener(onClickListener);
    }

    // 初始化无内容和无网络提示图标、提示信息和重新加载点击事件 (可重新设置背景图上下位置)
    public void initDefaultNullView(int drawableId, String tips, View.OnClickListener onClickListener, int tvNullBottom, int llytOffNetBottom) {
        tvDefaultNull.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
        tvDefaultNull.setText(tips);
        if (tvNullBottom != 0) {
            tvDefaultNull.setPadding(0, 0, 0, tvNullBottom);
        }
        if (llytOffNetBottom != 0) {
            llytDefaultOffNet.setPadding(0, 0, 0, llytOffNetBottom);
        }
        tvReload.setOnClickListener(onClickListener);
    }

    // 设置无内容背景
    public void setNullView(View contentView) {
        rlytDefaultNullView.setVisibility(View.VISIBLE);
        tvDefaultNull.setVisibility(View.VISIBLE);
        llytDefaultOffNet.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
    }

    // 设置成功获取数据状态
    public void setSuccessView(View contentView) {
        rlytDefaultNullView.setVisibility(View.GONE);
        if (contentView != null) {
            contentView.setVisibility(View.VISIBLE);
        }
    }

    // 设置无网路背景
    public void setOffNetView(View contentView) {
        rlytDefaultNullView.setVisibility(View.VISIBLE);
        tvDefaultNull.setVisibility(View.GONE);
        llytDefaultOffNet.setVisibility(View.VISIBLE);
        tvShopping.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        showTips(Configration.OFF_LINE_TIPS);
    }

    // 按钮连续两次点击在2秒内认为是一次点击，多次的点击被视为无效
    public boolean isEffectClick() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if ((currentTime - lastClickTime) > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return true;
        }
        return false;
    }
}
