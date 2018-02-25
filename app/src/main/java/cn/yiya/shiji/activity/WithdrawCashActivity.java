package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.RegisterInfo;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.RegexValidateUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;

/**
 * Created by Tom on 2016/9/29.
 */
public class WithdrawCashActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;

    private TextView tvCanCashSum;
    private EditText etCashSum;
    private TextView tvTips;

    private EditText etZhifubaoName;
    private EditText etZhifubaoNum;
    private EditText etPhone;
    private EditText etCode;
    private TextView tvCode;

    private TextView tvCommit;

    private TimeCounter timer;
    private float canCashSum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_cash);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        canCashSum = intent.getFloatExtra("can_cash_sum", 0);

    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("提现");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        tvCanCashSum = (TextView) findViewById(R.id.tv_can_cash_sum);
        tvCanCashSum.setText(Util.FloatKeepTwo(canCashSum));
        tvTips = (TextView) findViewById(R.id.tv_tips);
        tvTips.setVisibility(View.GONE);
        etCashSum = (EditText) findViewById(R.id.et_cash_sum);

        etZhifubaoName = (EditText) findViewById(R.id.et_zhifubao_name);
        etZhifubaoNum = (EditText) findViewById(R.id.et_zhifubao_num);
        etPhone = (EditText) findViewById(R.id.et_telephone_num);
        etCode = (EditText) findViewById(R.id.et_code_num);

        tvCode = (TextView) findViewById(R.id.tv_code);
        tvCommit = (TextView) findViewById(R.id.tv_commit);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvCode.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
        etCashSum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)) {
                    tvTips.setVisibility(View.GONE);
                    tvCommit.setEnabled(false);
                }else {
                    if (Integer.parseInt(s.toString()) > canCashSum) {
                        tvTips.setVisibility(View.VISIBLE);
                        tvTips.setText("已超出可提现金额范围");
                        tvCommit.setEnabled(false);
                    }else if(Integer.parseInt(s.toString()) < 100){
                        tvTips.setVisibility(View.VISIBLE);
                        tvTips.setText("可提现金额不小于100元");
                        tvCommit.setEnabled(false);
                    }else {
                        tvTips.setVisibility(View.GONE);
                        tvCommit.setEnabled(true);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
            case R.id.tv_code:
                getCode();
                break;
            case R.id.tv_commit:
                withdrawCommit();
                break;
        }

    }

    private void getCode() {
        if(!NetUtil.IsInNetwork(this)){
            showTips(Configration.OFF_LINE_TIPS);
            return;
        }
        verifyPhone(etPhone.getText().toString().trim());

        if (timer != null) {
            showTips("验证码下发需要时间，请耐心等待！");
            return;
        }

        HashMap<String, String> maps = new HashMap<>();
        maps.put("mobile", etPhone.getText().toString().trim());
        maps.put("mode", "cash");

        new RetrofitRequest<RegisterInfo>(ApiRequest.getApiShiji().getVerifyCode(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            timer = new TimeCounter(60 * 1000, 1000);
                            timer.start();
                        }

                    }
                }
        );
    }

    private void withdrawCommit() {
        if(!NetUtil.IsInNetwork(this)){
            showTips(Configration.OFF_LINE_TIPS);
            return ;
        }
        String sum = etCashSum.getText().toString().trim();
        String zhifubaoName = etZhifubaoName.getText().toString();
        String zhifubao = etZhifubaoNum.getText().toString();
        String phone = etPhone.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        if(TextUtils.isEmpty(zhifubaoName)){
            showTips("请填写支付宝姓名");
            return ;
        }
        if(TextUtils.isEmpty(zhifubao)){
            showTips("请填写支付宝账号");
            return ;
        }
        if (TextUtils.isEmpty(phone)) {
            showTips("请输入手机号");
            return ;
        }
        if (!RegexValidateUtil.isMobileNO(phone)) {
            showTips("手机号输入不正确，请检查");
            return ;
        }
        if (TextUtils.isEmpty(code)) {
            showTips("请输入验证码");
            return ;
        }
        if (code.length() != 6) {
            showTips("请输入6位验证码");
            return ;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("code", code);
        map.put("amount", sum);
        map.put("alipay_account", zhifubao);
        map.put("alipay_name", zhifubaoName);
        showPreDialog("");

        new RetrofitRequest<>(ApiRequest.getApiShiji().cashBeMoney(map)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if (msg.isSuccess()) {
                    showCustomSingleDialog("提现申请已提交，请等待处理！", new ProgressDialog.DialogClick() {
                        @Override
                        public void OkClick() {
                            finish();
                        }

                        @Override
                        public void CancelClick() {

                        }
                    });
                } else {
                    showTips(msg.message);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }



    // 计时器
    class TimeCounter extends CountDownTimer{

        public TimeCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvCode.setText(millisUntilFinished / 1000 + "s");

        }

        @Override
        public void onFinish() {
            tvCode.setText("发送验证码");
            timer = null;

        }
    }

    //验证手机号输入
    private void verifyPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            showTips("请输入手机号");
            return ;
        }
        if (!RegexValidateUtil.isMobileNO(phone)) {
            showTips("手机号输入不正确，请检查");
            return ;
        }

    }

    //验证验证码输入
    private void verifyCode(String code) {
        if (TextUtils.isEmpty(code)) {
            showTips("请输入验证码");
            return ;
        }
        if (code.length() != 6) {
            showTips("请输入6位验证码");
            return ;
        }
    }


}
