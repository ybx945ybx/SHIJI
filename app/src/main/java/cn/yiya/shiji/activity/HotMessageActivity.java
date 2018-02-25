package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.navigation.NewsInfo;
import cn.yiya.shiji.h5.BridgeHighWebView;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ShareDialog;

/**
 * Created by Tom on 2016/4/14.
 */
public class HotMessageActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private BridgeHighWebView mWebview;
    private ImageView ivBack, ivShare;
    private TextView tvTitle;
    private String id;
    private NewsInfo newsInfo;

    private RelativeLayout rlWithoutNet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hot_message);
        id = getIntent().getStringExtra("id");

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        ivShare = (ImageView) findViewById(R.id.title_right);
        mWebview = (BridgeHighWebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        rlWithoutNet = (RelativeLayout) findViewById(R.id.rl_without_net);

    }

    @Override
    protected void initEvents() {
        ivShare.setOnClickListener(this);
        rlWithoutNet.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void init() {
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
                shareNews();
                break;
            case R.id.rl_without_net:
                initData();
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

    private void shareNews() {
        if (!NetUtil.NetAvailable(this)) {
            showTips(Configration.OFF_LINE_TIPS);
            return;
        }
        String url = newsInfo.getShare_info().getUrl();
        new ShareDialog(this,newsInfo.getShare_info().getWx().getTitle(),
                Util.transfer(newsInfo.getShare_info().getWx().getImage()),url,newsInfo.getShare_info().getDes()).build().show();
    }

    private void initData() {
        if (!NetUtil.IsInNetwork(HotMessageActivity.this)) {
            rlWithoutNet.setVisibility(View.VISIBLE);
            mWebview.setVisibility(View.GONE);
            return;
        } else {
            rlWithoutNet.setVisibility(View.GONE);
            mWebview.setVisibility(View.VISIBLE);
        }
        new RetrofitRequest<NewsInfo>(ApiRequest.getApiShiji().getNewsInfo(String.valueOf(id))).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if(msg.isSuccess()){
                            newsInfo = (NewsInfo) msg.obj;
                            tvTitle.setText(newsInfo.getTitle());
                            mWebview.loadUrl(newsInfo.getContent());
                            showPreDialog("正在加载");
                        }
                    }
                }
        );

        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    hidePreDialog();
                }
            }
        });


    }
}
