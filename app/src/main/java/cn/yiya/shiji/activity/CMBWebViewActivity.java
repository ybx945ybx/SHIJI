package cn.yiya.shiji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import cmb.pb.util.CMBKeyboardFunc;
import cn.yiya.shiji.R;
import cn.yiya.shiji.business.CookiesManager;

/**
 * Created by chenjian on 2016/4/20.
 */
public class CMBWebViewActivity extends BaseAppCompatActivity implements View.OnClickListener{

    private TextView tvTitle;
    private ImageView ivBack;
    private TextView tvRight;
    private WebView mWebView;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cmb_webview);
        url = getIntent().getStringExtra("url");

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("银行卡安全支付");
        tvRight = (TextView) findViewById(R.id.title_right);
        tvRight.setVisibility(View.GONE);
        ivBack = (ImageView) findViewById(R.id.title_back);

        mWebView = (WebView) findViewById(R.id.cmb_webview);

        WebSettings set = mWebView.getSettings();
        // 支持JS
        set.setJavaScriptEnabled(true);
        set.setSaveFormData(false);
        set.setSupportZoom(false);

        mWebView.addJavascriptInterface(this, "NativeInterface");

        loadUrl();
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    private void loadUrl() {
        CookiesManager.setUrlCookie(this, url);
        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 使用当前的WebView加载页面
                CMBKeyboardFunc kbFunc = new CMBKeyboardFunc(CMBWebViewActivity.this);
                if (kbFunc.HandleUrlCall(mWebView, url) == false) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    return true;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBackKey();
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
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @JavascriptInterface
    public void finishWebview() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
