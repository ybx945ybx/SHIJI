package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import cn.yiya.shiji.R;
import cn.yiya.shiji.h5.BridgeHighWebView;

/**
 * Created by Tom on 2016/8/4.
 */
public class RedPeopleApplyActivity extends BaseAppCompatActivity {

    private BridgeHighWebView mWebview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_people_apply);
        initViews();
        initEvents();
        init();
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
        mWebview.loadUrl("http://h5.qnmami.com/app/redsApplication/html/index.html");
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void goBackKey() {
        if (mWebview.canGoBack()) {
            mWebview.goBack();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @JavascriptInterface
    public void setTitle(String title){

    }
}
