package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.CashAccountListAdapter;
import cn.yiya.shiji.adapter.CashAccountMonthListAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.CashEntity;
import cn.yiya.shiji.utils.NetUtil;

/**
 * Created by Tom on 2016/9/30.
 */
public class CashedRecordActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;

    private RecyclerView rycvCashedRecord;
    private CashAccountListAdapter mAdapter;
    private CashAccountMonthListAdapter mMonthAdapter;

    private int type;      // 8是已提现  9是已使用
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashed_record);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        if(type == 8) {
            tvTitle.setText("已提现记录");
        }else if (type == 9){
            tvTitle.setText("已使用记录");
        }
        findViewById(R.id.title_right).setVisibility(View.GONE);

        rycvCashedRecord = (RecyclerView) findViewById(R.id.rycv_cashed);
        rycvCashedRecord.setItemAnimator(new DefaultItemAnimator());
        rycvCashedRecord.setLayoutManager(new LinearLayoutManager(this));
        if(type == 8){
            mMonthAdapter = new CashAccountMonthListAdapter(this, new ArrayList<CashEntity.ListEntity.CashInfoEntity>(), type);
            rycvCashedRecord.setAdapter(mMonthAdapter);
        }else {
            mAdapter = new CashAccountListAdapter(this, type);
            rycvCashedRecord.setAdapter(mAdapter);
        }

        addDefaultNullView();
        initDefaultNullView(R.mipmap.nothing_order, "暂无明细", this);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void init() {
        getCashOrderDetail();
    }

    private void getCashOrderDetail() {
        final HashMap map = setMap();
        new RetrofitRequest<CashEntity>(ApiRequest.getApiShiji().getCashOrder(map)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){
                    CashEntity obj = (CashEntity) msg.obj;
                    if(obj.getList()!= null && obj.getList().size() > 0){
                        if(type == 8){
                            ArrayList<CashEntity.ListEntity.CashInfoEntity> mouthList = new ArrayList<>();
                            for (int i = 0; i < obj.getList().size(); i++){
                                mouthList.addAll(obj.getList().get(i).getCash_info());
                            }
                            mMonthAdapter.setmList(mouthList);
                            mMonthAdapter.notifyDataSetChanged();
                        }else {
                            mAdapter.setmList(obj.getList());
                            mAdapter.notifyDataSetChanged();
                        }
                        setSuccessView(rycvCashedRecord);
                    }else {
                        setNullView(rycvCashedRecord);
                    }
                }else {
                    if(!NetUtil.IsInNetwork(CashedRecordActivity.this)){
                        setOffNetView(rycvCashedRecord);
                    }
                }
            }
        });
    }

    private HashMap setMap(){
        HashMap<String, String> map = new HashMap<>();
        switch (type){
            case 8:
                map.put("first_type", "be_money");
                map.put("second_type", "order_make");
                break;
            case 9:
                map.put("first_type", "be_used");
                map.put("second_type", "order_make");
                break;
        }
        return map;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tv_reload:
                getCashOrderDetail();
                break;
        }
    }
}
