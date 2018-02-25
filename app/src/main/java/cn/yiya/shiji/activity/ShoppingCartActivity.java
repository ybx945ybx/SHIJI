package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.CookiesManager;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.business.YiChuangYun;
import cn.yiya.shiji.entity.HtmlData;
import cn.yiya.shiji.entity.HtmlInfo;
import cn.yiya.shiji.h5.BridgeHighWebView;
import cn.yiya.shiji.h5.CallBackFunction;
import cn.yiya.shiji.utils.NetUtil;

/**
 * 新版本购物车界面
 * Created by Tom on 2016/6/22.
 */
public class ShoppingCartActivity extends BaseAppCompatActivity {

    private BridgeHighWebView mBhWebView;
    private static final String url = "http://h5.qnmami.com/app/cart/html/index.html";
//    private static final String url = "http://h5.qnmami.com/app/cart_swipeout/html/index.html";

    private static final int GOTOGOODSDETAIL = 6;          // 跳商品详情
    private static final int GOTOSUBMIT = 10;              // 跳结算
    private static final int GOTOSITEDETAIL = 11;          // 跳网站详情

    private static final int REQUEST_SUBMIT = 100;
    private static final int REQUEST_LIST = 101;
    private static final int REQUEST_DETAIL = 102;
    private static final int REQUEST_LOGIN = 103;
    private boolean bCreate;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        mBhWebView = (BridgeHighWebView) findViewById(R.id.mBhWebView);
        addDefaultNullView();
        initOffLineView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loardWebView();
            }
        });

        // 设置确保登录态传进来
        CookiesManager.setUrlCookie(this, url);

        WebSettings webSettings = mBhWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mBhWebView.addJavascriptInterface(this, "control");

        mBhWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                if (message != null) {
//                    //弹出对话框
//                    Toast.makeText(ShoppingCartActivity.this, message, Toast.LENGTH_LONG).show();
//                    result.cancel();
//                }
//                return true;
//            }
//
//            @Override
//            public boolean onJsConfirm(WebView view, String url,
//                                       String message, JsResult result) {
//                return super.onJsConfirm(view, url, message, result);
//            }
//
//            @Override
//            public boolean onJsPrompt(WebView view, String url, String message,
//                                      String defaultValue, JsPromptResult result) {
//                return super.onJsPrompt(view, url, message, defaultValue, result);
//            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                } else {
                }
            }
        });

//        mBhWebView.setWebViewClient(new WebViewClient() {
//        });

        // 判断是否登录加载webview
        new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    loardWebView();
                } else if (msg.isLossLogin()) {
                    Intent intent = new Intent(ShoppingCartActivity.this, LoginActivity.class);
                    startActivityForResult(intent, REQUEST_LOGIN);
                    overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                } else {
                    if (!NetUtil.IsInNetwork(ShoppingCartActivity.this)) {
                        setOffNetView(mBhWebView);
                    }
                }
            }
        });


//        bCreate = true;

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        mBhWebView.callHandler("refreshPage", "", new CallBackFunction() {
                            @Override
                            public void onCallBack(String data) {

                            }
                        });
                        break;
                }
                return false;
            }
        });

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
        if (mBhWebView.canGoBack()) {
            mBhWebView.goBack();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @JavascriptInterface
    public void goAhead(String message) {
        HtmlInfo info = new Gson().fromJson(message, HtmlInfo.class);
        HtmlData htmlData = new Gson().fromJson(new Gson().toJson(info.getData()), HtmlData.class);
        switch (info.getType()){
            case GOTOGOODSDETAIL:
                // 跳转到商品详情
                Intent intent = new Intent(this, GoodsDetailActivity.class);
                intent.putExtra("data", new Gson().toJson(info.getData()));
                startActivityForResult(intent, REQUEST_DETAIL);
                break;
            case GOTOSITEDETAIL:
                // 跳转到该网站的商品列表
                Intent intentGoodsList = new Intent(ShoppingCartActivity.this, NewMallGoodsActivity.class);
                intentGoodsList.putExtra("id", htmlData.getSiteId());
                startActivityForResult(intentGoodsList, REQUEST_LIST);
                break;
            case GOTOSUBMIT:
                // 跳转到结算页
                Intent intentSubmit = new Intent(ShoppingCartActivity.this, SubmitOrderActivity.class);
                intentSubmit.putExtra("cartids", getCartIds(htmlData.getShopping_cart_ids()));
                startActivityForResult(intentSubmit, REQUEST_SUBMIT);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SUBMIT:
                    setResult(RESULT_OK);
                    finish();
                    break;
                case REQUEST_DETAIL:
                case REQUEST_LIST:
                    break;
                case REQUEST_LOGIN:
                    loardWebView();
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if(requestCode == REQUEST_LOGIN){
                finish();
            }
        }
    }

    private String getCartIds(String[] ids ){
        String cartIds = "";
        for (int i = 0; i < ids.length; i++) {
            cartIds += ids[i] + ",";
        }
        return (cartIds.substring(0,cartIds.length()-1));
    }

    private void loardWebView(){
        if(NetUtil.IsInNetwork(this)){
//            mBhWebView.loadUrl(url + "?android=" + Math.random() * 100);
            if (url.contains("?")) {
                mBhWebView.loadUrl(url + "&android=" + Math.random() * 100);
            } else {
                mBhWebView.loadUrl(url + "?android=" + Math.random() * 100);
            }
            setSuccessView(mBhWebView);
        }else {
            setOffNetView(mBhWebView);
//            showTips(Configration.OFF_LINE_TIPS);
        }

    }

    // 在线客服
    @JavascriptInterface
    public void gotoCustomService(String message){
        showPreDialog("正在加载客服系统");
        YiChuangYun.openKF5(YiChuangYun.KF5Chat, this, 0, "", new YiChuangYun.onFinishInitListener() {
            @Override
            public void onFinishInit() {
                hidePreDialog();
            }
        });
    }

    // 退换货政策
    @JavascriptInterface
    public void gotoReturnRules(String message){
        Intent intentAccept = new Intent(ShoppingCartActivity.this, AcceptActivity.class);
        intentAccept.putExtra("type", 1);
        startActivity(intentAccept);
    }

    // 返回上一页
    @JavascriptInterface
    public void backToParentPage(String message){
        onBackPressed();
    }

    // 点击去逛逛，跳转到首页
    @JavascriptInterface
    public void goShopping(String message){
        Intent intentHome = new Intent(ShoppingCartActivity.this, NewMainActivity.class);
        intentHome.putExtra("fragid", NewMainActivity.MALL);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentHome);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (bCreate) {
            Message msgHandleObj = new Message();
            msgHandleObj.obj = "";
            msgHandleObj.what = 1;
            handler.sendMessage(msgHandleObj);
        }else {
            bCreate = true;
        }
    }
}
