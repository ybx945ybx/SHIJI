package cn.yiya.shiji.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.MallListActivity;
import cn.yiya.shiji.activity.NewSearchActivity;
import cn.yiya.shiji.activity.PopularBrandsListActivity;
import cn.yiya.shiji.adapter.NewBrandOrMallAdapter;
import cn.yiya.shiji.adapter.NewClassifyTypeAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.CustomEvent;
import cn.yiya.shiji.entity.BrandsItem;
import cn.yiya.shiji.entity.BrandsObject;
import cn.yiya.shiji.entity.HotCategoryObject;
import cn.yiya.shiji.entity.HotMallItem;
import cn.yiya.shiji.entity.HotMallObject;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.views.FullyGridLayoutManager;

/**
 * Created by Amy on 2016/6/14.
 */
public class NewClassifyFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private Activity mActivity;
    private Handler mHandler;

    private RelativeLayout rlSearch;

    private SwipeRefreshLayout srlRefresh;
    private SwipeRefreshLayout.OnRefreshListener listener; //SwiperefreshLayout刷新监听
    //分类
    private RecyclerView rvClassifyType;
    private NewClassifyTypeAdapter typeAdapter;
    private boolean isType;
    //热门品牌
    private RelativeLayout rlAllHotBrands;
    private RecyclerView rvHotBrands;
    private NewBrandOrMallAdapter brandsAdapter;
    private boolean isBrands;
    //热门商城
    private RelativeLayout rlAllHotMalls;
    private RecyclerView rvHotMalls;
    private NewBrandOrMallAdapter mallsAdapter;
    private boolean isMalls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(getActivity().getMainLooper());
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_classify, null);
        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        rlSearch = (RelativeLayout) getActivity().findViewById(R.id.toolbar_middle_search_layout);
        srlRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.srl_refresh);

        rvClassifyType = (RecyclerView) mView.findViewById(R.id.rv_classify_type);
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
        rvClassifyType.setLayoutManager(layoutManager);
        rvClassifyType.setNestedScrollingEnabled(false);
        typeAdapter = new NewClassifyTypeAdapter(mActivity);
        rvClassifyType.setAdapter(typeAdapter);

        rlAllHotBrands = (RelativeLayout) mView.findViewById(R.id.rl_all_hot_brands);
        rvHotBrands = (RecyclerView) mView.findViewById(R.id.rv_hot_brands);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rvHotBrands.getLayoutParams();
        layoutParams.width = SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 16) * 2;
        layoutParams.height = layoutParams.width * 182 / 364;
        rvHotBrands.setLayoutParams(layoutParams);
        rvHotBrands.setNestedScrollingEnabled(false);
        rvHotBrands.setLayoutManager(new FullyGridLayoutManager(mActivity, 4));
        rvHotBrands.setHasFixedSize(true);
        brandsAdapter = new NewBrandOrMallAdapter(mActivity);

        rlAllHotMalls = (RelativeLayout) mView.findViewById(R.id.rl_all_hot_malls);
        rvHotMalls = (RecyclerView) mView.findViewById(R.id.rv_hot_malls);
        rvHotMalls.setLayoutParams(layoutParams);
        rvHotMalls.setNestedScrollingEnabled(false);
        rvHotMalls.setLayoutManager(new FullyGridLayoutManager(mActivity, 4));
        rvHotMalls.setHasFixedSize(true);
        mallsAdapter = new NewBrandOrMallAdapter(mActivity);

        //无网络状态
        setMainView(mView);
        initDefaultNullView(R.mipmap.zanwugouwubiji, "", this, 0, 230);
    }

    @Override
    protected void initEvents() {
        rlSearch.setOnClickListener(this);
        rlAllHotBrands.setOnClickListener(this);
        rlAllHotMalls.setOnClickListener(this);
    }

    @Override
    protected void init() {
        setSwipeRefresh();
    }


    /**
     * 设置下拉刷新，刷新后更新页面数据
     */
    public void setSwipeRefresh() {

        listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstCategory();
                getHotBrands();
                getHotMalls();
            }
        };
        srlRefresh.setOnRefreshListener(listener);
        srlRefresh.post(new Runnable() {
            @Override
            public void run() {
                srlRefresh.setRefreshing(true);
            }
        });
        listener.onRefresh();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            //搜索商品和品牌
            case R.id.toolbar_middle_search_layout:
                startActivity(new Intent(mActivity, NewSearchActivity.class));
                break;
            case R.id.rl_without_net:
                setSwipeRefresh();
                break;
            //全部热门品牌
            case R.id.rl_all_hot_brands:
                CustomEvent.onEvent(mContext, mTracker, "NewClassifyFragment", CustomEvent.AllBrands);
                intent = new Intent(mActivity, PopularBrandsListActivity.class);
                startActivity(intent);
                break;
            //全部热门商城
            case R.id.rl_all_hot_malls:
                CustomEvent.onEvent(mContext, mTracker, "NewClassifyFragment", CustomEvent.AllMallList);
                intent = new Intent(mActivity, MallListActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_reload:
                setSwipeRefresh();
                break;
            default:
                break;
        }
    }

    public static ArrayList<BrandsItem> trimEmptyBrand(ArrayList<BrandsItem> list) {
        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.isEmpty(list.get(i).getLogo())) {
                list.remove(i);
            }
        }
        return list;
    }

    public static ArrayList<HotMallItem> trimEmptyMall(ArrayList<HotMallItem> list) {
        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.isEmpty(list.get(i).getLogo())) {
                list.remove(i);
            }
        }
        return list;
    }

    /**
     * 获取一级热门分类
     */
    private void getFirstCategory() {
        new RetrofitRequest<HotCategoryObject>(ApiRequest.getApiShiji().getHotCategoryFirst("1")).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    isType = true;
                    visable();
                    HotCategoryObject obj = (HotCategoryObject) msg.obj;
                    if (obj == null || obj.list.size() == 0) return;
                    typeAdapter.setList(obj.list);
                    typeAdapter.notifyDataSetChanged();
                } else {
                    isType = false;
                    invisable();
                }
            }
        });
    }

    /**
     * 获取热门品牌
     */
    private void getHotBrands() {
        new RetrofitRequest<BrandsObject>(ApiRequest.getApiShiji().getHotBrandsSearch(String.valueOf(10))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    isBrands = true;
                    visable();
                    BrandsObject obj = (BrandsObject) msg.obj;
                    if (obj.list != null && obj.list.size() > 0) {
                        brandsAdapter.setBrandsList(obj.list);
                        rvHotBrands.setAdapter(brandsAdapter);
//                        brandsAdapter.setList(trimEmptyBrand(obj.list));
//                        brandsAdapter.notifyDataSetChanged();
                    }
                } else {
                    isBrands = false;
                    invisable();
                }
                srlRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        srlRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }


    /**
     * 获取热门商城
     */
    private void getHotMalls() {
        new RetrofitRequest<HotMallObject>(ApiRequest.getApiShiji().getHotMallsSearch(String.valueOf(10))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    isMalls = true;
                    visable();
                    HotMallObject obj = (HotMallObject) msg.obj;
                    if (obj.list != null && obj.list.size() > 0) {
                        mallsAdapter.setMallsList(obj.list);
                        rvHotMalls.setAdapter(mallsAdapter);
//                        mallsAdapter.setList(trimEmptyMall(obj.list));
//                        mallsAdapter.notifyDataSetChanged();
                    }

                } else {
                    isMalls = false;
                    invisable();
                }
                srlRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        srlRefresh.setRefreshing(false);

                    }
                });
            }
        });
    }

    private void visable() {
        if (isType && isBrands && isMalls) {
            setSuccessView(srlRefresh);
        }
    }

    private void invisable() {
        if (!isType || !isBrands || !isMalls) {
            setOffNetView(srlRefresh);
        }
    }
}
