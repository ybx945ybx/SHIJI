package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.HtmlInfo;
import cn.yiya.shiji.entity.NotifyItem;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/1/15.
 */
public class ExcangeCouponActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private EditText etCode;
    private Button btnExchange;
    private TextView tvRecord;
    private LayoutInflater inflater;
    private View layout;
    private View layoutTips;
    private TextView tvContext;
    private TextView tvTips;
    private String data;
    private int needLogin;
    private boolean exchanged;
    private static final int LOGIN_RESULT = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_coupon);
        inflater = getLayoutInflater();
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            data = intent.getStringExtra("data");
        }
    }

    //点击兑换码后的吐司  type =0 失败 1 虚拟物 2 实物
    private void showCustomToast(int type, String msg, String des){
        hideSoftInput();
        setBackgroundAlpha(0.7f);
        switch (type){
            case 0:
                layoutTips = inflater.inflate(R.layout.custom_toast_layout,(ViewGroup)findViewById(R.id.toast_layout_root));
                tvContext = (TextView) layoutTips.findViewById(R.id.toast_context);
                tvContext.setTextColor(Color.parseColor("#3c3c3c"));
                tvContext.setText(msg);
                break;
            case 1:
                layoutTips = inflater.inflate(R.layout.custom_toast_layout,(ViewGroup)findViewById(R.id.toast_layout_root));
                tvContext = (TextView) layoutTips.findViewById(R.id.toast_context);
                tvContext.setTextColor(Color.parseColor("#ff6666"));
                tvContext.setText(msg);
                break;
            case 2:
                layoutTips = inflater.inflate(R.layout.custom_toast_tips_layout,(ViewGroup)findViewById(R.id.toast_layout_tips_root));
                tvContext = (TextView) layoutTips.findViewById(R.id.toast_context);
                tvTips = (TextView) layoutTips.findViewById(R.id.toast_tips);
                tvContext.setText(msg);
                tvTips.setText(des);
                break;
        }
        final Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layoutTips);
        toast.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 1700);
        timer.schedule(new TimerTask() {
            public void run() {
                toast.cancel();
            }
        }, 1500);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    setBackgroundAlpha(1.0f);
                    break;
            }
        }
    };

    //收起软键盘
    private void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etCode.getWindowToken(),0);
    }
    //设置背景透明度
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.edit_code:
                setBackgroundAlpha(1.0f);
                break;
            case R.id.exchange_btn:
                getExchangeCode();
                break;
            case R.id.exchange_record:
                setBackgroundAlpha(1.0f);
                Intent intent = new Intent(this,ExchangeCouponRecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("exchanged", exchanged);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    // 获取兑换码
    private void getExchangeCode(){
        new RetrofitRequest<NotifyItem>(ApiRequest.getApiShiji().exchangeCode(etCode.getText().toString().trim())).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            exchanged = true;
                            NotifyItem object = (NotifyItem) msg.obj;
                            if (object.getStatus() == 2) {
                                showCustomToast(0, object.getMsg(), object.getDes());
                            } else {
                                if (object.getDes() == null) {
                                    showCustomToast(1, object.getMsg(), object.getDes());
                                } else {
                                    showCustomToast(2, object.getMsg(), object.getDes());
                                }
                            }
                        } else {
                            showTips(msg.message);
                        }
                    }
                }
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Activity.RESULT_OK){
            Util.getNewUserPullLayer(this, data);
        }else if (resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("兑换优惠码");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        etCode = (EditText) findViewById(R.id.edit_code);
        btnExchange = (Button) findViewById(R.id.exchange_btn);
        tvRecord = (TextView) findViewById(R.id.exchange_record);
        if(!TextUtils.isEmpty(data)){
            HtmlInfo info = new Gson().fromJson(data, HtmlInfo.class);
            needLogin = info.getNeed();
            if( needLogin == 2 ){
                //检查是否已经登录，如果已经登录直接跳转，没有的话到登录界面
                if(TextUtils.isEmpty(BaseApplication.getInstance().readUserId())){
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_RESULT);
                    overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                }
            }
        }
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        btnExchange.setOnClickListener(this);
        tvRecord.setOnClickListener(this);
        etCode.setOnClickListener(this);

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    btnExchange.setEnabled(true);
                } else {
                    btnExchange.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void init() {

    }
}
