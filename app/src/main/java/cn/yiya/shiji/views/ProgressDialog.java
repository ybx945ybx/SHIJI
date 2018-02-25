package cn.yiya.shiji.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.VersionData;
import cn.yiya.shiji.receiver.DownloadAppService;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.dialog.Effectstype;
import cn.yiya.shiji.views.dialog.NiftyDialogBuilder;
import cn.yiya.shiji.views.dialog.SystemAlertDialogBuilder;

/**
 * Created by chenjian on 2015/9/28.
 */
public class ProgressDialog {

    static NiftyDialogBuilder dialogBuilder;

    /**
     * 请求对话框，进度对话框
     *
     * @param context    当前上下文
     * @param strTip     进度提示文字
     * @param bTouchMiss 触摸对话框外是否消失
     * @return
     */
    public static Dialog creatRequestDialog(final Context context, String strTip, boolean bTouchMiss) {

        final Dialog requestDialog = new Dialog(context, R.style.dialog);
        requestDialog.setContentView(R.layout.progressbar_style);
        requestDialog.setCanceledOnTouchOutside(bTouchMiss);
        Window window = requestDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        lp.width = (int) (0.5 * width);

        TextView tvLoad = (TextView) requestDialog.findViewById(R.id.tv_load);
        if (strTip == null || strTip.length() == 0) {
            tvLoad.setText("正在发送请求");
        } else {
            tvLoad.setText(strTip);
        }
        return requestDialog;
    }

    public static NiftyDialogBuilder showCustomDialog(Context context, String title, String message, String positiveText,
                                                      View.OnClickListener secondListner,
                                                      String negativeText,
                                                      View.OnClickListener firstListener) {
        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
        dialogBuilder
                .withTitle(title)                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage(message)                     //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)                               //def
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                .withButton1Text(negativeText)
                .withButton2Text(positiveText)
                .setCustomView(R.layout.custom_view, context)
                .setButton1Click(firstListener)
                .setButton2Click(secondListner)
                .show();
        return dialogBuilder;
    }

    /**
     * 版本升级
     *
     * @param versionData 当前版本信息
     * @return
     */
    public static NiftyDialogBuilder showUpdateVersionDialog(final Context context, final VersionData versionData, final int type) {

        boolean bForce = versionData.getAndroid().getForce() > 0;
        String negative = bForce ? "退出程序" : "取消升级";
        String title = bForce ? "版本升级(重要版本更新)" : "版本升级" + "(" + versionData.getAndroid().getVersion() + ")";
        dialogBuilder = showCommonScrollDialog(context, title, versionData.getAndroid().getDesc(),
                "确定升级", negative, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean bForce = versionData.getAndroid().getForce() > 0;
                        BaseApplication.bUpdate = false;
                        if (bForce) {
                            ((Activity) context).finish();
                            if (type > 0) {
                                System.exit(0);
                                android.os.Process.killProcess(android.os.Process.myPid());
                            } else {
                                BaseApplication.bRefuse = true;
                                if (dialogBuilder != null) {
                                    dialogBuilder.dismiss();
                                    dialogBuilder = null;
                                }
                            }
                        } else {
                            if (dialogBuilder != null) {
                                dialogBuilder.dismiss();
                                dialogBuilder = null;
                            }
                        }
                    }
                }, new View.OnClickListener(){
                @Override
                public void onClick (View v){
                    BaseApplication.bRefuse = false;
                    BaseApplication.bDownLoadApp = true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        try {

                            Intent intent = new Intent(context, DownloadAppService.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("download_url", versionData.getAndroid().getUrl());
                            context.startService(intent);
                        } catch (Exception e) {
                            Util.toast(context, "手机下载器未打开，请退出App打开", true);
                            e.printStackTrace();
                        }
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(versionData.getAndroid().getUrl());
                        intent.setData(content_url);
                        context.startActivity(intent);
                    }

                    if (dialogBuilder != null) {
                        dialogBuilder.dismiss();
                        dialogBuilder = null;
                    }
            }
        });

        return dialogBuilder;
    }

    // 文字能滚动的对话框
    public static NiftyDialogBuilder showCommonScrollDialog(Context context,
                                                            String title, String contentStr, String resPositiveButtonId,
                                                            String resNegativeButtonId,
                                                            View.OnClickListener firstListener, View.OnClickListener secondListner) {
        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
        dialogBuilder
                .withTitle(title)                                  //.withTitle(null)  no title
                .withTitleColor("#333333")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage(contentStr)                     //.withMessage(null)  no Msg
                .withMessageColor("#333333")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)                               //def
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withDuration(400)                                          //def
                .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                .withButton1Text(resNegativeButtonId)
                .withButton2Text(resPositiveButtonId)
                .setButton1Click(firstListener)
                .setButton2Click(secondListner)
                .isCancelable(false)
                .show();
        return dialogBuilder;
    }

    public static NiftyDialogBuilder showCommonScrollDialogCancel(Context context,
                                                                  String title, String contentStr, String resNegativeButtonId,
                                                                  String resPositiveButtonId,
                                                                  final DialogClick listener) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
        dialogBuilder
                .withTitle(title)                                  //.withTitle(null)  no title
                .withTitleColor("#333333")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage(contentStr)                     //.withMessage(null)  no Msg
                .withMessageColor("#333333")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)                               //def
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withDuration(400)                                          //def
                .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                .withButton1Text(resNegativeButtonId)
                .withButton2Text(resPositiveButtonId)
//                .setCustomView(R.layout.common_scroll_dialog, context)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
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

    public static NiftyDialogBuilder showCustomSingleDialog(Context mContext, String message, final DialogClick listener) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(mContext);
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

    public static NiftyDialogBuilder showCustomSingleDialogTip(Context mContext, String message) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(mContext);
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
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .show();
        return dialogBuilder;
    }

    public static AlertDialog.Builder getAlertDialogBuilder(Context context, int theme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return new AlertDialog.Builder(context, theme);
        } else {
            return new AlertDialog.Builder(context);
        }
    }

    // 显示对话框
    public static void showDialog(Context context, String title, final DialogCallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(title);
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.clickCall();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public interface DialogCallBack {
        public void clickCall();
    }

    // 闪购提示对话框(系统弹出对话框)
    public static SystemAlertDialogBuilder showCommonFlashSaleDialogCancel(Context context, String contentStr, String resNegativeButtonId,
                                                                     String resPositiveButtonId,
                                                                     final DialogClick listener) {
        final SystemAlertDialogBuilder dialogBuilder = SystemAlertDialogBuilder.getInstance(context);
        dialogBuilder
                .withTitle("提示")                                        //.withTitle(null)  no title
                .withTitleColor("#333333")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage(contentStr)                                    //.withMessage(null)  no Msg
                .withMessageColor("#333333")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)                               //def
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withDuration(400)                                          //def
                .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                .withButton1Text(resNegativeButtonId)
                .withButton2Text(resPositiveButtonId)
//                .setCustomView(R.layout.common_scroll_dialog, context)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
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

    public interface DialogClick{
        void OkClick();
        void CancelClick();
    }
}
