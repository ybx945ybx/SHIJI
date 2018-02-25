package cn.yiya.shiji.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.HotShopItemAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.GetLocalRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.navigation.CountryListInfo;
import cn.yiya.shiji.entity.navigation.StoreObject;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.views.FullyGridLayoutManager;

/**
 * Created by Tom on 2016/3/31.
 */
public class HotShopFragment extends BaseFragment {
    LinearLayout llytDialog;
    ProgressBar progressBar;
    TextView tvLoad;
    private RecyclerView rycvHotShop;                   // 热门店铺列表
    private RelativeLayout rlytAnother;
    private HotShopItemAdapter hotShopItemAdapter;      // 热门店铺item适配器
    private String city_mall_id;
    private String categroy_id;
    private int count;
    private int type;                                   // 1是城市下的 2是mall下的
    private ArrayList<CountryListInfo> mList;           // mAllList, mThreeList, mColthList, mLuggageList, mOrnamentList;

    private String countryId;
    private boolean bNet;
    private String cityId;

    private String longitude;
    private String latitude;
    private View mView;

    public static HotShopFragment instanceFragment(boolean bNet, String countryId, String cityId, String city_mall_id, String categroy_id, int type) {
        Bundle args = new Bundle();
        args.putString("city_mall_id", city_mall_id);
        args.putString("categroy_id", categroy_id);
        args.putInt("type", type);
        args.putString("countryId", countryId);
        args.putBoolean("bNet", bNet);
        args.putString("cityId", cityId);


        HotShopFragment hotShopFragment = new HotShopFragment();
        hotShopFragment.setArguments(args);

        return hotShopFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        city_mall_id = getArguments().getString("city_mall_id");
        categroy_id = getArguments().getString("categroy_id");
        type = getArguments().getInt("type");
        bNet = getArguments().getBoolean("bNet");
        countryId = getArguments().getString("countryId");
        cityId = getArguments().getString("cityId");

        longitude =   ((BaseApplication)getActivity().getApplication()).getLongitude();
        latitude = ((BaseApplication)getActivity().getApplication()).getLatitude();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_hot_shop, container, false);

        initViews();
        initEvents();
        init();

        return mView;
    }

    @Override
    protected void initViews() {
        llytDialog = (LinearLayout)mView.findViewById(R.id.llyt_progressbar);
        progressBar = (ProgressBar)mView.findViewById(R.id.progressBar);
        tvLoad = (TextView)mView.findViewById(R.id.tv_load);
        rycvHotShop = (RecyclerView)mView.findViewById(R.id.hot_shop_list);
        rycvHotShop.setItemAnimator(new DefaultItemAnimator());
        rycvHotShop.setLayoutManager(new FullyGridLayoutManager(getActivity(), 3));
        rycvHotShop.setNestedScrollingEnabled(false);
        hotShopItemAdapter = new HotShopItemAdapter(getActivity(), countryId, cityId);
        rycvHotShop.setAdapter(hotShopItemAdapter);
        rlytAnother = (RelativeLayout)mView.findViewById(R.id.rlyt_another);
        rlytAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                another(city_mall_id, categroy_id);
            }
        });
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {
        initData(0);
    }

    private void initData(final int offset) {
        showPregressDiago("");
        if (type == 1) {
            if(NetUtil.IsInNetwork(getActivity())){
                new RetrofitRequest<StoreObject>(ApiRequest.getApiShiji().getStoreListOfCity(MapRequest.setStoreListOfCity(
                        0, city_mall_id, categroy_id, longitude, latitude, countryId, 1))).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        handleMessage(msg,0);
                    }
                });
            }else {
                GetLocalRequest.getInstance().getLocalStoreList("city", city_mall_id,
                        new Handler(Looper.getMainLooper()), countryId, city_mall_id, 0, new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                handleMessage(msg,0);
                            }
                        });
            }
        } else {
            if(NetUtil.IsInNetwork(getActivity())){
                new RetrofitRequest<StoreObject>(ApiRequest.getApiShiji().getStoreListOfMall(MapRequest.setStoreListOfMall(
                        0, city_mall_id, countryId, cityId, categroy_id, 1))).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        handleMessage(msg,0);
                    }
                });
            }else {
                GetLocalRequest.getInstance().getLocalStoreList("mall", city_mall_id, new Handler(Looper.getMainLooper())
                        , countryId, cityId, 0, new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                handleMessage(msg, 0);
                            }
                        });
            }
        }
        hidPregressDiago();
    }

    private  void  handleMessage(HttpMessage msg,int offset){
        if (msg.isSuccess()) {
            StoreObject object = (StoreObject) msg.obj;
            if (object.getCount() != null) {
                count = Integer.parseInt(object.getCount());
            }
            if (object.list != null && object.list.size() > 0) {
                if (offset > 0) {
                    if (hotShopItemAdapter.getMlist() != null) {
                        hotShopItemAdapter.getMlist().clear();
                    }

                    hotShopItemAdapter.getMlist().addAll(object.list);
                    hotShopItemAdapter.notifyDataSetChanged();
                } else {
                    hotShopItemAdapter.setMlist(object.list);
                    hotShopItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private  void  handleMessage(HttpMessage msg){
        if (msg.isSuccess()) {
            StoreObject object = (StoreObject) msg.obj;

            if (object.list != null && object.list.size() > 0) {
                if (hotShopItemAdapter.getMlist() != null) {
                    hotShopItemAdapter.getMlist().clear();
                    hotShopItemAdapter.getMlist().addAll(object.list);
                } else {
                    hotShopItemAdapter.setMlist(object.list);
                }

                rycvHotShop.getAdapter().notifyDataSetChanged();
            } else {
                tips();
            }
        } else {
            showToast(msg.obj.toString());
        }
        hidPregressDiago();
    }

    private void another(String city_mall_id, String categroy_id) {
        showPregressDiago("");
        if (type == 1) {
            if(NetUtil.IsInNetwork(getActivity())){
                new RetrofitRequest<StoreObject>(ApiRequest.getApiShiji().getStoreListOfCity(MapRequest.setStoreListOfCity(
                        ((int)(1+Math.random()*(Math.ceil(count/6))) - 1)*6 , city_mall_id, categroy_id, longitude, latitude, countryId, 0)))
                        .handRequest(new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                handleMessage(msg);
                            }
                        });
            }else{
                GetLocalRequest.getInstance().getLocalStoreList("city", city_mall_id,
                        new Handler(Looper.getMainLooper()), countryId, city_mall_id, ((int)(1+Math.random()*(Math.ceil(count/6))) - 1)*6, new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                handleMessage(msg);
                            }
                        });
            }
        } else {
            if (NetUtil.IsInNetwork(getActivity())) {
                new RetrofitRequest<StoreObject>(ApiRequest.getApiShiji().getStoreListOfMall(MapRequest.setStoreListOfMall(
                        (int) (Math.random() * (count - 6)), city_mall_id, countryId, cityId, categroy_id, 0))).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        handleMessage(msg);
                    }
                });
            } else {
                GetLocalRequest.getInstance().getLocalStoreList("mall", city_mall_id, new Handler(Looper.getMainLooper())
                        , countryId, cityId, (int) (Math.random() * (count - 6)), new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                handleMessage(msg);
                            }
                        });
            }
        }
    }

    private void showPregressDiago(String str) {
        llytDialog.setVisibility(View.VISIBLE);
        if (str == null || str.length() == 0) {
            tvLoad.setText("正在发送请求");
        } else {
            tvLoad.setText(str);
        }
    }

    private void hidPregressDiago() {
        llytDialog.setVisibility(View.GONE);
    }
    private void tips(){
        if(NetUtil.IsInNetwork(getActivity())){
            showToast(Configration.OFF_LINE_TIPS);
        }
    }
}
