package cn.yiya.shiji.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.CollocationSelectedFirstSecondAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.HotCategoryFirstSecondObject;
import cn.yiya.shiji.views.NoAlphaItemAnimator;

/**
 * Created by Tom on 2016/7/21.
 */
public class CollocationSelectedGoodsFragment extends BaseFragment {

    private View mView;

    private RecyclerView rycvFirstSecond;
    private CollocationSelectedFirstSecondAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_collocation_selected_goods, null);
        intIntent();
        initViews();
        initEvents();
        init();
        return mView;
    }

    private void intIntent() {

    }

    @Override
    protected void initViews() {
        rycvFirstSecond = (RecyclerView) mView.findViewById(R.id.expListView);
        rycvFirstSecond.setItemAnimator(new NoAlphaItemAnimator());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rycvFirstSecond.setLayoutManager(linearLayoutManager);
        mAdapter = new CollocationSelectedFirstSecondAdapter(getActivity());
        rycvFirstSecond.setAdapter(mAdapter);
    }

    @Override
    protected void initEvents() {
        mAdapter.setExpandCollapseListener(new CollocationSelectedFirstSecondAdapter.ExpandCollapseListener() {
            @Override
            public void Expand(int position) {
                linearLayoutManager.scrollToPositionWithOffset(position, 0);
            }

            @Override
            public void Collapse(int position) {
                linearLayoutManager.scrollToPositionWithOffset(0, 0);
            }
        });
    }

    @Override
    protected void init() {
        new RetrofitRequest<HotCategoryFirstSecondObject>(ApiRequest.getApiShiji().getHotCategoryFirstSecond("1")).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){
                    HotCategoryFirstSecondObject object = (HotCategoryFirstSecondObject)msg.obj;
                    if(object.getList() != null && object.getList().size() > 0){
                        mAdapter.setmList(object.getList());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
