package cn.yiya.shiji.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.navigation.MapInfo;

/**
 * Created by chenjian on 2016/4/12.
 */
public class MapWebViewActivity extends BaseAppCompatActivity {

    WebView mWebView;
    String url;
    MapInfo info;
    String lat;
    String lng;
    String des;
    String zoom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        mWebView = (WebView) findViewById(R.id.map_webview);
        String data = getIntent().getStringExtra("data");
        if (!TextUtils.isEmpty(data)) {
            info = new Gson().fromJson(data, MapInfo.class);

            lat = info.getPoint().getLatitude();
            lng = info.getPoint().getLongitude();
            zoom = info.getZoom();
            des = info.getDes();
            if (des == null) {
                des = "";
            }
            url = "http://h5.qnmami.com/app/map/html/index.html" + "?lat=" + lat
                    + "&lng=" + lng + "&zoom=" + zoom + "&des=" + des;
            try {
                url = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            WebSettings webSettings = mWebView.getSettings();
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
            mWebView.addJavascriptInterface(this, "control");

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

            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }
            });

            mWebView.loadUrl(url);
        }
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {

    }

    @JavascriptInterface
    public void goNavigation() {
        try {
            String uri = "geo:" + lat + "," + lng + "?q=" + des;// + "&z=" + zoom;
            Uri mUri = Uri.parse(uri);
            Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
            startActivity(mIntent);
        } catch (Exception e) {
            showTips("未安装任何地图应用,无法导航");
        }
    }

    @JavascriptInterface
    public void backToParentPage() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
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
