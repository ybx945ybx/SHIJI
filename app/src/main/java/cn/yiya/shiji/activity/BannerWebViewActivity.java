package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.yiya.shiji.R;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.views.ShareDialog;

/**
 * Created by Tom on 2016/4/29.
 */
public class BannerWebViewActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private WebView mWebview;
    private ImageView ivBack, ivShare;
    private TextView tvTitle;
    private String url;
    private String title =  "活动详情";
    private String image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_line);

        url = getIntent().getStringExtra("url");
        image = getIntent().getStringExtra("image");

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        showPreDialog("正在加载");
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText(title);
        ivShare = (ImageView)findViewById(R.id.title_right);


        mWebview = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);


        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    hidePreDialog();
                }
            }
        });

        mWebview.addJavascriptInterface(this, "control");
        mWebview.loadUrl(url);
    }

    @Override
    protected void initEvents() {
        ivShare.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
                shareBanner();
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

    private void shareBanner(){
        if(!NetUtil.NetAvailable(this)){
            showTips(Configration.OFF_LINE_TIPS);
            return;
        }
        new ShareDialog(this, title, image, url,title).build().show();
//        ShareTools.getInstance().showShare(this, title, image, url,title);
    }

    @JavascriptInterface
    public void setTitle(String title){
        tvTitle.setText(title);
        //this.title = title;
    }

    /**
     * 跳转搭配详情
     */
    @JavascriptInterface
    public void gotoPairDetail (String workId) {
        Intent intent = new Intent(this, NewMatchDetailActivity.class);
        try {
            JSONObject jsonObject = new JSONObject(workId);
            intent.putExtra("work_id", jsonObject.getInt("workId"));
            startActivity(intent);
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
