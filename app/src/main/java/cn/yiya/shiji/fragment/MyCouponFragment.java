package cn.yiya.shiji.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CouponDetailActivity;
import cn.yiya.shiji.activity.MyCouponActivity;
import cn.yiya.shiji.adapter.CouponAdapter;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.navigation.CouponDetailInfo;
import cn.yiya.shiji.utils.NetUtil;

/**
 * Created by Tom on 2016/4/6.
 */
public class MyCouponFragment extends BaseFragment {
    private RecyclerView rycvCoupon;
    private CouponAdapter couponAdapter;
    private int mPosition;
    private ArrayList<CouponDetailInfo> countryCouponList;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View mView;

    public static MyCouponFragment instanceFragment(int position){
        Bundle args = new Bundle();
        args.putInt("position", position);
        MyCouponFragment myCouponFragment = new MyCouponFragment();
        myCouponFragment.setArguments(args);
        return myCouponFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_coupon, null);

        initViews();
        initEvents();
        init();

        return mView;
    }

    @Override
    protected void initViews() {
        rycvCoupon = (RecyclerView)mView.findViewById(R.id.coupon_list);
        rycvCoupon.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout = (SwipeRefreshLayout)mView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rycvCoupon.setLayoutManager(linearLayoutManager);
        couponAdapter = new CouponAdapter(getActivity(), countryCouponList);
        rycvCoupon.setAdapter(couponAdapter);
    }

    @Override
    protected void initEvents() {
        // 刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                couponAdapter.notifyDataSetChanged();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        // item的点击
        couponAdapter.setOnItemClickListener(new CouponAdapter.OnItemClickListener(){

            @Override
            public void OnItemClick(CouponDetailInfo info) {
                if(MyCouponActivity.netState && !NetUtil.IsInNetwork(getActivity())){
                    showToast(Configration.OFF_LINE_TIPS);
                    return;
                }
                BaseApplication.setCouponDetailInfo(info);
                Intent intent = new Intent(getActivity(), CouponDetailActivity.class);
                intent.putExtra("couponId", info.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void init() {

    }

    public ArrayList<CouponDetailInfo> getCountryCouponList() {
        return countryCouponList;
    }

    public void setCountryCouponList(ArrayList<CouponDetailInfo> countryCouponList) {
        this.countryCouponList = countryCouponList;
    }
}
