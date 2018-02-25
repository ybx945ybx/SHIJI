package cn.yiya.shiji.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ShopOrderListAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.OrderListObject;
import cn.yiya.shiji.utils.NetUtil;

/**
 * 店铺订单
 * Created by Amy on 2016/10/19.
 */

public class ShopOrderListFragment extends BaseFragment implements View.OnClickListener {

    private boolean isVisiable = false;
    private boolean isInitView = false;
    private Context mContext;
    private View mView;
    private int status;    //0:全部,1:待付款,2:在途,3:待缴税,4:完成,5:失效
    private int shopId;

    private RecyclerView recyclerView;
    private ShopOrderListAdapter mAdapter;
    private int nOffset;
    private boolean isBottom;
    private static final int LIMIT_TEN = 10;

    private int lastVisibleItemPosition;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getInt("status");
            shopId = bundle.getInt("shop_id", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_shop_order_list, container, false);
        initViews();
        initEvents();
        isInitView = true;
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //无网络状态
        setMainView(mView);
        initDefaultNullView(R.mipmap.nothing_order, "暂无相关订单", this, 0, 230);
    }

    @Override
    protected void initEvents() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取适配器的Item个数以及最后可见的Item
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                // 如果最后可见的Item比总数小1，表示最后一个，预加载的意思
                if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) == totalItemCount - 1) && !isBottom) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });

    }

    @Override
    protected void init() {
        if (isVisiable && isInitView) {
            getOrderList();
        }
    }

    public ShopOrderListFragment getInstance(int shopId, int status) {
        ShopOrderListFragment fragment = new ShopOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        bundle.putInt("shop_id", shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisiable = true;
            init();
        } else {
            isVisiable = false;
        }
    }

    private void getOrderList() {
        nOffset = 0;
        isBottom = false;
        final HashMap<String, String> maps = new HashMap<>();
        maps.put("status", String.valueOf(status));
        maps.put("offset", String.valueOf(nOffset));
        maps.put("limit", String.valueOf(LIMIT_TEN));
        maps.put("shop_id", String.valueOf(shopId));

        showPreDialog("正在加载");
        new RetrofitRequest<OrderListObject>(ApiRequest.getApiShiji().getStoreOrderList(maps))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            OrderListObject object = (OrderListObject) msg.obj;
                            if (object != null && object.list != null && !object.list.isEmpty()) {
                                setSuccessView(recyclerView);
                                mAdapter = new ShopOrderListAdapter(mContext, object.list);
                                recyclerView.setAdapter(mAdapter);
                            } else {
                                isBottom = true;
                                setNullView(recyclerView);
                            }

                        } else {
                            if (!NetUtil.IsInNetwork(mContext)) {
                                setOffNetView(recyclerView);
                            }
                        }
                        hidePreDialog();
                    }
                });

    }

    private void loadMore() {

        nOffset += LIMIT_TEN;

        final HashMap<String, String> maps = new HashMap<>();
        maps.put("status", String.valueOf(status));
        maps.put("offset", String.valueOf(nOffset));
        maps.put("limit", String.valueOf(LIMIT_TEN));
        maps.put("shop_id", String.valueOf(shopId));

        new RetrofitRequest<OrderListObject>(ApiRequest.getApiShiji().getStoreOrderList(maps))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            OrderListObject object = (OrderListObject) msg.obj;
                            if (object != null && object.list != null && !object.list.isEmpty()) {
                                mAdapter.getmList().addAll(object.list);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                isBottom = true;
                            }
                        }

                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reload:
                getOrderList();
                break;
            default:
                break;
        }
    }
}
