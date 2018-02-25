package cn.yiya.shiji.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import cn.yiya.shiji.R;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.HttpOptions;
import cn.yiya.shiji.utils.MyPreference;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.dialog.Effectstype;
import cn.yiya.shiji.views.dialog.NiftyDialogBuilder;


/**
 * Created by chenjian on 2016/5/27.
 */
public abstract class BaseFragment extends Fragment {

    int mScreenWidth;
    int mScreenHeight;
    public Dialog progressDialog;
    public Context mContext;
    public Tracker mTracker;

    // 无内容和无网络背景
    private RelativeLayout rlytDefaultNullView;
    private TextView tvDefaultNull;
    private LinearLayout llytDefaultOffNet;
    private TextView tvReload;
    private FrameLayout parent;

    // 掉接口跳转的按钮连续点击2秒内被认为是一次
    public static final int MIN_CLICK_DELAY_TIME = 2000;
    public long lastClickTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mContext = BaseApplication.getInstance();
        mTracker = ((BaseApplication)getActivity().getApplication()).getDefaultTracker();
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

    public void showPreDialog(String str, boolean bTouchCancel) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = ProgressDialog.creatRequestDialog(getActivity(), str, bTouchCancel);
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
        progressDialog = ProgressDialog.creatRequestDialog(getActivity(), str, false);
        progressDialog.show();
    }

    public void hidePreDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //右进左出动画
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void showToast(String str){
        if (getActivity() != null) {
            Toast.makeText(getActivity(),str, Toast.LENGTH_SHORT).show();
        }
    }
    public void showToast(int strId){
        if (getActivity() != null){
            Toast.makeText(getActivity(),strId, Toast.LENGTH_SHORT).show();
        }
    }

    /** Debug输出Log日志 **/
    protected void showLogDebug(String tag, String msg)
    {
        if (HttpOptions.ShowLog) {
            Log.d(tag, msg);
        }
    }

    /** Error输出Log日志 **/
    protected void showLogError(String tag, String msg)
    {
        if (HttpOptions.ShowLog) {
            Log.e(tag, msg);
        }
    }

    /** 含有标题、内容、图标、两个按钮的对话框 **/
    protected AlertDialog showMutiAlertDialog(String title, String message,
                                              int icon, String positiveText,
                                              DialogInterface.OnClickListener onPositiveClickListener,
                                              String negativeText,
                                              DialogInterface.OnClickListener onNegativeClickListener){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle(title)
                .setMessage(message).setIcon(icon)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .show();
        return alertDialog;
    }

    // 一个确定按钮的对话框
    protected void showSingleDialog(String msg, DialogInterface.OnClickListener onPositiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        builder.setTitle("提示");
        builder.setPositiveButton("确定", onPositiveClickListener);
        builder.create().show();
    }

    // 两个按钮的对话框
    protected AlertDialog showSimpleDialog(String msg, DialogInterface.OnClickListener onPositiveClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setMessage(msg)
                .setPositiveButton("确定", onPositiveClickListener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .show();
        return alertDialog;
    }

    // 显示自定义对话框
    protected NiftyDialogBuilder showCustomDialog(String title, String message, String positiveText,
                                                  final ProgressDialog.DialogClick listenerSecond,
                                                  String negativeText,
                                                  final ProgressDialog.DialogClick listenerFirst) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
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
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
        dialogBuilder
                .withTitle("提示")                                  //.withTitle(null)  no title
                .withTitleColor("#333333")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage(message)                     //.withMessage(null)  no Msg
                .withMessageColor("#333333")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)                               //def
//                .withIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher))
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withDuration(400)                                          //def
                .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                .withButton1Text("取消")
                .withButton2Text("确定")
//                .setCustomView(R.layout.custom_view, getActivity())
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
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
        dialogBuilder
                .withTitle("提示")                                  //.withTitle(null)  no title
                .withTitleColor("#333333")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage(message)                     //.withMessage(null)  no Msg
                .withMessageColor("#333333")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)                               //def
//                .withIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher))
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withDuration(400)                                          //def
                .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                .withButton2Text("确定")
//                .setCustomView(R.layout.custom_view, getActivity())
                .setButton1Click(new View.OnClickListener() {
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

    public void addGuide(final LinearLayout llytTips, String spName){
        if (!MyPreference.takeSharedPreferences(getActivity(), spName)) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setStartOffset(500);
            llytTips.setVisibility(View.VISIBLE);
            llytTips.setAnimation(alphaAnimation);
            alphaAnimation.startNow();
            MyPreference.saveSharedPreferences(getActivity(), spName, true);
            final Handler mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 10:
                            if(llytTips.getVisibility() == View.VISIBLE) {
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

    /**初始化无内容和无网络控件
     * @param mView
     */
    public void setMainView(View mView){
        rlytDefaultNullView = (RelativeLayout) mView.findViewById(R.id.rlyt_default_null_view);
        tvDefaultNull = (TextView) mView.findViewById(R.id.tv_default_null);
        llytDefaultOffNet = (LinearLayout) mView.findViewById(R.id.llyt_off_net);
        tvReload = (TextView) mView.findViewById(R.id.tv_reload);
    }

    // 添加无内容和无网路图
    public void addDefaultNullView(){
        rlytDefaultNullView = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.default_null_layout, null);
        tvDefaultNull = (TextView) rlytDefaultNullView.findViewById(R.id.tv_default_null);
        llytDefaultOffNet = (LinearLayout) rlytDefaultNullView.findViewById(R.id.llyt_off_net);
        tvReload = (TextView) rlytDefaultNullView.findViewById(R.id.tv_reload);
        View view = getActivity().getWindow().getDecorView().findViewById(R.id.layout_root);
        if(view == null){
            return;
        }
        ViewParent viewParent = view.getParent();
        if(viewParent instanceof FrameLayout){

            parent = (FrameLayout)viewParent;
            parent.addView(rlytDefaultNullView);
        }
    }

    // 初始化无内容和无网络提示图标、提示信息和重新加载点击事件
    public void initDefaultNullView(int drawableId, String tips, View.OnClickListener onClickListener){
        tvDefaultNull.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
        tvDefaultNull.setText(tips);
        tvReload.setOnClickListener(onClickListener);
    }

    // 初始化无内容和无网络提示图标、提示信息和重新加载点击事件 (可重新设置背景图上下位置)
    public void initDefaultNullView(int drawableId, String tips, View.OnClickListener onClickListener, int tvNullBottom, int llytOffNetBottom){
        tvDefaultNull.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
        tvDefaultNull.setText(tips);
        if(tvNullBottom != 0) {
            tvDefaultNull.setPadding(0, 0, 0, tvNullBottom);
        }
        if(llytOffNetBottom != 0){
            llytDefaultOffNet.setPadding(0, 0, 0, llytOffNetBottom);
        }
        tvReload.setOnClickListener(onClickListener);
    }

    // 设置无内容背景
    public void setNullView(View contentView){
        rlytDefaultNullView.setVisibility(View.VISIBLE);
        tvDefaultNull.setVisibility(View.VISIBLE);
        llytDefaultOffNet.setVisibility(View.GONE);
        if(contentView!=null) {
            contentView.setVisibility(View.GONE);
        }
    }

    // 设置成功获取数据状态
    public void setSuccessView(View contentView){
        rlytDefaultNullView.setVisibility(View.GONE);
        if(contentView!=null) {
            contentView.setVisibility(View.VISIBLE);
        }
    }

    // 设置无网路背景
    public void setOffNetView(View contentView){
        rlytDefaultNullView.setVisibility(View.VISIBLE);
        tvDefaultNull.setVisibility(View.GONE);
        llytDefaultOffNet.setVisibility(View.VISIBLE);
        if(contentView!=null) {
            contentView.setVisibility(View.GONE);
        }
    }

    // 按钮连续两次点击在2秒内认为是一次点击，多次的点击被视为无效
    public boolean isEffectClick(){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if((currentTime - lastClickTime) > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return true;
        }
        return false;
    }
}
