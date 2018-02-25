package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.CashAccountExplain;
import cn.yiya.shiji.entity.CashAggregationEntity;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/9/28.
 */
public class CashAccountActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;

    private TextView tvAccountSum;

    private RelativeLayout rlytCanCash;
    private TextView tvCanCashSum;
    private RelativeLayout rlytPendingIncome;
    private TextView tvPendingIncomeSum;
    private RelativeLayout rlytCashed;
    private TextView tvCashedSum;
    private RelativeLayout rlytUsed;
    private TextView tvUsed;

    private RelativeLayout rlytZhifubao;

    private TextView tvMore;
    private TextView tvDesc;

    private String url;
    private float canCashSum;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_account);
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("现金账户");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        tvAccountSum = (TextView) findViewById(R.id.tv_account_sum);

        rlytCanCash = (RelativeLayout) findViewById(R.id.rlyt_can_cash);
        tvCanCashSum = (TextView) findViewById(R.id.tv_can_cash_sum);
        rlytPendingIncome = (RelativeLayout) findViewById(R.id.rlyt_pending_income);
        tvPendingIncomeSum = (TextView) findViewById(R.id.tv_pending_income_sum);
        rlytCashed = (RelativeLayout) findViewById(R.id.rlyt_cashed);
        tvCashedSum = (TextView) findViewById(R.id.tv_cashed_sum);
        rlytUsed = (RelativeLayout) findViewById(R.id.rlyt_used);
        tvUsed = (TextView) findViewById(R.id.tv_used_sum);

        rlytZhifubao = (RelativeLayout) findViewById(R.id.rlyt_zhifubao_tixian);

        tvMore = (TextView) findViewById(R.id.tv_more);
        tvDesc = (TextView) findViewById(R.id.tv_des);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        rlytCanCash.setOnClickListener(this);
        rlytPendingIncome.setOnClickListener(this);
        rlytCashed.setOnClickListener(this);
        rlytUsed.setOnClickListener(this);
        rlytZhifubao.setOnClickListener(this);
        tvMore.setOnClickListener(this);

    }

    @Override
    protected void init() {
        getCashAggregation();
        getCashExplain();
    }

    // 获取现金账户详情
    private void getCashAggregation() {
        showPreDialog("");
        new RetrofitRequest<CashAggregationEntity>(ApiRequest.getApiShiji().getCashAggregation()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if (msg.isSuccess()) {
                    CashAggregationEntity entity = (CashAggregationEntity) msg.obj;
                    canCashSum = entity.getCash();
                    tvCanCashSum.setText(Util.FloatKeepTwo(entity.getCash()));
                    tvPendingIncomeSum.setText(Util.FloatKeepTwo(entity.getCash_in()));
                    tvCashedSum.setText(Util.FloatKeepTwo(entity.getBe_money_all()));
                    tvUsed.setText(Util.FloatKeepTwo(entity.getOrder_used_all()));
                    float sum = entity.getCash() + entity.getCash_in() + entity.getBe_money_all() + entity.getOrder_used_all();
                    tvAccountSum.setText(Util.FloatKeepTwo(sum));
                }
            }
        });
    }
    
    // 获取现金账户说明
    private void getCashExplain(){
        new RetrofitRequest<CashAccountExplain>(ApiRequest.getApiShiji().getCashAccountExplainInfo()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){
                    CashAccountExplain obj = (CashAccountExplain) msg.obj;
                    tvDesc.setText(obj.getInfo());
                    url = obj.getUrl();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.rlyt_can_cash:
                Intent intentIncomDetail = new Intent(this, IncomeDetailActivity.class);
                startActivity(intentIncomDetail);
                break;
            case R.id.rlyt_pending_income:
                Intent intentPendIncom = new Intent(this, PendingIncomActivity.class);
                startActivity(intentPendIncom);
                break;
            case R.id.rlyt_cashed:
                Intent intentCashed = new Intent(this, CashedRecordActivity.class);
                intentCashed.putExtra("type", 8);
                startActivity(intentCashed);
                break;
            case R.id.rlyt_used:
                Intent intentUsed = new Intent(this, CashedRecordActivity.class);
                intentUsed.putExtra("type", 9);
                startActivity(intentUsed);
                break;
            case R.id.rlyt_zhifubao_tixian:
                Intent intentWithdraw = new Intent(this, WithdrawCashActivity.class);
                intentWithdraw.putExtra("can_cash_sum", canCashSum);
                startActivity(intentWithdraw);
                break;
            case R.id.tv_more:
                Intent intent = new Intent(this, AcceptActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("type", 5);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
