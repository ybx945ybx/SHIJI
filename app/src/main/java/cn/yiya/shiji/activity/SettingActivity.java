package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.business.ShareTools;
import cn.yiya.shiji.business.YiChuangYun;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.VersionData;
import cn.yiya.shiji.utils.DataCleanManager;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.dialog.NiftyDialogBuilder;

public class SettingActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private static final int RESULT_QUIT = 13;
	private Handler mHandler;
//	private String version;
    private NiftyDialogBuilder dialog;
    private boolean bCancel = false;
    private TextView tvCache;
    private ImageView ivBack;
    private TextView tvTitle;
    private RelativeLayout rlytAdvice;
    private RelativeLayout rlytAgreement;
    private RelativeLayout rlytAboutUs;
    private RelativeLayout rlytAdviceList;
    private RelativeLayout rlytUpdate;
    private RelativeLayout rlytCache;

    private TextView tvQuit;

    private static String cacheDir = new String(Environment.getExternalStorageDirectory().getPath() + "/shiji/cache");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ShareTools.getInstance().initShare(this);
        mHandler = new Handler(getMainLooper());
        initViews();
        initEvents();
        init();
    }

    @Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.rlyt_advice:
            showPreDialog("正在加载客服系统");
            YiChuangYun.openKF5(YiChuangYun.FeedBack, this, 0, "", new YiChuangYun.onFinishInitListener() {
                @Override
                public void onFinishInit() {
                    hidePreDialog();
                }
            });
			break;
		case R.id.rlyt_agreement:
			startActivity(new Intent(this, AgreementActivity.class));
			break;
        case R.id.rlyt_advice_list:
            showPreDialog("正在加载客服系统");
            YiChuangYun.openKF5(YiChuangYun.FeedBackList, this, 0, "", new YiChuangYun.onFinishInitListener() {
                @Override
                public void onFinishInit() {
                    hidePreDialog();
                }
            });
			break;
		case R.id.tv_quit:
			quit();
			break;
        case R.id.rlyt_about_us:
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
            break;
		case R.id.title_back:
            onBackPressed();
			break;
        case R.id.rlyt_cache:
            showCacheDialog("确定清除图片缓存?");
            break;
        case R.id.rlyt_update_version:
            checkUpdate();
            break;
		}
	}

	private void quit(){
        setResult(RESULT_QUIT);
        showCustomMutiDialog("确定退出当前账号登录？", new ProgressDialog.DialogClick() {
            @Override
            public void OkClick() {
                new RetrofitRequest<>(ApiRequest.getApiShiji().setLoginOut()).handRequest(
                        new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                if (msg.isSuccess()) {
                                    mApp.clearUser();
                                    clearCookie();
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    showTips(msg.message);
                                }
                            }
                        }
                );
            }

            @Override
            public void CancelClick() {

            }

        });
    }

	private String getVersion(){
		try{PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(),0);
			return info.versionName;
		}catch(Exception e){
			e.printStackTrace();
			return "找不到版本号";
		}
	}

    // 判断当前是否有更新
    private void checkUpdate() {
        if (BaseApplication.bUpdate) {
            showTips("正在升级中，请稍候");
            return;
        }

        if (BaseApplication.bDownLoadApp) {
            showTips("下载新版本中，请查看通知栏进度");
            return;
        }

        showPreDialog("正在检查更新");
        final int localVerson = mApp.getCurVersion();
        new RetrofitRequest<VersionData>(ApiRequest.getApiShiji().getVersion(String.valueOf(localVerson))).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        hidePreDialog();
                        if (bCancel) {
                            return;
                        }
                        if (msg.isSuccess() && msg.obj != null) {
                            VersionData remoteVer = (VersionData) msg.obj;
                            int remoteVersion = Integer.parseInt(remoteVer.getAndroid().getCode());
                            if (remoteVersion > localVerson) {
                                dialog = ProgressDialog.showUpdateVersionDialog(SettingActivity.this, remoteVer, 0);
                            } else {
                                showTips("当前已经是最新版本" + Util.getCurVersion(SettingActivity.this) + "--" + Util.getChannelName(SettingActivity.this));
                            }
                        } else {
                            showTips("当前已经是最新版本" + Util.getCurVersion(SettingActivity.this) + "--" + Util.getChannelName(SettingActivity.this));
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        if (progressDialog != null && progressDialog.isShowing()) {
            hidePreDialog();
            bCancel = true;
        }else {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

	private void clearCookie(){
		SharedPreferences sp = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("acookie", "");
		editor.putString("hcookie", "");
		editor.commit();
	}

    private void showCacheDialog(String title) {
        showCustomMutiDialog(title, new ProgressDialog.DialogClick() {
            @Override
            public void OkClick() {
                DataCleanManager.deleteFolderFile(cacheDir, false);
                tvCache.setText("0.00M");
            }

            @Override
            public void CancelClick() {

            }
        });
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("设置");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        rlytCache = (RelativeLayout)findViewById(R.id.rlyt_cache);
        rlytAdvice = (RelativeLayout)findViewById(R.id.rlyt_advice);
        rlytAdviceList = (RelativeLayout)findViewById(R.id.rlyt_advice_list);
        rlytAgreement = (RelativeLayout)findViewById(R.id.rlyt_agreement);
        rlytAboutUs = (RelativeLayout)findViewById(R.id.rlyt_about_us);
        rlytUpdate = (RelativeLayout)findViewById(R.id.rlyt_update_version);

        tvCache = (TextView)findViewById(R.id.tv_cache);
        String size = "";
        try {
            size = DataCleanManager.getCacheSize(new File(cacheDir));
        } catch (Exception e) {
        }
        tvCache.setText(size);

        tvQuit = (TextView) findViewById(R.id.tv_quit);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        rlytCache.setOnClickListener(this);
        rlytAdvice.setOnClickListener(this);
        rlytAdviceList.setOnClickListener(this);
        rlytAgreement.setOnClickListener(this);
        rlytAboutUs.setOnClickListener(this);
        tvQuit.setOnClickListener(this);
        rlytUpdate.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }
}
