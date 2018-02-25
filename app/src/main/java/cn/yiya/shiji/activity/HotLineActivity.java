package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.yiya.shiji.R;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.navigation.HotLineInfo;
import cn.yiya.shiji.h5.BridgeHighWebView;
import cn.yiya.shiji.h5.CallBackFunction;
import cn.yiya.shiji.h5.DefaultHandler;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ShareDialog;

/**
 * Created by Tom on 2016/4/14.
 */
public class HotLineActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private BridgeHighWebView mWebview;
    private ImageView ivBack, ivShare;
    private TextView tvTitle;
    private String url;
    private String name;
    private Handler handler;

    private String lineInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        url = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");
        lineInfo = getIntent().getStringExtra("lineInfo");

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
//        findViewById(R.id.title_right).setVisibility(View.INVISIBLE);
        ivShare = (ImageView)findViewById(R.id.title_right);

        mWebview = (BridgeHighWebView) findViewById(R.id.webview);

        mWebview.setDefaultHandler(new DefaultHandler());

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebview.addJavascriptInterface(this, "control");
        mWebview.addJavascriptInterface(this, "NativeInterface");
        mWebview.loadUrl(url);

        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    hidePreDialog();
                }
            }
        });


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                mWebview.callHandler("returnBack", "abc", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        if (data.equals("true")) {
                            finish();
                        }
                    }
                });
                return false;
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
                shareLine();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new Thread() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void shareLine(){
        if(!NetUtil.NetAvailable(this)){
            showTips(Configration.OFF_LINE_TIPS);
            return;
        }
        HotLineInfo hotLineInfo = new Gson().fromJson(lineInfo, HotLineInfo.class);
        String url = hotLineInfo.getShare_info().getUrl() ;
        new ShareDialog(this,hotLineInfo.getShare_info().getWx().getTitle(),
                Util.transfer(hotLineInfo.getShare_info().getWx().getImage()), url, hotLineInfo.getShare_info().getDes()).build().show();
    }
}
