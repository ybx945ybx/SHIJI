package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ShopTypeAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.CustomEvent;
import cn.yiya.shiji.entity.ChannelInfo;
import cn.yiya.shiji.entity.OrderNo;
import cn.yiya.shiji.entity.PayOrderNoPost;
import cn.yiya.shiji.entity.ShopTypeEntity;
import cn.yiya.shiji.entity.ShoppingCartPost;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * Created by Tom on 2016/10/19.
 */
public class SelectShopTypeActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private RecyclerView rycvShopType;
    private ShopTypeAdapter mAdapter;

    private TextView tvShopIndroduce;
    private TextView tvNext;
    private String typeId;
    private String typePrice;

    private ScrollView scrollView;
    float lastY;
    float maxY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shop_type);
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("选择店铺类型");
        findViewById(R.id.title_right).setVisibility(View.GONE);


        scrollView = (ScrollView) findViewById(R.id.scrollview);
        rycvShopType = (RecyclerView) findViewById(R.id.rycv_shop_type);
        rycvShopType.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rycvShopType.setLayoutManager(linearLayoutManager);
        mAdapter = new ShopTypeAdapter(this);
        rycvShopType.setAdapter(mAdapter);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)rycvShopType.getLayoutParams();
        layoutParams.height = SimpleUtils.getScreenWidth(this) * 427/375;
        layoutParams.topMargin = SimpleUtils.dp2px(this, 16);
        rycvShopType.setLayoutParams(layoutParams);

        tvShopIndroduce = (TextView) findViewById(R.id.tv_introduce);
        tvNext = (TextView) findViewById(R.id.tv_next);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        mAdapter.setOnTypeSelectedListener(new ShopTypeAdapter.OnTypeSelectedListener() {
            @Override
            public void onTypeSelected(String desc, int type_id, String price) {
                tvShopIndroduce.setText(desc);
                typeId = String.valueOf(type_id);
                typePrice = price;
            }
        });

    }

    @Override
    protected void init() {
        getShopType();
    }

    private void getShopType() {
        showPreDialog("");
        new RetrofitRequest<ShopTypeEntity>(ApiRequest.getApiShiji().getShopType()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if (msg.isSuccess()) {
                    ShopTypeEntity object = (ShopTypeEntity) msg.obj;
                    if (object.getList() != null && object.getList().size() > 0) {
                        mAdapter.setmList(object.getList());
                        mAdapter.notifyDataSetChanged();
                        tvShopIndroduce.setText(object.getList().get(0).getDesc());
                        typeId = String.valueOf(object.getList().get(0).getId());
                        typePrice = object.getList().get(0).getPrice();
                    }
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
            case R.id.tv_next:
                commitToService();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    // 提交订单到给服务器得到订单号码
    private void commitToService() {
        showPreDialog("");
        new RetrofitRequest<>(ApiRequest.getApiShiji().openShop(typeId)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if(msg.isSuccess()){
                    OrderNo no = (OrderNo) msg.obj;
                    String strOrderNo = no.getOrder_num();
                    Intent intent = new Intent(SelectShopTypeActivity.this, OpenShopPayActivity.class);
                    intent.putExtra("strOrderNo", strOrderNo);
                    intent.putExtra("typeId", typeId);
                    intent.putExtra("typePrice", typePrice);
                    startActivity(intent);
                }else {
                    showTips(msg.message);
                }
            }
        });
    }
}
