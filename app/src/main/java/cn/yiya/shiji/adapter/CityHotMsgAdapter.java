package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.HotMessageActivity;
import cn.yiya.shiji.activity.ShopSortActivity;
import cn.yiya.shiji.activity.TravelCityActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.GetLocalRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.navigation.NewsInfo;
import cn.yiya.shiji.entity.navigation.StoreCategoryInfo;
import cn.yiya.shiji.entity.navigation.StoreObject;
import cn.yiya.shiji.fragment.HotShopFragment;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * Created by chenjian on 2016/4/21.
 */
public class CityHotMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SHOP_TYPE = 1111;
    public static final int LIST_TYPE = 2222;
    public static final int TXT_TYPE = 3333;

    private Context mContext;
    public ArrayList<NewsInfo> mNewsLists;
    private ArrayList<StoreCategoryInfo> mTitleLists;
    private String nId;
    private String nCountryId;
    private String nCityId;
    private boolean bNet;
    private String longitude;
    private String latitude;
    public CityHotMsgAdapter(Context mContext, String id, String cityId, String countryId, boolean net, ArrayList<NewsInfo> mNewsLists, ArrayList<StoreCategoryInfo> mTitleLists) {
        this.mContext = mContext;
        this.mNewsLists = mNewsLists;
        this.mTitleLists = mTitleLists;
        this.nId = id;
        this.nCityId = cityId;
        this.nCountryId = countryId;
        this.bNet = net;

        longitude = BaseApplication.getInstance().getLongitude();
        latitude = BaseApplication.getInstance().getLatitude();
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SHOP_TYPE:
                return new ShopViewHolder(parent);
            case LIST_TYPE:
                return new CityHotMessageViewHolder(parent);
            case TXT_TYPE:
                return new CityTxtViewHolder(parent);
            default:
                throw new RuntimeException("Unknown issue type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case LIST_TYPE:
                final NewsInfo info;
                if (mTitleLists == null || mTitleLists.size() == 0) {
                    if (position - 1 > mNewsLists.size()) {
                        return;
                    } else {
                        info = mNewsLists.get(position - 1);
                    }
                } else {
                    if (position - 2 > mNewsLists.size()) {
                        return;
                    } else {
                        info = mNewsLists.get(position - 2);
                    }
                }
                CityHotMessageViewHolder pHolder = (CityHotMessageViewHolder) holder;
                BitmapTool.showImageView(info.getCover(), pHolder.ivHotMessage);
                pHolder.tvTitle.setText(info.getTitle());
                pHolder.tvContext.setText(info.getBrief());

                pHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!NetUtil.IsInNetwork(mContext)){
                            Toast.makeText(mContext, Configration.OFF_LINE_TIPS, Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            Intent intent = new Intent(mContext, HotMessageActivity.class);
                            intent.putExtra("id", info.getId());
                            mContext.startActivity(intent);
                        }
                    }
                });
                break;
        }
//        if (!bNet) {
//            ShopViewHolder shopViewHolder = (ShopViewHolder) holder;
//            shopViewHolder.tabHotShop.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        if (mTitleLists == null || mTitleLists.size() == 0) {
            if (mNewsLists == null || mNewsLists.size() == 0)
                return 0;
            else {
                return mNewsLists.size() + 1;
            }
        } else {
            if (mNewsLists == null || mNewsLists.size() == 0)
                return 1;
            else {
                return mNewsLists.size() + 2;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return SHOP_TYPE;
        } else if (position == 1) {
            return TXT_TYPE;
        } else {
            return LIST_TYPE;
        }
    }

    protected class ShopViewHolder extends RecyclerView.ViewHolder {

        private TabLayout tabHotShop;                                   // 热门店铺TabLayout
        private ViewPager vpHotShop;
        RelativeLayout rlyt;
        TextView tvTxt;

        public ShopViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.city_footer_hotmessage_head, parent, false));
            itemView.setVisibility(View.GONE);
            tabHotShop = (TabLayout) itemView.findViewById(R.id.tab_hot_shop);
            tabHotShop.setTabMode(TabLayout.MODE_SCROLLABLE);
            vpHotShop = (ViewPager) itemView.findViewById(R.id.viewpager_hot_shop);
            tvTxt = (TextView) itemView.findViewById(R.id.hot_more_txt);

            HotShopFragmentPageAdapter adapter = new HotShopFragmentPageAdapter(((TravelCityActivity) mContext).getSupportFragmentManager());
            vpHotShop.setAdapter(adapter);
            tabHotShop.setTabsFromPagerAdapter(adapter);
            tabHotShop.setupWithViewPager(vpHotShop);

            if(bNet){
                new RetrofitRequest<StoreObject>(ApiRequest.getApiShiji().getStoreListOfCity(MapRequest.setStoreListOfCity(
                        0, nCityId, "", longitude, latitude, nCountryId, 1))).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                          handleMessage(msg);
                    }
                });
            }else{
                GetLocalRequest.getInstance().getLocalStoreList("city", nCityId, new Handler(Looper.getMainLooper()),
                        nCountryId, nCityId, 0, new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        handleMessage(msg);
                    }
                });
            }

            rlyt = (RelativeLayout) itemView.findViewById(R.id.more_shop_layout);
            if (!bNet) {
                rlyt.setClickable(false);
                tvTxt.setVisibility(View.GONE);
                tabHotShop.setVisibility(View.GONE);
            }
            if (mTitleLists.size() == 0) {
                rlyt.setVisibility(View.GONE);
            }
            rlyt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentMoreShop = new Intent(mContext, ShopSortActivity.class);
                    intentMoreShop.putExtra("id", nId);
                    intentMoreShop.putExtra("type", 1);
                    intentMoreShop.putExtra("cityId", nCityId);
                    intentMoreShop.putExtra("countryId", nCountryId);
                    mContext.startActivity(intentMoreShop);
                }
            });
        }

        private void handleMessage(HttpMessage msg){
            if (msg.isSuccess()) {
                StoreObject object = (StoreObject) msg.obj;
                if (object.getCount() != null) {
                    int count = Integer.parseInt(object.getCount());
                    if (count > 3) {
                        LinearLayout.LayoutParams rycvLayoutParams = (LinearLayout.LayoutParams) vpHotShop.getLayoutParams();
                        rycvLayoutParams.width = SimpleUtils.getScreenWidth(mContext);
                        rycvLayoutParams.height = SimpleUtils.dp2px(mContext, 32) + (((SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 64)) / 3) + SimpleUtils.dp2px(mContext, 64)) * 2;
                        vpHotShop.setLayoutParams(rycvLayoutParams);
                        itemView.setVisibility(View.VISIBLE);
                    } else if (count > 0) {
                        LinearLayout.LayoutParams rycvLayoutParams = (LinearLayout.LayoutParams) vpHotShop.getLayoutParams();
                        rycvLayoutParams.width = SimpleUtils.getScreenWidth(mContext);
                        rycvLayoutParams.height = SimpleUtils.dp2px(mContext, 32) + (((SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 64)) / 3) + SimpleUtils.dp2px(mContext, 64));
                        vpHotShop.setLayoutParams(rycvLayoutParams);
                        itemView.setVisibility(View.VISIBLE);
                    } else {
                        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                        lp.height = 0;
                        lp.width = 0;
                        itemView.setLayoutParams(lp);
                        return;
                    }
                }else {
                    ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                    lp.height = 0;
                    lp.width = 0;
                    itemView.setLayoutParams(lp);
                    return;
                }
            } else {
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                lp.height = 0;
                lp.width = 0;
                itemView.setLayoutParams(lp);
                return;
            }
        }
    }



    public class HotShopFragmentPageAdapter extends FragmentPagerAdapter {

        public HotShopFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return HotShopFragment.instanceFragment(bNet, nCountryId, nId, nId, mTitleLists.get(position).getId(), 1);
        }

        @Override
        public int getCount() {
            return mTitleLists.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleLists.get(position).getName();
        }
    }

    public static class CityHotMessageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHotMessage;
        TextView tvTitle;
        TextView tvContext;

        public CityHotMessageViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.hot_message_item, parent, false));
            ivHotMessage = (ImageView) itemView.findViewById(R.id.iv_hotmessage);
            tvTitle = (TextView) itemView.findViewById(R.id.hotmessage_title);
            tvContext = (TextView) itemView.findViewById(R.id.hotmessage_context);
        }

    }

    public static class CityTxtViewHolder extends RecyclerView.ViewHolder {
        TextView tvHotMessage;

        public CityTxtViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.travel_txt_info, parent, false));
            tvHotMessage = (TextView) itemView.findViewById(R.id.travle_txt);
            tvHotMessage.setText("热门资讯");
        }

    }
}
