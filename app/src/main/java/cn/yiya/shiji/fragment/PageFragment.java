package cn.yiya.shiji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import cn.yiya.shiji.entity.ShoppingCarSourceObject;
import cn.yiya.shiji.entity.ShoppingCartObject;

public class PageFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private PageFragmentItemAdapter mAdapter;
    private ArrayList<PublishedGoodsInfo> mList = new ArrayList<>();
    private int mPosition;  //0 购买记录 1 购物车
    private int lastVisibleItemPosition;
    private boolean isBottom;
    private View mView;

    public static PageFragment instancePage(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_page, container, false);

        initViews();
        initEvents();
        init();

        return mView;
    }

    @Override
    protected void initViews() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.pageList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PageFragmentItemAdapter(mList, getActivity(), 0);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvents() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取适配器的Item个数以及最后可见的Item
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visivleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (totalItemCount <= 3) {
                    return;
                }
                //如果最后可见的item比总数小1，则表示最后一个，这里小3，预加载的意思
                if (visivleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 3 && !isBottom) {
                    getData(mPosition, mList.size());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                LinearLayoutManager linesrLayoutManager = (LinearLayoutManager) layoutManager;
                lastVisibleItemPosition = linesrLayoutManager.findLastCompletelyVisibleItemPosition();
            }
        });
    }

    @Override
    protected void init() {
        getData(mPosition, 0);
    }

    private void getData(int position, int offset) {
        switch (position) {
            case 0:
                getGoodsBoughtData(offset);
                break;
            case 1:
                getShoppingCartData();
                break;
        }
    }

    //获取购买记录数据
    private void getGoodsBoughtData(int offset) {
        new RetrofitRequest<NewGoodsObject>(ApiRequest.getApiShiji().getGoodsBoughtList(
                MapRequest.setMapTen(offset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    NewGoodsObject info = (NewGoodsObject) msg.obj;
                    if (info.getList() != null && info.getList().size() > 0) {
                        for (int i = 0; i < info.getList().size(); i++) {
                            PublishedGoodsInfo goodsInfo = new PublishedGoodsInfo();
                            goodsInfo.setCover(info.getList().get(i).getCover());
                            goodsInfo.setBrand(info.getList().get(i).getBrand());
                            goodsInfo.setPrice(info.getList().get(i).getPrice() + "");
                            goodsInfo.setTitle(info.getList().get(i).getTitle());
                            goodsInfo.setSite(info.getList().get(i).getSiteTag());
                            goodsInfo.setGoods_id(info.getList().get(i).getGoods_id());
                            mList.add(goodsInfo);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        isBottom = true;
                    }
                }
            }
        });
    }

    //获取购物车数据
    private void getShoppingCartData() {
        ShoppingCarSourceObject source = new ShoppingCarSourceObject();
        source.setSource("1");
        new RetrofitRequest<ShoppingCartObject>(ApiRequest.getApiShiji().getShoppingCartList(source))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            ShoppingCartObject info = (ShoppingCartObject) msg.obj;
                            if (info.getList() != null) {
                                for (int i = 0; i < info.getList().size(); i++) {
                                    for (int j = 0; j < info.getList().get(i).getGoodses().size(); j++) {
                                        PublishedGoodsInfo goodsInfo = new PublishedGoodsInfo();
                                        goodsInfo.setCover(info.getList().get(i).getGoodses().get(j).getCover());
                                        goodsInfo.setBrand(info.getList().get(i).getGoodses().get(j).getBrand());
                                        goodsInfo.setPrice(info.getList().get(i).getGoodses().get(j).getPrice() + "");
                                        goodsInfo.setTitle(info.getList().get(i).getGoodses().get(j).getTitle());
                                        goodsInfo.setSite(info.getList().get(i).getSiteTag());
                                        goodsInfo.setGoods_id(info.getList().get(i).getGoodses().get(j).getGoodsId());
                                        mList.add(goodsInfo);
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {
                                isBottom = true;
                            }
                        }
                    }
                });
    }

}
