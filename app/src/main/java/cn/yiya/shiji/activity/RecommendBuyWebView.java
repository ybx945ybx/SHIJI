package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.h5.BridgeHighWebView;

/**
 * Created by Tom on 2016/4/21.
 */
public class RecommendBuyWebView extends BaseAppCompatActivity implements View.OnClickListener{
    private BridgeHighWebView mWebview;
    private ImageView ivBack;
    private TextView tvTitle;
    private String url;
    private String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_line);

        url = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        showPreDialog("正在加载");
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText(name);
        findViewById(R.id.title_right).setVisibility(View.INVISIBLE);

        mWebview = (BridgeHighWebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        mWebview.addJavascriptInterface(this, "NativeInterface");
        mWebview.loadUrl(url);

        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    hidePreDialog();
                }
            }
        });

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
        }
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
}
