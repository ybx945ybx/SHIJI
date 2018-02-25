package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.BannerWebViewActivity;
import cn.yiya.shiji.activity.TravelCountryActivity;
import cn.yiya.shiji.entity.BannerItem;
import cn.yiya.shiji.entity.navigation.CountryListInfo;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.MyPreference;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.views.joinable.ParallaxViewHolder;

/**
 * Created by bobbyadiprabowo on 3/3/15.
 */
public class ParallaxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int BANNER_TYPE = 111;
    public static final int LIST_TYPE = 222;

    private ArrayList<CountryListInfo> mListData;
    ArrayList<BannerItem> mBannerData = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;

    public ParallaxAdapter(Context mContext, ArrayList<CountryListInfo> list, ArrayList<BannerItem> banner) {
        this.mContext = mContext;
        this.mListData = list;
        this.mBannerData = banner;
        inflater = LayoutInflater.from(mContext);
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case BANNER_TYPE:
                return new BannerViewHolder(viewGroup);
            case LIST_TYPE:
                return new PViewHolder(inflater.inflate(R.layout.item_parallax, viewGroup, false));
            default:
                throw new RuntimeException("Unknown issue type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BANNER_TYPE:
//                ((BannerViewHolder)holder).bindData(mBannerData);
                break;
            case LIST_TYPE:
               final CountryListInfo cInfo;
                if (mBannerData == null || mBannerData.size() == 0) {
                    if (position > mListData.size()) {
                        return;
                    } else {
                        cInfo = mListData.get(position - 1);
                    }
                } else {
                    if (position > mListData.size() + 1) {
                        return;
                    } else {
                        cInfo = mListData.get(position - 1);
                    }
                }
                final PViewHolder pHolder = (PViewHolder)holder;
                pHolder.tvName.setText(cInfo.getName());
                pHolder.tvCnName.setText(cInfo.getCn_name());
                if(cInfo.getCity_num() == 0){
                    pHolder.llytCityCount.setVisibility(View.INVISIBLE);
                }else {
                    pHolder.tvCityCount.setText(cInfo.getCity_num() + "");
                    pHolder.llytCityCount.setVisibility(View.VISIBLE);
                }
                BitmapTool.showFullImageView(mContext, cInfo.getCover(), pHolder.getBackgroundImage());
                pHolder.getBackgroundImage().reuse();
                pHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, TravelCountryActivity.class);
                        intent.putExtra("id", cInfo.getId());
                        mContext.startActivity(intent);
                    }
                });

                //给国家列表第一个添加引导
                if(position == 1){
                    if (!MyPreference.takeSharedPreferences(mContext, MyPreference.TRAVEL_GUIDE)) {
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                        alphaAnimation.setFillAfter(true);
                        alphaAnimation.setDuration(1000);
                        alphaAnimation.setStartOffset(500);
                        pHolder.llytTips.setVisibility(View.VISIBLE);
                        pHolder.llytTips.setAnimation(alphaAnimation);
                        alphaAnimation.startNow();
                        MyPreference.saveSharedPreferences(mContext, MyPreference.TRAVEL_GUIDE, true);
                        final Handler mHandler = new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what) {
                                    case 10:
                                        if(pHolder.llytTips.getVisibility() == View.VISIBLE) {
                                            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                                            alphaAnimation.setFillAfter(true);
                                            alphaAnimation.setDuration(1000);
                                            pHolder.llytTips.setVisibility(View.INVISIBLE);
                                            pHolder.llytTips.setAnimation(alphaAnimation);
                                            alphaAnimation.startNow();
                                        }
                                        break;
                                }
                            }
                        };

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            public void run() {
                                mHandler.sendEmptyMessage(10);
                            }
                        }, 3000);
                    }
                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        return mListData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return BANNER_TYPE;
        } else {
            return LIST_TYPE;
        }
    }

    protected class BannerViewHolder extends RecyclerView.ViewHolder {

        private ConvenientBanner banner;

        public BannerViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_new_recommend, parent, false));
            if (mBannerData == null || mBannerData.size() == 0) {
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                lp.height = 0;
                lp.width = 0;
                itemView.setLayoutParams(lp);
                return;
            }
            banner = (ConvenientBanner) itemView.findViewById(R.id.banner);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)banner.getLayoutParams();
            layoutParams.width = SimpleUtils.getScreenWidth(mContext);
            layoutParams.height = layoutParams.width * 159/375;
            banner.setLayoutParams(layoutParams);
            banner.setPageIndicator(new int[]{R.mipmap.main_indictator_normal, R.mipmap.main_indictator_focused});
            banner.startTurning(4000);
            banner.setVisibility(View.VISIBLE);
            banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, mBannerData);
//            new WorkCtrl(new ).bannerList(new MsgCallBack() {
//                @Override
//                public void onResult(HttpMessage msg) {
//                    if (msg.isSuccess()) {
//                        BannerObject object = (BannerObject) msg.obj;
//                        if (object.list != null && object.list.size() > 0) {
//                            ArrayList<BannerItem> list = object.list;
//
//                        }
//                    }
//                }
//            });
        }
    }

    class NetworkImageHolderView implements Holder<BannerItem> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(final Context context, final int position, final BannerItem item) {
            int width = SimpleUtils.getScreenWidth(mContext);
            int height = width * 159/375;
            BitmapTool.clipShowImageView(item.getImage(), imageView, R.drawable.user_dafault, width, height);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentBanner = new Intent(mContext, BannerWebViewActivity.class);
                    intentBanner.putExtra("image", item.getImage());
                    intentBanner.putExtra("url", item.getUrl());
                    intentBanner.putExtra("title", item.getTitle());
                    mContext.startActivity(intentBanner);
                }
            });
        }
    }

//        public AutoGallery mGalleryTravle;
//
//        public BannerViewHolder(ViewGroup parent) {
//            super(LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.travle_head_banner, parent, false));
//
//            if (mBannerData == null || mBannerData.size() == 0) {
//                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
//                lp.height = 1;
//                lp.width = 1;
//                itemView.setLayoutParams(lp);
//                return;
//            }
//            mGalleryTravle = (AutoGallery) itemView.findViewById(R.id.travle_banner);
//            mGalleryTravle.setVisibility(View.VISIBLE);
//            itemView.findViewById(R.id.view_down).setVisibility(View.VISIBLE);
//        }
//
//        public void bindData(ArrayList<BannerItem> mlist) {
//            if (mBannerData == null || mBannerData.size() == 0) {
//                return;
//            }
//            BannerAdapter adapter = new BannerAdapter(mContext, mlist);
//            mGalleryTravle.setAdapter(adapter, mlist.size());
//
//            mGalleryTravle.getGallery().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    BannerItem bannerItem = (BannerItem) parent.getItemAtPosition(position);
//                    Intent intentBanner = new Intent(mContext, BannerWebViewActivity.class);
//                    intentBanner.putExtra("image", bannerItem.getImage());
//                    intentBanner.putExtra("url", bannerItem.getUrl());
//                    intentBanner.putExtra("title", bannerItem.getTitle());
//                    mContext.startActivity(intentBanner);
//                }
//            });
//        }
//    }

    public static class PViewHolder extends ParallaxViewHolder {
        public TextView tvCnName;
        public TextView tvName;
        public TextView tvCityCount;
        public LinearLayout llytCityCount;
        public LinearLayout llytTips;

        public PViewHolder(View parent) {
            super(parent);
            tvCityCount = (TextView) parent.findViewById(R.id.tv_city_count);
            tvCnName = (TextView) parent.findViewById(R.id.tv_name_ch);
            tvName = (TextView) parent.findViewById(R.id.tv_name);
            llytCityCount = (LinearLayout) parent.findViewById(R.id.llyt_city_count);
            llytTips = (LinearLayout) parent.findViewById(R.id.llyt_tips);
//            Drawable drawable = parent.getContext().getResources().getDrawable(R.drawable.title_travel_defaule);
//            getBackgroundImage().setImageDrawable(drawable);
            animateImage();
        }

        @Override
        public int getParallaxImageId() {
            return R.id.parallax_background;
        }
    }
}


