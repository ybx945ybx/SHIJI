package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.CookiesManager;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.GoodsIdInfo;
import cn.yiya.shiji.entity.HtmlInfo;
import cn.yiya.shiji.entity.HtmlThematicInfo;
import cn.yiya.shiji.entity.IssueActivityDetailInfo;
import cn.yiya.shiji.entity.MainBannerInfo;
import cn.yiya.shiji.entity.ShareEntity;
import cn.yiya.shiji.entity.ShareSecondEntity;
import cn.yiya.shiji.h5.BridgeHighWebView;
import cn.yiya.shiji.h5.CallBackFunction;
import cn.yiya.shiji.h5.DefaultHandler;
import cn.yiya.shiji.views.ShareDialog;

/**
 * Created by chenjian on 2016/6/14.
 */
public class NewLocalWebActivity extends BaseAppCompatActivity implements View.OnClickListener {
//    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout toolbar;
    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivRight;
    private BridgeHighWebView mWebView;
//    private LinearLayout llytOffNet;
//    private TextView tvReload;
//    private TextView tvNetOff;
//    private TextView tvWebDefeat;
    private String url;
    private String data;
    private int type = 0;
    private MainBannerInfo.ListEntity bannerInfo;    //活动
    private HtmlThematicInfo info;                  //专题
    private ShareEntity share;                      //分享
    private final static int REQUEST_CODE = 1000;
    private final static int REQUEST_WELFARE = 1001;
    private Handler handler;
    private long startTime;
    private long currentTime;
    private boolean firstInitComplete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_local_webview);
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
//        llytOffNet = (LinearLayout) findViewById(R.id.llyt_off_net);
//        tvReload = (TextView) findViewById(R.id.tv_reload);
//        tvNetOff = (TextView) findViewById(R.id.tv_net_off);
//        tvWebDefeat = (TextView) findViewById(R.id.tv_webview_defeat);

//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        toolbar = (RelativeLayout) findViewById(R.id.ll_title);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        ivBack = (ImageView) findViewById(R.id.title_back);
        ivRight = (ImageView) findViewById(R.id.title_right);
        mWebView = (BridgeHighWebView) findViewById(R.id.new_webview);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        ivRight.setOnClickListener(this);
//        tvReload.setOnClickListener(this);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mWebView.reload();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        if (intent == null) return;

        url = getIntent().getStringExtra("url");
        data = getIntent().getStringExtra("data");
        type = getIntent().getIntExtra("type", 0);

        // 2表示从优惠活动专题页面进来， 4表示从首页活动图进来   203 是消息推送   204 广告图
        if (type == 2) {
            toolbar.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(data)) {
                info = new Gson().fromJson(data, HtmlThematicInfo.class);
                getShopActivityDetail(Integer.valueOf(info.getItemId()));
                mWebView.setDefaultHandler(new DefaultHandler());
                mWebView.send(data);
            }
        } else if (type == 203) {
            toolbar.setVisibility(View.VISIBLE);
            tvTitle.setText(getIntent().getStringExtra("title"));
            String hshare = getIntent().getStringExtra("hshare");
            if(TextUtils.isEmpty(hshare)){
                findViewById(R.id.title_right).setVisibility(View.GONE);
            }else {
                share = new Gson().fromJson(getIntent().getStringExtra("hshare"), ShareEntity.class);
            }

        } else if (type == 204) {
            toolbar.setVisibility(View.VISIBLE);
            tvTitle.setText(getIntent().getStringExtra("title"));
            share = new Gson().fromJson(data, ShareEntity.class);

        } else {
            toolbar.setVisibility(View.VISIBLE);
            tvTitle.setText(getIntent().getStringExtra("title"));
            if (type == 4) {
                share = new Gson().fromJson(data, ShareEntity.class);
            } else if (!TextUtils.isEmpty(data)) {
                bannerInfo = new Gson().fromJson(data, MainBannerInfo.ListEntity.class);
                if (bannerInfo != null) {
                    share = bannerInfo.getShare();
                }
            }
        }

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.addJavascriptInterface(this, "control");

        // 设置确保登录态传进来
        CookiesManager.setUrlCookie(this, url);
        loadUrl(url);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
//                    firstInitComplete = true;
                } else {
                    // 加载中
//                    if(!firstInitComplete) {
//                        showTips("在加载");
//                        currentTime = System.currentTimeMillis();
//                        long animTime = startTime + 1000;
//                        if (animTime < currentTime) {
//                            setWebDefeat();
//                        }
//                    }
                }

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        mWebView.callHandler("appLoginSuccess", "", new CallBackFunction() {
                            @Override
                            public void onCallBack(String data) {

                            }
                        });
                        break;
                    case 2:
                        onBackPressed();
                        break;
                }
                return false;
            }
        });
    }

//    private void setWebDefeat(){
//        mWebView.setVisibility(View.GONE);
//        llytOffNet.setVisibility(View.VISIBLE);
//        tvWebDefeat.setVisibility(View.VISIBLE);
//        tvNetOff.setVisibility(View.GONE);
//    }
//
//    private void setOffNet(){
//        mWebView.setVisibility(View.GONE);
//        llytOffNet.setVisibility(View.VISIBLE);
//        tvNetOff.setVisibility(View.VISIBLE);
//        tvWebDefeat.setVisibility(View.GONE);
//    }

    @JavascriptInterface
    public void goAhead(String data) {
        HtmlInfo info = new Gson().fromJson(data, HtmlInfo.class);
        switch (info.getType()) {
            case 4:                 // 点击活动图片及查看更多  跳转活动详情页
                Intent intent = new Intent(NewLocalWebActivity.this, HomeIssueActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
                break;
            case 6:                 // 点击商品，跳转商品详情页
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(info.getData()));
                    Intent intent1 = new Intent(NewLocalWebActivity.this, NewGoodsDetailActivity.class);
                    intent1.putExtra("goodsId", jsonObject.getString("goodsId"));
                    if(jsonObject.has("group")){
                        intent1.putExtra("group", jsonObject.getInt("group"));
                    }
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                Intent intent2 = new Intent(NewLocalWebActivity.this, CollocationSelectedGoodsListActivity.class);
                GoodsIdInfo goodsId = new Gson().fromJson(info.getData().toString(), GoodsIdInfo.class);
                intent2.putExtra("goodsId", goodsId.getGoodsId());
                intent2.putExtra("isSimilar", true);
                startActivity(intent2);
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                mWebView.reload();
            } else if (requestCode == REQUEST_WELFARE) {
                Message msgHandleObj = new Message();
                msgHandleObj.obj = "";
                msgHandleObj.what = 1;
                handler.sendMessage(msgHandleObj);

            }
        }
    }

    @JavascriptInterface
    public void backToParentPage(String data) {
        Message messageBack = new Message();
        messageBack.what = 2;
        messageBack.obj = "";
        handler.sendMessage(messageBack);
//        onBackPressed();
    }

    @JavascriptInterface
    public void shareActivities(String data) {
        goToShare();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            //分享
            case R.id.title_right:
                goToShare();
                break;
//            case R.id.tv_reload:
//                mWebView.setVisibility(View.VISIBLE);
//                llytOffNet.setVisibility(View.GONE);
//                mWebView.resumeTimers();
//                loadUrl(url);
//                break;
            default:
                break;
        }
    }

    private void loadUrl(String url) {
//        if(!NetUtil.IsInNetwork(this)){
//            setOffNet();
//            return;
//        }
//        startTime = System.currentTimeMillis();
        if (url.contains("?")) {
            mWebView.loadUrl(url + "&android=" + Math.random() * 100);
        } else {
            mWebView.loadUrl(url + "?android=" + Math.random() * 100);
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
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void getShopActivityDetail(int id) {
        new RetrofitRequest<IssueActivityDetailInfo>(ApiRequest.getApiShiji().getIssueActivityDetail(String.valueOf(id)))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            IssueActivityDetailInfo obj = (IssueActivityDetailInfo) msg.obj;
                            if (obj != null || obj.getShare() != null) {
                                share = obj.getShare();
                            }
                        }
                    }
                });
    }


    /**
     * 分享
     */
    private void goToShare() {
        if (share == null) return;
        new ShareDialog(this, share.getTitle(), share.getCover(), share.getUrl(), share.getDescription()).build().show();
    }

    /**
     * 领取新人红包
     */
    @JavascriptInterface
    public void gotoLogin(String data) {
        Intent intentLogin = new Intent(NewLocalWebActivity.this, LoginActivity.class);
        startActivityForResult(intentLogin, REQUEST_WELFARE);
        overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);

    }

    /**
     * 跳转搭配详情
     */
    @JavascriptInterface
    public void gotoPairDetail(String workId) {
        Intent intent = new Intent(this, NewMatchDetailActivity.class);
        try {
            JSONObject jsonObject = new JSONObject(workId);
            intent.putExtra("work_id", jsonObject.getInt("workId"));
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新分享信息
     */
    @JavascriptInterface
    public void setShareInfo(String message) {
        ShareSecondEntity shareSecondEntity = new Gson().fromJson(message, ShareSecondEntity.class);
        share.setCover(shareSecondEntity.getShareTimelineImage());
        share.setUrl(shareSecondEntity.getUrl());
        share.setTitle(shareSecondEntity.getShareTimelineTitle());
        share.setDescription(shareSecondEntity.getShareAppMessageDesc());

    }

    // 点击品牌，跳转品牌详情
    @JavascriptInterface
    public void gotoBrandDetail(String message) {
        Intent intent = new Intent(this, NewSingleBrandActivity.class);
        try {
            JSONObject jsonObject = new JSONObject(message);
            intent.putExtra("brand_id", jsonObject.getInt("brandId"));
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // 跳转到分类  商品列表
    @JavascriptInterface
    public void gotoGoodsClassify(String message) {
        Intent intent = new Intent(this, NewGoodsListActivity.class);
        HtmlInfo info = new Gson().fromJson(message, HtmlInfo.class);
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(info.getData()));
            String id = jsonObject.getString("secondId");
            intent.putExtra("source", 1002);
            intent.putExtra("id", Integer.parseInt(id));
            intent.putExtra("title", jsonObject.getString("name"));
            intent.putExtra("categoryid", String.valueOf(jsonObject.getString("genderId")));
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // 跳转红包页面
    @JavascriptInterface
    public void goRedPackets(String message){
        Intent intent = new Intent(this, RedPackageActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);

    }

    // 跳转积分页面
    @JavascriptInterface
    public void goIntegral(String message){
        Intent intent1 = new Intent(this, ScoreActivity.class);
        startActivity(intent1);

    }

    // 跳闪购
    @JavascriptInterface
    public void showFlashSales(String message){
        Intent intent = new Intent(NewLocalWebActivity.this, HomeIssueActivity.class);
        try {
            JSONObject jsonObject = new JSONObject(message);
            intent.putExtra("flashId", jsonObject.getString("activityId"));
            intent.putExtra("type", 11);
            startActivity(intent);
        }catch (Exception e){

        }
    }

    // 跳专题
    @JavascriptInterface
    public void showThematic(String message){
        Intent intent = new Intent(NewLocalWebActivity.this, NewLocalWebActivity.class);
        try {
            JSONObject jsonObject = new JSONObject(message);
            HtmlThematicInfo mInfo = new HtmlThematicInfo();
            mInfo.setItemId(jsonObject.getString("activityId"));
            mInfo.setTypeId(jsonObject.getString("typeId"));
            String dataJson = new Gson().toJson(mInfo);
            intent.putExtra("data", dataJson);
            intent.putExtra("type", 2);
            intent.putExtra("url", "http://h5.qnmami.com/app/homeActivities/html/index.html");//正式
            startActivity(intent);
        }catch (Exception e){

        }
    }
}
