package cn.yiya.shiji.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.CookiesManager;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.HtmlInfo;
import cn.yiya.shiji.h5.BridgeHighWebView;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;


/**
 * 首页四个按钮跳转至此界面
 * Created by Tom on 2016/3/10.
 */
public class MallHomeSkipWebView extends BaseAppCompatActivity {
    private BridgeHighWebView mWebview;
    public Dialog mProgressDialog;
    private String data;
    private String url;
    private TextView tvTitle;
    private String title;
    private static final int SKIP_NEED_LOGIN = 2;
    private int needLogin;
    private boolean beLogin;
    private static final int LOGIN_RESULT = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_skip_webview);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            data = intent.getStringExtra("data");
            HtmlInfo info = new Gson().fromJson(data, HtmlInfo.class);
            url = info.getUrl();
            title = info.getTitle();
            needLogin = info.getNeed();
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

        if( needLogin == 2 ){
            //检查是否已经登录，如果已经登录直接跳转，没有的话到登录界面
            // 判断是否登录加载webview
            new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        loardWebView();
                        beLogin = true;
                    } else if (msg.isLossLogin()) {
                        Intent intent = new Intent(MallHomeSkipWebView.this, LoginActivity.class);
                        startActivityForResult(intent, LOGIN_RESULT);
                        overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                    } else {
                        if (!NetUtil.IsInNetwork(MallHomeSkipWebView.this)) {
                            setOffNetView(mWebview);
                        }
                    }
                }
            });
        }else {
            beLogin = true;
        }

        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText(title);

        CookiesManager.setUrlCookie(this, url);

        mWebview = (BridgeHighWebView) findViewById(R.id.webview);

        addDefaultNullView();
        initOffLineView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loardWebView();
            }
        });
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebview.addJavascriptInterface(this, "control");

        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (message != null) {
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

        mWebview.setWebViewClient(new WebViewClient() {
        });
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(beLogin){
            mWebview.loadUrl(url + "?from=android");
            beLogin = false;
        }
    }

    private void loardWebView(){
        if(NetUtil.IsInNetwork(this)){
            mWebview.loadUrl(url);
            setSuccessView(mWebview);
        }else {
            setOffNetView(mWebview);
            showTips(Configration.OFF_LINE_TIPS);
        }

    }

    @JavascriptInterface
    public void goAhead(String message) {
//        HtmlInfo info = new Gson().fromJson(message, HtmlInfo.class);
//        Intent intent = new Intent(this, GoodsDetailActivity.class);
//        intent.putExtra("data", new Gson().toJson(info.getData()));
//        startActivity(intent);
        try {
            HtmlInfo info = new Gson().fromJson(message, HtmlInfo.class);
            Intent intent = new Intent(this, NewGoodsDetailActivity.class);
            intent.putExtra("goodsId", new JSONObject(new Gson().toJson(info.getData())).getString("goodsId"));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }catch (JSONException e) {
            e.printStackTrace();
        }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == LOGIN_RESULT){
                beLogin = true;
                Util.getNewUserPullLayer(this, data);
            }
        }else if (resultCode == Activity.RESULT_CANCELED) {
            finish();
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
