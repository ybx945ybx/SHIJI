package cn.yiya.shiji.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.pingplusplus.android.PaymentActivity;

import java.util.HashMap;
import java.util.Map;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.CMBPayInfo;

/**
 * Created by Tom on 2016/10/18.
 */
public class OpenShopPayActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvNeedPay;
    private RadioButton rbZhifubao;
    private RadioButton rbWeixin;
    private RadioButton rbZhaoshang;
    private TextView tvCommitPay;

    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 招行银行卡支付渠道
     */
    private static final String CHANNEL_CMB = "cmb";

    private static final int REQUEST_CODE_PAYMENT = 200;
    private static final int REQUEST_CODE_CMB = 201;
    private String strOrderNo;
    private String typeId;
    private String typePrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_shop_pay);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            strOrderNo = intent.getStringExtra("strOrderNo");
            typeId = intent.getStringExtra("typeId");
            typePrice = intent.getStringExtra("typePrice");
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("支付");
        findViewById(R.id.title_right).setVisibility(View.GONE);
        tvNeedPay = (TextView) findViewById(R.id.tv_need_pay);
        tvNeedPay.setText(" " + typePrice);
        rbZhifubao = (RadioButton) findViewById(R.id.rb_zhifubao);
        rbWeixin = (RadioButton) findViewById(R.id.rb_weixin);
        rbZhaoshang = (RadioButton) findViewById(R.id.rb_zhaoshang);
        tvCommitPay = (TextView) findViewById(R.id.tv_commit_pay);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvCommitPay.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tv_commit_pay:
                goToPay();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void goToPay() {
        if (!(rbZhifubao.isChecked() || rbWeixin.isChecked() || rbZhaoshang.isChecked())) {
            showTips("请选择支付方式");
            return;
        }

        if (rbZhaoshang.isChecked()) {
            goPayCMB();
            return;
        }

        String channel_id = "";
        if (rbZhifubao.isChecked()) {
            channel_id = CHANNEL_ALIPAY;
        } else if (rbWeixin.isChecked()) {
            channel_id = CHANNEL_WECHAT;
        }

        showPreDialog("正在提交订单");

        Map<String, String> maps = new HashMap<>();
        maps.put("order_no", strOrderNo);
        maps.put("channel", channel_id);
        new RetrofitRequest<>(ApiRequest.getApiShiji().PayShoppingCartListToPing(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        hidePreDialog();
                        if (msg.isSuccess()) {
                            LinkedTreeMap<String, String> result = (LinkedTreeMap<String, String>) msg.obj;
                            String data = new Gson().toJson(result);
                            Intent intent = new Intent();
                            String packageName = getPackageName();
                            ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
                            intent.setComponent(componentName);
                            intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
                            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                        }
                    }
                }
        );
    }

    private void goPayCMB() {
        new RetrofitRequest<CMBPayInfo>(ApiRequest.getApiShiji().getCMBPay(strOrderNo)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    CMBPayInfo object = (CMBPayInfo) msg.obj;
                    if (object != null) {
                        Intent intent = new Intent(OpenShopPayActivity.this, CMBWebViewActivity.class);
                        intent.putExtra("url", object.getUrl());
                        startActivityForResult(intent, REQUEST_CODE_CMB);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PAYMENT:
                    String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                    String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                    String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                    if (result.equals("cancel")) {
                        showTips("你已取消了支付");
                    } else if (result.equals("fail")) {
                        showTips("支付失败");
                    } else if (result.equals("success")) {
                        showTips("支付成功");
                        goNext();
                    } else if (result.equals("invalid")) {
                        showTips("亲!您还没有安装微信哦");
                    }
                    break;
                case REQUEST_CODE_CMB:
                    goNext();
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
//            onBackPressed();
        }

    }

    private void goNext(){
        Intent intent = new Intent(this, EditUserInfoActivity.class);
        intent.putExtra("typeId", typeId);
        startActivity(intent);
        finish();
    }
}
