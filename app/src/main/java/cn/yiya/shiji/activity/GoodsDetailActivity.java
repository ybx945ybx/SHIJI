package cn.yiya.shiji.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.CookiesManager;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.business.ShareTools;
import cn.yiya.shiji.business.YiChuangYun;
import cn.yiya.shiji.entity.GoodsIdInfo;
import cn.yiya.shiji.entity.HtmlInfo;
import cn.yiya.shiji.entity.ShareWxInfo;
import cn.yiya.shiji.entity.ShoppingCartPost;
import cn.yiya.shiji.entity.ShoppingCartPostObject;
import cn.yiya.shiji.h5.BridgeHighWebView;
import cn.yiya.shiji.h5.CallBackFunction;
import cn.yiya.shiji.h5.DefaultHandler;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.views.ShareDialog;

/**
 * 新版商品详情界面
 * Created by Tom on 2016/6/23.
 */
public class GoodsDetailActivity extends BaseAppCompatActivity {

    private BridgeHighWebView mWebView;
    private String data;
    //测试
//    private static final String url = "http://t1.h5.qnmami.com/app/item/html/index.html" + "?android=" + Math.random()*100;
    //正式
    private static final String url = "http://h5.qnmami.com/app/item/html/index.html" + "?android=" + Math.random() * 100;
    private Handler handler;
    public final static int REQUEST_LOGIN = 1000;
    public final static int GOTOGOODSDETAIL = 6;
    public final static int GOTOHOMEISSUE = 4;
    private boolean isLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ShareTools.getInstance().initShare(this);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Uri uri = intent.getData();
            if (uri == null) {
                data = intent.getStringExtra("data");
            } else {
                //从网页跳转
                String goodsId = uri.getQueryParameter("goodsId");
                GoodsIdInfo goodsIdInfo = new GoodsIdInfo();
                goodsIdInfo.setGoodsId(goodsId);
                data = new Gson().toJson(goodsIdInfo);
            }
        }
    }

    @Override
    protected void initViews() {

        mWebView = (BridgeHighWebView) findViewById(R.id.mBhWebView);
        mWebView.setDefaultHandler(new DefaultHandler());

        addDefaultNullView();
        initOffLineView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loardWebView();
            }
        });

        // 设置确保登录态传进来
        CookiesManager.setUrlCookie(this, url);

        mWebView.send(data);

        mWebView.addJavascriptInterface(this, "control");

        loardWebView();
        // 判断是否登录加载webview
//        new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
//            @Override
//            public void onResult(HttpMessage msg) {
//                if (msg.isSuccess()) {
//                    loardWebView();
//                } else if (msg.isLossLogin()) {
//                    Intent intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
//                    startActivityForResult(intent, REQUEST_LOGIN);
//                    overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
//                } else {
//                    if (!NetUtil.IsInNetwork(GoodsDetailActivity.this)) {
//                        setOffNetView(mWebView);
//                    }
//                }
//            }
//        });
        checkLogin();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        mWebView.callHandler("reload", "", new CallBackFunction() {
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

    private void loardWebView() {
        if (NetUtil.IsInNetwork(this)) {
            mWebView.loadUrl(url);
            setSuccessView(mWebView);
        } else {
            setOffNetView(mWebView);
        }

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
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                isLogin = true;
                CookiesManager.setUrlCookie(this, url);
                Message msgHandleObj = new Message();
                msgHandleObj.obj = "";
                msgHandleObj.what = 1;
                handler.sendMessage(msgHandleObj);
            }
        } else {

        }
    }

    // 跳转到商品详情
    @JavascriptInterface
    public void goAhead(String message) {
        HtmlInfo info = new Gson().fromJson(message, HtmlInfo.class);
        switch (info.getType()) {
            case GOTOGOODSDETAIL:               // 跳转商品详情
                Intent intent = new Intent();
                intent.setClass(GoodsDetailActivity.this, GoodsDetailActivity.class);
                intent.putExtra("data", new Gson().toJson(info.getData()));
                startActivity(intent);
                break;
            case GOTOHOMEISSUE:                 // 跳转活动详情
                Intent intent1 = new Intent();
                intent1.setClass(GoodsDetailActivity.this, HomeIssueActivity.class);
                intent1.putExtra("data", message);
                startActivity(intent1);
                break;
        }

    }

    // 在线客服
    @JavascriptInterface
    public void gotoCustomService(String message) {
        if(isLogin){
            gotoService();
        }else {
            Intent intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
            overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
        }
//        checkLgin(3, message);
    }

    private void gotoService() {
        showPreDialog("正在加载客服系统");
        GoodsIdInfo info = new Gson().fromJson(data, GoodsIdInfo.class);
        YiChuangYun.openKF5(YiChuangYun.KF5Chat, this, YiChuangYun.GOODES_DETAIL, info.getGoodsId(), new YiChuangYun.onFinishInitListener() {
            @Override
            public void onFinishInit() {
                hidePreDialog();
            }
        });
    }

    // 点击分享
    @JavascriptInterface
    public void shareItem(String message) {
        ShareWxInfo info = new Gson().fromJson(message, ShareWxInfo.class);
        new ShareDialog(this, info.getShareTimelineTitle(), info.getShareTimelineImage(),
                info.getUrl(), info.getShareAppMessageDesc()).build().show();
    }

    // 返回上一页
    @JavascriptInterface
    public void backToParentPage(String message) {
        onBackPressed();
    }

    // 弹出图片放大层
    @JavascriptInterface
    public void surfImageThumb(String message) {
        Intent intent = new Intent(GoodsDetailActivity.this, HtmlActicity.class);
        intent.putExtra("imageurl", message);
        startActivity(intent);
    }

    // 点击网站，跳网站详情
    @JavascriptInterface
    public void gotoSiteMall(String message) {
        Intent intentGoodsList = new Intent(GoodsDetailActivity.this, NewMallGoodsActivity.class);
        try {
            JSONObject jsonObject = new JSONObject(message);
            intentGoodsList.putExtra("id", jsonObject.getInt("shopId"));
            startActivity(intentGoodsList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 点击品牌，跳转品牌详情
    @JavascriptInterface
    public void gotoBrandDetail(String message) {
        Intent intent = new Intent(GoodsDetailActivity.this, NewSingleBrandActivity.class);
        try {
            JSONObject jsonObject = new JSONObject(message);
            intent.putExtra("brand_id", jsonObject.getInt("brandId"));
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // 点击笔记，跳转笔记详情
    @JavascriptInterface
    public void showWorkDetail(String message) {
        Intent intent = new Intent(GoodsDetailActivity.this, NewWorkDetailsActivity.class);
        try {
            JSONObject jsonObject = new JSONObject(message);
            intent.putExtra("work_id", jsonObject.getInt("workId"));
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 点击品牌晒单，跳转该品牌晒单列表页
    @JavascriptInterface
    public void gotoWorkList(String message) {
        Intent intent = new Intent(GoodsDetailActivity.this, NewSingleBrandActivity.class);
        try {
            JSONObject jsonObject = new JSONObject(message);
            intent.putExtra("brand_id", jsonObject.getInt("brandId"));
            intent.putExtra("type", 2);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // 进入页面，录入浏览记录
    @JavascriptInterface
    public void recordVisitGoods(String message) {
        Intent intent = new Intent(GoodsDetailActivity.this, NewWorkDetailsActivity.class);
        intent.putExtra("work_id", message);
        startActivity(intent);
    }

    // 点击跳转购物车
    @JavascriptInterface
    public void gotoShoppingCart(String message) {
//<<<<<<< HEAD
//        if(isLogin){
//            Intent intent = new Intent(GoodsDetailActivity.this, ShoppingCartActivity.class);
//            startActivity(intent);
//        }else {
//            Intent intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
//            startActivityForResult(intent, REQUEST_LOGIN);
//            overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
//        }
////        checkLgin(1, message);
////        Intent intent = new Intent(GoodsDetailActivity.this, ShoppingCartActivity.class);
////        startActivity(intent);
//=======
        Intent intent = new Intent(GoodsDetailActivity.this, NewShoppingCartActivity.class);
        startActivity(intent);
//>>>>>>> develop3.4
    }

    // 点击直接购买到结算页
    @JavascriptInterface
    public void gotoPay(String message) {
        goPay(message);
    }

    private void goPay(String message){
        Intent intentSubmit = new Intent(GoodsDetailActivity.this, SubmitOrderActivity.class);
        try {
            JSONObject jsonObject = new JSONObject(message);
            ArrayList<ShoppingCartPost> listCart = new ArrayList<>();
            ShoppingCartPost info = new ShoppingCartPost();
            info.setNum(jsonObject.getInt("num"));
            info.setSkuId(jsonObject.getString("skuId"));
            info.setGoodsId(jsonObject.getString("goodsId"));
            try {
                info.setRecommend(jsonObject.getString("recommendId"));
            } catch (Exception e) {

            }
            listCart.add(info);
            ShoppingCartPost[] arrCart = new ShoppingCartPost[listCart.size()];
            for (int i = 0; i < listCart.size(); i++) {
                arrCart[i] = listCart.get(i);
            }
            String str = new Gson().toJson(arrCart);
            String str64 = android.util.Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
            ShoppingCartPostObject object = new ShoppingCartPostObject();
            object.setData(str64);
            object.setFormat("base64");
            String json = new Gson().toJson(object);
            intentSubmit.putExtra("json", json);
            intentSubmit.putExtra("directBuy", true);
            startActivity(intentSubmit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private void checkLogin(){  //  1是跳购物车  2是客服
        new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    isLogin = true;
                } else if (msg.isLossLogin()) {
                    isLogin = false;
                } else {
                    if (!NetUtil.IsInNetwork(GoodsDetailActivity.this)) {
                        setOffNetView(mWebView);
                    }
                }
            }
        });
    }

    @JavascriptInterface
    public void gotoLogin(String message){
        Intent intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
        overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
    }
}
