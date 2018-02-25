package cn.yiya.shiji.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.CollocationSelectedGoodsListAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;

/**
 * Created by Tom on 2016/7/25.
 */
public class CollocationUsedGoodsFragment extends BaseFragment {

    private View mView;
    private RecyclerView rycvUsedGoods;
    private CollocationSelectedGoodsListAdapter mAdapter;

    private int nOffset;
    private boolean isBottom;
    private int lastVisibleItemPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_collocation_used_goods, null);
        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        rycvUsedGoods = (RecyclerView) mView.findViewById(R.id.rycv_collocation_used_goods);
        rycvUsedGoods.setItemAnimator(new DefaultItemAnimator());
        rycvUsedGoods.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mAdapter = new CollocationSelectedGoodsListAdapter(getActivity(),false);
        rycvUsedGoods.setAdapter(mAdapter);
    }

    @Override
    protected void initEvents() {
        rycvUsedGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取适配器的Item个数以及最后可见的Item
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visivleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                //如果最后可见的item比总数小1，则表示最后一个，这里小3，预加载的意思
                if (visivleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 3 && !isBottom) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                lastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
            }
        });

        mAdapter.setOnItemClickListener(new CollocationSelectedGoodsListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(NewGoodsItem info) {
                Intent intent = new Intent();
                intent.putExtra("info", new Gson().toJson(info));
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }

    @Override
    protected void init() {
        getList();
    }

    private void getList() {
        showPreDialog("");
        nOffset = 0;
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getUsedGoods(
                MapRequest.setMapForty(nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if(msg.isSuccess()){
                    MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                    if (object != null && object.getList().size() > 0) {
                        mAdapter.setmList(object.getList());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        isBottom = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        nOffset += 40;
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getUsedGoods(
                MapRequest.setMapForty(nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if(msg.isSuccess()){
                    MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                    if (object != null && object.getList().size() > 0) {
                        mAdapter.getmList().addAll(object.getList());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        isBottom = true;
                    }
                }
            }
        });
    }
}
