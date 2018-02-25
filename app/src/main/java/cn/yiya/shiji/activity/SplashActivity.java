package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.networkbench.agent.impl.NBSAppAgent;
import com.networkbench.com.google.gson.Gson;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

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
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.config.CustomEvent;
import cn.yiya.shiji.config.HttpOptions;
import cn.yiya.shiji.entity.AppScreenImageEntity;
import cn.yiya.shiji.entity.CommonWebViewEntity;
import cn.yiya.shiji.entity.DeviceInfo;
import cn.yiya.shiji.entity.HtmlInfo;
import cn.yiya.shiji.entity.SystemSkinObject;
import cn.yiya.shiji.utils.FileUtil;
import cn.yiya.shiji.utils.SharedPreUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

public class SplashActivity extends Activity {

    long startTime;
    long endTime;
    SimpleDraweeView startgif;
    private int adType;             // 广告图类型
    private String adContent;       // 广告图 跳转信息
    private String adShare;
    String mainActivityDest = "";
    String flashId;

    SharedPreferences sharedPreferences;
    String skinTime;
    public Bitmap bitmap;

    private String ADImageUrl;
    private String adTime;
    private SharedPreferences sPreferences;
    public Bitmap adbitmap;

    float density;

    public final static String SKIN_PATH = Environment.getExternalStorageDirectory() + "/shiji/appSkin/";     //  获得SD卡的路径
    private ImageView ivBackground;
    private Handler mHandler;
    private Runnable runnable;

    private String bottomName;        // 底部中间按钮文字和url链接
    private String bottomUrl;
    private String bottomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action=mainIntent.getAction();
            if(mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        startTime = System.currentTimeMillis();
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                initSuccess();
                return false;
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            mainActivityDest = intent.getStringExtra("main_activity_dest");
            flashId = intent.getStringExtra("flashId");
        }

        initViews();
        init();

    }

    private void initViews() {
        startgif = (SimpleDraweeView) findViewById(R.id.startgif);
        ivBackground = (ImageView) findViewById(R.id.startimage);

        getDeviceInfo();
//        BaseApplication.getInstance().getAppStartImage();    // 获取网络启动图片
        getSkin();
        getAppScreenImage();
        getCommonWebView();                 // 获取底部按钮文字及连接

        Uri uri = Uri.parse("asset:///start/gif.gif");
        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true)
                        .build();
        startgif.setController(draweeController);

        goToNextActivity();
    }

    private void getCommonWebView() {
        new RetrofitRequest<CommonWebViewEntity>(ApiRequest.getApiShiji().getCommonWebView()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){
                    CommonWebViewEntity obj = (CommonWebViewEntity) msg.obj;
                    if(obj.getList() != null && obj.getList().size() > 0){
                        BaseApplication.getInstance().mainbottomName = obj.getList().get(0).getName();
                        BaseApplication.getInstance().mainbottomUrl = obj.getList().get(0).getUrl();
                        BaseApplication.getInstance().saveThemeIcon(obj.getCover());

                        bottomName = obj.getList().get(0).getName();
                        bottomUrl = obj.getList().get(0).getUrl();
                        bottomTitle = obj.getList().get(0).getTitle();
                    }
                }
            }
        });
    }

    private void init() {
        // 听云监听
        if (HttpOptions.ShowTYPoint) {
            NBSAppAgent.setLicenseKey(Configration.TingYunKey).start(this);
        }

        //友盟统计入口
        MobclickAgent.updateOnlineConfig(this);
        CustomEvent.onEvent(SplashActivity.this, ((BaseApplication) getApplication()).getDefaultTracker(), "SplashActivity", CustomEvent.Splash);
        AnalyticsConfig.enableEncrypt(true);

        BaseApplication.isInit = true;
        DisplayMetrics metric = getResources().getDisplayMetrics();
        density = metric.densityDpi;
    }

    // 获取最新的APP启动图
    private void getAppScreenImage() {
        new RetrofitRequest<AppScreenImageEntity>(ApiRequest.getApiShiji().getAppScreenImage()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess() && msg.obj != null) {
                    AppScreenImageEntity entity = (AppScreenImageEntity) msg.obj;
                    ADImageUrl = entity.getImage();
                    AppScreenImageEntity.Share shareEntity = entity.getShare();
                    if (shareEntity != null) {
                        adShare = new Gson().toJson(shareEntity);
                    }
                    adType = entity.getType();
                    adContent = entity.getContent();
                    sPreferences = getSharedPreferences("app", 0);
                    if (TextUtils.isEmpty(ADImageUrl)) {                                        // 删除广告图
                        File adCaptureFile = new File(BaseApplication.ALBUM_PATH + "ad.jpg");
                        adCaptureFile.delete();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL downUrl = new URL(ADImageUrl);
                                    InputStream is = downUrl.openStream();
                                    adbitmap = BitmapFactory.decodeStream(is);
                                    BaseApplication.getInstance().saveFile(adbitmap, "ad.jpg");
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
        });
    }


    // 当前是否新版本
    private boolean checkVersion() {
        String curVersion = "";
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            curVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String sharedVersion = SharedPreUtil.getString(getApplicationContext(),
                SharedPreUtil.DM_APP_DB,
                SharedPreUtil.newAppVersion);

        if (TextUtils.isEmpty(sharedVersion) || !sharedVersion.equals(curVersion)) {
            SharedPreUtil.putString(getApplicationContext(), // 提示一次就把newAppVersion的值修改掉
                    SharedPreUtil.DM_APP_DB,
                    SharedPreUtil.newAppVersion,
                    curVersion);
            return true;
        }

        return false;
    }

    // 获取设备详细信息并上传到服务器
    private void getDeviceInfo() {
        DeviceInfo info = new DeviceInfo();
        info.setChannel(Util.getChannelName(this));
        info.setCode(Util.getCurVersion(this));
        info.setPhone(Util.getPhoneInfo(this));
        info.setNetwork(Util.GetNetworkType(this));
        info.setIdfa(Util.getUUID(this));
        String code = SharedPreUtil.getString(this, "shiji", "htmlCode");
        info.setOs("Android_" + android.os.Build.VERSION.SDK_INT);
        info.setVersion(Util.getCurVersionName(this) + "(" + code + ")");
        String json = new Gson().toJson(info);
        String str64 = android.util.Base64.encodeToString(json.getBytes(), Base64.NO_WRAP);
        SharedPreUtil.putString(this, "shiji", "device", str64);
    }

    private void getSkin() {
        new RetrofitRequest<SystemSkinObject>(ApiRequest.getApiShiji().getSkin()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            sharedPreferences = getSharedPreferences("skin", 0);
                            skinTime = sharedPreferences.getString("skintime", "0");
                            final SystemSkinObject object = (SystemSkinObject) msg.obj;

                            // 判断数据存在
                            if (object.empty || object.timestamp == null) {
                                File skinFile = new File(SKIN_PATH);
                                FileUtil.deleteFile(skinFile);
                                return;
                            }

                            if (object == null || object.list == null || object.list.size() == 0) {
                                return;
                            }

                            // 判断时间的顺序性,如果时间相等并且文件存在，则不必去下载
                            if (Long.parseLong(object.timestamp) <= Long.parseLong(skinTime) && bFile()) {
                                return;
                            }

                            sharedPreferences.edit().putString("skintime", "" + object.timestamp).commit();
                            sharedPreferences.edit().putString("begintime", "" + object.begin_time).commit();
                            sharedPreferences.edit().putString("endtime", "" + object.end_time).commit();
                            getDownSkinImage(object.list);

                        } else {
                        }
                    }
                }
        );
    }

    private void getDownSkinImage(final ArrayList<HtmlInfo> list) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < list.size(); i++) {
                        URL downUrl;
                        if (!TextUtils.isEmpty(list.get(i).getUrl())) {
                            String url = Util.transferImage(list.get(i).getUrl(), SimpleUtils.getScreenWidth(SplashActivity.this)/15);
                            downUrl = new URL(url);
                            InputStream is = downUrl.openStream();
                            bitmap = BitmapFactory.decodeStream(is);
                            is.close();
                            saveFile(SKIN_PATH, bitmap, list.get(i).getType() + ".png");

                            if(list.get(i).getType() == 4){
//                                String url1 = Util.clipImageViewByWH(list.get(i).getUrl(), SimpleUtils.getScreenWidth(SplashActivity.this)/15, SimpleUtils.dp2px(SplashActivity.this, 37));
                                String url1 = Util.transferImage(list.get(i).getUrl(), SimpleUtils.dp2px(SplashActivity.this, 33));
                                downUrl = new URL(url1);
                                InputStream is1 = downUrl.openStream();
                                bitmap = BitmapFactory.decodeStream(is1);
                                is1.close();
                                saveFile(SKIN_PATH, bitmap, 14 + ".png");
                            }else if(list.get(i).getType() == 9){
//                                String url1 = Util.clipImageViewByWH(list.get(i).getUrl(), SimpleUtils.getScreenWidth(SplashActivity.this)/15, SimpleUtils.dp2px(SplashActivity.this, 37));
                                String url1 = Util.transferImage(list.get(i).getUrl(), SimpleUtils.dp2px(SplashActivity.this, 33));
                                downUrl = new URL(url1);
                                InputStream is1 = downUrl.openStream();
                                bitmap = BitmapFactory.decodeStream(is1);
                                is1.close();
                                saveFile(SKIN_PATH, bitmap, 13 + ".png");
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void saveFile(String path, Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);

        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }

        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));

        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }

        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
    }

    private boolean bFile() {
        File path1 = new File(SKIN_PATH);
        return path1.exists();
    }

    private void goToNextActivity() {
        runnable = new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        };
        endTime = System.currentTimeMillis();
        long animTime = startTime + 4300;
        if (animTime > endTime) {
            mHandler.postDelayed(runnable, animTime - endTime);
        } else {
            initSuccess();
        }
    }

    private void initSuccess() {
        if (checkVersion()) {
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            intent.putExtra("adType", adType);
            intent.putExtra("adContent", adContent);
            intent.putExtra("adShare", adShare);
            intent.putExtra("bottomName", bottomName);
            intent.putExtra("bottomUrl", bottomUrl);
            intent.putExtra("bottomTitle", bottomTitle);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            //判断是否有main_activity_dest flash
            if (!TextUtils.isEmpty(mainActivityDest)) {

                Intent intent = new Intent(getApplicationContext(), ADActivity.class);
                if (mainActivityDest.equals("flash")) {
                    intent.putExtra("main_activity_dest", "flash");
                    intent.putExtra("flashId", flashId);
                } else if (mainActivityDest.equals("service")) {
                    intent.putExtra("main_activity_dest", "service");
                    intent.putExtra("msgtype", getIntent().getStringExtra("type"));
                } else if (mainActivityDest.equals("community")) {
                    intent.putExtra("main_activity_dest", "community");
                } else if (mainActivityDest.equals("homeIssue")) {
                    intent.putExtra("main_activity_dest", "flash");
                    intent.putExtra("activityId", getIntent().getStringExtra("activityId"));
                } else if (mainActivityDest.equals("newLocalWebViewThem")) {
                    intent.putExtra("main_activity_dest", "newLocalWebViewThem");
                    intent.putExtra("data", getIntent().getStringExtra("data"));
                    intent.putExtra("type", 2);
                    intent.putExtra("url", "http://h5.qnmami.com/app/homeActivities/html/index.html");//正式
                } else if (mainActivityDest.equals("newLocalWebView")) {
                    intent.putExtra("main_activity_dest", "newLocalWebView");
                    intent.putExtra("activityId", getIntent().getStringExtra("activityId"));
                    intent.putExtra("name", getIntent().getStringExtra("name"));
                    intent.putExtra("url", getIntent().getStringExtra("url"));
                    intent.putExtra("hshare", getIntent().getStringExtra("hshare"));

                } else if (mainActivityDest.equals("newWorkDetail")) {
                    intent.putExtra("main_activity_dest", "newWorkDetail");
                    intent.putExtra("workId", getIntent().getStringExtra("workId"));

                } else if (mainActivityDest.equals("newMatchDetail")) {
                    intent.putExtra("main_activity_dest", "newMatchDetail");
                    intent.putExtra("matchId", getIntent().getStringExtra("matchId"));

                } else if (mainActivityDest.equals("userDetail")) {
                    intent.putExtra("main_activity_dest", "userDetail");
                    intent.putExtra("userId", getIntent().getStringExtra("userId"));
                } else if (mainActivityDest.equals("orderDetail")) {
                    intent.putExtra("main_activity_dest", "orderDetail");
                    intent.putExtra("subOrderNum", getIntent().getStringExtra("subOrderNum"));
                    intent.putExtra("orderNum", getIntent().getStringExtra("orderNum"));
                } else if (mainActivityDest.equals("shoppingCart")) {
                    intent.putExtra("main_activity_dest", "shoppingCart");
                }

                mainActivityDest = "";
                intent.putExtra("bottomName", bottomName);
                intent.putExtra("bottomUrl", bottomUrl);
                intent.putExtra("bottomTitle", bottomTitle);
                startActivity(intent);
            } else {
                Intent intent1 = new Intent(SplashActivity.this, ADActivity.class);
                intent1.putExtra("adType", adType);
                intent1.putExtra("adContent", adContent);
                intent1.putExtra("adShare", adShare);
                intent1.putExtra("bottomName", bottomName);
                intent1.putExtra("bottomUrl", bottomUrl);
                intent1.putExtra("bottomTitle", bottomTitle);
                startActivity(intent1);
            }

            finish();
        }
    }

    //处理还未进入NewMainActivity就退出时出现退出不完全
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int currentVersion = android.os.Build.VERSION.SDK_INT;
            if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
