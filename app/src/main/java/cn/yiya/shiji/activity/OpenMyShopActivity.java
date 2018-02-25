package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.CookiesManager;
import cn.yiya.shiji.entity.ShareEntity;
import cn.yiya.shiji.entity.ShareSecondEntity;
import cn.yiya.shiji.h5.BridgeHighWebView;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ShareDialog;

/**
 * Created by Tom on 2016/10/18.
 */
public class OpenMyShopActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private ImageView ivShare;
    private TextView tvTitle;
    private BridgeHighWebView mWebView;
    private String url = "http://open.xiaoshiji.qnmami.com/invitation/introduce?inApp=1";
    private ShareEntity share = new ShareEntity();
    private int type;               //    1是创建店铺  2是邀请开店
    private static int REQUEST_LOGIN = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_my_shop);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            type = intent.getIntExtra("type", 0);
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("开店介绍");
        ivShare = (ImageView) findViewById(R.id.title_right);
//        if(type == 1){
//            ivShare.setVisibility(View.GONE);
//        }else {
            ivShare.setVisibility(View.VISIBLE);
//        }
        mWebView = (BridgeHighWebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this, "control");

        // 设置确保登录态传进来
        CookiesManager.setUrlCookie(this, url);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
    }

    @Override
    protected void init() {
        mWebView.loadUrl(url);
    }

    // 分享
    private void goToShare() {
        if (share == null) return;
        new ShareDialog(this, share.getTitle(), share.getCover(), share.getUrl(), share.getDescription()).build().show();
    }

    // 登录态
    @JavascriptInterface
    public void showLogin (String data) {
        Intent intentLogin = new Intent(this, LoginActivity.class);
        startActivityForResult(intentLogin, REQUEST_LOGIN);
        overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);

    }

    // 创建店铺去
    @JavascriptInterface
    public void goCreateShop(String data) {
        Intent intent = new Intent(this, SelectShopTypeActivity.class);
        startActivity(intent);

    }

    //更新分享信息
    @JavascriptInterface
    public void setShareInfo(String message) {
        ShareSecondEntity shareSecondEntity = new Gson().fromJson(message, ShareSecondEntity.class);
        share.setCover(shareSecondEntity.getShareTimelineImage());
        share.setUrl(shareSecondEntity.getUrl());
        share.setTitle(shareSecondEntity.getShareTimelineTitle());
        share.setDescription(shareSecondEntity.getShareAppMessageDesc());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
                goToShare();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        goBackKey();
    }

    private void goBackKey() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK){
            if(requestCode == REQUEST_LOGIN){
                Util.getNewUserPullLayer(this, data);
            }
        }
    }
}
