package cn.yiya.shiji.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.views.ProgressDialog;

/**
 * Created by chenjian on 2015/10/21.
 */
public class AcceptActivity extends BaseAppCompatActivity {

    public Dialog mProgressDialog;
    private int type;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent != null){
            type = intent.getIntExtra("type", 0);
            if(type == 5){
                url = intent.getStringExtra("url");
            }
        }
        setContentView(R.layout.activity_webview);

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        this.findViewById(R.id.title_right).setVisibility(View.GONE);
        this.findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        switch (type) {
            case 1:
                ((TextView) this.findViewById(R.id.title_txt)).setText("柿集退换货政策");
                break;
            case 2:
                ((TextView) this.findViewById(R.id.title_txt)).setText("用户协议");
                break;
            case 3:
                ((TextView) this.findViewById(R.id.title_txt)).setText("店主指南");
                break;
            case 4:
                ((TextView) this.findViewById(R.id.title_txt)).setText("常见问题");
                break;
            case 5:
                ((TextView) this.findViewById(R.id.title_txt)).setText("现金账户指南");
                break;
            default:
                break;

        }

        WebView webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    closemDialog();
                } else {
                    showmDialog("正在加载中");
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        switch (type) {
            case 1:
                webView.loadUrl("http://shijiapp.cn/index.php/returnpolicy/");
                break;
            case 2:
                webView.loadUrl("file:///android_asset/agreement.html");
                break;
            case 3:
                webView.loadUrl("http://www.shiji-app.com/xiaoshiji_host_guide");
                break;
            case 4:
                webView.loadUrl("http://www.shiji-app.com/xiaoshiji_qa");
                break;
            case 5:
                webView.loadUrl(url);
                break;
            default:
                break;
        }

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {

    }

    public void showmDialog(String str) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.creatRequestDialog(this, str, false);
            mProgressDialog.show();
        }

    }

    public void closemDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
