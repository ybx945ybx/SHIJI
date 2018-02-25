package cn.yiya.shiji.config;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.kf5sdk.init.KF5SDKInitializer;

import org.lasque.tusdk.core.TuSdk;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.StartImageObject;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.entity.navigation.CouponDetailInfo;
import cn.yiya.shiji.entity.navigation.LocalObject;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.receiver.LocationService;
import cn.yiya.shiji.receiver.PushService;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.CrashHandler;
import cn.yiya.shiji.utils.SharedPreUtil;

public class BaseApplication extends Application {
    public static String Session = "";
    public static String Cookie = "";
    public static String ClientId = "";
    public static String FileId = "";
    private static final String GT_CID = "GetuiClientId";

    private static final String SESSION_NAME = "AppSession";
    private static final String COOKIE_NAME = "AppCookie";

    public static boolean canPalyVoice = true;
    public static boolean canVibrite = true;
    public static boolean canNotify = true;

    public static final String TagNotify = "TagNotify";
    public static final String TagVibrite = "TagVibrite";
    public static final String TagVoice = "TagVoice";
    public static final String FILEID = "FileId";
    public static final String USERID = "UserId";
    public static final String USERNAME = "UserName";
    public static final String PHONE = "phone";
    public static final String M_ID = "mid";
    public static final String HAVE_SHOP = "HaveShop";
    public static final String THEM_ICON = "ThemIcon";

    private static CouponDetailInfo couponDetailInfo = null;

    public static CouponDetailInfo getCouponDetailInfo() {
        return couponDetailInfo;
    }

    public static void setCouponDetailInfo(CouponDetailInfo info) {
        BaseApplication.couponDetailInfo = info;
    }

    public static boolean bUpdate = false;  // 是否提醒更新
    public static boolean bRefuse = false;  // 是否拒绝更新
    public static boolean bDownLoadApp = false;
    public static boolean bShowOrder = false;  //是否晒单成功
    public static boolean bCollocationSuccess = false;  // 是否成功发布搭配
    public static String WX_CODE;             // 微信登陆code

    public static boolean isInit = false;

    public static ArrayList<NewGoodsItem> testList = new ArrayList<>();

    private Tracker mTracker;

    private static BaseApplication instance;

    public static LocalObject localObject;
    public static boolean needRefresh;                        // 其他地方登录后刷新个人中心状态

    public LocationService locationService;

    private String longitude;
    private String latitude;
    public boolean fastTopGuide = false;
//	public static Bitmap collocation;   	  // 发布搭配的图片
//	public static String collocation;   	  // 发布搭配的图片

    public final static String ALBUM_PATH = Environment
            .getExternalStorageDirectory() + "/shiji/appStart/";     //  获得SD卡的路径

    private String startImageUrl, ADImageUrl;
    private String serviceTime, adTime;
    private SharedPreferences sPreferences;
    public Bitmap bitmap;

    public String mainbottomName;     // 底部中间按钮的名字
    public String mainbottomUrl;      // 底部中间按钮的链接

    @Override
    public void onCreate() {
        super.onCreate();
        if (HttpOptions.CrashDeBug) {
            //初始化崩溃处理
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());//初始化,传入context
        }
        //注册个推推送接收服务
        Netroid.init(this);
        readSharedPrefrence();
        startService(new Intent(this, PushService.class));
        Log.e("个推", "startService------");
        //初始化imageloader
        BitmapTool.initImageLoader(this);
        Fresco.initialize(this);
        initTuSdk();
        instance = this;
        KF5SDKInitializer.initialize(this);
        locationService = new LocationService(getApplicationContext());
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    private void initTuSdk() {
        TuSdk.init(this, Configration.TUKEY);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    // 启动图和广告图下载
    public void getAppStartImage() {
        new RetrofitRequest<StartImageObject>(ApiRequest.getApiShiji().getAppStartImage()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess() && msg.obj != null) {
                    StartImageObject object = (StartImageObject) msg.obj;
                    ADImageUrl = object.getAndroid().getAd_image();
                    adTime = object.getAndroid().getAd_time();
                    sPreferences = getSharedPreferences("app", 0);
                    final String adtime = sPreferences.getString("adtime", "0");
                    if (TextUtils.isEmpty(ADImageUrl) || TextUtils.isEmpty(adtime)) {                                        // 删除广告图
                        File adCaptureFile = new File(ALBUM_PATH + "ad.jpg");
                        adCaptureFile.delete();
                    } else if (!TextUtils.isEmpty(adtime)) {
                        if (Long.parseLong(adTime) > Long.parseLong(adtime) && BitmapTool.getSDFreeSize() > 0) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        URL downUrl = new URL(ADImageUrl);
                                        InputStream is = downUrl.openStream();
                                        bitmap = BitmapFactory.decodeStream(is);
                                        saveFile(bitmap, "ad.jpg");
                                        sPreferences.edit().putString("adtime", "" + adTime);
                                        sPreferences.edit().commit();
                                        is.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                }
            }
        });
    }

    public void saveFile(Bitmap bm, String fileName) throws IOException {
        if (bm == null) {
            return;
        }

        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    public static String getDeviceInfo() {
        return SharedPreUtil.getString(getInstance(), "shiji", "device");
    }

    /**
     * 保存session到Application
     *
     * @param cookie
     */
    public static void setCookie(String cookie) {
        if (TextUtils.isEmpty(cookie) || Cookie.equals(cookie)) {
            return;
        }
        Cookie = cookie;
    }

    /**
     * 保存session
     */
    public synchronized void saveCookie() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        sp.edit().putString(COOKIE_NAME, Cookie).commit();
    }

    /**
     * 读取session
     */
    public String readCookie() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        return sp.getString(COOKIE_NAME, "");
    }

    /**
     * 保存session到Application
     *
     * @param session
     */
    public static void setSession(String session) {
        if (TextUtils.isEmpty(session) || Session.equals(session)) {
            return;
        }
        Session = session;
    }

    /**
     * 保存session
     */
    public synchronized void saveSession() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        sp.edit().putString(SESSION_NAME, Session).commit();
    }

    /**
     * 读取session
     */
    public String readSession() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        return sp.getString(SESSION_NAME, "");
    }

    public void saveNotifyTag(boolean canNotify) {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        BaseApplication.canNotify = canNotify;
        sp.edit().putBoolean(TagNotify, canNotify).commit();
    }

    public void saveNotifyVoiceTag(boolean canNotify) {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        canPalyVoice = canNotify;
        sp.edit().putBoolean(TagVoice, canNotify).commit();
    }

    public void saveNotifyVibriteTag(boolean canNotify) {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        canVibrite = canNotify;
        sp.edit().putBoolean(TagVibrite, canNotify).commit();
    }

    /**
     * 保存CID
     *
     * @param clientId
     */
    public synchronized void saveCID(String clientId) {
        Log.e("个推", "Got ClientId:" + "savecid");
        if (TextUtils.isEmpty(clientId)) {
            return;
        }
        ClientId = clientId;
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        sp.edit().putString(GT_CID, ClientId).commit();
//		Session = readSession();
//		if(TextUtils.isEmpty(Session)){
//			return;
//		}
        new RetrofitRequest<>(ApiRequest.getApiShiji().uploadCID(clientId)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    Log.e("个推", "Got ClientId:" + "成功上传cid");
                }
            }
        });
    }

    /**
     * 读取保存的CID
     */
//	private void readClientId(){
//		SharedPreferences sp = getSharedPreferences(Configuration.SHAREDPREFERENCE, MODE_PRIVATE);
//		ClientId = sp.getString(GT_CID,"");
//	}
    private void readSharedPrefrence() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        ClientId = sp.getString(GT_CID, "");
        Session = sp.getString(SESSION_NAME, "");
        Cookie = sp.getString(COOKIE_NAME, "");
        canNotify = sp.getBoolean(TagNotify, true);
        canPalyVoice = sp.getBoolean(TagVoice, true);
        canVibrite = sp.getBoolean(TagVibrite, true);
        FileId = sp.getString(FILEID, "");
    }

    // 保存用户ID
    public void saveUserId(String id) {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USERID, id);
        editor.putString(FILEID, id);
        FileId = id;
        editor.commit();
    }

    // 保存用户昵称
    public void saveUserName(String name) {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USERNAME, name);
        editor.commit();
    }

    // 保存用户手机号码
    public void saveUserPhone(String phone) {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PHONE, phone);
        editor.commit();
    }

    public void saveUserMID(String mid) {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(M_ID, mid);
        editor.commit();
    }

    public void saveHaveShop(boolean haveShop) {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(HAVE_SHOP, haveShop);
        editor.commit();
    }

    public void saveThemeIcon(String cover) {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(THEM_ICON, cover);
        editor.commit();
    }

    public String getThemeIcon() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        return sp.getString(THEM_ICON, "");
    }

    // 清除用户ID
    public void clearUser() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USERID, "");
        editor.putString(USERNAME, "");
        editor.putString(PHONE, "");
        editor.putString(M_ID, "");
        editor.putBoolean(HAVE_SHOP, false);
        editor.commit();
    }

    // 读取用户ID
    public String readUserId() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        return sp.getString(USERID, "");
    }

    // 读取用户昵称
    public String readUserName() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        return sp.getString(USERNAME, "");
    }

    // 读取用户手机号码
    public String readUserPhone() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        return sp.getString(PHONE, "");
    }

    public String readUserMID() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        return sp.getString(M_ID, "");
    }

    public String readUserCID() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        return sp.getString(GT_CID, "");
    }

    // 读取用户是否开店
    public boolean readHaveShop() {
        SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        return sp.getBoolean(HAVE_SHOP, false);
    }


    public int getCurVersion() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 添加谷歌分析 2016-1-14 tomyang
     *
     * @return
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
