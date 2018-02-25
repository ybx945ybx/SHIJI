package cn.yiya.shiji.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.CookiesManager;
import cn.yiya.shiji.views.ProgressDialog;


/**
 * 积分兑换页面
 * Created by Tom on 2016/3/9.
 */
public class ExchangeScoreActivity extends BaseAppCompatActivity {
    public Dialog mProgressDialog;
    WebView mWebview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initViews();
        initEvents();
        init();
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

    public void showmDialog(String str){
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.creatRequestDialog(this, str, false);
            mProgressDialog.show();
        }

    }

    public void closemDialog(){
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    protected void initViews() {
        this.findViewById(R.id.title_right).setVisibility(View.GONE);
        this.findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView)this.findViewById(R.id.title_txt)).setText("积分兑换");

        String url = "http://h5.qnmami.com/points/index.html";
        CookiesManager.setUrlCookie(this, url);

        mWebview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                if(message!=null){
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
                if (newProgress == 100) {
                    closemDialog();
                } else {
                    showmDialog("正在加载中");
                }
            }
        });

        mWebview.setWebViewClient(new WebViewClient(){});

        mWebview.loadUrl(url);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {

    }
}
