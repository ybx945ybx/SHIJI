package cn.yiya.shiji.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.YiChuangYun;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.utils.SharedPreUtil;

/**
 * Created by Tom on 2016/8/31.
 */
public class ServiceMessageActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private RelativeLayout rlytHelp;
    private ImageView ivHelpDot;
    private ImageView ivHelpMore;
    private RelativeLayout rlytAdvice;
    private ImageView ivAdviceDot;
    private ImageView ivAdviceMore;

    private MsgReceiver msgReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_messsage);
        //动态注册广播接收器
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.yiya.shiji.pushReciver");
        registerReceiver(msgReceiver, intentFilter);

        initViews();
        initEvents();
        init();
//        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            String type = intent.getStringExtra("type");
            if (!TextUtils.isEmpty(type)) {
                if (type.equals("msg")) {
                    // 在线客服
                    showPreDialog("正在加载客服系统");
                    YiChuangYun.openKF5(YiChuangYun.KF5Chat, this, 0, "", new YiChuangYun.onFinishInitListener() {
                        @Override
                        public void onFinishInit() {
                            hidePreDialog();
                            SharedPreUtil.putString(ServiceMessageActivity.this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_MSG, "");
                            ivHelpMore.setVisibility(View.VISIBLE);
                            ivHelpDot.setVisibility(View.GONE);
                        }
                    });

                } else if (type.equals("id")) {
                    showPreDialog("正在加载客服系统");
                    YiChuangYun.openKF5(YiChuangYun.FeedBackList, this, 0, "", new YiChuangYun.onFinishInitListener() {
                        @Override
                        public void onFinishInit() {
                            hidePreDialog();
                            SharedPreUtil.putString(ServiceMessageActivity.this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_ID, "");
                            ivAdviceMore.setVisibility(View.VISIBLE);
                            ivAdviceDot.setVisibility(View.GONE);
                        }
                    });
                }
            }

        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("客服消息");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        rlytHelp = (RelativeLayout) findViewById(R.id.msg_help);
        ivHelpDot = (ImageView) findViewById(R.id.iv_help_dot);
        ivHelpMore = (ImageView) findViewById(R.id.iv_more_help);
        rlytAdvice = (RelativeLayout) findViewById(R.id.msg_advice);
        ivAdviceDot = (ImageView) findViewById(R.id.iv_advice_dot);
        ivAdviceMore = (ImageView) findViewById(R.id.iv_more_advice);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        rlytAdvice.setOnClickListener(this);
        rlytHelp.setOnClickListener(this);
    }

    @Override
    protected void init() {
        if(!TextUtils.isEmpty(SharedPreUtil.getString(this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_MSG, ""))){
            ivHelpMore.setVisibility(View.GONE);
            ivHelpDot.setVisibility(View.VISIBLE);
        }else {
            ivHelpMore.setVisibility(View.VISIBLE);
            ivHelpDot.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(SharedPreUtil.getString(this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_ID, ""))){
            ivAdviceMore.setVisibility(View.GONE);
            ivAdviceDot.setVisibility(View.VISIBLE);
        }else {
            ivAdviceMore.setVisibility(View.VISIBLE);
            ivAdviceDot.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.msg_advice:
                showPreDialog("正在加载客服系统");
                YiChuangYun.openKF5(YiChuangYun.FeedBackList, this, 0, "", new YiChuangYun.onFinishInitListener() {
                    @Override
                    public void onFinishInit() {
                        hidePreDialog();
                        SharedPreUtil.putString(ServiceMessageActivity.this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_ID, "");
                        ivAdviceMore.setVisibility(View.VISIBLE);
                        ivAdviceDot.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.msg_help:
                showPreDialog("正在加载客服系统");
                YiChuangYun.openKF5(YiChuangYun.KF5Chat, this, 0, "", new YiChuangYun.onFinishInitListener() {
                    @Override
                    public void onFinishInit() {
                        hidePreDialog();
                        SharedPreUtil.putString(ServiceMessageActivity.this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_MSG, "");
                        ivHelpMore.setVisibility(View.VISIBLE);
                        ivHelpDot.setVisibility(View.GONE);
                    }
                });
                break;
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(msgReceiver);
    }

    /**
     * 广播接收器
     * @author len
     *
     */
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("type").equals("msg")){
                ivHelpMore.setVisibility(View.GONE);
                ivHelpDot.setVisibility(View.VISIBLE);
            }else if(intent.getStringExtra("type").equals("id")){
                ivAdviceMore.setVisibility(View.GONE);
                ivAdviceDot.setVisibility(View.VISIBLE);
            }
        }

    }
}
