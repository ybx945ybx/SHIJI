package cn.yiya.shiji.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ParallaxAdapter;
import cn.yiya.shiji.adapter.TravelLineAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.GetLocalRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.BannerItem;
import cn.yiya.shiji.entity.BannerObject;
import cn.yiya.shiji.entity.navigation.CountryListInfo;
import cn.yiya.shiji.entity.navigation.CountryListObject;
import cn.yiya.shiji.entity.navigation.HotLineInfo;
import cn.yiya.shiji.entity.navigation.HotLineObject;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.MySwipeRefreshLayout;
import cn.yiya.shiji.views.joinable.JoinableAdapter;
import cn.yiya.shiji.views.joinable.JoinableLayout;
import cn.yiya.shiji.views.joinable.ParallaxRecyclerView;
import cn.yiya.shiji.views.joinable.ParallaxViewHolder;
import cn.yiya.shiji.views.joinable.RvJoiner;

/**
 * Created by Tom on 2016/3/16.
 */
public class TravleNavigationFragment extends BaseFragment implements View.OnClickListener {
    private View mView;

    private MySwipeRefreshLayout swipeRefreshLayout;                              // 刷新布局

    private ParallaxRecyclerView recyclerView;                                  // 国家列表
    private ParallaxAdapter parallaxAdapter;                                    // 国家列表适配器

    private LinearLayout llytMoreCounty;                                        // 查看更多国家
    private ImageView ivMoreCountry;
    private ProgressBar pbMoreCountry;
    private TravelLineAdapter travleLineAdapter;                                // 热门线路适配器

    private TextView tvLine;
    private LinearLayout llytMoreLine;                                          // 热门线路列表底部查看更多按钮
    private ImageView ivMoreLine;
    private ProgressBar pbMoreLine;

    private View viewText;

    private boolean bNet;

    private RvJoiner rvJoiner = new RvJoiner();
    private JoinableLayout coutryMoreJoin;
    private JoinableLayout lineMoreJoin;
    private JoinableLayout lineTextJoin;

    ArrayList<BannerItem> mBannerList = new ArrayList<>();
    ArrayList<CountryListInfo> mCountrtList = new ArrayList<>();
    ArrayList<HotLineInfo> mTravelLineList = new ArrayList<>();
    boolean bLoadBanner;
    boolean bLoadCountry;
    boolean bLoadLine;

//    private RelativeLayout rlWithoutNew;
//    private TextView tvreload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_travle_navigation, null);
        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.county_footer_more:
                if (Util.BeNotQuicklyClick()) {
                    getCountry(mCountrtList.size());
                }
                break;
            case R.id.line_footer_more:
                if (Util.BeNotQuicklyClick()) {
                    getLine(mTravelLineList.size());
                }
                break;
            case R.id.tv_reload:
                initData();
                break;
        }
    }

    private void initData() {
        bNet = NetUtil.IsInNetwork(getActivity());
        showPreDialog("正在加载");
        getBanner();
        getCountry(0);
        getLine(0);
    }

    public void getBanner() {
        bLoadBanner = false;
        new RetrofitRequest<BannerObject>(ApiRequest.getApiShiji().getNaviBannerList()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if (msg.isSuccess()) {
                    bLoadBanner = true;
                    BannerObject bannerObject = (BannerObject) msg.obj;
                    if (bannerObject.list != null && bannerObject.list.size() > 0) {
                        if (mBannerList != null) {
                            mBannerList.clear();
                        }
                        for (int i = 0; i < bannerObject.list.size(); i++) {
                            BannerItem item = new BannerItem();
                            item.setId(bannerObject.list.get(i).getId());
                            item.setImage(bannerObject.list.get(i).getImage());
                            item.setUrl(bannerObject.list.get(i).getUrl());
                            item.setTitle(bannerObject.list.get(i).getTitle());
                            mBannerList.add(item);
                        }
                    }
                    setToTalAdapter();

                } else {
                    if (!NetUtil.IsInNetwork(getActivity())) {
                        bLoadBanner = true;
                        mBannerList.clear();
                        setToTalAdapter();
                    }
                }
            }
        });
    }

    private void getCountry(final int offset) {
        bLoadCountry = false;
        if (offset > 0) {
            showLoad(llytMoreCounty, ivMoreCountry, pbMoreCountry);
        }

        if (NetUtil.IsInNetwork(getActivity())) {
            new RetrofitRequest<CountryListObject>(ApiRequest.getApiShiji().getCountryList(
                    MapRequest.setMapTen(offset))).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleMessage(msg, offset);
                }
            });
        } else {
            GetLocalRequest.getInstance().getCountryList(offset, new Handler(Looper.getMainLooper()), new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleMessage(msg, offset);
                }
            });
        }
    }

    private void handleMessage(HttpMessage msg, int offset) {
        if (msg.isSuccess()) {
            bLoadCountry = true;
            CountryListObject object = (CountryListObject) msg.obj;

            if (offset > 0) {
                if (object != null) {
                    mCountrtList.addAll(object.list);
                    parallaxAdapter.notifyDataSetChanged();
                    hideLoad(llytMoreCounty, ivMoreCountry, pbMoreCountry);
                    setCountyFoot(object.list.size());
                }
            } else {
                mCountrtList = object.list;
                setToTalAdapter();
                setSuccessView(swipeRefreshLayout);
            }
        } else {
            if (!NetUtil.IsInNetwork(getActivity())) {
                setOffNetView(swipeRefreshLayout);
            }
        }
        hidePreDialog();
    }

    private void getLine(final int offset) {
        if (offset > 0) {
            showLoad(llytMoreLine, ivMoreLine, pbMoreLine);
        }
        bLoadLine = false;

        new RetrofitRequest<HotLineObject>(ApiRequest.getApiShiji().getShopLineList(
                MapRequest.setIdSix("", offset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    bLoadLine = true;
                    HotLineObject object = (HotLineObject) msg.obj;
                    if (offset > 0) {
                        mTravelLineList.addAll(object.list);
                        travleLineAdapter.notifyDataSetChanged();
                        hideLoad(llytMoreLine, ivMoreLine, pbMoreLine);
                        setLineFoot(object.list.size());
                    } else {
                        mTravelLineList = object.list;
                        setToTalAdapter();
                    }
                } else {
                    if (!NetUtil.IsInNetwork(getActivity())) {
                        if (offset > 0) {
                            hideLoad(llytMoreLine, ivMoreLine, pbMoreLine);
                            showToast(Configration.ON_DOWONLOAD_SOURCE);
                            return;
                        } else {
                            bLoadLine = true;
                            mTravelLineList.clear();
                            setToTalAdapter();
                        }
                    }
                }
                hidePreDialog();
            }
        });
    }

    private synchronized void setToTalAdapter() {

        if (recyclerView.getAdapter() != null) {
            rvJoiner = null;
            rvJoiner = new RvJoiner();
            recyclerView.setAdapter(null);
        }

        if (bLoadBanner && bLoadCountry && bLoadLine) {
            if (getActivity() == null) {
                return;
            }
            parallaxAdapter = new ParallaxAdapter(getActivity(), mCountrtList, mBannerList);
            rvJoiner.add(new JoinableAdapter(parallaxAdapter, ParallaxAdapter.BANNER_TYPE, ParallaxAdapter.LIST_TYPE));
            if (mCountrtList.size() > 9) {
                coutryMoreJoin = new JoinableLayout(R.layout.country_more_data_layout, new JoinableLayout.Callback() {
                    @Override
                    public void onInflateComplete(View view, ViewGroup parent) {
                        llytMoreCounty = (LinearLayout) view.findViewById(R.id.county_footer_more);
                        ivMoreCountry = (ImageView) view.findViewById(R.id.footer_img);
                        pbMoreCountry = (ProgressBar) view.findViewById(R.id.footer_progressbar);
                        llytMoreCounty.setOnClickListener(TravleNavigationFragment.this);
                    }
                });
                rvJoiner.add(coutryMoreJoin);
            }

            travleLineAdapter = new TravelLineAdapter(getActivity(), mTravelLineList);
            if (mTravelLineList != null && mTravelLineList.size() > 0) {
                lineTextJoin = new JoinableLayout(R.layout.travel_txt_info, new JoinableLayout.Callback() {
                    @Override
                    public void onInflateComplete(View view, ViewGroup parent) {
                        tvLine = (TextView) view.findViewById(R.id.travle_txt);
                        tvLine.setText("热门线路");
                    }
                });
                rvJoiner.add(lineTextJoin);
                rvJoiner.add(new JoinableAdapter(travleLineAdapter));
                if (mTravelLineList.size() > 5) {
                    lineMoreJoin = new JoinableLayout(R.layout.line_more_data_layout, new JoinableLayout.Callback() {
                        @Override
                        public void onInflateComplete(View view, ViewGroup parent) {
                            llytMoreLine = (LinearLayout) view.findViewById(R.id.line_footer_more);
                            ivMoreLine = (ImageView) view.findViewById(R.id.footer_img);
                            pbMoreLine = (ProgressBar) view.findViewById(R.id.footer_progressbar);
                            llytMoreLine.setBackgroundColor(Color.parseColor("#ffffff"));
                            llytMoreLine.setOnClickListener(TravleNavigationFragment.this);
                        }
                    });
                    rvJoiner.add(lineMoreJoin);
                }
            }

            recyclerView.setAdapter(rvJoiner.getAdapter());

            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    for (int i = 0; i < recyclerView.getChildCount(); i++) {
                        if (recyclerView.getChildViewHolder(recyclerView.getChildAt(i)) instanceof ParallaxViewHolder) {
                            ((ParallaxViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i))).animateImage();
                        }
                    }
                }
            });
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        hidePreDialog();
    }

    private void setCountyFoot(int size) {
        if (size < 10) {
            rvJoiner.remove(coutryMoreJoin);
        }
    }

    private void setLineFoot(int size) {
        if (size < 6) {
            rvJoiner.remove(lineMoreJoin);
        }
    }

    private void showLoad(View moreView, ImageView imageView, ProgressBar progressBar) {
        moreView.setClickable(false);
        imageView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoad(View moreView, ImageView imageView, ProgressBar progressBar) {
        moreView.setClickable(true);
        imageView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void initViews() {
        swipeRefreshLayout = (MySwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh);

        ((TextView) getActivity().findViewById(R.id.toolbar_middle_txt)).setText("目的地");

        recyclerView = (ParallaxRecyclerView) mView.findViewById(R.id.parallax_list);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

//        rlytDefaultNullView = (RelativeLayout) mView.findViewById(R.id.rlyt_default_null_view);
//        tvDefaultNull = (TextView) mView.findViewById(R.id.tv_default_null);
//        llytDefaultOffNet = (LinearLayout) mView.findViewById(R.id.llyt_off_net);
//        tvReload = (TextView) mView.findViewById(R.id.tv_reload);
        setMainView(mView);
        initDefaultNullView(R.mipmap.zanwugouwubiji, "", this, 0, 230);
    }

    @Override
    protected void initEvents() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bNet = NetUtil.IsInNetwork(getActivity());
                initData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void init() {
        initData();
    }
}
