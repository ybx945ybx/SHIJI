package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.SellerCommissionAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.ShopCommissionInfo;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.FullyLinearLayoutManager;

/**
 * 邀请开店
 * Created by Amy on 2016/10/21.
 */

public class InviteShopActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private int shopId;

    private ImageView ivBack;
    private TextView tvTitle, tvRight;

    private TextView tvOpenMoney, tvInviteMoney;

    private RelativeLayout rlHelp;
    private RelativeLayout rlShareQRCode;
    private RelativeLayout rlInviteFriend;

    private TextView tvSellerNum, tvWages;
    private RecyclerView rvSellerList;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_shop);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            shopId = intent.getIntExtra("shop_id", 0);
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("邀请开店");
        tvRight = (TextView) findViewById(R.id.title_right);
        tvRight.setText("常见问题");

        tvOpenMoney = (TextView) findViewById(R.id.tv_open_money);
        tvInviteMoney = (TextView) findViewById(R.id.tv_invite_money);

        rlHelp = (RelativeLayout) findViewById(R.id.rl_help);
        rlShareQRCode = (RelativeLayout) findViewById(R.id.rl_share_qrcode);
        rlInviteFriend = (RelativeLayout) findViewById(R.id.rl_invite_friend);

        tvSellerNum = (TextView) findViewById(R.id.tv_seller_num);
        tvWages = (TextView) findViewById(R.id.tv_wages);

        rvSellerList = (RecyclerView) findViewById(R.id.rv_seller_list);
        layoutManager = new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSellerList.setLayoutManager(layoutManager);
        rvSellerList.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        rlHelp.setOnClickListener(this);
        rlShareQRCode.setOnClickListener(this);
        rlInviteFriend.setOnClickListener(this);
    }

    @Override
    protected void init() {
        getShopCommissionInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
            case R.id.rl_help:
                Intent intentHelp = new Intent(this, AcceptActivity.class);
                intentHelp.putExtra("type", 4);
                startActivity(intentHelp);
                break;
            case R.id.rl_share_qrcode:
                Intent intentCode = new Intent(this, ShareQRCodeActivity.class);
                intentCode.putExtra("shop_id", shopId);
                startActivity(intentCode);
                break;
            case R.id.rl_invite_friend:
                Intent intentOpenShop = new Intent(this, OpenMyShopActivity.class);
                intentOpenShop.putExtra("type", 2);
                startActivity(intentOpenShop);
                break;
            default:
                break;
        }
    }

    private void getShopCommissionInfo() {
        new RetrofitRequest<ShopCommissionInfo>(ApiRequest.getApiShiji().getShopCommissionInfo())
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            ShopCommissionInfo info = (ShopCommissionInfo) msg.obj;
                            if (msg != null) {
                                tvSellerNum.setText(info.getSeller_num() + "");
                                tvWages.setText(Util.FloatKeepTwo(info.getWages()));
                                tvOpenMoney.setText(Util.FloatKeepTwo(info.getShop_price()));
                                tvInviteMoney.setText(Util.FloatKeepTwo(info.getShare_profit()));
                                List<ShopCommissionInfo.SellerEntity> mList = info.getSeller();
                                if (mList == null || mList.isEmpty()) {
                                    mList = new ArrayList<>();
                                    ShopCommissionInfo.SellerEntity item = new ShopCommissionInfo.SellerEntity();
                                    item.setCommission("暂无");
                                    item.setMobile("暂无");
                                    item.setShop_name("暂无");
                                    mList.add(item);
                                }
                                SellerCommissionAdapter mAdapter = new SellerCommissionAdapter(InviteShopActivity.this, mList);
                                rvSellerList.setAdapter(mAdapter);
                            }
                        }
                    }
                });
    }

}
