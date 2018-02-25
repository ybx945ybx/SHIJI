package cn.yiya.shiji.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
public class CashAccountListFragment extends BaseFragment {
    private View mView;
    private RecyclerView mRecyclerView;
    private CashAccountListAdapter mAdapter;
    private CashAccountMonthListAdapter mMonthAdapter;

    // 无内容和无网络背景
    private RelativeLayout rlytDefaultNullView;
    private TextView tvDefaultNull;
    private LinearLayout llytDefaultOffNet;
    private TextView tvReload;

    private int type;                   // 1,2,3是待入账   4，5，6，7是可提现

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cash_account_list, null);
        initViews();
        initEvents();
        init();
        return mView;
    }

    public CashAccountListFragment getInstance(int type) {
        CashAccountListFragment fragment = new CashAccountListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initIntent() {
        type = getArguments().getInt("type");
    }

    @Override
    protected void initViews() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rycv_cash_account_list);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(type == 1 || type == 2 || type == 3) {
            mMonthAdapter = new CashAccountMonthListAdapter(getActivity(), new ArrayList<CashEntity.ListEntity.CashInfoEntity>(), type);
            mRecyclerView.setAdapter(mMonthAdapter);
        }else {
            mAdapter = new CashAccountListAdapter(getActivity(), type);
            mRecyclerView.setAdapter(mAdapter);
        }

        rlytDefaultNullView = (RelativeLayout) mView.findViewById(R.id.rlyt_default_null_view);
        tvDefaultNull = (TextView) mView.findViewById(R.id.tv_default_null);
        llytDefaultOffNet = (LinearLayout) mView.findViewById(R.id.llyt_off_net);
        tvReload = (TextView) mView.findViewById(R.id.tv_reload);

        initDefaultNullview(R.mipmap.nothing_order, "暂无明细", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.tv_reload) {
                    getCashOrderDetail(type);
                }
            }
        });

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {
        getCashOrderDetail(type);
    }

    private void getCashOrderDetail(int type) {
        HashMap map = setMap(type);
        new RetrofitRequest<CashEntity>(ApiRequest.getApiShiji().getCashOrder(map)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    CashEntity obj = (CashEntity) msg.obj;
                    handleMessage(obj);
                } else {
                    if (!NetUtil.IsInNetwork(getActivity())) {
                        setOffNetview(mRecyclerView);
                    }
                }
            }
        });
    }

    private void handleMessage(CashEntity obj) {
        switch (type){
            case 1:
            case 2:
            case 3:
                if(obj.getList() != null && obj.getList().size() > 0){
                    ArrayList<CashEntity.ListEntity.CashInfoEntity> mouthList = new ArrayList<>();
                    for (int i = 0; i < obj.getList().size(); i++){
                        mouthList.addAll(obj.getList().get(i).getCash_info());
                    }
                    mMonthAdapter.setmList(mouthList);
                    mMonthAdapter.notifyDataSetChanged();
                    setSuccessview(mRecyclerView);
                }else {
                    setNullview(mRecyclerView);
                }
                break;
            case 4:
            case 5:
            case 6:
            case 7:
                if(obj.getList() != null && obj.getList().size() > 0){
                    mAdapter.setmList(obj.getList());
                    mAdapter.notifyDataSetChanged();
                    setSuccessview(mRecyclerView);
                }else {
                    setNullview(mRecyclerView);
                }
                break;
        }
    }

    // first_type	是	类型1 (will_in_cash, have_in_cash, be_money， be_used)
    // second_type	是	类型2 (order_make, down_order_make, back_order, other) 返利收入  提成收入  退货  其他
    // 1,2,3是待入账   4，5，6，7是可提现  8是已提现  9是已使用
    private HashMap setMap(int type) {
        HashMap<String, String > map = new HashMap<>();
        switch (type){
            case 1:
                map.put("first_type", "will_in_cash");
                map.put("second_type", "order_make");
                break;
            case 2:
                map.put("first_type", "will_in_cash");
                map.put("second_type", "back_order");
                break;
            case 3:
                map.put("first_type", "will_in_cash");
                map.put("second_type", "down_order_make");
                break;
            case 4:
                map.put("first_type", "have_in_cash");
                map.put("second_type", "order_make");
                break;
            case 5:
                map.put("first_type", "have_in_cash");
                map.put("second_type", "back_order");
                break;
            case 6:
                map.put("first_type", "have_in_cash");
                map.put("second_type", "down_order_make");
                break;
            case 7:
                map.put("first_type", "have_in_cash");
                map.put("second_type", "other");
                break;

        }

        return map;
    }

    // 初始化无内容和无网络提示图标、提示信息和重新加载点击事件
    private void initDefaultNullview(int drawableId, String tips, View.OnClickListener onClickListener){
        tvDefaultNull.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
        tvDefaultNull.setText(tips);
        tvReload.setOnClickListener(onClickListener);
    }

    // 设置无内容背景
    private void setNullview(View contentView){
        rlytDefaultNullView.setVisibility(View.VISIBLE);
        tvDefaultNull.setVisibility(View.VISIBLE);
        llytDefaultOffNet.setVisibility(View.GONE);
        if(contentView!=null) {
            contentView.setVisibility(View.GONE);
        }
    }

    // 设置成功获取数据状态
    private void setSuccessview(View contentView){
        rlytDefaultNullView.setVisibility(View.GONE);
        if(contentView!=null) {
            contentView.setVisibility(View.VISIBLE);
        }
    }

    // 设置无网路背景
    private void setOffNetview(View contentView){
        rlytDefaultNullView.setVisibility(View.VISIBLE);
        tvDefaultNull.setVisibility(View.GONE);
        llytDefaultOffNet.setVisibility(View.VISIBLE);
        if(contentView!=null) {
            contentView.setVisibility(View.GONE);
        }
    }

}
