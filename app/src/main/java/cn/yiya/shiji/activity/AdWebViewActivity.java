package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.AppScreenImageEntity;
import cn.yiya.shiji.entity.ShareEntity;
import cn.yiya.shiji.h5.BridgeHighWebView;

/**
 * Created by Tom on 2016/9/8.
 */
public class AdWebViewActivity extends BaseAppCompatActivity{
    private ImageView ivBack;
    private TextView tvTitle;
    private BridgeHighWebView mWebview;
    private String url;
    private String data;
    private String title;
    private AppScreenImageEntity.Share share;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_webview);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            url = intent.getStringExtra("url");
            data = intent.getStringExtra("data");

        }
    }

    @Override
    protected void initViews() {
        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView)findViewById(R.id.title_txt)).setText("申请红人");
        findViewById(R.id.title_right).setVisibility(View.GONE);
        mWebview = (BridgeHighWebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new WebViewClient());
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (message != null) {
                    //弹出对话框
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    result.cancel();
                }
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    closemDialog();
//                } else {
//                    showmDialog("正在加载中");
//                }
            }
        });
        mWebview.loadUrl(url);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {

    }

}
