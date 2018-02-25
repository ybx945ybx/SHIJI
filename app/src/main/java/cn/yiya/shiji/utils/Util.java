/**
 * The MIT License (MIT)
 * Copyright (c) 2012-2014 唐虞科技(TangyuSoft) Corporation
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cn.yiya.shiji.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.BaseAppCompatActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.download.DownLoadAppListener;
import cn.yiya.shiji.entity.HtmlVersionInfo;
import cn.yiya.shiji.entity.LayerItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.dialog.Effectstype;
import cn.yiya.shiji.views.dialog.NiftyDialogBuilder;

/**
 * @author bin
 */
public class Util {

    private static BroadcastReceiver sdCardReceiver;
    private static boolean isSDCardReceiverRegister = false;
    private static boolean isSDCardAvailable = false;
    private static boolean isSDCardWriteable = false;
    private static Context saveContext;
    private static int nCount = 0;
    private static String IMAGEURL = "http://private.cdnqiniu02.qnmami.com/";
    private static String OTHERURL = "http://information.cdnqiniu02.qnmami.com/";


    private static long lastClickTime = 0;
    public static final int MIN_CLICK_DELAY_TIME = 1000;


    public static String transfer(String head) {
        if (!TextUtils.isEmpty(head)) {
            if (head.startsWith("http:")) {
                return head;
            } else {
                return IMAGEURL + head;
            }
        }
        return null;
    }

    // 按高度和宽度裁剪图片
//    public static String cropImageBywh(String path, int width, int heigh){
//        int[] wh = getImageWidthHeight(path);
//        int w = wh[0];
//        int h = wh[1];
//        String url = "";
//        if(w > h){
//            transferImage(path, width);
//        }else {
//            transferImageHeigh(path, heigh);
//        }
//        return url;
//    }

    // android在不加载图片的前提下获得图片的宽高
    public static void setImageWidthHeight(final SimpleDraweeView simpleDraweeView, final String path, final int width, final int heigh) {
        final String url = "";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL fileUrl = new URL(transUrl(path));
                    HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeFile(transUrl(path), options); // 此时返回的bitmap为null
                    is.close();
                    int w = options.outWidth;
                    int h = options.outHeight;
                    if (w > h) {
                        simpleDraweeView.setImageURI(transferImage(path, width));
                    } else {
                        simpleDraweeView.setImageURI(transferImageHeigh(path, heigh));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        return new int[]{options.outWidth,options.outHeight};
    }

    public static String transUrl(String head) {
        String url = "";
        if (!TextUtils.isEmpty(head)) {
            if (head.startsWith("http:")) {
                url = head;
            } else {
                url = IMAGEURL + head;
            }
        }
        return url;
    }

    // 缩放处理
    public static String transferImage(String head, int width) {
        String url = "";
        if (!TextUtils.isEmpty(head)) {
            if (head.startsWith("http:")) {
                url = head + "?imageMogr2/thumbnail/" + width + "x";
            } else {
                url = IMAGEURL + head + "?imageMogr2/thumbnail/" + width + "x";
            }
        }
        return url;
    }

    // 缩放处理
    public static String transferImageHeigh(String head, int heigh) {
        String url = "";
        if (!TextUtils.isEmpty(head)) {
            if (head.startsWith("http:")) {
                url = head + "?imageMogr2/thumbnail/" + "x" + heigh;
            } else {
                url = IMAGEURL + head + "?imageMogr2/thumbnail/" + "x" + heigh;
            }
        }
        return url;
    }


    // 限定缩略图宽，然后进行等比缩放，不裁剪
    public static String transferCropImage(String head, int width) {
        String url = "";
        if (!TextUtils.isEmpty(head)) {
            if (head.startsWith("http:")) {
                url = head + "?imageView2/2/w/" + width;
            } else {
                url = IMAGEURL + head + "?imageView2/2/w/" + width;
            }
        }
        return url;
    }

    // 限定缩略图高，然后进行等比缩放，不裁剪
    public static String transferCropImageHeigh(String head, int heigh) {
        String url = "";
        if (!TextUtils.isEmpty(head)) {
            if (head.startsWith("http:")) {
                url = head + "?imageView2/2/h/" + heigh;
            } else {
                url = IMAGEURL + head + "?imageView2/2/h/" + heigh;
            }
        }
        return url;
    }

    public static String transfer2(String head) {
        if (!TextUtils.isEmpty(head)) {
            if (head.startsWith("http:")) {
                return head;
            } else {
                return OTHERURL + head;
            }
        }
        return null;
    }

    public static void toast(Context ctx, String msg, boolean isShort) {
        Toast.makeText(ctx, msg,
                isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    public static void toast(Context ctx, int msgid, boolean isShort) {
        Toast.makeText(ctx, msgid,
                isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    public static boolean isNull(Object o) {
        return o == null ? true : false;
    }

    public static boolean isNull(List<?> list) {
        return list == null || list.size() == 0 ? true : false;
    }

    public static boolean isNull(String str) {
        return TextUtils.isEmpty(str) ? true : false;
    }

    /**
     * @param src
     * @param key
     * @return >= 0 is in array. negative value means not.
     */
    public static int isInArray(int[] src, int key) {
        if (isNull(src)) return -1;
        for (int i = 0; i < src.length; i++) {
            if (src[i] == key) {
                return i;
            }
        }
        return -1;
    }

    /**
     * find param2 in param1
     *
     * @param src
     * @param key
     * @return if return positive value. that is index in array. negative value means not.
     */
    public static int isInList(List<? extends Object> src, Object key) {
        if (isNull(src)) return -1;
        if (isNull(key)) return -1;
        for (int i = 0; i < src.size(); i++) {
            if (isNull(src.get(i))) continue;
            if (src.get(i).equals(key)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * parameter 2 is contain in parameter 1.
     *
     * @param sourceFlag
     * @param compareFlag
     * @return
     */
    public static boolean isFlagContain(int sourceFlag, int compareFlag) {
        return (sourceFlag & compareFlag) == compareFlag;
    }

    /**
     * Whether show StatueBar or not.
     *
     * @param active  in which Activity
     * @param visible View.VISIBLE is show, otherwise is dismiss
     */
    public static void statueBarVisible(Activity active, final int visible) {
        if (visible == View.VISIBLE) {
            active.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            active.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    /**
     * check SD card is available. then u may be call toastShort to communicate
     * with user.
     *
     * @return <b>true</b> the SD card is available. <b>false</b> not.
     */
    public static boolean sdcardIsOnline() {
        final String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ? true : false;
    }

    /**
     * update the isSDCardAvailable and isSDCardWriteable state
     */
    private static void sdCardUpdateState(Context context) {
        final String sdCardState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(sdCardState)) {
            isSDCardAvailable = isSDCardWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(sdCardState)) {
            isSDCardAvailable = true;
            isSDCardWriteable = false;
        } else {
            isSDCardAvailable = isSDCardWriteable = false;
        }
    }

    /**
     * open the SD card state listener. Generally call it in "onStart" method.
     */
    public static synchronized void sdCardStartListener(Context context,
                                                        sdcardListener lis) {
        if (saveContext != null && saveContext != context) {
            sdCardStopListener(saveContext);
        }
        mSdcardListener = lis;
        saveContext = context;
        sdCardReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sdCardUpdateState(context);
                if (mSdcardListener != null)
                    mSdcardListener.onReceiver(isSDCardAvailable,
                            isSDCardWriteable);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        if (!isSDCardReceiverRegister) {
            context.registerReceiver(sdCardReceiver, filter);
            isSDCardReceiverRegister = true;
        }
        sdCardUpdateState(context);
    }

    /**
     * close the SD card state listener. Generally call it in "onStart" method.
     */
    public static synchronized void sdCardStopListener(Context context) {
        if (isSDCardReceiverRegister && saveContext == context) {
            context.unregisterReceiver(sdCardReceiver);
            isSDCardReceiverRegister = false;
            mSdcardListener = null;
        }
    }

    public static sdcardListener mSdcardListener;

    public interface sdcardListener {
        void onReceiver(boolean isAvailable, boolean isCanWrite);
    }

    public static void sysSetActionBness(Activity action, float bness) {
        WindowManager.LayoutParams lp = action.getWindow().getAttributes();
        lp.screenBrightness = bness;
        action.getWindow().setAttributes(lp);
    }

    public static float sysGetActionBness(Activity action) {
        return action.getWindow().getAttributes().screenBrightness;
    }

    public static void sysIsLockScreen(Activity act, boolean isLock) {
        if (isLock) {
            switch (act.getResources().getConfiguration().orientation) {
                case Configuration.ORIENTATION_PORTRAIT:
                    act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
            }
        } else {
            act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && cm.getActiveNetworkInfo() != null) {
            return cm.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = lm.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public static boolean is3rd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static class DensityUtil {

        /**
         * dp to px
         */
        public static int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        /**
         * px to dp
         */
        public static int px2dip(Context context, float pxValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }
    }

    //取整
    public static String FloatKeepZero(float f) {
        DecimalFormat df = new DecimalFormat("0");
        return df.format(f);
    }

    // 保留一位小数
    public static String FloatKeepOne(float f) {
        float f1 = Math.round(f * 10) / 10f;
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(f1);
    }

    // 保留两位小数
    public static String FloatKeepTwo(float f) {
        float f1 = Math.round(f * 100) / 100f;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(f1);
    }

    // 保留三位小数
    public static String FloatKeepThree(float f) {
        float f1 = Math.round(f * 1000) / 1000f;
        DecimalFormat df = new DecimalFormat("0.000");
        return df.format(f1);
    }

    /**
     * 比较版本号
     *
     * @param oldVersion 之前版本
     * @param newVersion 当前版本
     * @return
     */
    public static boolean isHighVersion(String oldVersion, String newVersion) {
        String[] oldV = oldVersion.split("\\.");
        String[] newV = newVersion.split("\\.");
        // 如果版本.长度不一样，后面的缺省为0，如果保证长度一样，这里可以不需要
        if (oldV.length < newV.length) {
            String[] t = new String[newV.length];
            System.arraycopy(oldV, 0, t, 0, oldV.length);
            for (int i = oldV.length; i < t.length; i++) {
                t[i] = "0";
            }
            oldV = t;
        } else if (oldV.length > newV.length) {
            String[] t = new String[oldV.length];
            System.arraycopy(newV, 0, t, 0, newV.length);
            for (int i = newV.length; i < t.length; i++) {
                t[i] = "0";
            }
            newV = t;
        }

        for (int i = 0; i < oldV.length; i++) {
            try {
                int n1 = Integer.valueOf(oldV[i]).intValue();
                int n2 = Integer.valueOf(newV[i]).intValue();
                if (n1 < n2) {
                    return true;
                } else if (n1 > n2) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }


    // 保存商品Cookie 客户端使用===>已提交订单
    public static void saveAndroidGoodsCookie(Context context, String cookie) {
        SharedPreferences sp = context.getSharedPreferences(Configration.SHAREDPREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("acookie", "");
        editor.putString("acookie", cookie);
        editor.commit();
    }

    // 得到商品Cookie 客户端使用===>已提交订单
    public static String getAndroidGoodsCookie(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Configration.SHAREDPREFERENCE, context.MODE_PRIVATE);
        return sp.getString("acookie", "");
    }

    // 保存商品Cookie,发送给H5页面使用
    public static void saveH5GoodsCookie(Context context, String cookie) {
        SharedPreferences sp = context.getSharedPreferences(Configration.SHAREDPREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("hcookie", "");
        editor.putString("hcookie", cookie);
        editor.commit();
    }

    // 得到商品Cookie,发送给H5页面使用
    public static String getH5GoodsCookie(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Configration.SHAREDPREFERENCE, context.MODE_PRIVATE);
        return sp.getString("hcookie", "");
    }

    // 单个解析
    public static String singleJsonData(String data, String name) {
        StringReader sr = new StringReader(data);
        JsonReader jr = new JsonReader(sr);
        try {
            jr.beginObject();
            if (jr.nextName().equals(name)) {
                return jr.nextString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

    //单个解析booolean
    public static boolean singleJsonDataBoolean(String data, String name) {
        StringReader sr = new StringReader(data);
        JsonReader jr = new JsonReader(sr);
        try {
            jr.beginObject();
            if (jr.nextName().equals(name)) {
                return jr.nextBoolean();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 为了解决ListView在ScrollView中只能显示一行数据的问题
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static String setCookieWebView(Context context) {
        String cookie = ((BaseApplication) context.getApplicationContext()).readSession();
        return cookie;
    }

    /**
     * 从字符串中截取连续4位数字 用于从短信中获取动态密码
     *
     * @param str 短信内容
     * @return 截取得到的4位动态密码 由于号码不确定，首先需要匹配短信内容包含"柿集App"名字
     */
    public static String getDynamicPassword(String str) {
        String dynamicPassword = "";
        Pattern p = Pattern.compile("柿集App");
        Matcher m = p.matcher(str);
        while (m.find()) {
            Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
            Matcher matcher = continuousNumberPattern.matcher(str);
            while (matcher.find()) {
                if (matcher.group().length() == 6) {
                    dynamicPassword = matcher.group();
                }
            }
        }
        return dynamicPassword;
    }

    public static boolean checkBuildVerson() {
        if (Build.VERSION.SDK_INT >= 19) {
            return true;
        }
        return false;
    }

    public static boolean checkBuildVersonFour() {
        if (Build.VERSION.SDK_INT > 19) {
            return true;
        }
        return false;
    }

    /**
     * 获取渠道名
     *
     * @param ctx 此处习惯性的设置为activity，实际上context就可以
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Context ctx) {
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    // 获取手机型号
    public static String getPhoneInfo(Context context) {
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String mtype = android.os.Build.MODEL; // 手机型号
        return mtype;
    }

    public static String getUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    // 获取版本信息code
    public static int getCurVersion(Context context) {
        if (context == null) {
            return 0;
        }

        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getCurVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String GetNetworkType(Context context) {
        String strNetworkType = "";

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }
            }
        }
        return strNetworkType;
    }

    public static int getActionBarSize(Context context) {
        TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return size;
    }

    public static int getStatusBarSize(Context context) {
        int statusBarSize = 0;
        if (context != null) {
            int id = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (id > 0) {
                statusBarSize = context.getResources().getDimensionPixelSize(id);
            }
        }
        return statusBarSize;
    }

    public static void saveServerSearchData(Handler mhandler, final SharedPreferences sp, String datas) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("searchData", datas);
        editor.commit();
    }

    // 2016-03-07
    public static void savePopularSearchData(Handler mhandler, final SharedPreferences sp, String datas) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("popularData", datas);
        editor.commit();
    }

    public static void saveLocalSearchRecord(Handler mhandler, final SharedPreferences sp, String datas) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("record", datas);
        editor.commit();
    }

    public static void cleanLocalSearchRecord(Handler mhandler, final SharedPreferences sp) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("record", "");
        editor.commit();
    }

    public static void hintIMETool(Context context, View view) {
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showIMETool(Context context, View view) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = manager.isActive();
        if (isOpen)
            return;
        else {
            manager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }

    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(BaseApplication.getInstance().readUserId());
    }

    /**
     * 判断app是在前台运行还是在后台运行
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    //下载app
    public static void downloadApp(final Context mContext, String urlAdress, String appName) {
        try {
            DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(urlAdress);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            //设置允许使用的网络类型，这里是移动网络和wifi都可以
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
//						 request.setShowRunningNotification(true);
            request.setDestinationInExternalPublicDir("download", appName + ".apk");
            request.setMimeType("application/vnd.android.package-archive");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            //不显示下载界面
            request.setVisibleInDownloadsUi(true);
            request.setDescription("正在下载应用。。。");
            long id = downloadManager.enqueue(request);
            SharedPreferences sPreferences = mContext.getSharedPreferences("down", 0);
            sPreferences.edit().putLong("downId", id).commit();

        } catch (Exception e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("系统下载已被停用，请打开");
            builder.setTitle("提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                    mContext.startActivity(intent);
                }
            });
            builder.create().show();
        }
    }

    public static SpannableString changeTextStytle(String text, int position) {
        SpannableString spanString = new SpannableString(text);
        StyleSpan span = new StyleSpan(Typeface.BOLD);
        spanString.setSpan(span, position, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 判断手机是否安装了微信
     * 2016-2-23  tomyang
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    // 计算时间差，返回分秒
    public static long[] counterTime(String start, String end) {
        long[] temp = new long[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeStart = 0;
        long timeEnd = 0;

        try {
            timeStart = sdf.parse(start).getTime();
            timeEnd = sdf.parse(end).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        temp[0] = (timeEnd - timeStart) / 60000;
        temp[1] = ((timeEnd - timeStart) - temp[0] * 60 * 1000) / 1000;

        return temp;
    }

    // 计算时间差，返回时分秒
    public static float[] counterTimeLong(String start, String end) {
        float[] temp = new float[3];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeStart = 0;
        long timeEnd = 0;

        try {
            timeStart = sdf.parse(start).getTime();
            timeEnd = sdf.parse(end).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long diff = timeEnd - timeStart;
        float hour = 0;
        float minute = 0;
        float second;

        second = diff / 1000;
        if (second >= 60) {
            minute = second / 60;
            second = second % 60;
        }

        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        temp[0] = second;
        temp[1] = minute;
        temp[2] = hour;

        return temp;
    }

    // 比较当前时间，是否在某个时间段
    public static int compareTime(String now, String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long startTime;
        long endTime;
        long nowTime;

        try {
            startTime = sdf.parse(start).getTime();
            endTime = sdf.parse(end).getTime();
            nowTime = sdf.parse(now).getTime();
            if (nowTime < startTime) {
                return -1;  // 开抢提醒
            }

            if (nowTime >= startTime && nowTime <= endTime) {
                return 0;   // 倒计提醒
            }

            if (nowTime > endTime) {
                return 1;   // 已关闭
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -3;
    }

    // 获取String类型的小时和分钟，做闪购提醒
    public static String getHourMinuteTime(String time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ctime = "";
        try {
            Date date1 = formatter.parse(time);
            String dateString = formatter.format(date1);
            ctime = dateString.substring(11, 16);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ctime;
    }

    // 主题对话框
    public static void showMsg(Context mContext, String title,
                               String reminder, String posivitiveName, String neutralName, DialogInterface.OnClickListener onClickListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(reminder);
//        SpannableString spannableString = new SpannableString(posivitiveName);
//        spannableString.setSpan(new ForegroundColorSpan(0xFFed5137),0 ,posivitiveName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setPositiveButton(posivitiveName, onClickListener);
        builder.setNeutralButton(neutralName, onClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setTextColor(0xFFed5137);
        Button neutralButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        neutralButton.setTextColor(0x65000000);
    }

    public static int[] getImgInfo(Context mContext, int drawable) {
        int[] imgInfo = new int[2];
        Drawable mBitmap = ContextCompat.getDrawable(mContext, drawable);//BitmapFactory.decodeResource(mContext.getResources(),drawable);
        imgInfo[0] = mBitmap.getIntrinsicWidth();
        imgInfo[1] = mBitmap.getIntrinsicHeight();
        return imgInfo;
    }

    public static void getHtmlVersion(final Context mContext, final Handler mHandler, final DownLoadAppListener callBack) {
        int code = Util.getCurVersion(mContext);
        new RetrofitRequest<HtmlVersionInfo>(ApiRequest.getApiShiji().getHtmlFileInfo(String.valueOf(code))).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            HtmlVersionInfo info = (HtmlVersionInfo) msg.obj;
                            String localHtmlInfo = SharedPreUtil.getString(mContext, "shiji", "htmlInfo");
                            if (info == null) {          // 测试环境为空
                                return;
                            }
                            if (info.getAndroid() != null) {
                                final String newHtmlInfo = info.getAndroid().getMd5();
                                String code = info.getAndroid().getLatest_version();
                                SharedPreUtil.putString(mContext, "shiji", "htmlCode", code);
                                if (!localHtmlInfo.equals(newHtmlInfo) || !bFile("/shiji/android/")) {
                                    String pathDownloadHtml = info.getAndroid().getDownload_path();
                                    if (TextUtils.isEmpty(pathDownloadHtml)) {
                                        return;
                                    }
                                    File sdcardDir = Environment.getExternalStorageDirectory();
                                    String path = sdcardDir.getPath() + "/shiji/";
                                    File path1 = new File(path);
                                    if (path1.exists()) {
                                        File path2 = new File(sdcardDir.getPath() + "/shiji/android/");
                                        if (path2.exists())
                                            FileUtil.deleteFile(path2);
                                    }
                                    path1.mkdirs();

                                    DownLoaderTask task = new DownLoaderTask(pathDownloadHtml, path, mContext,
                                            new ZipExtractorTask.OnFinishUnZipListner() {
                                                @Override
                                                public void onFinishUnZip() {
                                                    if (mContext == null) {
                                                        return;
                                                    }

                                                    SharedPreUtil.putString(mContext, "shiji", "htmlInfo", newHtmlInfo);
                                                    nCount = 0;
                                                    if (callBack != null) {
                                                        callBack.onSuccess();
                                                    }
                                                }

                                                @Override
                                                public void onFailDownload() {
                                                    nCount++;
                                                    if (nCount < 3) {
                                                        mHandler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                getHtmlVersion(mContext, mHandler, callBack);
                                                            }
                                                        }, 200);

                                                        if (nCount == 2) {
                                                            nCount = 0;
                                                        }
                                                    }
                                                }
                                            });
                                    task.execute();
                                } else {
                                    if (callBack != null) {
                                        callBack.onFail();
                                    }
                                }
                            }
                        } else {
                            if (callBack != null) {
                                callBack.onFail();
                            }
                        }
                    }
                }
        );
    }

    // 判断当前文件是否为空
    public static boolean bFile(String file) {
        File sdcardDir = Environment.getExternalStorageDirectory();
        String path = sdcardDir.getPath() + file;
        File path1 = new File(path);
        return path1.exists();
    }

    // 获取手机cpu名称
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            br.close();
            String[] array = text.split(":\\s+", 2);
            if (array.length >= 2) {
                return array[1];
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 跳转拨号界面
    public static void dialPhoneNumber(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

//    public float getTextWidth(Context Context, String text, int textSize){
//        TextPaint paint = new TextPaint();
//        float scaledDensity = Context.getResource().getDisplayMetrics().scaledDensity;
//        paint.setTextSize(scaledDensity * textSize);
//        return paint.measureText(text);
//    }

    // 快速点击判断
    public static boolean BeNotQuicklyClick() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if ((currentTime - lastClickTime) > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return true;
        }
        return false;
    }

    // 以宽和高的最小量为基准 通过七牛等比例缩放并下载

    public static String clipImageViewByWH(String imageviewUrl, int width, int height) {
        String url = "";
        if (!TextUtils.isEmpty(imageviewUrl)) {
            url = imageviewUrl + "?imageView2/1/w/" + width + "/h/" + height;
            return url;
        }

        return imageviewUrl;
    }

    // 裁剪活动图片
    public static String ClipImageBannerView(String imageviewUrl) {
        String url = "";
        if (!TextUtils.isEmpty(imageviewUrl)) {
            url = imageviewUrl + "?imageView2/1/w/800/h/406";
            return url;
        }
        return imageviewUrl;
    }

    // 裁剪闪购图片
    public static String ClipImageFlashSaleView(String imageviewUrl) {
        String url = "";
        if (!TextUtils.isEmpty(imageviewUrl)) {
            url = imageviewUrl + "?imageView2/1/w/637/h/406";
            return url;
        }
        return imageviewUrl;
    }

    public static String ScaleImageGoodes(String imageviewUrl, int width) {
        String url = "";
        if (!TextUtils.isEmpty(imageviewUrl)) {
            url = imageviewUrl + "?imageView2/2/w/" + width;
            return url;
        }
        return imageviewUrl;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    // 显示自定义带确定和取消按钮的对话框
    public static NiftyDialogBuilder showCustomDialog(Context context, String message, final ProgressDialog.DialogClick listener) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
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

    /**
     * 新用户红包弹层图片提示
     */
    public static void getNewUserPullLayer(final Context mContext, Intent data) {
        if (data != null) {
            if (data.getBooleanExtra("isNew", false)) {
                new RetrofitRequest<LayerItem>(ApiRequest.getApiShiji().getNewUserPullLayer()).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            LayerItem item = (LayerItem) msg.obj;
                            if (item == null) return;
                            int show = item.getShow();
                            if (show == 1) {
                                //展示
                                showImageDialog(item, mContext);
                            }
                        }
                    }
                });
            }
        }

    }

    private static void showImageDialog(LayerItem item, final Context mContext) {
        if (TextUtils.isEmpty(item.getImage()) || mContext == null) return;
        final Dialog dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_pop_layer, null);
        ImageView ivLayer = (ImageView) view.findViewById(R.id.iv_layer);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);

        int width = SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 50) * 2;
        Netroid.displayImage(Util.ScaleImageGoodes(item.getImage(), width), ivLayer);

        if (!TextUtils.isEmpty(item.getClose())) {
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
        }

        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * 获取对象的深克隆
     *
     * @param t
     * @param <T> T 泛型  <T extends Serializable> 定义T 这个泛型继承
     *            Serializable
     * @return
     */
    public static <T extends Serializable> T getDeepClone(T t) throws
            RuntimeException {
        T result = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            //序列化
            ByteArrayOutputStream byteArrayOutputStream = new
                    ByteArrayOutputStream();
            out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(t);

            //反序列化
            ByteArrayInputStream byteArrayInputStream = new
                    ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            in = new ObjectInputStream(byteArrayInputStream);
            result = (T) in.readObject();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null)
                    out.close();

                if (in != null)
                    in.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
    // 判断是否安装新浪微博
    public static boolean isWeiboInstalled(@NonNull Context context) {
        PackageManager pm;
        if ((pm = context.getApplicationContext().getPackageManager()) == null) {
            return false;
        }
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            String name = info.packageName.toLowerCase(Locale.ENGLISH);
            if ("com.sina.weibo".equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void showRecommendToast(Context mContext) {
        View layoutTips = LayoutInflater.from(mContext).inflate(R.layout.collect_goods_toast, null);
        TextView tvContent = (TextView) layoutTips.findViewById(R.id.toast_context);
        tvContent.setText("已添加至个人中心－店主推荐");
        final Toast toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, SimpleUtils.getScreenHeight(mContext) * 7 / 24);
        toast.setView(layoutTips);
        toast.show();
    }

    public static void showCustomToast(Context mContext, String text) {
        View layoutTips = LayoutInflater.from(mContext).inflate(R.layout.collect_goods_toast, null);
        TextView tvContent = (TextView) layoutTips.findViewById(R.id.toast_context);
        tvContent.setText(text);
        final Toast toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0 , 0);
        toast.setView(layoutTips);
        toast.show();
    }

    public static String transferLevel(int level){
        switch (level){
            case 0:
                return "H1";
            case 1:
                return "H2";
            case 2:
                return "H3";
            case 3:
                return "H4";
            case 4:
                return "H5";
            default:
                return "H6";
        }
    }

//    private String transferLevelNeed(int level){
//        switch (level){
//            case 0:
//                return "要求：无要求";
//            case 1:
//                return "要求：≥" + entity.getLevel().get(0) + "成长值";
//            case 2:
//                return "要求：≥" + entity.getLevel().get(1) + "成长值";
//            case 3:
//                return "要求：≥" + entity.getLevel().get(2) + "成长值";
//            case 4:
//                return "要求：≥" + entity.getLevel().get(3) + "成长值";
//            default:
//                return "要求：≥" + entity.getLevel().get(4) + "成长值";
//        }
//    }

}
