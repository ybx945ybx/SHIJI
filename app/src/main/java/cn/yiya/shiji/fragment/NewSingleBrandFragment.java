package cn.yiya.shiji.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewSingleBrandActivity;
import cn.yiya.shiji.adapter.NewGoodsHotAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.NewGoodsObject;
import cn.yiya.shiji.views.FullyLinearLayoutManager;

/**
 * 品牌详情 最热门  新上架  最折扣
 * Created by Amy on 2016/9/27.
 */

public class NewSingleBrandFragment extends BaseFragment {

    private Context mContext;
    private View mView;

    private RecyclerView recyclerView;
    private FullyLinearLayoutManager layoutManager;
    private NewGoodsHotAdapter adapter;

    private int type;  // 1最热门  2新上架  3最折扣
    private int brandId;
    private boolean bShowPrice;  //是否显示原价
    private boolean isRecommend;  // 是否是店主添加

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
            brandId = bundle.getInt("brand_id");
            bShowPrice = bundle.getBoolean("show_price", false);
            isRecommend = bundle.getBoolean("isRecommend", false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.item_single_brand_recyclerview, container, false);
        initViews();
        initEvents();
        init();
        return mView;
    }


    public NewSingleBrandFragment getInstance(int brandId, int type, boolean bShowPrice, boolean isRecommend) {
        NewSingleBrandFragment fragment = new NewSingleBrandFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("brand_id", brandId);
        bundle.putBoolean("show_price", bShowPrice);
        bundle.putBoolean("isRecommend", isRecommend);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initViews() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        layoutManager = new FullyLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NewGoodsHotAdapter(mContext, bShowPrice, isRecommend);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initEvents() {
        adapter.setOnItemClickListener(new NewGoodsHotAdapter.OnItemClickListener() {
            @Override
            public void gotoLogin() {
                ((NewSingleBrandActivity) getActivity()).login();
            }
        });
    }

    @Override
    protected void init() {
        switch (type) {
            case 1:
                getHotGoodsList();
                break;
            case 2:
                getNewGoodsList();
                break;
            case 3:
                getDiscountGoodsList();
                break;
            default:
                break;
        }
    }

    /**
     * 获取最热门商品
     */
    private void getHotGoodsList() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("brand_id", String.valueOf(brandId));
        maps.put("limit", String.valueOf(20));
        if(isRecommend){
            maps.put("isShopkeeper", String.valueOf(1));
        }
        new RetrofitRequest<NewGoodsObject>(ApiRequest.getApiShiji().getHotSingleList(maps)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    recyclerView.setNestedScrollingEnabled(false);
                    NewGoodsObject info = (NewGoodsObject) msg.obj;
                    if (info.getGoods() != null && info.getGoods().size() > 0) {
                        adapter.setmLists(info.getGoods());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * 获取新上架商品
     */
    private void getNewGoodsList() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("brand_id", String.valueOf(brandId));
        maps.put("limit", String.valueOf(20));
        if(isRecommend){
            maps.put("isShopkeeper", String.valueOf(1));
        }
        new RetrofitRequest<NewGoodsObject>(ApiRequest.getApiShiji().getNewGoodsList(maps)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    recyclerView.setNestedScrollingEnabled(false);
                    NewGoodsObject info = (NewGoodsObject) msg.obj;
                    if (info.getGoods() != null && info.getGoods().size() > 0) {
                        adapter.setmLists(info.getGoods());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * 获取最折扣商品
     */
    private void getDiscountGoodsList() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("brand_id", String.valueOf(brandId));
        maps.put("limit", String.valueOf(20));
        if(isRecommend){
            maps.put("isShopkeeper", String.valueOf(1));
        }
        new RetrofitRequest<NewGoodsObject>(ApiRequest.getApiShiji().getDiscountGoods(maps)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    recyclerView.setNestedScrollingEnabled(false);
                    NewGoodsObject info = (NewGoodsObject) msg.obj;
                    if (info.getGoods() != null && info.getGoods().size() > 0) {
                        adapter.setmLists(info.getGoods());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
