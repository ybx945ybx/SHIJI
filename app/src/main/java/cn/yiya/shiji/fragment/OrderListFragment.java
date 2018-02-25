package cn.yiya.shiji.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.ChooseGoodsActivity;
import cn.yiya.shiji.activity.OrderDetailActivity;
import cn.yiya.shiji.adapter.OrderListAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.OrderListObject;
import cn.yiya.shiji.utils.NetUtil;

/**
 * 订单
 * Created by Amy on 2016/10/26.
 */

public class OrderListFragment extends BaseFragment implements View.OnClickListener {

    private boolean isVisiable = false;
    private boolean isInitView = false;
    private Context mContext;
    private View mView;
    private int status;    //0:全部,1:待付款,2:在途,3:待缴税,4:完成,5:失效

    private RecyclerView recyclerView;
    private OrderListAdapter mAdapter;
    private int nOffset;
    private boolean isBottom;
    private static final int LIMIT_TEN = 10;

    private int lastVisibleItemPosition;
    private LinearLayoutManager layoutManager;

    private static final int ORDER_DETAIL = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getInt("status");
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

        mAdapter = new OrderListAdapter(mContext, status);
        recyclerView.setAdapter(mAdapter);

        //无网络状态
        setMainView(mView);
        initDefaultNullView(R.mipmap.nothing_order, "暂无相关订单", this, 0, 230);
    }

    @Override
    protected void initEvents() {
        mAdapter.setOnItemClickListener(new OrderListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String orderno, String suborderno, int type) {
                if (type == mAdapter.OTHER) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), OrderDetailActivity.class);
                    intent.putExtra("orderno", orderno);
                    intent.putExtra("orderId", suborderno);
                    startActivityForResult(intent, ORDER_DETAIL);
                } else if (type == mAdapter.SHARE_ORDER) {
                    Intent intent = new Intent(getActivity(), ChooseGoodsActivity.class);
                    intent.putExtra("orderno", orderno);
                    intent.putExtra("orderId", suborderno);
                    startActivity(intent);
                }
            }
        });
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

    public OrderListFragment getInstance(int status) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
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

        showPreDialog("正在加载");
        new RetrofitRequest<OrderListObject>(ApiRequest.getApiShiji().getAllOrderList(maps))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            OrderListObject object = (OrderListObject) msg.obj;
                            if (object != null && object.list != null && !object.list.isEmpty()) {
                                setSuccessView(recyclerView);
                                mAdapter.setmList(object.list);
                                mAdapter.notifyDataSetChanged();
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

        new RetrofitRequest<OrderListObject>(ApiRequest.getApiShiji().getAllOrderList(maps))
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == ORDER_DETAIL) {
                getOrderList();
            }
        }
    }


    private SendCount sendCount;

    public interface SendCount {
        void chengCount(int size);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            sendCount = (SendCount) activity;
        } catch (Exception e) {

        }
    }
}

