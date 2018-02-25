package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.PageFragmentItemAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.NewGoodsObject;
import cn.yiya.shiji.entity.PublishedGoodsInfo;


public class ChooseGoodsActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private TextView tvCancle;
    private RecyclerView mRecyclerView;
    private PageFragmentItemAdapter mAdapter;
    private ArrayList<PublishedGoodsInfo> mList = new ArrayList<>();
    private int offset = 0;
    private String sub_order_num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_goods);

        sub_order_num = getIntent().getStringExtra("orderId");

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        tvCancle = (TextView) findViewById(R.id.title_cancle);
        mRecyclerView = (RecyclerView) findViewById(R.id.goods_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PageFragmentItemAdapter(mList, this, 1);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvents() {
        tvCancle.setOnClickListener(this);
    }

    @Override
    protected void init() {
        getData();
    }

    private void getData() {
    // 获取相应订单号的购买记录
        new RetrofitRequest<NewGoodsObject>(ApiRequest.getApiShiji().getGoodsBoughtList(MapRequest.setOrderNo(sub_order_num, offset)))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            NewGoodsObject info = (NewGoodsObject) msg.obj;
                            if (info.getList() != null) {
                                for (int i = 0; i < info.getList().size(); i++) {
                                    PublishedGoodsInfo goodsInfo = new PublishedGoodsInfo();
                                    goodsInfo.setCover(info.getList().get(i).getCover());
                                    goodsInfo.setBrand(info.getList().get(i).getBrand());
                                    goodsInfo.setPrice(info.getList().get(i).getPrice() + "");
                                    goodsInfo.setTitle(info.getList().get(i).getTitle());
                                    goodsInfo.setSite(info.getList().get(i).getSite());
                                    goodsInfo.setGoods_id(info.getList().get(i).getGoods_id());
                                    mList.add(goodsInfo);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.title_cancle:
                onBackPressed();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
