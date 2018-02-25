package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.CookiesManager;
import cn.yiya.shiji.entity.ShareEntity;
import cn.yiya.shiji.entity.ShareSecondEntity;
import cn.yiya.shiji.h5.BridgeHighWebView;
import cn.yiya.shiji.views.ShareDialog;

/**
 * Created by Tom on 2016/10/28.
 */
public class XiaoShiJiWebViewActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private String url;
    private ShareEntity share = new ShareEntity();
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivShare;
    private BridgeHighWebView mWebView;
    private String shopName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaoshiji_webview);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            shopName = intent.getStringExtra("name");
            url = intent.getStringExtra("url");
            String shareInfo = intent.getStringExtra("share");
            ShareSecondEntity shareSecondEntity = new Gson().fromJson(shareInfo, ShareSecondEntity.class);
            share.setCover(shareSecondEntity.getShareTimelineImage());
            share.setUrl(url);
            share.setTitle(shareSecondEntity.getShareTimelineTitle());
            share.setDescription(shareSecondEntity.getShareAppMessageDesc());
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText(shopName);
        ivShare = (ImageView) findViewById(R.id.title_right);
        mWebView = (BridgeHighWebView) findViewById(R.id.mWebView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        mWebView.addJavascriptInterface(this, "control");
        // 设置确保登录态传进来
        CookiesManager.setUrlCookie(this, url);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                } else {
                    // 加载中
                }

            }
        });

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

    public void goToShare() {
        if (share == null) return;
        new ShareDialog(this, share.getTitle(), share.getCover(), share.getUrl(), share.getDescription()).build().show();
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
}
