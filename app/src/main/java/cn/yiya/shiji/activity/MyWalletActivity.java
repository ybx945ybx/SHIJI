package cn.yiya.shiji.activity;
/**
 * 我的钱包界面
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.User;

public class MyWalletActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;

    private RelativeLayout rlytScore;
    private TextView tvScore;
    private RelativeLayout rlytRedPacket;
    private TextView tvPackage;
    private RelativeLayout rlytExchangeCoupon;
    private RelativeLayout rlytExchangeScore;


    private Handler mHandler;
    private User mUser;
    private static final int REQUEST_EXCHANGE_SCORE = 111;
    private static final int REQUEST_EXCHANGE_COUPON = 222;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        mHandler = new Handler();
        initViews();
        initEvents();
        init();
    }

    private void getData(){
        new RetrofitRequest<User>(ApiRequest.getApiShiji().getUserDetail()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            mUser = (User) msg.obj;
                            tvScore.setText("" + mUser.getScore());
                            tvPackage.setText(mUser.getPackages() + "");
                        } else {
                            showTips(msg.message);
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.rlyt_score:
                Intent intent = new Intent(this,ScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.rlyt_red_packet:
                Intent intent1 = new Intent(this,RedPackageActivity.class);
                intent1.putExtra("type",1);
                startActivity(intent1);
                break;
            case R.id.rlyt_exchange_coupon:
                Intent intent2 = new Intent(this,ExcangeCouponActivity.class);
                startActivity(intent2);
                break;
            case R.id.rlyt_exchange_score:
                Intent intent3 = new Intent(this, ExchangeScoreActivity.class);
                startActivityForResult(intent3, REQUEST_EXCHANGE_SCORE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_CANCELED && requestCode == REQUEST_EXCHANGE_SCORE){
            if(requestCode == REQUEST_EXCHANGE_SCORE){
                getData();
            }else if(requestCode == REQUEST_EXCHANGE_COUPON){
                if(data != null){
                    boolean exchanged = data.getBooleanExtra("exchanged", false);
                    if(exchanged){
                        getData();
                    }
                }
            }

        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("我的钱包");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        rlytScore = (RelativeLayout)findViewById(R.id.rlyt_score);
        tvScore = (TextView)findViewById(R.id.tv_score);
        rlytRedPacket =(RelativeLayout)findViewById(R.id.rlyt_red_packet);
        tvPackage = (TextView)findViewById(R.id.tv_red_packet);
        rlytExchangeCoupon =(RelativeLayout)findViewById(R.id.rlyt_exchange_coupon);
        rlytExchangeScore = (RelativeLayout) findViewById(R.id.rlyt_exchange_score);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        rlytScore.setOnClickListener(this);
        rlytRedPacket.setOnClickListener(this);
        rlytExchangeCoupon.setOnClickListener(this);
        rlytExchangeScore.setOnClickListener(this);
    }

    @Override
    protected void init() {
        getData();
    }
}
