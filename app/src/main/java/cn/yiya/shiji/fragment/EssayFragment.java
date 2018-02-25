package cn.yiya.shiji.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.HomeIssueActivity;
import cn.yiya.shiji.activity.LoginActivity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.activity.NewGoodsListActivity;
import cn.yiya.shiji.activity.NewLocalWebActivity;
import cn.yiya.shiji.activity.NewMainActivity;
import cn.yiya.shiji.activity.NewSingleBrandActivity;
import cn.yiya.shiji.activity.RedPackageActivity;
import cn.yiya.shiji.activity.ScoreActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.CookiesManager;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.CommonWebViewEntity;
import cn.yiya.shiji.entity.HtmlInfo;
import cn.yiya.shiji.entity.HtmlThematicInfo;
import cn.yiya.shiji.entity.ShareEntity;
import cn.yiya.shiji.entity.ShareSecondEntity;
import cn.yiya.shiji.h5.BridgeHighWebView;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ShareDialog;

/**
 * Created by Tom on 2016/10/17.
 */
public class EssayFragment extends BaseFragment {

    private View mView;
    private BridgeHighWebView mWebView;
//    private SwipeRefreshLayout swipeRefreshLayout;
//    private LinearLayout llytOffNet;
//    private TextView tvReload;
//    private TextView tvNetOff;
//    private TextView tvWebDefeat;
    private String url;
    private ShareEntity share = new ShareEntity();                      //分享
    private static int REQUEST_LOGIN = 100;
    private NewMainActivity mActivity;
    private long startTime;
    private long currentTime;
    private boolean firstInitComplete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (NewMainActivity) getActivity();
        initIntent();
    }

    private void initIntent() {
        url = getArguments().getString("url");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.essay_fragment, null);
        initViews();
        initEvents();
        init();
        return mView;
    }

    public EssayFragment getInstance(String url){
        EssayFragment fragment = new EssayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    protected void initViews() {
//        llytOffNet = (LinearLayout) mView.findViewById(R.id.llyt_off_net);
//        tvReload = (TextView) mView.findViewById(R.id.tv_reload);
//        tvNetOff = (TextView) mView.findViewById(R.id.tv_net_off);
//        tvWebDefeat = (TextView) mView.findViewById(R.id.tv_webview_defeat);

//        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh);

        mWebView = (BridgeHighWebView) mView.findViewById(R.id.webview);
        ViewGroup.LayoutParams params = mWebView.getLayoutParams();
        params.width = SimpleUtils.getScreenWidth(getActivity());
        if(Util.checkBuildVersonFour()) {
            params.height = SimpleUtils.getScreenHeight(getActivity()) - SimpleUtils.dp2px(getActivity(), 126);
        }else {
            params.height = SimpleUtils.getScreenHeight(getActivity()) - SimpleUtils.dp2px(getActivity(), 129);
        }
        mWebView.setLayoutParams(params);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this, "control");

        // 设置确保登录态传进来
        CookiesManager.setUrlCookie(getActivity(), url);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
//                    firstInitComplete = true;
                } else {
                    // 加载中
//                    if(!firstInitComplete) {
//                        currentTime = System.currentTimeMillis();
//                        long animTime = startTime + 1000;
//                        if (animTime < currentTime) {
//                            setWebDefeat();
//                        }
//                    }
                }

            }
        });

//        mWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
//                mWebView.setVisibility(View.GONE);
//                llytOffNet.setVisibility(View.VISIBLE);
//            }
//        });

        loadUrl(url);

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

    private void loadUrl(String url) {
//        if(!NetUtil.IsInNetwork(getActivity())){
////            setOffNet();
//            return;
//        }
        if(!TextUtils.isEmpty(url)) {
//            startTime = System.currentTimeMillis();
            if (url.contains("?")) {
                mWebView.loadUrl(url + "&android=" + Math.random() * 100);
            } else {
                mWebView.loadUrl(url + "?android=" + Math.random() * 100);
            }
        }else {
            getCommonWebView();
        }
    }

    @Override
    protected void initEvents() {
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mWebView.reload();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//        tvReload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mWebView.setVisibility(View.VISIBLE);
//                llytOffNet.setVisibility(View.GONE);
//                loadUrl(url);
//            }
//        });
    }

    @Override
    protected void init() {

    }

    private void getCommonWebView() {
        new RetrofitRequest<CommonWebViewEntity>(ApiRequest.getApiShiji().getCommonWebView()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){
                    CommonWebViewEntity obj = (CommonWebViewEntity) msg.obj;
                    if(obj.getList() != null && obj.getList().size() > 0){
                        url = obj.getList().get(0).getUrl();
                        startTime = System.currentTimeMillis();
                        if (url.contains("?")) {
                            mWebView.loadUrl(url + "&android=" + Math.random() * 100);
                        } else {
                            mWebView.loadUrl(url + "?android=" + Math.random() * 100);
                        }
                    }
                }
            }
        });
    }

    // 跳转红包页面
    @JavascriptInterface
    public void goRedPackets(String message){
        Intent intent = new Intent(getActivity(), RedPackageActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);

    }

    // 跳转积分页面
    @JavascriptInterface
    public void goIntegral(String message){
        Intent intent1 = new Intent(getActivity(), ScoreActivity.class);
        startActivity(intent1);

    }

    // 分享
    @JavascriptInterface
    public void goToShare() {
        if (share == null) return;
        new ShareDialog(getActivity(), share.getTitle(), share.getCover(), share.getUrl(), share.getDescription(), true).build().show();
    }

    //更新分享信息
    @JavascriptInterface
    public void setShareInfo(String message) {
        if(message.equals("undefined")){
            share = null;
            postHandler(11);
        }else {
            if(share == null){
                share = new ShareEntity();
            }
            ShareSecondEntity shareSecondEntity = new Gson().fromJson(message, ShareSecondEntity.class);
            share.setCover(shareSecondEntity.getShareTimelineImage());
            share.setUrl(shareSecondEntity.getUrl());
            share.setTitle(shareSecondEntity.getShareTimelineTitle());
            share.setDescription(shareSecondEntity.getShareAppMessageDesc());
            postHandler(10);
        }

    }

    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    mActivity.canShare(true);
                    mActivity.needShare = true;
                    break;
                case 11:
                    mActivity.canShare(false);
                    mActivity.needShare = false;
                    break;
            }
        }
    };

    private void postHandler(final int what){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                mHandler.sendEmptyMessage(what);
            }
        }, 1000);
    }

    // 跳转到商品详情
    @JavascriptInterface
    public void goAhead(String message) {
        HtmlInfo info = new Gson().fromJson(message, HtmlInfo.class);
        switch (info.getType()) {
            case 6:               // 跳转商品详情
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(info.getData()));
                    Intent intent1 = new Intent(getActivity(), NewGoodsDetailActivity.class);
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
            case 4:                 // 跳转活动详情
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), HomeIssueActivity.class);
                intent1.putExtra("data", message);
                startActivity(intent1);
                break;
        }

    }

    // 点击品牌，跳转品牌详情
    @JavascriptInterface
    public void gotoBrandDetail(String message) {
        Intent intent = new Intent(getActivity(), NewSingleBrandActivity.class);
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
        Intent intent = new Intent(getActivity(), NewGoodsListActivity.class);
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

    // 登录态
    @JavascriptInterface
    public void gotoLogin(String data) {
        Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intentLogin, REQUEST_LOGIN);
        getActivity().overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);

    }

    // 跳闪购
    @JavascriptInterface
    public void showFlashSales(String message){
        Intent intent = new Intent(getActivity(), HomeIssueActivity.class);
        try {
            JSONObject jsonObject = new JSONObject(message);
            intent.putExtra("flashId", jsonObject.getString("activityId"));
            intent.putExtra("type", 11);
            getActivity().startActivity(intent);
        }catch (Exception e){

        }
    }

    // 跳专题
    @JavascriptInterface
    public void showThematic(String message){
        Intent intent = new Intent(getActivity(), NewLocalWebActivity.class);
        try {
            JSONObject jsonObject = new JSONObject(message);
            HtmlThematicInfo mInfo = new HtmlThematicInfo();
            mInfo.setItemId(jsonObject.getString("activityId"));
            mInfo.setTypeId(jsonObject.getString("typeId"));
            String dataJson = new Gson().toJson(mInfo);
            intent.putExtra("data", dataJson);
            intent.putExtra("type", 2);
            intent.putExtra("url", "http://h5.qnmami.com/app/homeActivities/html/index.html");//正式
            getActivity().startActivity(intent);
        }catch (Exception e){

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {

            }
        }
    }

    public boolean goBackKey() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

}
